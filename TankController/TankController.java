package TankController;

import Tanks.*;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Point;

public class TankController {

    protected final Tank tank;
    protected boolean death;

    public TankController(Tank t) {
        tank = t;
    }
    
    /**
     * For Human controllers, tracks point in field larger than screen.
     * @param screenPoint 
     */
    public void setScreenPoint(Point screenPoint)
    {
        
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
