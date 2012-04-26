package TankController;

import Game.GUI;
import Tanks.*;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;

/*
 * THIS CLASS NEEDS WORK.
 */

public class HumanKeyboardController extends HumanController implements KeyEventDispatcher
{
    private Configuration c;
    private boolean kU = false, kD = false, kL = false, kR = false, fire = false, defense = false;
    private int aimDir = 0;
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

    public int controlType()
    {
        return CT_KEYBOARD;
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
              
        tank.defend(defense);
        
      /*if(!c.mouse)
        {
            tank.aim(aimDir);
        }*/
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
    @Deprecated
    public boolean dispatchKeyEvent(KeyEvent e) {
      /*boolean pressed = e.getID() == KeyEvent.KEY_PRESSED;
        int ev = e.getKeyCode();
        
        if(ev == c.mUp) 
        {
            kU = pressed;
            return true;
        }
        if(ev == c.mDown) 
        {
            kD = pressed;
            return true;
        }
        if(ev == c.mLeft) 
        {
            kL = pressed;
            return true;
        }
        if(ev == c.mRight) 
        {
            kR = pressed;
            return true;
        }
        if(ev == c.kSpace) 
        {
            tank.specialFire();
            return true;
        }
        if(!c.mouse)
        {
            if((ev == c.aUp)&&pressed)
                aimDir = 0;
            else if((ev == c.aDown)&&pressed)
                aimDir = 1;
            else if((ev == c.aLeft)&&pressed)
                aimDir = 2;
            else if((ev == c.aRight)&&pressed)
                aimDir = 3;
            if(ev == c.aFire)
            {
                if(pressed)
                    fire = true;
                else
                    fire = false;
            }
            else if(ev == c.aDefense)
            {
                if(pressed)
                    defense = true;
                else
                    defense = false;
            }
        }*/
        return false;
    }
    
    public static class Configuration extends TankController.GenericConfiguration<HumanKeyboardController>
    {
        public int mUp, mDown, mLeft, mRight, kSpace, aUp, aDown, aLeft, aRight, aFire, aDefense;
        public boolean mouse;
        
        public Configuration(int config)
        {            
            if(config == 1)
            {
                mUp = KeyEvent.VK_W;
                mDown = KeyEvent.VK_S;
                mLeft = KeyEvent.VK_A;
                mRight = KeyEvent.VK_D;
                kSpace = KeyEvent.VK_SPACE;
                mouse = true;
            }
            else if(config == 2)
            {
                mUp = KeyEvent.VK_UP;
                mDown = KeyEvent.VK_DOWN;
                mLeft = KeyEvent.VK_LEFT;
                mRight = KeyEvent.VK_RIGHT;
                kSpace = KeyEvent.VK_NUMPAD0;
                mouse = true;
            }
            else if(config == 3)
            {
                mUp = KeyEvent.VK_W;
                mDown = KeyEvent.VK_S;
                mLeft = KeyEvent.VK_A;
                mRight = KeyEvent.VK_D;
                kSpace = KeyEvent.VK_SPACE;
                aUp = KeyEvent.VK_I;
                aDown = KeyEvent.VK_K;
                aLeft = KeyEvent.VK_J;
                aRight = KeyEvent.VK_L;
                aFire = KeyEvent.VK_SHIFT;
                aDefense = KeyEvent.VK_SEMICOLON;
                mouse = false;
            }
        }
    }
    
    @Override
    public void deactivate() {
        GUI.theGUI.deregisterControls(null, null, this);
        GUI.theGUI.deregisterController(this);
    }
}
