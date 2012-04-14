import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TankGame
{
    private static JFrame frame;
    
    public static void main(String[] Args) throws Exception
    {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        GUI gui = new GUI(d);
        
        frame = new JFrame("Tanks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             
        gui.addKeyListener(new KeyListener()
        {
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
        });
        
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

        frame.getContentPane().add(gui);       
    } 
}
