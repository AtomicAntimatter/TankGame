
package TankController.AI;

import Game.GUI;
import TankController.TankController;
import Tanks.Tank;
import java.util.Iterator;

public class SimplisticAI extends TankController {
    private static long RECALC_INT = 1000, FIRE_INT = 300;
    private long recalcT = 0, fireT = 0;
    protected Tank target;
    protected int mD = 1, rD = 1;
    
    public SimplisticAI (Tank t) {
        super(t);
    }
    
    @Override
    public synchronized void poll() {
        super.poll();
        
        if(death) return;

        if(System.currentTimeMillis() - recalcT > RECALC_INT) {
            recalculate();
            recalcT = System.currentTimeMillis();
        }
       
        if (target != null) {
            tank.movePoint(target.getCenterPoint());
            if (System.currentTimeMillis() - fireT > FIRE_INT) {
                tank.fire();
                fireT = System.currentTimeMillis();
            }
            else tank.cooldown();
        }
        
        mD = Math.random()<.8d?mD:-mD;
        rD = Math.random()<.9d?rD:-rD;
        
        tank.move(mD);
        tank.rotate(rD);
    }

    private synchronized void recalculate() {
        synchronized(GUI.theGUI.tanks())
        {
            double distance = (target != null) ? target.getCenterPoint().distance(tank.getCenterPoint()) : Double.MAX_VALUE, newdist = 0;
            Iterator i = GUI.theGUI.tanks().iterator();
            while(i.hasNext()) {
                Tank t = (Tank)i.next();
                if(t != tank && (newdist = t.getCenterPoint().distance(tank.getCenterPoint())) < distance) {
                    distance = newdist;
                    target = t;
                }
            }
        }
    }
    
    public static class Controller extends TankController.GenericConfiguration<SimplisticAI> {
        
    }
    
}
