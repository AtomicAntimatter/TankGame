/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Network;

import Tanks.Tank;

/**
 *
 * @author harrison
 */
public class ClientStatePacket implements ClientPacket {
    private final long ID;
    public final Tank tank;
    
    @Override
    public long clientID() {
        return ID;
    }
    
    private ClientStatePacket() {
        ID = -1; tank = null;
    }
    public ClientStatePacket(long _id, Tank _tank) {
        ID = _id;
        tank = _tank;
    }
}
