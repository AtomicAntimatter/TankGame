package Tanks;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.*;

public class Player extends Tank implements MouseMotionListener, MouseListener, KeyEventDispatcher  
{
    private int up, left, down, right, space;
    private boolean kUp = false, kLeft = false, kDown = false, kRight = false, kSpace = false;
    private Point2D mousePoint = new Point2D.Double(0,0);
    
    public Player(Color _tankColor, String _tankName, String _tankNumber, 
            Point2D _centerPoint, double _tankAngle, int _up, int _down, 
            int _left, int _right, int _space, Rectangle2D _bounds)
    {
    	super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds);
    	
        up = _up; 
        down = _down; 
        left = _left; 
        right = _right;
        space = _space;
    }
    
    public boolean dispatchKeyEvent(KeyEvent e) 
    {
        boolean pressed = e.getID() == KeyEvent.KEY_PRESSED;
        int ev = e.getKeyCode();
        
        if (ev == up) 
        {
            kUp = pressed;     
            return true;
        }
        if (ev == left) 
        {
            kLeft = pressed;      
            return true;
        }
        if (ev == down) 
        {
            kDown = pressed;    
            return true;
        }
        if (ev == right) 
        {
            kRight = pressed;   
            return true;
        }
        if (ev == space) 
        {
            kSpace = pressed;
            return true;
        }
        return false;
    }
    
    protected void update()
    {
    	if(kUp)
    	{
    		tankMoveStep(Math.cos(tankAngle-Math.PI/2),Math.sin(tankAngle-Math.PI/2));
    	}
    	if(kLeft)
    	{
    		tankAngleStep(-0.1d);
    	}
    	if(kDown)
    	{
    		tankMoveStep(-Math.cos(tankAngle-Math.PI/2),-Math.sin(tankAngle-Math.PI/2));
    	}
    	if(kRight)
    	{
    		tankAngleStep(0.1d);
    	}
    	if(kSpace)
    	{
    	}
    }
     
    protected void setBarrelAngle()
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
    
    protected void tankMoveStep(double xChange, double yChange)
    {
    	oldCenterPoint = centerPoint;
        centerPoint = new Point2D.Double(centerPoint.getX()+xChange, centerPoint.getY()+yChange);
    }
    
    protected void tankAngleStep(double angleChange)
    {
    	oldTankAngle = tankAngle;
        tankAngle += angleChange;
    }
    
    public Point2D getMousePoint()
    {
        return mousePoint;
    }
    
    public void mouseDragged(java.awt.event.MouseEvent e) 
    {
        mousePoint = new Point2D.Double(e.getX()+16, e.getY()+16);
        e.consume();
    }

    public void mouseMoved(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
    
    public void mouseClicked(java.awt.event.MouseEvent e) {}

    public void mousePressed(java.awt.event.MouseEvent e) {}

    public void mouseReleased(java.awt.event.MouseEvent e) {}

    public void mouseEntered(java.awt.event.MouseEvent e) {}

    public void mouseExited(java.awt.event.MouseEvent e) {} 
}
