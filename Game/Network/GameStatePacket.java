/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Network;

import java.util.Collections;
import java.util.Set;

/**
 *
 * @author harrison
 */
public class GameStatePacket implements ServerPacket {
    public final Set bulls, tanks;
   
    private GameStatePacket() {
        bulls = tanks = null;
    }
    public GameStatePacket(Set _bulls, Set _tanks) {
        bulls = Collections.unmodifiableSet(_bulls);
        tanks = Collections.unmodifiableSet(_tanks);
    }
}
