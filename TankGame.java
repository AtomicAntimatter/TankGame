
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TankGame
{
    private static JFrame frame;
    private static Thread master;
    private static MainMenu mm;
    private static GUI gui;
    private static GameController gc;
    
    public static void main(String[] Args)
    {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        MasterThread mt = new MasterThread();
        mm = new MainMenu(d);
        gui = new GUI(d);
        gc = new GameController(true);
        
        frame = new JFrame("Tanks");
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             
        mm.addKeyListener(mt);
        gui.addKeyListener(mt);
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        if(gd.isFullScreenSupported())
        {
            frame.setUndecorated(true);
            frame.setResizable(false);
            gd.setFullScreenWindow(frame);
            frame.validate();
        }
        else 
        {
            frame.pack();
            frame.setSize(d);
            frame.setVisible(true);
        }

        frame.getContentPane().add(mm);      
        
        master = new Thread(mt);
        master.start();
    } 
    
    private static class MasterThread implements Runnable, KeyListener
    {
        public void run()
        {
            while(true)
            {
                gc.setStatus(gui.getStatus(), mm.getStatus());
                                
                switch(gc.loadPanel())
                { 
                    case 1:
                        break;
                    case 2:  
                        gui.launchGame(mm.getFieldDimension());
                        frame.getContentPane().remove(mm);
                        frame.getContentPane().add(gui);
                        frame.repaint();
                        break;
                }

                try
                {
                    Thread.sleep(30);                       
                }catch(Exception e)
                {
                }
            }
        }
        
        public void keyReleased(KeyEvent e){}
        public void keyTyped(KeyEvent e){}

        public void keyPressed(KeyEvent e)
        {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
                frame.setVisible(false);
                System.exit(0);
            }
        }
    }
}
