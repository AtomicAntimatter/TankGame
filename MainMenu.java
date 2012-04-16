import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class MainMenu extends JPanel implements ActionListener
{
    private Dimension d;
    private Dimension fd;
    private int width, height;
    private JButton play, settings, back;
    private JLabel menuIMGL;
    boolean playB = false;
    
    public MainMenu(Dimension a)
    {
        d = a;
        fd = d;
        this.setLayout(null);
        this.setBounds(0, 0, d.width, d.height); 
        this.setBackground(Color.black);  
        this.setVisible(true);
        this.setFocusable(true);
		
        width = this.getWidth();
        height = this.getHeight();
        
        play = new JButton("Play");
        settings = new JButton("Settings");
        back = new JButton("Back");
        
        play.setBounds((int)(width/2-50),(int)(height/2-40),100,20);
        settings.setBounds((int)(width/2-50),(int)(height/2-10),100,20);
        back.setBounds((int)(width/2-50),(int)(height/2+20),100,20);
        
        play.addActionListener(this);
        settings.addActionListener(this);
        back.addActionListener(this);
        
        try
        { 
            BufferedImage menuIMG = ImageIO.read(this.getClass().getResource("/Resources/MainMenu.png")); 
            menuIMGL = new JLabel(new ImageIcon(menuIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            menuIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
                
        this.add(play);
        this.add(settings);
        this.add(back);
        this.add(menuIMGL);
        
        showMenu();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        this.setFocusable(true);
        
        if(e.getSource() == play)
        {     
            playB = true;
        }
        if(e.getSource() == settings)
        {
            showSettings();
        }
        if(e.getSource() == back)
        {
            showMenu();
        }
    }
    
    public Object[] getMenuStatus()
    {
        Object[] menuStatus = {playB, fd}; 
        return menuStatus;
    }
    
    public void showSettings()
    {
        play.setVisible(false);
        settings.setVisible(false);
        back.setVisible(true);
    }
    
    public void showMenu()
    {  
        play.setVisible(true);
        settings.setVisible(true);
        back.setVisible(false);
    }
}
