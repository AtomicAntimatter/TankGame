package TankController;

import Tanks.*;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;

public class HumanController extends TankController implements MouseMotionListener, MouseListener, KeyEventDispatcher 
{
    private int up, left, down, right, fire;
    private boolean kU = false, kD = false, kL = false, kR = false;
    private Point mousePoint;
    private Point oldScreenPoint;
    private Point screenPoint;
    
    public HumanController(Tank t, int _up, int _down, int _left, int _right, int _space) 
    {
        super(t);
        up = _up;
        down = _down;
        left = _left;
        right = _right;
        fire = _space;
        mousePoint = new Point(0,0);
        screenPoint = new Point(0,0);
        oldScreenPoint = new Point(0,0);
    }

    public void poll() {
        tank.move((kU ? 1 : 0) - (kD ? 1 : 0));
        tank.rotate((kR ? 1 : 0) - (kL ? 1 : 0));
    }
    
    public void setScreenPoint(Point _screenPoint)
    {
        screenPoint.setLocation(_screenPoint);
        if(!screenPoint.equals(oldScreenPoint))
        {
            int dx = oldScreenPoint.x-screenPoint.x;
            int dy = oldScreenPoint.y-screenPoint.y;
            mousePoint.translate(dx, dy);
            tank.movePoint(mousePoint); 
            oldScreenPoint = (Point)screenPoint.clone();
        }      
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        boolean pressed = e.getID() == KeyEvent.KEY_PRESSED;
        int ev = e.getKeyCode();

        if (ev == up) 
        {
            kU = pressed;
            return true;
        }
        if (ev == down) 
        {
            kD = pressed;
            return true;
        }
        if (ev == left) 
        {
            kL = pressed;
            return true;
        }
        if (ev == right) 
        {
            kR = pressed;
            return true;
        }
        if (ev == fire) 
        {
            tank.fire();
            return true;
        }
        return false;
    }

    public void mouseDragged(java.awt.event.MouseEvent e) 
    {
        mousePoint = new Point(e.getX() - screenPoint.x, e.getY() - screenPoint.y);
        tank.movePoint(mousePoint);
    }

    public void mouseMoved(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }

    public void mouseClicked(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
}
