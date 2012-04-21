/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Network;

import Tanks.Bullets.Bullet;

/**
 *
 * @author harrison
 */
public class FiredPacket implements ClientPacket {
    private final long ID;
    public final Bullet bullet;
    
    @Override
    public long clientID() {
        return ID;
    }
    
    private FiredPacket() {
        ID = -1; bullet = null;
    }
    public FiredPacket(long _id, Bullet _bullet) {
        ID = _id; bullet = _bullet;
    }
    
}
