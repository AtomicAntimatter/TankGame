
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import Tanks.*;
import TankController.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GUI extends JPanel implements Runnable
{
    private Dimension d, fd;
    private int width, height;
    
    private boolean runGame;
    private Thread animator;
    private final int DELAY = 30;
    
    private GameField field;
    private Graphics2D myG;
    
    private Set tanks = new HashSet(),
                conts = new HashSet();

    public GUI(Dimension a) 
    {       
        d = a;
        this.setLayout(null);
        
        this.setBackground(Color.black);  
        this.setVisible(true);
        this.setFocusable(true);
             
        Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Resources/cursor.png"));
	this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0,0), "cursor"));       
    } 
    
    public void launchGame(Dimension _fd)
    {
        fd = _fd;
        this.setBounds(0, 0, fd.width, fd.height); 
        width = this.getWidth();
        height = this.getHeight();
           
        field = new GameField(Color.CYAN,new Rectangle2D.Double(width*0.005,width*0.005,width*0.99,height-width*0.01));
        
        Tank testTank = new HeavyTank(Color.CYAN,"TEST","1",new Point2D.Double(width/2,height/2),0,field.getBounds());        
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
          
        animator = new Thread(this);
    	animator.start(); 	
    }
    
    public void cycle() 
    { 	
        Iterator i = conts.iterator();
        while(i.hasNext())
            ((TankController)i.next()).poll();
        i = tanks.iterator();
        while(i.hasNext())
            ((Tank)i.next()).doMove();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);   
        myG = (Graphics2D)g;
     
        field.drawField(myG);
        Iterator i = tanks.iterator();
        while(i.hasNext())
            ((Tank)i.next()).drawTank(myG);
    } 
    
    public void run() 
    {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) 
        {
            if(runGame)
            {
                cycle();
                repaint();
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
    
    public boolean getStatus()
    {
        return runGame;
    }
}