
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import Tanks.*;
import TankController.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GUI extends JPanel
{
    private Dimension d, fd;
    private int width, height;
    private boolean runGame;  
    private GameField field;  
    private Set tanks = new HashSet(), conts = new HashSet();

    public GUI(Dimension a) 
    {       
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
        Iterator i = conts.iterator();
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
        
        i = tanks.iterator();
        while(i.hasNext())
        {
            Tank c = (Tank)i.next();
            c.doMove();     
        }
        
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

    public void paintComponent(Graphics g)
    {     
        super.paintComponent(g);
        g.translate(field.getScreenPoint().x,field.getScreenPoint().y);
        Graphics2D myG = (Graphics2D)g;
        
        field.drawField(myG);
        Iterator i = tanks.iterator();
        while(i.hasNext())
        {
            ((Tank)i.next()).drawTank(myG);
        }
    }    
    
    public boolean getStatus()
    {
        return runGame;
    }
}