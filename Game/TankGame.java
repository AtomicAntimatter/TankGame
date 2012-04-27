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
    private static boolean fullscreen;
    private static boolean isServer;
    
    public static void main(String[] Args)
    {
        isServer = true;
        
        d = Toolkit.getDefaultToolkit().getScreenSize();
        MasterThread mt = new MasterThread();
        mm = new MainMenu(d); 
        gui = new GUI(d);
        gc = new GameController(true);
        
        frame = new JFrame("Tanks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setLayout(null);
        
        Image img = Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("/Resources/cursor.png"));
	frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH), new Point(16,16), "cursor"));
        
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        
        if(gd.isFullScreenSupported())
        {   
            fullscreen = true;
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
        private final int DELAY = 50;
        
        @Override
        public void run() 
        {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();

            while(true) 
            {
                gc.setStatus(gui.getStatus(), mm.getStatus());
                
                switch(gc.loadPanel())
                { 
                    case 1:
                        mm.launchMenu();
                        frame.getContentPane().remove(gui);
                        frame.getContentPane().add(mm);
                        frame.invalidate();
                        break;
                    case 2:  
                        gui.launchGame(mm.getTankSetup());
                        frame.getContentPane().remove(mm);
                        frame.getContentPane().add(gui);
                        frame.invalidate();
                        break;
                }
                
                if(fullscreen)
                {
                    try
                    {
                        Graphics g = myStrategy.getDrawGraphics();
                        if(gui.getStatus())
                        {       
                            gui.cycle();  
                            gui.paint(g);
                            gui.requestFocus();
                        }
                        else if(mm.getStatus())
                        {
                            mm.paint(g);
                        }

                        myStrategy.show();
                        g.dispose();
                    }catch(Exception e){}
                }
                else
                {
                    if(gui.getStatus())
                    {       
                        gui.cycle();  
                        gui.repaint();
                        gui.requestFocus();
                    }
                }
                    
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
        
        @Override
        public void keyReleased(KeyEvent e){}
        @Override
        public void keyTyped(KeyEvent e){}

        @Override
        public void keyPressed(KeyEvent e)
        {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
                frame.setVisible(false);
                System.exit(0);
            }
            else if(e.getKeyCode() == KeyEvent.VK_F1)
            {
                if(fullscreen)
                {
                    gd.setFullScreenWindow(null);
                    frame.dispose();
                    frame.setUndecorated(false);
                    frame.setVisible(true);
                    frame.setSize(d);       
                    fullscreen = false;
                }
                else
                {
                    frame.dispose(); 
                    frame.setUndecorated(true);
                    gd.setFullScreenWindow(frame);
                    fullscreen = true;
                    frame.createBufferStrategy(4);
                    myStrategy = frame.getBufferStrategy();
                }   
            }
            else if((e.getKeyCode() == KeyEvent.VK_F2)&&(gui.getStatus()))
            {
                gui.endGame();
            }
        }
    }
}
