package TankController;

import Tanks.*;
import java.awt.geom.Point2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.Point;

public class HumanController extends TankController implements MouseMotionListener, MouseListener, KeyEventDispatcher
{
    private int up, left, down, right, space;
    private boolean kUp = false, kDown = false, kLeft = false, kRight = false, kSpace = false;
    private Point2D mousePoint = new Point2D.Double(0,0);
    
    public HumanController(Tank t,int _up, int _down, int _left, int _right, int _space)
    { 	
        super(t);
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
        
        if(ev == up) 
        {
            tank.move(1);  
            return true;
        }
        if(ev == down) 
        {
            tank.move(-1);    
            return true;
        }
        if(ev == left) 
        {
            tank.rotate(-1);      
            return true;
        }  
        if(ev == right) 
        {
            tank.rotate(1);   
            return true;
        }
        if(ev == space) 
        {
            tank.fire();
            return true;
        }
        if(ev == KeyEvent.VK_ESCAPE)
        {
            System.exit(0);
        }
        return false;
    }
      
    public void mouseDragged(java.awt.event.MouseEvent e) 
    {
        mousePoint = new Point2D.Double(e.getX()+16, e.getY()+16);
        tank.movePoint(mousePoint);
        
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
