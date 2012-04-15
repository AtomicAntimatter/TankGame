package Tanks;

import java.awt.*;
import java.awt.geom.*;

public class Tank
{
    private Color tankColor;
    private String tankName;
    private String tankNumber;
    protected Point2D centerPoint, oldCenterPoint, mousePoint;
    protected double tankAngle, oldTankAngle, barrelAngle = 0;
    private Rectangle2D tankRect, tankFront;
    private GeneralPath tankBarrel;
    private Shape tankShape, barrelShape, frontShape;
    private final double tankWidth = 30, tankHeight = 60; 
    private AffineTransform tankTrans, barrelTrans;
    private Rectangle2D inverseBoundN, inverseBoundS, inverseBoundE, inverseBoundW;
    
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
        
        tankRect = new Rectangle2D.Double(0, 0, tankWidth, tankHeight);
        tankFront = new Rectangle2D.Double(0,0,tankWidth,tankHeight*0.1);
        makeBarrel();
        
        tankShape = tankRect;
        barrelShape = tankBarrel;
        frontShape = tankFront;
    }
    
    public void updateInput(Object[] inputs)
    {      
        if(Boolean.valueOf(inputs[0].toString()))
    	{
    		tankMoveStep(Math.cos(tankAngle-Math.PI/2),Math.sin(tankAngle-Math.PI/2));
    	}   	
    	if(Boolean.valueOf(inputs[1].toString()))
    	{
    		tankMoveStep(-Math.cos(tankAngle-Math.PI/2),-Math.sin(tankAngle-Math.PI/2));
    	}
        if(Boolean.valueOf(inputs[2].toString()))
    	{
    		tankAngleStep(-0.1d);
    	}
    	if(Boolean.valueOf(inputs[3].toString()))
    	{
    		tankAngleStep(0.1d);
    	}
    	if(Boolean.valueOf(inputs[4].toString()))
    	{
    	}
        
        mousePoint = (Point2D)inputs[5];
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
   
    public void moveTank()
    {           
        tankTrans.setToTranslation(centerPoint.getX()-tankWidth/2, centerPoint.getY()-tankHeight/2);         
        tankShape = tankTrans.createTransformedShape(tankRect);
        
        if(didHit())
        {
            centerPoint = oldCenterPoint;        
            tankTrans.setToTranslation(centerPoint.getX()-tankWidth/2, centerPoint.getY()-tankHeight/2);         
            tankShape = tankTrans.createTransformedShape(tankRect);
        }
        tankTrans.rotate(tankAngle,tankRect.getWidth()/2,tankRect.getHeight()/2);
        tankShape = tankTrans.createTransformedShape(tankRect);
        
        if(didHit())
        {
            tankAngle = oldTankAngle;
        }

        frontShape = tankTrans.createTransformedShape(tankFront);
        
        setBarrelAngle();   
        barrelTrans.setToTranslation(centerPoint.getX()-tankWidth*0.2, centerPoint.getY()-tankWidth*0.25);
        barrelTrans.rotate(barrelAngle,tankRect.getWidth()*0.2,tankRect.getHeight()*0.15);
        barrelShape = barrelTrans.createTransformedShape(tankBarrel);
    }
    
    public boolean didHit()
    {
        if(tankShape.intersects(inverseBoundN)||tankShape.intersects(inverseBoundS)||tankShape.intersects(inverseBoundE)||tankShape.intersects(inverseBoundW))
        {
            return true;
        }
        return false;
    }
      
    public void drawTank(Graphics2D g)
    {
        g.setColor(tankColor);
              
        g.draw(tankShape);
        g.draw(frontShape);
        g.fill(barrelShape);   
        g.drawString(tankNumber, (int)(centerPoint.getX()-tankWidth*0.9), (int)(centerPoint.getY()-tankHeight*0.7));
    }
    
    private void makeBarrel()
    {
        int x1Points[] = {0, 0, (int)(tankWidth*0.4), (int)(tankWidth*0.4), 
            (int)(tankWidth*0.35), (int)(tankWidth*0.3), (int)(tankWidth*0.1), 
            (int)(tankWidth*0.05)};
             
        int y1Points[] = {0, (int)(tankWidth*0.5), (int)(tankWidth*0.5), 0, 0, 
            (int)(tankWidth*0.5), (int)(tankWidth*0.5), 0};
               
        tankBarrel = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x1Points.length);  
        tankBarrel.moveTo(x1Points[0], y1Points[0]);
        tankBarrel.lineTo(x1Points[1], y1Points[1]); 
        tankBarrel.curveTo((int)(tankWidth*0.1), (int)(tankWidth*0.75), 
                (int)(tankWidth*0.3), (int)(tankWidth*0.75),x1Points[2], 
                y1Points[2]);
        
        for(int i = 3; i < x1Points.length; i++)
        {
            tankBarrel.lineTo(x1Points[i], y1Points[i]);
        }
        
        tankBarrel.closePath();
    }
    
    public Point2D getCenterPoint()
    {
        return centerPoint;
    }
    
    public String getName()
    {
        return tankName;
    }
}
