
package Tanks.Bullets;

import Tanks.Tank;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class RangeBullet extends Bullet {
    
    private Shape form;
    private int power;
    
    protected RangeBullet(double _x, double _y, Tank _p, int _pow) {
        super(_x, _y, _p);
        power = _pow;
    }

    //@Override
    public static Bullet make(double _x, double _y, Tank _p, int tier) {
        RangeBullet b;
        switch (tier) {
            case 1:
                b = new RangeBullet(_x, _y, _p, 70);
                b.form = new Ellipse2D.Double(0, 0, 3, 10);
                b.setBullet(30, 70);
                break;
            case 2:
                b = new RangeBullet(_x, _y, _p, 100);
                b.form = tierTwo;
                b.setBullet(35, 80);
                break;
            default:
                throw new RuntimeException("Invalid RangeBullet tier");
        }
        return b;
    }

    @Override
    public Shape form() {
        AffineTransform rotateBullet = new AffineTransform();
        rotateBullet.setToTranslation(x, y);
        rotateBullet.rotate(ba);
        Shape bulletShape = rotateBullet.createTransformedShape(form);
        return bulletShape;
    }

    @Override
    public int power(Tank t) {
        return power;
	}
    
    private static final Shape tierTwo;
    static {
        double xPoints[] = {5,10,5,0};           
        double yPoints[] = {0,20,10,20};
               
        GeneralPath bul = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);  
        bul.moveTo(xPoints[0], yPoints[0]);
        
        for(int i = 1; i < xPoints.length; i++)
        {
            bul.lineTo(xPoints[i], yPoints[i]);
        }
        
        bul.closePath();
        
        tierTwo = bul.createTransformedShape(AffineTransform.getRotateInstance(Math.PI/2));
    }
}
