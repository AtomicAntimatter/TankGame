/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks.Bullets;

import Tanks.Tank;
import Tanks.Types.HeavyTank;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author harrison
 */
public final class HeavySpecialBullet extends Bullet {
    private Shape form;
    private static double RMIN;
    private double rsc;
    private int powMax;
    protected int EXPLODE_MAX = 20;
    private int explodeSequence = EXPLODE_MAX + 1;

    protected HeavySpecialBullet(double _x, double _y, double _rsc, Tank _p) {
        super(_x, _y, _p);
        rsc = _rsc;
    }

    public static HeavySpecialBullet make(int x, int y, int powMax, HeavyTank parent) {
        HeavySpecialBullet b = new HeavySpecialBullet(x, y, 10, parent);
        b.form = new Ellipse2D.Double(0, 0, RMIN, RMIN);
        b.powMax = powMax;
        b.setBullet(7, 50);
        return b;
    }

    @Override
    protected Shape form() {
        Shape sh;
        if (explodeSequence <= EXPLODE_MAX) {
            double sc = (1 - EXPLODE_MAX/explodeSequence) * rsc;
            sh = AffineTransform.getScaleInstance(sc, sc).createTransformedShape(form);
        }
        else sh = form;
        AffineTransform bt = AffineTransform.getTranslateInstance(x, y);
        bt.rotate(ba);
        return bt.createTransformedShape(sh);
    }
    
    @Override
    public boolean isDead() {
        return explodeSequence <= 0;
    }

    @Override
    public void checkCollisions() {
        super.checkCollisions();
        if(death)
            explodeSequence--;
    }
    
    @Override
    public int power(Tank t) {
        return (int)Math.max(t.getCenterPoint().distanceSq(x, y) * (1 - EXPLODE_MAX/explodeSequence), 30);
    }
}
