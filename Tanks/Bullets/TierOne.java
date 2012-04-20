package Tanks.Bullets;

import Tanks.Tank;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class TierOne extends Bullet 
{
    private final Shape bulletDefinition;
    
    public TierOne (double _x, double _y, double _a, Tank _p) 
    {
        super(_x,_y,_p); 
        
        double bulletSpeed;
        double range;
        
        if(_p.getClass().equals(Tanks.Types.HeavyTank.class))
        {
            bulletDefinition = makeHeavy();
            bulletSpeed = 15;
            range = 50;
        }
        else if(_p.getClass().equals(Tanks.Types.RangeTank.class))
        {
            bulletDefinition = makeRange();
            bulletSpeed = 30;
            range = 70;
        }
        else
        {
            bulletDefinition = makeMage();
            bulletSpeed = 20;    
            range = 40;
        }
        setBullet(bulletSpeed, _a, range);
    }
    
    public Shape form()
    {       
        AffineTransform rotateBullet = new AffineTransform();
        rotateBullet.setToTranslation(x, y);
        rotateBullet.rotate(ba);
        Shape bulletShape = rotateBullet.createTransformedShape(bulletDefinition);
        return bulletShape;
    }
    
    private Shape makeHeavy()
    {
        
        return new Ellipse2D.Double(0, 0, 20, 20);
    }
    
    private Shape makeRange()
    {
        return new Ellipse2D.Double(0, 0, 3, 10);
    }
    
    private Shape makeMage()
    {
        Area a = new Area(new Ellipse2D.Double(0,0,15,15));
        Area b = new Area(new Ellipse2D.Double(2.5,2.5,10,10));
        a.subtract(b);
        return a;
    }
}
