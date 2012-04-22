package Game;

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
import java.util.ConcurrentModificationException;

public class GUI extends JPanel
{
    public static GUI theGUI = null; 
    private boolean runGame;    
    private GameField field;   
    private final Set tanks = Collections.synchronizedSet(new HashSet()),
                      conts = Collections.synchronizedSet(new HashSet()),
                      bulls = Collections.synchronizedSet(new HashSet());

    public GUI(Dimension a) 
    {       
        theGUI = this;  
        this.setLayout(null);
        this.setBackground(Color.black);  
        this.setFocusable(true);
        this.setVisible(true);  
        this.setBounds(0, 0, a.width, a.height); 
    } 

    public void launchGame(GameController.TankManager tm)
    {           
        field = tm.gf;
        
        for(int i = 0; i < tm.getSize(); i++)
        {
            tanks.add(tm.getTankType(i));
            TankController tc = (TankController)tm.getTankController(i);
            conts.add(tc); 
            if(tm.isHuman(i))
            {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEventDispatcher)tc);  
                if(((HumanController)tc).isMouse())
                {
                    this.addMouseListener((MouseListener)tc);
                    this.addMouseMotionListener((MouseMotionListener)tc);
                }
            }
        }       
        runGame = true;
    }
    
    public void endGame()
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
                if(c.getClass().equals(HumanController.class))
                {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher((KeyEventDispatcher)c);
                    this.removeMouseListener((MouseListener)c);
                    this.removeMouseListener((MouseListener)c);
                }
            }
        }
        conts.clear();
    }
    
    public void addNotify()
    {
    	super.addNotify();	
    }
    
    public void cycle() 
    { 	
        try
        {
            Iterator i;
            synchronized(conts)
            {
                i = conts.iterator();
                while(i.hasNext())
                {
                    TankController c = (TankController)i.next();
                    if(c.getClass().equals(HumanController.class))
                    {
                        field.setTankPoint(c.getTank().getCenterPoint());
                    }
                    c.poll();
                }
                field.done();
            }

            synchronized(tanks) 
            {
                i = tanks.iterator();
                while(i.hasNext())
                {
                    Tank c = (Tank)i.next();
                    c.doMove();     
                }
            }

            synchronized(conts) 
            {
                i = conts.iterator();
                while(i.hasNext())
                {
                    TankController c = (TankController)i.next();
                    if(c.getClass().equals(HumanController.class))
                    {
                        c.setScreenPoint(field.getScreenPoint());
                    }  
                }
            }

            synchronized(bulls) 
            {
                Set deadBulls = new HashSet();
                i = bulls.iterator();
                while(i.hasNext()) 
                {
                    Bullet b = (Bullet)i.next();
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

            synchronized(tanks) 
            {
                Set deadTanks = new HashSet();
                i = tanks.iterator();
                while(i.hasNext()) 
                {
                    Tank t = (Tank)i.next();
                    if(t.isDead())
                    {
                        deadTanks.add(t);
                    }
                }
                tanks.removeAll(deadTanks);
            }
        }catch(ConcurrentModificationException e)
        {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g)
    {     
        super.paintComponent(g);
        g.translate(field.getScreenPoint().x,field.getScreenPoint().y);
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
    
    public boolean getStatus()
    {
        return runGame;
    }
    
    public boolean launchBullet(Bullet b) 
    {
        return bulls.add(b);
    }
           
    public Set tanks() 
    {
        return Collections.unmodifiableSet(tanks);
    }

    public void updateState(Set _tanks, Set _bulls) {
        tanks.addAll(_tanks);
        bulls.addAll(_bulls);
    }
    
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
    
    public void deregisterControls(MouseListener ml, MouseMotionListener mml, KeyEventDispatcher ked) {
        if(ml!=null)  removeMouseListener(ml);
        if(mml!=null) removeMouseMotionListener(mml);
        if(ked!=null) KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(ked);
    }
    
    public boolean deregisterController(TankController c) {
        return conts.remove(c);
    }
}