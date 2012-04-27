package TankController;

import Game.GUI;
import Tanks.*;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;

public class HumanKeyboardController extends HumanController implements KeyEventDispatcher
{
    private Configuration c;
    private boolean fire = false, defense = false;
    private byte aimDir = 0;
    private byte moveDir = 0;
    private int dir = 0;
    private Point mousePoint;
    private Point oldScreenPoint;
    private Point screenPoint;
    
    public HumanKeyboardController(Tank t, Configuration _c) 
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
        return CT_KEYBOARD;
    }
    
    @Override
    public void poll() 
    {
        super.poll();
        tank.move(dir);
        
        if(fire && !defense)
        {
            tank.fire();
        }  
        else
        {
            tank.cooldown();
        }
              
        tank.defend(defense);
    }
    
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
    public boolean dispatchKeyEvent(KeyEvent e) 
    {
        boolean pressed = e.getID() == KeyEvent.KEY_PRESSED;
        int ev = e.getKeyCode();
        
        if(ev == c.mUp) 
        {
            moveDir |= pressed?0x01:0;
            return true;
        }
        else if(ev == c.mDown) 
        {
            moveDir |= pressed?0x02:0;
            return true;
        }
        else if(ev == c.mLeft) 
        {
            moveDir |= pressed?0x03:0;
            return true;
        }
        else if(ev == c.mRight) 
        {
            moveDir |= pressed?0x04:0;
            return true;
        }
        else if(ev == c.mBack)
        {
            dir = pressed?1:-1;
            return true;
        }
        else if(ev == c.aSpec) 
        {
            tank.specialFire();
            return true;
        }
        else if(ev == c.aUp)
        {
            aimDir |= pressed?0x01:0;
            return true;
        }
        else if(ev == c.aDown)
        {
            aimDir |= pressed?0x02:0;
            return true;
        }
        else if(ev == c.aLeft)
        {
            aimDir |= pressed?0x03:0;
            return true;
        }
        else if(ev == c.aRight)
        {
            aimDir |= pressed?0x04:0;
            return true;
        }       
        if(ev == c.aFire)
        {
            fire = pressed;
            return true;
        }
        else if(ev == c.aDefense)
        {
            defense = pressed;
            return true;
        }
        return false;
    }
    
    public static class Configuration extends TankController.GenericConfiguration<HumanKeyboardController>
    {
        public int mUp, mDown, mLeft, mRight, mBack, mStop, aSpec, aUp, aDown, aLeft, aRight, aFire, aDefense;
        
        public Configuration()
        {            
            mUp = KeyEvent.VK_W;
            mDown = KeyEvent.VK_S;
            mLeft = KeyEvent.VK_A;
            mRight = KeyEvent.VK_D;
            mStop = KeyEvent.VK_Q;
            mBack = KeyEvent.VK_E;
            aSpec = KeyEvent.VK_F;
            aUp = KeyEvent.VK_UP;
            aDown = KeyEvent.VK_DOWN;
            aLeft = KeyEvent.VK_LEFT;
            aRight = KeyEvent.VK_RIGHT;
            aFire = KeyEvent.VK_SPACE;
            aDefense = KeyEvent.VK_SHIFT;            
        }
    }
    
    @Override
    public void deactivate() {
        GUI.theGUI.deregisterControls(null, null, this);
        GUI.theGUI.deregisterController(this);
    }
}
