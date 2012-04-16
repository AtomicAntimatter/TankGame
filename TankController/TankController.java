package TankController;

import Tanks.*;

public class TankController 
{
    protected Tank tank;
    
    public TankController(Tank t) {
        tank = t;
    }
    
    public Tank link(Tank t) {
        Tank u = tank;
        tank = t;
        return u;
    }
}
