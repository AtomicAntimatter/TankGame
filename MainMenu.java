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
    private JButton singlePlayer, settings, play, back;
    private JLabel menuIMGL, settingsIMGL, loadoutIMGL;
    private JComboBox fieldSize;
    private boolean playB = false;
    private int[] xDim = {0,1920,1680,1280,1024,800};
    private int[] yDim = {0,1080,1050,800,768,600}; 
    
    public MainMenu(Dimension a)
    {
        d = a;
        fd = d;
        xDim[0] = (int)d.getWidth();
        yDim[0] = (int)d.getHeight();
        
        this.setLayout(null);
        this.setBounds(0, 0, d.width, d.height); 
        this.setBackground(Color.black);  
        this.setVisible(true);
        this.setFocusable(true);
		
        width = this.getWidth();
        height = this.getHeight();
        
        play = makeButton();
        play.setText("Play");
        play.setBounds((int)(width/2-width*0.05),(int)(height/2-height*0.1),(int)(width*0.1),(int)(height*0.05));
        
        back = makeButton();
        back.setText("Back");
        back.setBounds((int)(width*0.2),(int)(height*0.8),(int)(width*0.1),(int)(height*0.05));
        
        try
        { 
            BufferedImage menuIMG = ImageIO.read(this.getClass().getResource("/Resources/Loadout.png")); 
            loadoutIMGL = new JLabel(new ImageIcon(menuIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            loadoutIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
        
        makeMain();
        makeSettings();
        makeSPLoadout();
        
        showMenu();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == singlePlayer)
        {     
            showSPLoadout();
        }
        if(e.getSource() == settings)
        {
            showSettings();
        }
        if(e.getSource() == play)
        {
            playB = true;
        }
        if(e.getSource() == back)
        {
            showMenu();
        }
        if(e.getSource() == fieldSize)
        {
            fieldSize.setFocusable(false);
            int choice = fieldSize.getSelectedIndex();
            fd = new Dimension(xDim[choice], yDim[choice]);
        }
        this.requestFocus();
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
    
    public void showSPLoadout()
    {
        this.removeAll();
        this.add(play);
        this.add(fieldSize);
        this.add(back);
        this.add(loadoutIMGL);
        repaint();
    }
    
    public Dimension getFieldDimension()
    {
        return fd;
    }
    
    private void makeMain()
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
    
    private void makeSettings()
    {
        try
        { 
            BufferedImage settingsIMG = ImageIO.read(this.getClass().getResource("/Resources/Settings.png")); 
            settingsIMGL = new JLabel(new ImageIcon(settingsIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            settingsIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
    }
    
    private void makeSPLoadout()
    {  
        String[] screenStrings = new String[xDim.length];
        
        for(int i = 0; i < xDim.length; i++)
        {
            screenStrings[i] = String.valueOf(xDim[i]) + "x" + String.valueOf(yDim[i]);
        }
        
        fieldSize = new JComboBox(screenStrings);
        fieldSize.setSelectedIndex(0);
        fieldSize.setBounds((int)(width*0.2), (int)(height/2), (int)(width*0.1), (int)(Math.max(height*0.03, 30)));
        fieldSize.addActionListener(this);
    }
    
    private void makeTPLoadout()
    {
        
    }
    
    private void makeMPLoadout()
    {
     
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
