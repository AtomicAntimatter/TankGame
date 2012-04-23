
package Tanks;

import Tanks.Bullets.Bullet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

public class PowerUp extends Bullet 
{
    private int power;
    
    protected PowerUp(double _x, double _y, Tank _par, int _pow) 
    {
        super(_x, _y, _par);
        power = _pow;
        h = 600;
        x -= form().getBounds().width/2*Math.cos(ba);
        y -= form().getBounds().height/2*Math.sin(ba);
        color = Color.RED.brighter();
    }
    
    //@Override
    public synchronized static Bullet make(double _x, double _y, double _a, Tank _parent, int _power) 
    {
        PowerUp pup = new PowerUp(_x,_y,_parent,_power);
        pup.vx = Math.cos(_a);
        pup.vy = Math.sin(_a);
        return pup;
    }

    @Override
    protected synchronized Shape form() {
        return new java.awt.geom.Rectangle2D.Double(x, y, 10, 10);
    }

    @Override
    public synchronized int power(Tank t) {
        return power;
    }
    
    @Override
    public synchronized void draw(Graphics2D g2) 
    {
        super.draw(g2);
        g2.setColor(Color.WHITE);
        g2.drawString(Integer.toString(power), (float)(x+5), (float)(y+5f));
    }
}
