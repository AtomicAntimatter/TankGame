package Tanks;

import Game.GUI;
import Tanks.Bullets.Bullet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.*;

public abstract class Tank
{  
    public final long tankID = (long)(Long.MAX_VALUE*Math.random());
    private static final double RAD_ERROR = Math.PI/30;
    private Color tankColor;
    private String tankName, tankNumber;
    private Point mousePoint;
    private AffineTransform barrelTrans, centerTrans;
    private static Area border;
    private double tankAngle, tankDir, barrelAngle = 0, da;
    private final double tankSpeed;
    private boolean defense, death;    
    private int rDir, life;
    protected Point centerPoint; 
    protected AffineTransform tankTrans;    
    protected Shape tankDefinition, barrelDefinition, tankShape, barrelShape, shieldShape;
    protected static final Shape shieldDefinition;
    protected static final int tankWidth = 30, tankHeight = 60;
    protected int power = 0, specialDrawSequence;
      
    static 
    {
        Area a = new Area(new Arc2D.Double(-25, tankWidth/2 - 50, 100, 100, -70, 140, Arc2D.OPEN));
        a.subtract(new Area(new Ellipse2D.Double(-17, tankWidth/2-42, 84, 84))); //based on actual arithmetic, not guess and check
        shieldDefinition = a;  
    }
    
