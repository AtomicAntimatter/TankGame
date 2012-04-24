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
    private static final double RAD_ERROR = 1*Math.PI/180;
    
    private Color tankColor;
    private String tankName, tankNumber;
    private Point mousePoint;
    private AffineTransform barrelTrans, centerTrans;
    private Area border;
    private double tankAngle, tankDir;
    private boolean defense, death;
    protected double barrelAngle = 0;     
    protected Point centerPoint; 
    protected AffineTransform tankTrans;    
    protected Shape tankDefinition, barrelDefinition, shieldDefinition, tankShape, barrelShape, shieldShape;
    protected final double tankWidth = 30, tankHeight = 60, tankSpeed;
    protected int specialDrawSequence;
    public final long tankID = (long)(Long.MAX_VALUE*Math.random());
    protected int life;
    protected int power = 0;
    
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
        Area smallerArea = new Area(bound);
        border = new Area(biggerBound);
        border.subtract(smallerArea);
        
        tankTrans = new AffineTransform();
        barrelTrans = new AffineTransform();
        centerTrans = new AffineTransform();
        
        tankTrans.setToTranslation(centerPoint.getX()-tankWidth/2, centerPoint.getY()-tankHeight/2);
        centerTrans.setToTranslation(centerPoint.getX(), centerPoint.getY());
        
        Area a = new Area(new Arc2D.Double(-tankHeight+tankWidth/2, -tankHeight, 100, 100, 40, 100, Arc2D.OPEN));
        a.subtract(new Area(new Ellipse2D.Double(-tankHeight+tankWidth/2+8, -tankHeight+8, 84, 84)));
        shieldDefinition = a;  
    }
    
    @Override
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
     * @param dir: Either 1 (move forward) or -1 (move backward)
     */
    public synchronized void move(int dir)
    {     
        double rotX = Math.cos(tankAngle);
        double rotY = Math.sin(tankAngle);
        
        tankTrans.translate(tankSpeed*rotX*dir, tankSpeed*rotY*dir);
        centerTrans.translate(tankSpeed*rotX*dir, tankSpeed*rotY*dir);  	
        
        if(collidesWithWall(tankTrans.createTransformedShape(tankDefinition)))
        {     
            tankTrans.translate(tankSpeed*rotX*-dir, tankSpeed*rotY*-dir);   
            centerTrans.translate(tankSpeed*rotX*-dir, tankSpeed*rotY*-dir);
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
            tankAngle += -dir*0.1d;
        }
    }
    
    @SuppressWarnings("deprecation")
    private synchronized void rotate() 
    {
      //double dx = mousePoint.x - centerPoint.x,
      //       dy = mousePoint.y - centerPoint.y,
      //       a  = Math.atan2(dy,dx),
        double da = barrelAngle - tankAngle;
        
        while(da > Math.PI) da -= 2*Math.PI;
        while(da < -Math.PI) da += 2*Math.PI;
        
        if(Math.abs(da) > RAD_ERROR)
            rotate(da>0?1:-1);
    }
    
    public void movePoint(Point p) 
    {
        mousePoint = p;
    }
    
    public abstract void fire();
    public abstract void cooldown();
    
    public void defend()
    {
        defense = true;
    }
    
    public void stopDefend()
    {
        defense = false;
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
        barrelTrans.rotate(barrelAngle+Math.PI/2,tankWidth*0.2,tankWidth*0.5); //ugly FIXIT.
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
        g.drawString(tankNumber, (int)(centerPoint.getX()-tankWidth*0.9), (int)(centerPoint.getY()-tankHeight*0.7)); 
        g.drawString(Integer.toString(power), (int)(centerPoint.x-tankWidth*0.1), (int)(centerPoint.getY()-tankHeight*0.7));

        if(defense)
        {
            g.fill(shieldShape);
        }

        g.drawLine(centerPoint.x, centerPoint.y, (int)(centerPoint.x+50*Math.cos(barrelAngle)), (int)(centerPoint.y+50*Math.sin(barrelAngle)));
        
        //specialDraw(g);
    }
    
    protected abstract void specialDraw(Graphics2D g);
     
    private void setBarrelAngle()
    {
        double coordinateX = mousePoint.getX()-centerPoint.getX();  
        double coordinateY = mousePoint.getY()-centerPoint.getY(); 

        double tempAngle = Math.atan2(coordinateY,coordinateX);

        if(tempAngle < 0)
        {
                tempAngle+=2*Math.PI;
        }
        if((tempAngle == 0)&&(mousePoint.getX()<centerPoint.getX()))
        {
                tempAngle = Math.PI;
        }

        barrelAngle = tempAngle;       
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
