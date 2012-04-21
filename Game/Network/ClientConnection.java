/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Network;

import Game.GUI;
import Tanks.Tank;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harrison
 */
public class ClientConnection extends Thread {
    private final Tank localTank;
    private final Socket sock;
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;
    private boolean running = true;
    
    public ClientConnection(Tank _us, String host, int port) throws UnknownHostException, IOException {
        sock = new Socket(host, port);
        ois = new ObjectInputStream(sock.getInputStream());
        oos = new ObjectOutputStream(sock.getOutputStream());
        localTank = _us;
    }
    
    @Override
    public void run() {
        try {
            while (running) {
                ServerPacket pack = (ServerPacket) ois.readObject();
                if(GameStatePacket.class.isInstance(pack)) {
                    GameStatePacket gsp = GameStatePacket.class.cast(pack);
                    GUI.theGUI.updateState(gsp.tanks, gsp.bulls);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
