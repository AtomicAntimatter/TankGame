package Tanks;

import java.awt.*;
import java.awt.geom.*;

public abstract class Tank
{
    private Color tankColor;
    private String tankName;
    private String tankNumber;
    private Point mousePoint;
    protected Point centerPoint; 
    private double tankAngle, barrelAngle = 0;     
    private AffineTransform barrelTrans, centerTrans;
    protected AffineTransform tankTrans;
    private Area border;
    protected Shape tankDefinition, barrelDefinition;
    protected Shape tankShape, barrelShape;
    protected final double tankWidth = 30, tankHeight = 60;
    private final double tankSpeed;
    protected double specialDrawSequence;
    
    public Tank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D bound, double _tankSpeed)
    {
        tankColor = _tankColor;
        tankName = _tankName;
        tankNumber = _tankNumber;
        centerPoint = _centerPoint;
        tankAngle = _tankAngle;
        mousePoint = new Point(0,0);
        tankSpeed = _tankSpeed;
        specialDrawSequence = 0;
        
        Rectangle2D biggerBound = new Rectangle2D.Double(bound.getX()-0.05, bound.getY()-0.05, bound.getWidth()+0.1, bound.getHeight()+0.1);
        Area smallerArea = new Area(bound);
        border = new Area(biggerBound);
        border.subtract(smallerArea);
        
        tankTrans = new AffineTransform();
        barrelTrans = new AffineTransform();
        centerTrans = new AffineTransform();
        
        tankTrans.setToTranslation(centerPoint.getX()-tankWidth/2, centerPoint.getY()-tankHeight/2);
        centerTrans.setToTranslation(centerPoint.getX(), centerPoint.getY());
    }
    
    public void move(int dir)
    {     
        double rotX = Math.cos(tankAngle-Math.PI/2);
        double rotY = Math.sin(tankAngle-Math.PI/2);
        
        tankTrans.translate(tankSpeed*rotX*dir, tankSpeed*rotY*dir);
        centerTrans.translate(tankSpeed*rotX*dir, tankSpeed*rotY*dir);  	
        
        if(didHit(tankTrans.createTransformedShape(tankDefinition)))
        {     
            tankTrans.translate(tankSpeed*rotX*-dir, tankSpeed*rotY*-dir);   
            centerTrans.translate(tankSpeed*rotX*-dir, tankSpeed*rotY*-dir);
        }
    }
    
    public void rotate(int dir) 
    {
        tankTrans.rotate(dir*0.1d,tankWidth/2,tankHeight/2);
        centerTrans.rotate(dir*0.1d);
        
        if(didHit(tankTrans.createTransformedShape(tankDefinition)))
        {     
            tankTrans.rotate(-dir*0.1d,tankWidth/2,tankHeight/2);  
            centerTrans.rotate(-dir*0.1d);
        }
    }
    
    public void movePoint(Point p) 
    {
        mousePoint = p;
    }
    
    public void fire() 
    {
    }
    
    public void specialFire()
    {
        specialDrawSequence = 0;
    }
    
    public void doMove()
    {    
        centerPoint.move((int)centerTrans.getTranslateX(), (int)(centerTrans.getTranslateY()));
        tankShape = tankTrans.createTransformedShape(tankDefinition);
        
        setBarrelAngle();
        barrelTrans.setToTranslation(centerPoint.getX()-tankWidth*0.2, centerPoint.getY()-tankWidth*0.5);
        barrelTrans.rotate(barrelAngle,tankWidth*0.2,tankWidth*0.5);
        barrelShape = barrelTrans.createTransformedShape(barrelDefinition);
    }
    
    public void drawTank(Graphics2D g)
    {
        g.setColor(tankColor);
        
        specialDraw(g);
        
        g.draw(tankShape);
        g.fill(barrelShape);   
        g.drawString(tankNumber, (int)(centerPoint.getX()-tankWidth*0.9), (int)(centerPoint.getY()-tankHeight*0.7));      
    }
    
    protected abstract void specialDraw(Graphics2D g);
     
    private void setBarrelAngle()
    {
        double coordinateX = centerPoint.getX()-mousePoint.getX();  
        double coordinateY = centerPoint.getY()-mousePoint.getY(); 

        double tempAngle = Math.atan2(coordinateY,coordinateX)-Math.PI/2;

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
    
    private boolean didHit(Shape t)
    {
        Area tankArea = new Area(t);
        tankArea.intersect(border);
        
        return !tankArea.isEmpty();
    } 
    
    public Point getCenterPoint()
    {
        return centerPoint;
    }
    
    public double getSpeed()
    {
        return tankSpeed;
    }
}
