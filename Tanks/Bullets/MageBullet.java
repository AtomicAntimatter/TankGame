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
public class MageBullet extends Bullet {

    private Shape form;
    private int power;

    protected MageBullet(double _x, double _y, Tank _p, int _pow) {
        super(_x, _y, _p);
        power = _pow;
    }

    //@Override
    public static Bullet make(double _x, double _y, double _a, Tank _p, int tier) {
        MageBullet b;
        switch (tier) {
            case 1:
                b = new MageBullet(_x, _y, _p, 50);
                Area a = new Area(new Ellipse2D.Double(0, 0, 15, 15));
                Area c = new Area(new Ellipse2D.Double(2.5, 2.5, 10, 10));
                a.subtract(c);
                b.form = a;
                b.setBullet(20, _a, 40);
                break;
            default:
                throw new RuntimeException("Invalid MageBullet tier");
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
}
