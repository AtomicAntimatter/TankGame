package TankController;

import Tanks.*;
import java.awt.Point;

public class TankController {

    protected Tank tank;

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
        //do nothing...
    }
}
