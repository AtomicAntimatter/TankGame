/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks.Bullets;

import Tanks.Tank;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author harrison
 */
public class RangeBullet extends Bullet {
    
    private Shape form;

    protected RangeBullet(double _x, double _y, Tank _p) {
        super(_x, _y, _p);
    }

    //@Override
    public static Bullet make(double _x, double _y, double _a, Tank _p, int tier) {
        RangeBullet b;
        switch (tier) {
            case 1:
                b = new RangeBullet(_x, _y, _p);
                b.form = new Ellipse2D.Double(0, 0, 3, 10);
                b.setBullet(30, _a, 70);
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
}
