package Game;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;

public class MainMenu extends JPanel implements ActionListener, ListSelectionListener
{
    private Dimension d;
    private Dimension fd;     
    private int width, height, bhlx, bhly, tankType;
    private JButton singlePlayer, twoPlayer, multiPlayer, settings, play, back, apply;
    private JLabel menuIMGL, settingsIMGL, loadoutIMGL;
    private JList optionList, tankList, fieldList;
    private JCheckBox windowMode;
    private boolean playB, windowB;
    private final String[] optionStr = {"Field Size", "Tank Type"};
    private final String[] tankStr = {"Heavy", "Range", "Mage"};
    private final String[] fieldStr = {"20000x20000", "10000x10000", "5000x5000", "1000x1000"};
  
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
        
        makeGeneral();
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
        if(e.getSource() == apply)
        {
            windowB = windowMode.isSelected();
        }
        this.requestFocus();
    }
    
    public void valueChanged(ListSelectionEvent e)
    {
       if((e.getSource() == optionList)&&(!e.getValueIsAdjusting()))
       {    
           if(optionList.getSelectedValue().equals("Field Size"))
           {
               this.remove(loadoutIMGL);
               this.remove(tankList);
               this.add(fieldList);
               this.add(loadoutIMGL);
               repaint();
           }
           if(optionList.getSelectedValue().equals("Tank Type"))
           {
               this.remove(loadoutIMGL);
               this.remove(fieldList);
               this.add(tankList);
               this.add(loadoutIMGL);
               repaint();
           }
       }
       if((e.getSource() == fieldList)&&(!e.getValueIsAdjusting()))
       {
           String fieldSizeStr = (String)fieldList.getSelectedValue();
           String[] dimStr = fieldSizeStr.split("x");
           fd = new Dimension(Integer.parseInt(dimStr[0]), Integer.parseInt(dimStr[1]));
       }
       if((e.getSource() == tankList)&&(!e.getValueIsAdjusting()))
       {
           tankType = tankList.getSelectedIndex();
       }
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
        this.add(twoPlayer);
        this.add(multiPlayer);
        this.add(settings);
        this.add(menuIMGL);
        repaint();
    }
    
    private void showSPLoadout()
    {
        this.removeAll();
        this.add(play);
        this.add(optionList);
        this.add(back);
        this.add(loadoutIMGL);
        repaint();
    }
    
    private void showTPLoadout()
    {
        this.add(back);
        repaint();
    }
    
    private void showMPLoadout()
    {
        this.add(back);
        repaint();
    }
    
    public Dimension getFieldDimension()
    {
        return fd;
    }
    
    private void makeGeneral()
    {
        play = makeButton();
        play.setText("Play");
        bhlx = play.getWidth()/2;
        bhly = play.getHeight()/2;
        play.setLocation((int)(width/2-bhlx),(int)(height/2-bhly));
        
        back = makeButton();
        back.setText("Back");
        back.setLocation((int)(width*0.2),(int)(height*0.8));
        
        optionList = new JList(optionStr);
        optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        optionList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        optionList.setBounds((int)(width*0.2), (int)(height/2), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.2, 200)));
        optionList.setBackground(Color.BLACK);
        optionList.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.CYAN));
        optionList.setForeground(Color.RED);
        optionList.addListSelectionListener(this);
        
        tankList = new JList(tankStr);
        tankList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tankList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        tankList.setBounds((int)(width*0.6), (int)(height/2), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.2, 200)));
        tankList.setBackground(Color.BLACK);
        tankList.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.CYAN));
        tankList.setForeground(Color.RED);
        tankList.addListSelectionListener(this);
        
        fieldList = new JList(fieldStr);
        fieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fieldList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fieldList.setBounds((int)(width*0.6), (int)(height/2), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.2, 200)));
        fieldList.setBackground(Color.BLACK);
        fieldList.setBorder(BorderFactory.createEtchedBorder(Color.RED, Color.CYAN));
        fieldList.setForeground(Color.RED);
        fieldList.addListSelectionListener(this);
        
        try
        { 
            BufferedImage menuIMG = ImageIO.read(this.getClass().getResource("/Resources/Loadout.png")); 
            loadoutIMGL = new JLabel(new ImageIcon(menuIMG.getScaledInstance((int)(width*0.8), (int)(height*0.8), Image.SCALE_SMOOTH)));
            loadoutIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
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
        singlePlayer.setLocation((int)(width/2-bhlx),(int)(height/2-bhly*3.2));
        
        twoPlayer = makeButton();
        twoPlayer.setText("Two Player");
        twoPlayer.setLocation((int)(width/2-bhlx),(int)(height/2-bhly));
        
        multiPlayer = makeButton();
        multiPlayer.setText("Multi-Player");
        multiPlayer.setLocation((int)(width/2-bhlx),(int)(height/2+bhly*1.2));
        
        settings = makeButton();
        settings.setText("Settings");         
        settings.setLocation((int)(width/2-bhlx),(int)(height/2+bhly*3.4));                          
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
