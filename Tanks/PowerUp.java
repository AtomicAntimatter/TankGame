/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

import Tanks.Bullets.Bullet;
import java.awt.Shape;

/**
 *
 * @author harrison
 */
public class PowerUp extends Bullet {
    private int power;
    
    protected PowerUp(double _x, double _y, Tank _par, int _pow) {
        super(_x, _y, _par);
        power = _pow;
    }

    @Override
    protected Shape form() {
        return new java.awt.geom.Rectangle2D.Double(x, y, 3, 3);
    }

    @Override
    public int power(Tank t) {
        return power;
    }
    
}
