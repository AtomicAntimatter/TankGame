import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import Tanks.*;

public class GUI extends JPanel implements Runnable
{
    private Dimension d;
    private int width, height;
    
    private Thread animator;
    private final int DELAY = 30;
    
    private GameField field;
    private Tank test;
    private Graphics2D myG;
    
    public GUI(Dimension a) throws Exception
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
    
    public void addNotify()
    {
    	super.addNotify();
        
        field = new GameField(Color.CYAN, new Rectangle2D.Double(width*0.005, 
                width*0.005, width*0.99, height-width*0.01));
        
        test = new Player(Color.CYAN,"TEST","1",new Point2D.Double(width/2,
                height/2),0,KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_A,
                KeyEvent.VK_D,KeyEvent.VK_SPACE, field.getBounds());        
               
        addMouseListener((MouseListener)test);
        addMouseMotionListener((MouseMotionListener)test);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEventDispatcher)test);
        
        animator = new Thread(this);
    	animator.start(); 	
    }
    
    public void cycle() 
    { 	
        test.moveTank();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);   
        myG = (Graphics2D)g;
     
        field.drawField(myG);
        test.drawTank(myG);
        
        Line2D line = new Line2D.Double(test.getCenterPoint(), test.getMousePoint());
        myG.setColor(Color.green);
        myG.draw(line);
    } 
    
    public void run() 
    {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) 
        {
            cycle();
            repaint();

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
}