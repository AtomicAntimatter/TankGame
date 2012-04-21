package Game;

import Tanks.Types.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import Tanks.*;
import TankController.*;
import Tanks.Bullets.Bullet;
import Tanks.Types.MageTank;
import Tanks.Types.RangeTank;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GUI extends JPanel
{
    public static GUI theGUI = null; 
    private Dimension d, fd;
    private int width, height;
    private boolean runGame;    
    private GameField field;   
    private final Set tanks = Collections.synchronizedSet(new HashSet()),
                      conts = Collections.synchronizedSet(new HashSet());
    private Set bulls = Collections.synchronizedSet(new HashSet());

    public GUI(Dimension a) 
    {       
        theGUI = this;
        d = a;
        this.setLayout(null);
        this.setBackground(Color.black);  
        this.setFocusable(true);
        this.setVisible(true);  
        this.setBounds(0, 0, d.width, d.height); 
    } 

    public void launchGame(Dimension _fd)
    {     
        fd = _fd; 
        width = fd.width;
        height = fd.height;
        field = new GameField(Color.CYAN,new Rectangle2D.Double(width*0.005,height*0.005,width*0.99,height*0.99));
        field.setScreenInfo(new Point(d.width/2-fd.width/2, d.height/2-fd.height/2), d);
        
        Tank testTank = new RangeTank(Color.CYAN,"TEST","1",new Point(width/2,height/2),0,field.getBounds());        
        TankController testControl = new HumanController(testTank,KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D,KeyEvent.VK_SPACE);
        tanks.add(testTank);
        conts.add(testControl);
        
        addMouseListener((MouseListener)testControl);
        addMouseMotionListener((MouseMotionListener)testControl);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEventDispatcher)testControl);  
        
        runGame = true;
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
       
    public boolean tankHit(Tank t) 
    {
        return tanks.remove(t);
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
            Iterator i = tanks.iterator();
            while(i.hasNext()) 
            {
                Object t = i.next();
                if(b.satisfied(t))
                {
                    tanks.remove(t);
                }
            }
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