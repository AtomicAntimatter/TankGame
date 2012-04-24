
package Tanks.Bullets;

import Tanks.Tank;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class HeavyBullet extends Bullet {

    private Shape form;
    private int power;

    protected HeavyBullet(double _x, double _y, Tank _p, int _pow) {
        super(_x, _y, _p);
        power = _pow;
    }

    //@Override
    public static Bullet make(double _x, double _y, Tank _p, int tier) {
        HeavyBullet b;
        switch (tier) {
            case 1:
                b = new HeavyBullet(_x, _y, _p, 100);
                b.form = new Ellipse2D.Double(0, 0, 20, 20);
                b.setBullet(15, 50);
                break;
            case 2:
                b = new HeavyBullet(_x, _y, _p, 100);
                b.form = tierTwo;
                b.setBullet(18, 60);
                break;
            default:
                throw new RuntimeException("Invalid HeavyBullet tier");
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
        double xPoints[] = {0,10,10,0,0,10};           
        double yPoints[] = {0,0,20,20,0,0};
               
        GeneralPath bul = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);  
        bul.moveTo(xPoints[0], yPoints[0]);
        
        for(int i = 1; i < xPoints.length-1; i++)
        {
            bul.lineTo(xPoints[i], yPoints[i]);
        }
        bul.curveTo(5, -5, 5, -5, xPoints[5], yPoints[5]);
        
        bul.closePath();
        
        tierTwo = bul.createTransformedShape(AffineTransform.getQuadrantRotateInstance(1));  //TODO: fix alignment bug.
    }

}
