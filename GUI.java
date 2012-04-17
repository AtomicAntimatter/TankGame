
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import Tanks.*;
import TankController.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.awt.image.BufferStrategy;

public class GUI extends JPanel implements Runnable
{
    private Dimension d, fd;
    private int width, height;
    
    private boolean runGame;
    private Thread animator;
    private final int DELAY = 30;
    
    private GameField field;
    BufferStrategy myStrategy;
    
    private Set tanks = new HashSet(),
                conts = new HashSet();

    public GUI(Dimension a) 
    {       
        d = a;
        this.setLayout(null);
        this.setBackground(Color.black);  
        this.setFocusable(true);
        this.setVisible(true);
        
        Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Resources/cursor.png"));
	this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0,0), "cursor"));      
    } 
    
    public void setBufferStrategy(BufferStrategy bs)
    {
        myStrategy = bs;
    }
    
    public void launchGame(Dimension _fd)
    {
        fd = _fd;
        this.setBounds(d.width/2-fd.width/2, d.height/2-fd.height/2, fd.width, fd.height); 
        width = this.getWidth();
        height = this.getHeight();
           
        field = new GameField(Color.CYAN,new Rectangle2D.Double(width*0.005,width*0.005,width*0.99,height-width*0.01));
        
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
          
        animator = new Thread(this);
    	animator.start(); 	
    }
    
    public void cycle() 
    { 	
        Iterator i = conts.iterator();
        while(i.hasNext())
        {
            ((TankController)i.next()).poll();
        }
        
        i = tanks.iterator();
        while(i.hasNext())
        {
            ((Tank)i.next()).doMove();
        }
    }

    private void render(Graphics2D myG)
    {     
        field.drawField(myG);
        Iterator i = tanks.iterator();
        while(i.hasNext())
        {
            ((Tank)i.next()).drawTank(myG);
        }
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
                Graphics2D myG = (Graphics2D)myStrategy.getDrawGraphics();
                render(myG);
                myG.dispose();
                myStrategy.show();
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