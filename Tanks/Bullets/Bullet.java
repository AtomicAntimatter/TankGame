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
    private Color color = Color.CYAN.darker();
    private boolean death;
    
    protected Bullet(double _x, double _y, Tank _parent) 
    {
        x = _x; 
        y = _y;    
        parent = _parent;  
        ba = parent.getBarrelAngle();
    }
    
    protected abstract Shape form();
    
    protected void setBullet(double _v, double _a, double _h)
    {
        vx = _v*Math.cos(_a) + (parent.getSpeed()*Math.cos(parent.getDirection()));
        vy = _v*Math.sin(_a) + (parent.getSpeed()*Math.sin(parent.getDirection()));
        h = _h;
        x -= form().getBounds().width/2*Math.cos(ba);
        y -= form().getBounds().height/2*Math.sin(ba);
    }
    
    public void move() 
    {
        x+=vx;
        y+=vy;
        h--;
    }
    
    public void checkCollisions() 
    {
        synchronized(GUI.theGUI.tanks())
        {
            Iterator i = GUI.theGUI.tanks().iterator();
            while(i.hasNext()) 
            {
                Tank t = (Tank)(i.next());
                if(t != parent && t.collidesWith(form())) 
                {
                    GUI.theGUI.tankHit(t);
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
            System.out.println("3");
        }
    }
    
    public void draw(Graphics2D g2) 
    {
        g2.setColor(color);
        g2.fill(form());
    }
    
    public boolean isDead()
    {
        return death;
    }
}
