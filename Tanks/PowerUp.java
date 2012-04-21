/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

import Tanks.Bullets.Bullet;
import java.awt.Color;
import java.awt.Graphics2D;
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
        color = Color.RED.brighter();
    }
    
    //@Override
    public static Bullet make(double _x, double _y, double _a, Tank _parent, int _power) {
        PowerUp pup = new PowerUp(_x,_y,_parent,_power);
        pup.setBullet(10, _a, 500);
        return pup;
    }

    @Override
    protected Shape form() {
        return new java.awt.geom.Rectangle2D.Double(x, y, 3, 3);
    }

    @Override
    public int power(Tank t) {
        return power;
    }
    
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        g2.setColor(Color.WHITE);
        g2.drawString(Integer.toBinaryString(power), (float)(x+1.5), (float)(y+1.5f));
    }
}
