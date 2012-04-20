import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class MainMenu extends JPanel implements ActionListener
{
    private Dimension d;
    private Dimension fd;     
    private int width, height, bhlx, bhly;
    private JButton singlePlayer, settings, play, back, apply;
    private JLabel menuIMGL, settingsIMGL, loadoutIMGL;
    private JRadioButton[] fieldSize;
    private JCheckBox windowMode;
    private boolean playB, windowB;
    private int[] xDim = {0,10000,1920,1680,1280,1024,800};
    private int[] yDim = {0,10000,1080,1050,800,768,600}; 
    
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
        bhlx = play.getWidth()/2;
        bhly = play.getHeight()/2;
        play.setLocation((int)(width/2-bhlx),(int)(height/2-bhly));
        
        back = makeButton();
        back.setText("Back");
        back.setLocation((int)(width*0.2),(int)(height*0.8));
        
        try
        { 
            BufferedImage menuIMG = ImageIO.read(this.getClass().getResource("/Resources/Loadout.png")); 
            loadoutIMGL = new JLabel(new ImageIcon(menuIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            loadoutIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
        
        makeMain();
        makeSettings();
        makeSPLoadout();     
    }
     
    public void launchMenu()
    {
        showMenu();
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
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
        for(int i = 0; i < xDim.length; i++)
        {
            if(e.getSource() == fieldSize[i])
            {
                fieldSize[i].setFocusable(false);
                fd = new Dimension(xDim[i], yDim[i]);
            }
        }
        if(e.getSource() == apply)
        {
            windowB = windowMode.isSelected();
        }
        this.requestFocus();
    }
    
    public boolean getStatus()
    {
        return !playB;
    }
    
    public GameController.GameSettings getSettings()
    {
        return new GameController.GameSettings(windowB, false);
    }
    
    public void invertWindowBox()
    {
        windowMode.setSelected(!windowMode.isSelected());
    }
    
    private void showSettings()
    {
        this.removeAll();
        this.add(back);
        this.add(apply);
        this.add(windowMode);
        this.add(settingsIMGL);
        repaint();
    }
    
    private void showMenu()
    {  
        this.removeAll();
        this.add(singlePlayer);
        this.add(settings);
        this.add(menuIMGL);
        repaint();
    }
    
    private void showSPLoadout()
    {
        this.removeAll();
        this.add(play);
        for(int i = 0; i < xDim.length; i++)
        {
            this.add(fieldSize[i]);
        }
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
        singlePlayer.setLocation((int)(width/2-bhlx),(int)(height/2-bhly*1.2));
        
        settings = makeButton();
        settings.setText("Settings");         
        settings.setLocation((int)(width/2-bhlx),(int)(height/2+bhly*1.2));                          
    }
    
    private void makeSettings()
    {
        try
        { 
            BufferedImage settingsIMG = ImageIO.read(this.getClass().getResource("/Resources/Settings.png")); 
            settingsIMGL = new JLabel(new ImageIcon(settingsIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            settingsIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
        
        apply = makeButton();
        apply.setText("Apply");
        apply.setLocation((int)(width*0.2 + bhlx*2.4),(int)(height*0.8));
        
        windowMode = new JCheckBox("Window Mode");
        windowMode.setBackground(Color.BLACK);
        windowMode.setForeground(Color.GREEN);
        windowMode.setBounds((int)(width/2)-(int)Math.max(width*0.1, 170), (int)(height/2), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.03, 30)));
        windowMode.addActionListener(this);
    }
    
    private void makeSPLoadout()
    {  
        fieldSize = new JRadioButton[xDim.length];
        ButtonGroup fieldGroup = new ButtonGroup();
        
        for(int i = 0; i < xDim.length; i++)
        {
            fieldSize[i] = new JRadioButton(String.valueOf(xDim[i]) + "x" + String.valueOf(yDim[i]));
            fieldSize[i].setBackground(Color.BLACK);
            fieldSize[i].setForeground(Color.GREEN);
            fieldSize[i].setBounds((int)(width*0.2), (int)(height/2+30*i), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.03, 30)));
            fieldSize[i].addActionListener(this);
            fieldGroup.add(fieldSize[i]);
        }
        fieldSize[0].setText("Current Resolution");
        fieldSize[0].setSelected(true);
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
            genericButton.setSize(Math.max((int)(width*0.1),200),Math.max((int)(height*0.05),50));
            genericButton.setIcon(new ImageIcon(buttonIMG.getScaledInstance(genericButton.getWidth(), genericButton.getHeight(), Image.SCALE_SMOOTH)));
            genericButton.setForeground(Color.RED);      
            genericButton.addActionListener(this);
            genericButton.setSize(Math.max((int)(width*0.1),200),Math.max((int)(height*0.05),50));
        }catch(Exception e) {}
        
        return genericButton;
    }
}
