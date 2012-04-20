package Tanks.Bullets;

import Tanks.Tank;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

public class HeavyBullet extends Bullet 
{
    protected double dir;
    
    public HeavyBullet (double _x, double _y, double _a, Tank _p) 
    {
        super(_x,_y,   //position
              5d,_a, //velocity
              100,     //lifetime
              3,       //size
              _p);     //parent
        dir = _a;
    }
    
    public Shape form()
    {
        AffineTransform rotateBullet = new AffineTransform();
        //rotateBullet.setToTranslation(parent.getCenterPoint().x, parent.getCenterPoint().y);
        rotateBullet.setToRotation(dir,x,y);
        Shape bulletShape = rotateBullet.createTransformedShape(new Ellipse2D.Double(x,y,9*r,4*r));
        return bulletShape;
    }
}
