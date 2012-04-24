package TankController;

import Game.GUI;
import Tanks.*;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/*
 * Code Cleaning needed.
 */

public class HumanMouseController extends TankController implements MouseMotionListener, MouseListener 
{
    private Configuration c;
    private boolean kU = false, kD = false, kL = false, kR = false, fire = false, defense = false;
    private int aimDir = 0;
    private Point mousePoint;
    private Point oldScreenPoint;
    private Point screenPoint;
    
    public HumanMouseController(Tank t, Configuration _c) 
    {
        super(t);     
        mousePoint = new Point(0,0);
        screenPoint = new Point(0,0);
        oldScreenPoint = new Point(0,0);
        c = _c;
    }
    
    @Deprecated
    public boolean isMouse()
    {
        return true;
    }
    
    @Override
    public void poll() 
    {
        super.poll();
        tank.move(/*(kU ? 1 : 0) - (kD ? 1 : 0)*/1);
        //tank.rotate((kR ? 1 : 0) - (kL ? 1 : 0));
        
        if(fire && !defense)
        {
            tank.fire();
        }  
        else
        {
            tank.cooldown();
        }
        
        if(defense)
        {
            tank.defend();
        }
        else
        {       
            tank.stopDefend();
        }  
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
    public boolean isHuman() {
        return true;
    }
    
    public static class Configuration extends TankController.GenericConfiguration<HumanMouseController>
    {
        public Configuration()
        {            
        
        }
    }

    @Override
    public void deactivate() {
        GUI.theGUI.deregisterControls(this, this, null);
        GUI.theGUI.deregisterController(this);
    }
}
