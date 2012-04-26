package TankController;

import Game.GUI;
import Tanks.*;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class HumanMouseController extends HumanController implements MouseMotionListener, MouseListener, KeyEventDispatcher
{
    private final Configuration c;
    private boolean fire = false, defense = false;
    private int dir = -1; 
    private Point mousePoint, oldScreenPoint, screenPoint;
    
    public HumanMouseController(Tank t, Configuration _c) 
    {
        super(t);     
        mousePoint = new Point(0,0);
        screenPoint = new Point(0,0);
        oldScreenPoint = new Point(0,0);
        c = _c;
    }
    
    @Override
    public int controlType()
    {
        return CT_MOUSE | CT_KEYBOARD;
    }

    @Override
    public void poll() 
    {
        super.poll();
        tank.move(dir);
        
        if(fire && !defense)
            tank.fire(); 
        else
            tank.cooldown();
        
        tank.defend(defense);  
    }
    
    /*
     * Sets mouse in correct spot regardless of screen tracking.
     */    
    @Override
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

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) 
    {
        mousePoint = new Point(e.getX() - screenPoint.x, e.getY() - screenPoint.y);
        tank.movePoint(mousePoint);
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
    
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            fire = true;
        }
        else if(e.getButton() == MouseEvent.BUTTON3)
        {
            defense = true;
        }
    }
    
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            fire = false;
        }
        else if(e.getButton() == MouseEvent.BUTTON3)
        {
            defense = false;
        }
    }
    
    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
    
    @Override
    public void mouseExited(java.awt.event.MouseEvent e) 
    {
        mouseDragged(e);
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) 
    {
        boolean pressed = e.getID() == KeyEvent.KEY_PRESSED;
        int ev = e.getKeyCode();
        
        if(ev == c.back) 
        {
            dir = pressed?1:-1;
            return true;
        }
        if(ev == c.stop) 
        {
            dir = pressed?0:-1;
            return true;
        }
        if((ev == c.spec)&&(pressed)) 
        {
            tank.specialFire();
            return true;
        }         
        return false;
    }
    
    public static class Configuration extends TankController.GenericConfiguration<HumanMouseController>
    {            
        public final int spec, stop, back;
        
        public Configuration(int config)
        {            
            switch(config)
            {
                case 0:
                    spec = KeyEvent.VK_Q;
                    stop = KeyEvent.VK_W;
                    back = KeyEvent.VK_E;
                    break;
                default:
                    spec = KeyEvent.VK_NUMPAD0;
                    stop = KeyEvent.VK_NUMPAD1;
                    back = KeyEvent.VK_NUMPAD2;
            }
        }
    }

    @Override
    public void deactivate() 
    {
        GUI.theGUI.deregisterControls(this, this, this);
        GUI.theGUI.deregisterController(this);
    }
}
