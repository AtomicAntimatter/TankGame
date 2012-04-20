package Tanks.Bullets;

import Tanks.Tank;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

public class HeavyBullet extends Bullet 
{
    public HeavyBullet (double _x, double _y, double _a, Tank _p) 
    {
        super(_x,_y,   //position
              5d,_a, //velocity
              100,     //lifetime
              3,       //size
              _p);     //parent
    }
    
    public Shape form()
    {
        //AffineTransform rotateBullet = new AffineTransform();
        //rotateBullet.setToTranslation(parent.getCenterPoint().x, parent.getCenterPoint().y);
        //rotateBullet.rotate(parent.getBarrelAngle());
        //Shape bulletShape = rotateBullet.createTransformedShape(new Ellipse2D.Double(parent.getCenterPoint().x - x, parent.getCenterPoint().y - y,r,3*r));
        return new Ellipse2D.Double(x, y,r,3*r);
    }
}
