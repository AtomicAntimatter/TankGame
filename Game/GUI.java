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

public class GUI extends JPanel
{
    public static GUI theGUI = null; 
    private boolean runGame;    
    private GameField field;   
    private Set tanks = Collections.synchronizedSet(new HashSet()),
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
            
            if(tm.isHuman(i)&&tm.isMouse(i))
            {
                HumanController hc = tm.getHumanTankControl(i);
                conts.add(hc);      
                this.addMouseListener((MouseListener)hc);
                this.addMouseMotionListener((MouseMotionListener)hc);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEventDispatcher)hc);  
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
            Set newBulls = Collections.synchronizedSet(new HashSet());
            i = bulls.iterator();
            while(i.hasNext()) 
            {
                Bullet b = (Bullet)i.next();
                if(!b.isDead())
                {
                    newBulls.add(b);
                }
            }
            bulls = newBulls;
        }
        
        synchronized(tanks) 
        {
            Set newTanks = Collections.synchronizedSet(new HashSet());
            i = tanks.iterator();
            while(i.hasNext()) 
            {
                Tank t = (Tank)i.next();
                if(!t.isDead())
                {
                    newTanks.add(t);
                }
            }
            tanks = newTanks;
        }
          
        synchronized(bulls) 
        {
            i = bulls.iterator();
            while(i.hasNext()) 
            {
                Bullet b = (Bullet)i.next();
                b.move();
                b.checkCollisions();
            }
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
    
    public static interface BooleanPredicate 
    {
        public boolean satisfied(Object o);
    }
    
    public void deleteIfTank(BooleanPredicate b) 
    {
        synchronized(tanks)
        {
            Set newTanks = Collections.synchronizedSet(new HashSet());
            Iterator i = tanks.iterator();
            while(i.hasNext()) 
            {
                Object t = i.next();
                if(!b.satisfied(t))
                {
                    newTanks.add(tanks);
                }
            }
            tanks = newTanks;
        }
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
}