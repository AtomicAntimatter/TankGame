/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Network;

import Game.GUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author harrison
 */
public class ServerConnection extends Thread {

    private ServerSocket ssock;
    private final Set clients;

    public ServerConnection(int port) throws IOException {
        clients = Collections.synchronizedSet(new HashSet());
        ssock = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            Socket csock = ssock.accept();
            Client cli = new Client(csock);
            clients.add(cli);
            cli.start();
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void broadcast(ServerPacket pack) throws IOException {
        synchronized (clients) {
            Iterator i = clients.iterator();
            while (i.hasNext()) {
                ((Client) i.next()).send(pack);
            }
        }
    }

    public void close() {
        synchronized (clients) {
            Iterator i = clients.iterator();
            while (i.hasNext()) {
                ((Client) i.next()).close();
            }
        }
    }

    private static class Client extends Thread {

        private final Socket sock;
        private final ObjectOutputStream oos;
        private final ObjectInputStream ois;
        private boolean running = true;

        public Client(Socket _sock) throws IOException {
            sock = _sock;
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
        }

        public void send(ServerPacket pack) throws IOException {
            oos.writeObject(pack);
        }

        @Override
        public void run() {
            try {
                while (running) {
                    ClientPacket pack = (ClientPacket) ois.readObject();

                    if (ClientStatePacket.class.isInstance(pack)) {
                        ClientStatePacket csp = ClientStatePacket.class.cast(pack);
                        GUI.theGUI.updateTank(csp.tank);
                    }
                    if (FiredPacket.class.isInstance(pack)) {
                        FiredPacket fp = FiredPacket.class.cast(pack);
                        GUI.theGUI.launchBullet(fp.bullet);
                    }
                }
                sock.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public void close() {
            running = false;
        }
    }
}
