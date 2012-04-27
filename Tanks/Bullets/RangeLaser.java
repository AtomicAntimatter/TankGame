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
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

/**
 *
 * @author harrison
 */
public final class RangeLaser extends Bullet {
    private static double RMIN = 3.0d;
    private double len, lmax, a;
    private int powMax;
    protected int LENGTH_MAX = 50;
    private int lengthSeq = 0;

    protected RangeLaser(double _x, double _y, double _lmax, Tank _p, double _a) {
        super(_x, _y, _p);
        lmax = _lmax;
        a = _a;
    }

    public static RangeLaser make(double x, double y, Tank parent, double _a) {
        RangeLaser b = new RangeLaser(x, y, 50, parent, _a);
        b.setBullet(7, 50);
        return b;
    }

    @Override
    protected Shape form() {
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(a);
        at.scale(lengthSeq,lengthSeq);
        return at.createTransformedShape(new Rectangle2D.Double(x-1, y-1, 2, 2));
    }
    
    @Override 
    public void move() {
        lengthSeq++;
    }
    
    @Override
    public boolean isDead() {
        return lengthSeq > LENGTH_MAX;
    }
    
    @Override
    public int power(Tank t) {
        return 300;
    }
}
