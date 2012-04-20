package Tanks.Bullets;

import Game.GUI;
import Tanks.Tank;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;

public class Bullet {
    protected double x,y,   //position
                     vx,vy, //velocity
                     r,
                     h;     //elevation = ttl
    protected final Tank parent;
    protected Color color = Color.YELLOW;
    
    protected Bullet(double _x, double _y, double _v, double _a, double _h, double _r, Tank _parent) {
        x=_x; y=_y; h=_h; parent = _parent;
        vx = _v*Math.cos(_a);
        vy = _v*Math.sin(_a);
        r = _r;
    }
    
    protected Shape form() {
        return new Ellipse2D.Double(x,y,2*r,2*r);
    }
    
    public void move() {
        x+=vx;
        y+=vy;
        h--;
    }
    
    public void checkCollisions() {
        Iterator i = GUI.theGUI.tanks().iterator();
        while(i.hasNext()) {
            Tank t = (Tank)(i.next());
            if(t != parent && t.collidesWith(form())) {
                GUI.theGUI.tankHit(t);
            }
        }
        if(h <= 0)
            GUI.theGUI.destroyBullet(this);
    }
    
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        //g2.draw(form());
        //g2.fill(form());
        g2.drawOval((int)x, (int)y, 2*(int)r, 2*(int)r);
    }
}
