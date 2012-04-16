import Resources.GameResult;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import Tanks.*;
import TankController.*;

public class GUI extends JPanel implements Runnable
{
    private Dimension d;
    private int width, height;
    
    private boolean runGame;
    private Thread animator;
    private final int DELAY = 30;
    
    private GameField field;
    private Tank testTank;
    private TankController testControl;
    private Graphics2D myG;
    
    public GUI(Dimension a) 
    {
        d = a;
        this.setLayout(null);
        this.setBounds(0, 0, d.width, d.height); 
        this.setBackground(Color.black);  
        this.setVisible(true);
        this.setFocusable(true);
             
        Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Resources/cursor.png"));
	this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0,0), "cursor"));
		
        width = this.getWidth();
        height = this.getHeight();
    } 
    
    public void launchGame()
    {
        field = new GameField(Color.CYAN,new Rectangle2D.Double(width*0.005,width*0.005,width*0.99,height-width*0.01));
        
        testTank = new HeavyTank(Color.CYAN,"TEST","1",new Point2D.Double(width/2,height/2),0,field.getBounds());        
        testControl = new HumanController(testTank,KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,KeyEvent.VK_D,KeyEvent.VK_SPACE);
          
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
        testControl.poll();
        testTank.doMove();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);   
        myG = (Graphics2D)g;
     
        field.drawField(myG);
        testTank.drawTank(myG);      
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
    
    public GameResult getGameResult()
    {
        if(runGame) return null;
        return new GameResult();
    }
}