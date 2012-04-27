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
    private final Set<Tank>           tanks = Collections.synchronizedSet(new HashSet());
    private final Set<TankController> conts = Collections.synchronizedSet(new HashSet());
    private final Set<Bullet>         bulls = Collections.synchronizedSet(new HashSet());

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
                if((((HumanController)tc).controlType() & HumanController.CT_KEYBOARD) != 0) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEventDispatcher)tc);  
                }
                if((((HumanController)tc).controlType() & HumanController.CT_MOUSE) != 0)
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
            for(TankController c : conts)
            {
                if(c.getClass().equals(HumanMouseController.class))
                {
                    if(KeyEventDispatcher.class.isInstance(c))
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher((KeyEventDispatcher)c);
                    if(MouseMotionListener.class.isInstance(c))
                        this.removeMouseMotionListener((MouseMotionListener)c);
                    if(MouseListener.class.isInstance(c))
                        this.removeMouseListener((MouseListener)c);
                }
            }
        }
        conts.clear();
    }
    
    public synchronized void cycle() 
    { 	  
        synchronized(conts)
        {
            for(TankController c : conts)
            {
                if(HumanController.class.isInstance(c))
                {       
                    screenPoint = field.getScreenPoint(c.getTank().getCenterPoint());
                    c.setScreenPoint(screenPoint);
                } 
                c.poll();
            }
            
        }

        synchronized(tanks) 
        {
            Set<Tank> deadTanks = new HashSet();
            for(Tank c : tanks)
            {              
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
            Set<Bullet> deadBulls = new HashSet();
            for(Bullet b : bulls)
            {
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
    @DontSynchronize
    public void paintComponent(Graphics g)
    {     
        super.paintComponent(g);
        g.translate(screenPoint.x,screenPoint.y);
        Graphics2D myG = (Graphics2D)g;
             
        field.drawField(myG);

        synchronized(tanks)
        {
            for(Tank t : tanks)
            {
                t.drawTank(myG);
            }
        }
        
        synchronized(bulls) 
        {
            for(Bullet b : bulls)
            {
                b.draw(myG);
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
            for(Tank t : tanks)
            {
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