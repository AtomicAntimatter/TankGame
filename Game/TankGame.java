package Game;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class TankGame
{
    private static JFrame frame;
    private static Thread master;
    private static MainMenu mm;
    private static GUI gui;
    private static GameController gc;
    private static BufferStrategy myStrategy;
    private static GraphicsDevice gd;
    private static Dimension d;
    
    public static void main(String[] Args)
    {
        d = Toolkit.getDefaultToolkit().getScreenSize();
        MasterThread mt = new MasterThread();
        mm = new MainMenu(d); 
        gui = new GUI(d);
        gc = new GameController(true);
        
        frame = new JFrame("Tanks");
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        Image img = Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("/Resources/cursor.png"));
	frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(16,16), "cursor"));
        
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
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
        
        frame.createBufferStrategy(4);
        myStrategy = frame.getBufferStrategy();
        
        mm.addKeyListener(mt);
        gui.addKeyListener(mt);
        
        frame.getContentPane().add(mm);
        mm.launchMenu();
        
        master = new Thread(mt);
        master.start();
    } 
    
    private static class MasterThread implements Runnable, KeyListener
    {
        private final int DELAY = 30;
        
        public void run() 
        {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();

            while (true) 
            {
                gc.setStatus(gui.getStatus(), mm.getStatus());
                gc.setSettings(mm.getSettings());
                
                if(gc.changeWindowMode())
                {
                    if(gc.isWindowMode())
                    {
                        gd.setFullScreenWindow(null);
                        frame.dispose();
                        frame.setUndecorated(false);
                        frame.setVisible(true);
                    }
                    else
                    {
                        frame.dispose(); 
                        frame.setUndecorated(true);
                        gd.setFullScreenWindow(frame);
                    }
                    frame.createBufferStrategy(4);
                    myStrategy = frame.getBufferStrategy();
                }
                
                switch(gc.loadPanel())
                { 
                    case 1:
                        break;
                    case 2:  
                        gui.launchGame(mm.getFieldDimension());
                        frame.getContentPane().remove(gui);
                        frame.getContentPane().add(gui);
                        frame.invalidate();
                        break;
                }
               
                Graphics g = myStrategy.getDrawGraphics();
                if(gui.getStatus())
                {       
                    gui.cycle();  
                    gui.paint(g);
                }
                else if(mm.getStatus())
                {
                    mm.paint(g);
                }
                
                myStrategy.show();
                g.dispose();
       
                timeDiff = System.currentTimeMillis() - beforeTime;
                sleep = DELAY - timeDiff;

                if(sleep < 0)
                {
                    sleep = 2;
                }

                try 
                {
                    Thread.sleep(sleep);
                }catch(InterruptedException e) 
                {
                }

                beforeTime = System.currentTimeMillis();
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
