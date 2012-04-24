package Game;

import Resources.DontSynchronize;
import javax.swing.JPanel;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.*;
import Tanks.*;
import TankController.*;
import Tanks.Bullets.Bullet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GUI extends JPanel
{
    public static GUI theGUI = null; 
    private boolean runGame;    
    private GameField field;
    private Point screenPoint;
    private final Set tanks = Collections.synchronizedSet(new HashSet()),
                      conts = Collections.synchronizedSet(new HashSet()),
                      bulls = Collections.synchronizedSet(new HashSet());

    @SuppressWarnings("LeakingThisInConstructor")
    public GUI(Dimension a) 
    {       
        theGUI = this;  
        screenPoint = new Point(0,0);
        this.setLayout(null);
        this.setBackground(Color.black);  
        this.setFocusable(true);
        this.setVisible(true);  
        this.setBounds(0, 0, a.width, a.height); 
    } 

    public synchronized void launchGame(GameController.TankManager tm)
    {           
        field = tm.gf;
        
        for(int i = 0; i < tm.getSize(); i++)
        {
            tanks.add(tm.getTankType(i));
            TankController tc = (TankController)tm.getTankController(i);
            conts.add(tc); 
            if(tm.isHuman(i))
            {
                //KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEventDispatcher)tc);  
                if(((HumanMouseController)tc).isMouse())
                {
                    this.addMouseListener((MouseListener)tc);
                    this.addMouseMotionListener((MouseMotionListener)tc);
                }
            }
        }       
        runGame = true;
    }
    
    public synchronized void endGame()
    {
        runGame = false;
        bulls.clear();
        tanks.clear();
       
        synchronized(conts)
        {
            Iterator i = conts.iterator();
            while(i.hasNext())
            {
                TankController c = (TankController)i.next();
                if(c.getClass().equals(HumanMouseController.class))
                {
                    //KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher((KeyEventDispatcher)c);
                    this.removeMouseListener((MouseListener)c);
                    this.removeMouseListener((MouseListener)c);
                }
            }
        }
        conts.clear();
    }
    
    public synchronized void cycle() 
    { 	  
        Iterator i;
        synchronized(conts)
        {
            i = conts.iterator();
            while(i.hasNext())
            {
                TankController c = (TankController)i.next();
                if(c.getClass().equals(HumanMouseController.class))
                {
                    screenPoint = field.getScreenPoint(c.getTank().getCenterPoint());
                } 
                c.poll();
            }
            
        }

        synchronized(tanks) 
        {
            Set deadTanks = new HashSet();
            i = tanks.iterator();
            while(i.hasNext())
            {
                Tank c = (Tank)i.next();                  
                if(c.isDead())
                {
                    deadTanks.add(c);
                }
                else
                {
                    c.doMove();
                }
            }
            tanks.removeAll(deadTanks);
        }

        synchronized(bulls) 
        {
            Set deadBulls = new HashSet();
            i = bulls.iterator();
            while(i.hasNext()) 
            {
                Bullet b = null;

                b = (Bullet)i.next();

                if(b.isDead())
                {
                    deadBulls.add(b);
                }
                else
                {
                    b.move();
                    b.checkCollisions(); 
                }
            }
            bulls.removeAll(deadBulls);
        }
    }

    @Override
    public synchronized void paintComponent(Graphics g)
    {     
        super.paintComponent(g);
        g.translate(screenPoint.x,screenPoint.y);
        Graphics2D myG = (Graphics2D)g;
             
        field.drawField(myG);
        Iterator i;
        synchronized(tanks)
        {
            i = tanks.iterator();
            while(i.hasNext())
            {
                ((Tank)i.next()).drawTank(myG);
            }
        }
        
        synchronized(bulls) 
        {
            i = bulls.iterator();
            while(i.hasNext())
            {
                ((Bullet)i.next()).draw(myG);
            }
        }
    }    
    
    public synchronized boolean getStatus()
    {
        return runGame;
    }
    
    public synchronized boolean launchBullet(Bullet b) 
    {
        return bulls.add(b);
    }
           
    public synchronized Set tanks() 
    {
        return Collections.unmodifiableSet(tanks);
    }

    public synchronized void updateState(Set _tanks, Set _bulls) {
        tanks.addAll(_tanks);
        bulls.addAll(_bulls);
    }
    
    @DontSynchronize
    public boolean updateTank(Tank replacement) {
        synchronized(tanks) {
            Iterator i = tanks.iterator();
            while(i.hasNext()){
                Tank t = (Tank)i.next();
                if(t.tankID == replacement.tankID) {
                    tanks.remove(t);
                    tanks.add(replacement);
                    return true;
                }
            }
        }
        return false;
    }
    
    public synchronized void deregisterControls(MouseListener ml, MouseMotionListener mml, KeyEventDispatcher ked) {
        if(ml!=null)  removeMouseListener(ml);
        if(mml!=null) removeMouseMotionListener(mml);
        if(ked!=null) KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(ked);
    }
    
    public synchronized boolean deregisterController(TankController c) {
        return conts.remove(c);
    }
}