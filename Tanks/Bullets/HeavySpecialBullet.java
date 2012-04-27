/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks.Bullets;

import Tanks.Tank;
import Tanks.Types.HeavyTank;
import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Message1_1Impl;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.text.NumberFormat;

/**
 *
 * @author harrison
 */
public final class HeavySpecialBullet extends Bullet {
    private Shape form;
    private static double RMIN = 3.0d;
    private final static NumberFormat nf = NumberFormat.getNumberInstance();
    static {
        nf.setMaximumFractionDigits(3);
    }
    private double rsc;
    private int powMax;
    protected int EXPLODE_MAX = 20;
    private int explodeSequence = -1;

    protected HeavySpecialBullet(double _x, double _y, double _rsc, Tank _p) {
        super(_x, _y, _p);
        rsc = _rsc;
    }

    public static HeavySpecialBullet make(int x, int y, int powMax, HeavyTank parent) {
        HeavySpecialBullet b = new HeavySpecialBullet(x, y, 50, parent);
        b.form = new Ellipse2D.Double(-RMIN/2, -RMIN/2, RMIN, RMIN);
        b.powMax = powMax;
        b.setBullet(7, 50);
        return b;
    }

    @Override
    protected Shape form() {
        AffineTransform bt = AffineTransform.getTranslateInstance(x, y);
        if (explodeSequence > 0) {
            double s = rsc * explodeSequence / EXPLODE_MAX;
            bt.scale(s, s);
            message = nf.format(s);
        }
        return bt.createTransformedShape(form);
    }
    
    @Override 
    public void move() {
        if(!death)
            super.move();
    }
    
    @Override
    public boolean isDead() {
        return explodeSequence > EXPLODE_MAX;
    }

    @Override
    public void checkCollisions() {
        super.checkCollisions();
        if(death)
            explodeSequence++;
    }
    
    @Override
    public int power(Tank t) {
        return (int)Math.max(t.getCenterPoint().distance(x, y) * powMax * explodeSequence/EXPLODE_MAX, 30);
    }
}
