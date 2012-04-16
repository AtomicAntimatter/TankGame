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
    private JButton singlePlayer, settings, back;
    private JLabel menuIMGL, settingsIMGL;
    private boolean playB = false;
    
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
          
        back = makeButton();
        back.setText("Back");
        back.setBounds((int)(width*0.2),(int)(height*0.8),(int)(width*0.1),(int)(height*0.05));
        
        setUpMenu();
        setUpSettings();
        
        showMenu();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        this.setFocusable(true);
        
        if(e.getSource() == singlePlayer)
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
    
    public boolean getStatus()
    {
        return !playB;
    }
    
    public void showSettings()
    {
        this.removeAll();
        this.add(back);
        this.add(settingsIMGL);
        repaint();
    }
    
    public void showMenu()
    {  
        this.removeAll();
        this.add(singlePlayer);
        this.add(settings);
        this.add(menuIMGL);
        repaint();
    }
    
    public Dimension getFieldDimension()
    {
        return fd;
    }
    
    private void setUpMenu()
    {
        try
        { 
            BufferedImage menuIMG = ImageIO.read(this.getClass().getResource("/Resources/MainMenu.png")); 
            menuIMGL = new JLabel(new ImageIcon(menuIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            menuIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
        
        singlePlayer = makeButton();
        singlePlayer.setText("Single Player");
        singlePlayer.setBounds((int)(width/2-width*0.05),(int)(height/2-height*0.06),(int)(width*0.1),(int)(height*0.05));
        
        settings = makeButton();
        settings.setText("Settings");         
        settings.setBounds((int)(width/2-width*0.05),(int)(height/2+height*0.01),(int)(width*0.1),(int)(height*0.05));                          
    }
    
    private void setUpSettings()
    {
        try
        { 
            BufferedImage settingsIMG = ImageIO.read(this.getClass().getResource("/Resources/Settings.png")); 
            settingsIMGL = new JLabel(new ImageIcon(settingsIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            settingsIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
    }
    
    private JButton makeButton()
    {
        JButton genericButton = new JButton("Generic");
        
        try
        { 
            BufferedImage buttonIMG = ImageIO.read(this.getClass().getResource("/Resources/Button.png"));           
            genericButton.setVerticalTextPosition(JButton.CENTER);
            genericButton.setHorizontalTextPosition(JButton.CENTER);
            genericButton.setIcon(new ImageIcon(buttonIMG.getScaledInstance((int)(width*0.1), (int)(height*0.05), Image.SCALE_SMOOTH)));
            genericButton.setForeground(Color.RED);      
            genericButton.addActionListener(this);
        }catch(Exception e) {}
        
        return genericButton;
    }
}
