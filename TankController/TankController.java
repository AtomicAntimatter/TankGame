package TankController;

import Game.GameController;
import Resources.ProgrammerBakaError;
import Tanks.*;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class TankController {

    protected final Tank tank;

    protected boolean death;

    public TankController(Tank t)
    {
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

    public void poll() 
    {
        death = tank.isDead();
        if(death) deactivate();
    }
    
    public boolean isDead() 
    {
        return death;
    }

    public void deactivate() 
    {
        
    }
    
    public static class GenericConfiguration<E extends TankController> {

        public E instantiate(GameController.TankManager.TankStyle t) {
            try {
                return (E) this.getClass().getEnclosingClass().getConstructors()[0].newInstance(t.getTank(), this);
            } catch (Exception ex) {
                throw new ProgrammerBakaError(ex.getMessage(), ex);
            }
        }
        
    }
}
