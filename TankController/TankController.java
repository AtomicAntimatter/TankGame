package TankController;

import Tanks.*;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Point;

public class TankController {

    protected Tank tank;
    protected boolean death;

    public TankController(Tank t) {
        tank = t;
    }
    
    public void setScreenPoint(Point screenPoint)
    {
        
    }
    
    public Tank link(Tank t) {
        Tank u = tank;
        tank = t;
        return u;
    }
    
    public Tank getTank()
    {
        return tank;
    }

    public void poll() {
        death = tank.isDead();
        if(death) deactivate();
    }
    
    public boolean isDead() {
        return death;
    }

    public void deactivate() {
        
    }
}