    public Tank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D bound, double _tankSpeed, int _life)
    {
        tankColor = _tankColor;
        tankName = _tankName;
        tankNumber = _tankNumber;
        centerPoint = _centerPoint;
        mousePoint = new Point(0,0);
        tankSpeed = _tankSpeed;
        specialDrawSequence = 0;
        tankAngle = _tankAngle - Math.PI/2;
        life = _life;
        
        Rectangle2D biggerBound = new Rectangle2D.Double(bound.getX()-0.05, bound.getY()-0.05, bound.getWidth()+0.1, bound.getHeight()+0.1);
        border = new Area(biggerBound);
        border.subtract(new Area(bound));
        
        tankTrans = new AffineTransform();
        barrelTrans = new AffineTransform();
        centerTrans = new AffineTransform();
        
        tankTrans.setToTranslation(centerPoint.x-tankWidth/2, centerPoint.y-tankHeight/2);
        centerTrans.setToTranslation(centerPoint.x, centerPoint.y);
    }
     
    @Override
    @SuppressWarnings(".EqualsMethodNotCheckingType")
    public synchronized boolean equals(Object o) 
    {
        return (Tank.class.isInstance(o) && Tank.class.cast(o).tankID == this.tankID);
    }

    @Override
    public synchronized int hashCode() 
    {
        return (int)Math.IEEEremainder(tankID, Integer.MAX_VALUE);
    }
    
    //aim() used to be here.
    
    /**
     * Moves the tank in the indicated direction.
     * 
     * @param dir: Either -1 (move forward) or 1 (move backward)
     */
    public synchronized void move(int dir)
    {     
        tankTrans.translate(0, tankSpeed*dir);
        centerTrans.translate(0, tankSpeed*dir);

        if(collidesWithWall(tankTrans.createTransformedShape(tankDefinition)))
        {     
            tankTrans.translate(0, tankSpeed*-dir);   
            centerTrans.translate(0, tankSpeed*-dir);
        }
        tankDir = dir;
    }
    
    @Deprecated
    public synchronized void rotate(int dir) 
    {
        tankTrans.rotate(dir*0.1d,tankWidth/2,tankHeight/2);
        centerTrans.rotate(dir*0.1d);
        tankAngle += dir*0.1d;
        
        if(collidesWithWall(tankTrans.createTransformedShape(tankDefinition)))
        {     
            tankTrans.rotate(-dir*0.1d,tankWidth/2,tankHeight/2);  
            centerTrans.rotate(-dir*0.1d);
            tankAngle -= dir*0.1d;
        }
    }
    
    @SuppressWarnings("deprecation")
    private synchronized void rotate() 
    {
        da = barrelAngle - tankAngle;
        if((barrelAngle > 0)&&(barrelAngle < Math.PI/2)&&(tankAngle > 3*Math.PI/2))
        {
            da = RAD_ERROR+1;    
        }
        else
        if((barrelAngle > 3*Math.PI/2)&&(barrelAngle < 2*Math.PI)&&(tankAngle < Math.PI/2))
        {
            da = -RAD_ERROR-1;    
        }
        
        if(tankAngle > 2*Math.PI)
        {
            tankAngle -= 2*Math.PI;
        }
        else
        if(tankAngle < 0)
        {
            tankAngle += 2*Math.PI;
        }
        
        rDir = 0;
        if(Math.abs(da) > RAD_ERROR)
        {
            rDir = da>0?1:-1;
        }

        rotate(rDir);
    }
    
    public void movePoint(Point p) 
    {
        mousePoint = p;
    }
    
    public abstract void fire();
    public abstract void cooldown();
    
    public void defend(boolean _defend)
    {
        defense = _defend;
    }
    
    public void specialFire()
    {
        specialDrawSequence = 0;
    }
    
    public void doMove()
    {    
        rotate(); //to mouse
        
        centerPoint.move((int)centerTrans.getTranslateX(), (int)(centerTrans.getTranslateY()));
        tankShape = tankTrans.createTransformedShape(tankDefinition);
        
        setBarrelAngle();
        barrelTrans.setToTranslation(centerPoint.getX()-tankWidth*0.2, centerPoint.getY()-tankWidth*0.5);
        barrelTrans.rotate(barrelAngle,tankWidth*0.2,tankWidth*0.5); //ugly TODO: un-magicknumberfy.
        barrelShape = barrelTrans.createTransformedShape(barrelDefinition);
        
        if(defense)
        {
            shieldShape = barrelTrans.createTransformedShape(shieldDefinition);
        }
    }
    
    public void drawTank(Graphics2D g)
    {
        g.setColor(tankColor);
  
        g.draw(tankShape);
        g.fill(barrelShape);   
        g.drawString(tankNumber, (int)(centerPoint.x-tankWidth*0.9), (int)(centerPoint.y-tankHeight*0.7)); 
        if(defense)
        {
            g.fill(shieldShape);
        }
        //specialDraw(g);
    }
    
    protected abstract void specialDraw(Graphics2D g);
     
    private void setBarrelAngle()
    {
        barrelAngle = Math.atan2(mousePoint.y-centerPoint.y,mousePoint.x-centerPoint.x);

        if(barrelAngle < 0)
        {
                barrelAngle+=2*Math.PI;
        }
        if((barrelAngle == 0)&&(mousePoint.x<centerPoint.x))
        {
                barrelAngle = Math.PI;
        }     
    }
    
    public boolean collidesWithWall(Shape t)
    {
        Area a = new Area(t);
        a.intersect(border);
        
        return !a.isEmpty();
    }
    
    public boolean collidesWith(Shape t)
    {
        Area a = new Area(t);
        Area b = new Area(tankShape);
        a.intersect(b);
        
        return !a.isEmpty();
    } 
    
    public boolean collidesWithShield(Shape t)
    {
        if(defense)
        {
            Area a = new Area(t);
            Area b = new Area(shieldShape);
            a.intersect(b);  
            return !a.isEmpty();
        }
        return false;
    }
    
    public Point getCenterPoint()
    {
        return centerPoint;
    }
    
    public double getSpeed()
    {
        return tankSpeed*tankDir;
    }
    
    public double getDirection()
    {
        return tankAngle;
    }
    
    public double getBarrelAngle()
    {
        return barrelAngle;
    }
    
    public double distanceFrom2(double _x, double _y) {
        return Math.pow(centerPoint.x-_x, 2) + Math.pow(centerPoint.y-_y, 2);
    }
    
    public void takeDamage(int amount, Bullet source) 
    {
        if(Tanks.PowerUp.class.isInstance(source)) {
            power += amount;
            return;
        }
        life -= amount;
        
        GUI.theGUI.launchBullet(PowerUp.make(centerPoint.x, centerPoint.y, 2*Math.PI*Math.random(), this, amount));
        
        if(life < 0)
        {
            death = true;
        }
    }
        
    public void notifyDeath()
    {
        death = true;
    }
    
    public boolean isDead()
    {
        return death;
    }
}
