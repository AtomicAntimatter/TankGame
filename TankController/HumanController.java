/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TankController;

import Tanks.Tank;

/**
 *
 * @author harrison
 */
public abstract class HumanController extends TankController {
    public static final int CT_MOUSE = 0x1, CT_KEYBOARD = 0x2, CT_JOYSTICK = 0x4;

    public HumanController(Tank t) {
        super(t);
    }

    public abstract int controlType();
    
}
