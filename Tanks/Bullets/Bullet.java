package Tanks.Bullets;

import Game.GUI;
import Tanks.Tank;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Iterator;

public abstract class Bullet 
{
    protected double x,y,vx,vy,h,ba;
    private final Tank parent;
    protected Color color = Color.CYAN.darker();
    private boolean death;
    public long bulletID = (long)(Long.MAX_VALUE*Math.random());
    
    protected Bullet(double _x, double _y, Tank _parent) 
    {
        x = _x; 
        y = _y;    
        parent = _parent;  
        ba = parent.getBarrelAngle();
    }
    
    public synchronized static Bullet make(double _x, double _y, Tank _p, int tier) {
        throw new RuntimeException("Attempted to make generic bullet");
    }
    protected abstract Shape form();
    public abstract int power(Tank t);
    
    protected synchronized void setBullet(double _v, double _h)
    {
        vx = _v*Math.cos(ba) - (parent.getSpeed()*Math.cos(parent.getDirection()));
        vy = _v*Math.sin(ba) - (parent.getSpeed()*Math.sin(parent.getDirection()));
        h = _h;
        x -= form().getBounds().width/2*Math.cos(ba+Math.PI/2);
        y -= form().getBounds().height/2*Math.sin(ba+Math.PI/2);
    }
    
    public synchronized void move() 
    {
        x+=vx;
        y+=vy;
        h--;
    }
    
    public synchronized void checkCollisions() 
    {
        synchronized(GUI.theGUI.tanks())
        {
            Iterator i = GUI.theGUI.tanks().iterator();
            while(i.hasNext()) 
            {
                Tank t = (Tank)(i.next());
                if(t != parent && t.collidesWith(form())) 
                {
                    t.takeDamage(this.power(t), this);
                    death = true;
                }
                if(t != parent && t.collidesWithShield(form()))
                {
                    death = true;
                }
            }     
        }
        if(h <= 0)
        {
            death = true;
        }
        if(parent.collidesWithWall(form()))
        {
            death = true;
        }
    }
    
    public synchronized void draw(Graphics2D g2) 
    {
        g2.setColor(color);
        g2.fill(form());
    }
    
    public synchronized boolean isDead()
    {
        return death;
    }

    @Override
    public synchronized int hashCode() {
        int hash = 5;
        return hash;
    }
    
    @Override
    public synchronized boolean equals(Object o) {
        return (Bullet.class.isInstance(o) && Bullet.class.cast(o).bulletID == this.bulletID);
    }
}
