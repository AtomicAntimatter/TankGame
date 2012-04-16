package Tanks;

import java.awt.*;
import java.awt.geom.*;

public abstract class Tank
{
    private Color tankColor;
    private String tankName;
    private String tankNumber;
    private Point2D centerPoint, oldCenterPoint, mousePoint;
    private double tankAngle, oldTankAngle, barrelAngle = 0;     
    private AffineTransform tankTrans, barrelTrans;
    private Rectangle2D inverseBoundN, inverseBoundS, inverseBoundE, inverseBoundW;
    protected Shape tankDefinition, barrelDefinition;
    protected Shape tankShape, barrelShape;
    protected final double tankWidth = 30, tankHeight = 60; 
    
    public Tank(Color _tankColor, String _tankName, String _tankNumber, Point2D _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        tankColor = _tankColor;
        tankName = _tankName;
        tankNumber = _tankNumber;
        centerPoint = _centerPoint;
        oldCenterPoint = centerPoint;
        tankAngle = _tankAngle;
        oldTankAngle = tankAngle;
        mousePoint = new Point2D.Double(0,0);
        
        inverseBoundN = new Rectangle2D.Double(0,0,_bounds.getWidth()+_bounds.getX()*2,_bounds.getY());
        inverseBoundS = new Rectangle2D.Double(0,_bounds.getHeight()+_bounds.getY(),_bounds.getWidth()+_bounds.getX()*2,_bounds.getY());
        inverseBoundE = new Rectangle2D.Double(_bounds.getWidth()+_bounds.getX(), 0, _bounds.getX(), _bounds.getHeight()+_bounds.getY()*2);
        inverseBoundW = new Rectangle2D.Double(0,0,_bounds.getX(),_bounds.getHeight()+_bounds.getY()*2);
         
        tankTrans = new AffineTransform();
        barrelTrans = new AffineTransform(); 
    }
    
    public void move(int dir)
    {      
        if(dir > 0)
    	{
    		tankMoveStep(Math.cos(tankAngle-Math.PI/2),Math.sin(tankAngle-Math.PI/2));
    	}   	
    	else
        if(dir < 0)
    	{
    		tankMoveStep(-Math.cos(tankAngle-Math.PI/2),-Math.sin(tankAngle-Math.PI/2));
    	}
        if(dir != 0)
            System.out.println(dir);
    }
    
    public void rotate(int dir) {
        if(dir < 0)
    	{
    		tankAngleStep(-0.1d);
    	}
        else
    	if(dir > 0)
    	{
    		tankAngleStep(0.1d);
    	}
    }
    
    public void movePoint(Point2D p) {
        mousePoint = p;
    }
    
    public void fire() {
        //
    }
    
    public void doMove()
    {           
        tankTrans.setToTranslation(centerPoint.getX()-tankWidth/2, centerPoint.getY()-tankHeight/2);         
        tankShape = tankTrans.createTransformedShape(tankDefinition);
        
        if(didHit())
        {
            centerPoint = oldCenterPoint;        
            tankTrans.setToTranslation(centerPoint.getX()-tankWidth/2, centerPoint.getY()-tankHeight/2);         
            tankShape = tankTrans.createTransformedShape(tankDefinition);
        }
        tankTrans.rotate(tankAngle,tankDefinition.getBounds().getWidth()/2,tankDefinition.getBounds().getHeight()/2);
        tankShape = tankTrans.createTransformedShape(tankDefinition);
        
        if(didHit())
        {
            tankAngle = oldTankAngle;
        }
        
        setBarrelAngle();   
        barrelTrans.setToTranslation(centerPoint.getX()-tankWidth*0.2, centerPoint.getY()-tankWidth*0.25);
        barrelTrans.rotate(barrelAngle,tankDefinition.getBounds().getWidth()*0.2,tankDefinition.getBounds().getHeight()*0.15);
        barrelShape = barrelTrans.createTransformedShape(barrelDefinition);
    }
    
    public void drawTank(Graphics2D g)
    {
        g.setColor(tankColor);
              
        g.draw(tankShape);
        g.fill(barrelShape);   
        g.drawString(tankNumber, (int)(centerPoint.getX()-tankWidth*0.9), (int)(centerPoint.getY()-tankHeight*0.7));
    }
    
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
    
    private void tankMoveStep(double xChange, double yChange)
    {
        oldCenterPoint = centerPoint;
        centerPoint = new Point2D.Double(centerPoint.getX()+xChange, centerPoint.getY()+yChange);
    }
    
    private void tankAngleStep(double angleChange)
    {
        oldTankAngle = tankAngle;
        tankAngle += angleChange;
    }
     
    private boolean didHit()
    {
        if(tankShape.intersects(inverseBoundN)||tankShape.intersects(inverseBoundS)||tankShape.intersects(inverseBoundE)||tankShape.intersects(inverseBoundW))
        {
            return true;
        }
        return false;
    }    
}
