package TankController;

import Tanks.*;
import java.awt.geom.Point2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.Point;

public class HumanController extends TankController implements MouseMotionListener, MouseListener, KeyEventDispatcher {

    private int up, left, down, right, fire;
    private boolean kU = false, kD = false, kL = false, kR = false;
    private Point2D mousePoint = new Point2D.Double(0, 0);

    public HumanController(Tank t, int _up, int _down, int _left, int _right, int _space) {
        super(t);
        up = _up;
        down = _down;
        left = _left;
        right = _right;
        fire = _space;
    }

    public void poll() {
        tank.move((kU ? 1 : 0) - (kD ? 1 : 0));
        tank.rotate((kR ? 1 : 0) - (kL ? 1 : 0));
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        boolean pressed = e.getID() == KeyEvent.KEY_PRESSED;
        int ev = e.getKeyCode();

        if (ev == up) {
            kU = pressed;
            return true;
        }
        if (ev == down) {
            kD = pressed;
            return true;
        }
        if (ev == left) {
            kL = pressed;
            return true;
        }
        if (ev == right) {
            kR = pressed;
            return true;
        }
        if (ev == fire) {
            tank.fire();
            return true;
        }
        if (ev == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        return false;
    }

    public void mouseDragged(java.awt.event.MouseEvent e) {
        mousePoint = new Point2D.Double(e.getX() + 16, e.getY() + 16);
        tank.movePoint(mousePoint);

    }

    public void mouseMoved(java.awt.event.MouseEvent e) {
        mouseDragged(e);
    }

    public void mouseClicked(java.awt.event.MouseEvent e) {
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    public void mouseExited(java.awt.event.MouseEvent e) {
    }
}
