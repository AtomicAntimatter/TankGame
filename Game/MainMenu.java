package Game;

import TankController.AI.SimplisticAI;
import TankController.HumanController.Configuration;
import TankController.HumanController;
import TankController.TankController;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.net.*;

public class MainMenu extends JPanel implements ActionListener, ListSelectionListener, ItemListener
{
    private Dimension d;
    private Dimension fd;     
    private int width, height, bhlx, bhly, tankType, mode;
    private JButton game, settings, play, back, apply;
    private JLabel menuIMGL, settingsIMGL, loadoutIMGL, hostL, portL, localHostL;
    private JCheckBox serverON, clientON, secondPlayer;
    private JList optionList, tankList, fieldList;
    private JTextField portT, hostNameT;
    private JCheckBox windowMode;
    private boolean playB, windowB;
    private final String[] optionStr = {"Field Size", "Tank Type", "Multiplayer Client", "Multiplayer Server"};
    private final String[] tankStr = {"Heavy", "Range", "Mage"};
    private final String[] fieldStr = {"20000x20000", "10000x10000", "5000x5000", "1000x1000"};
    private Configuration a, b;
    
    public MainMenu(Dimension _d)
    {
        d = _d;
        fd = d;
        tankType = 0;
        mode = 0;
        
        this.setLayout(null);
        this.setBounds(0, 0, d.width, d.height); 
        this.setBackground(Color.black);  
        this.setVisible(true);
        this.setFocusable(true);
		
        width = this.getWidth();
        height = this.getHeight();
        
        makeLoadout(); 
        makeMain();
        makeSettings();          
    }
     
    public void launchMenu()
    {
        playB = false;
        showMenu();
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == game)
        {     
            showGameLoadout();
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
               this.remove(hostNameT);
               this.remove(portT);
               this.remove(hostL);
               this.remove(portL);
               this.remove(localHostL);
               this.remove(clientON);
               this.remove(serverON);   
               this.add(fieldList);
               this.add(loadoutIMGL);
               repaint();
           }
           if(optionList.getSelectedValue().equals("Tank Type"))
           {
               this.remove(loadoutIMGL);
               this.remove(fieldList);
               this.remove(hostNameT);
               this.remove(portT);
               this.remove(hostL);
               this.remove(portL);
               this.remove(localHostL);
               this.remove(clientON);
               this.remove(serverON);
               this.add(tankList);
               this.add(loadoutIMGL);
               repaint();
           }
           if(optionList.getSelectedValue().equals("Multiplayer Client"))
           {
               this.remove(loadoutIMGL);
               this.remove(fieldList);
               this.remove(tankList);
               this.remove(serverON);
               this.remove(localHostL);
               this.add(clientON);
               this.add(hostL);
               this.add(portL);
               this.add(hostNameT);
               this.add(portT);
               this.add(loadoutIMGL);
               repaint();
           }
           if(optionList.getSelectedValue().equals("Multiplayer Server"))
           {
               this.remove(loadoutIMGL);
               this.remove(fieldList);
               this.remove(tankList);
               this.remove(hostNameT);
               this.remove(hostL);
               this.remove(clientON);
               this.add(serverON);
               this.add(localHostL);
               this.add(portL);
               this.add(portT);
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
    
    public void itemStateChanged(ItemEvent e)
    {
        if(e.getSource() == serverON)
        {
            if(serverON.isSelected())
            {
                mode = 1;
                clientON.setSelected(false);
            }
            else
            {
                mode = 0;
            }
        }          
        if(e.getSource() == clientON)
        {
            if(clientON.isSelected())
            {
                mode = 2;
                serverON.setSelected(false);
            }
            else
            {
                mode = 0;
            }
        }
    }
    
    public boolean getStatus()
    {
        return !playB;
    }
    
    public GameController.GameSettings getSettings()
    {       
        int portNum;
        try
        {
            portNum = Integer.parseInt(portT.getText());
        }
        catch(Exception e)
        {
            portNum = 0;
        }
                  
        return new GameController.GameSettings(windowB, false, portNum, hostNameT.getText(), mode);
    }
    
    public GameController.TankManager getTankSetup()
    {
        GameController.TankManager tm = new GameController.TankManager(fd, d);
        if(secondPlayer.isSelected())
        {
            a = new Configuration(2);
            b = new Configuration(3);
     
            GameController.TankManager.TankStyle t1 = new GameController.TankManager.TankStyle("Player One", Color.CYAN, "1", new Point(fd.width/2-60,fd.height/2), 0, tankType, true);
            HumanController h1 = new HumanController(t1.getTank(), a);
            tm.addTank(t1, h1);
            
            GameController.TankManager.TankStyle t2 = new GameController.TankManager.TankStyle("Player Two", Color.CYAN, "2", new Point(fd.width/2+60,fd.height/2), 0, tankType, true);
            SimplisticAI h2 = new SimplisticAI(t2.getTank());
            tm.addTank(t2, h2);
        }
        else
        {        
            a = new Configuration(1);
            GameController.TankManager.TankStyle t1 = new GameController.TankManager.TankStyle("Player One", Color.CYAN, "1", new Point(fd.width/2-60,fd.height/2), 0, tankType, true);
            GameController.TankManager.TankStyle t2 = new GameController.TankManager.TankStyle("Dummy", Color.GRAY, "2", new Point(fd.width/2+60,fd.height/2), 0, tankType, true);
            HumanController h1 = new HumanController(t1.getTank(), a);
            SimplisticAI h2 = new SimplisticAI(t2.getTank());
            tm.addTank(t1, h1);
            tm.addTank(t2, h2);
        }
        return tm;
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
        this.add(game);
        this.add(settings);
        this.add(menuIMGL);
        repaint();
    }
    
    private void showGameLoadout()
    {
        this.removeAll();
        this.add(play);
        this.add(optionList);
        this.add(back);
        this.add(secondPlayer);
        this.add(loadoutIMGL);
        repaint();
    }
    
    private void makeLoadout()
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
        
        portT = new JTextField("0");
        portT.setBounds((int)(width*0.6), (int)(height/2 - Math.max(height*0.02, 20)), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.02, 20)));
        
        hostNameT = new JTextField("X");
        hostNameT.setBounds((int)(width*0.6), (int)(height/2 + Math.max(height*0.02, 20)*2), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.02, 20)));
        
        portL = new JLabel("PORT NUMBER");
        portL.setBounds((int)(width*0.6), (int)(height/2 - Math.max(height*0.02, 20)*2), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.02, 20)));
        portL.setForeground(Color.RED);
        
        hostL = new JLabel("HOSTNAME");
        hostL.setBounds((int)(width*0.6), (int)(height/2 + Math.max(height*0.02, 20)), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.02, 20)));
        hostL.setForeground(Color.RED);
        
        serverON = new JCheckBox("Server");
        serverON.setBounds((int)(width*0.6), (int)(height/2 + Math.max(height*0.02, 20)*4), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.02, 20)));
        serverON.addItemListener(this);
        serverON.setBackground(Color.BLACK);
        serverON.setForeground(Color.RED);
        
        clientON = new JCheckBox("Client");
        clientON.setBounds((int)(width*0.6), (int)(height/2 + Math.max(height*0.02, 20)*4), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.02, 20)));
        clientON.addItemListener(this);
        clientON.setBackground(Color.BLACK);
        clientON.setForeground(Color.RED);
        
        try
        {
            localHostL = new JLabel("Hostname: " + InetAddress.getLocalHost().getHostName());
        }catch(UnknownHostException e)
        {
            localHostL = new JLabel("Unknown Local Host");
        }
        localHostL.setBounds((int)(width*0.6), (int)(height/2 + Math.max(height*0.02, 20)), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.02, 20)));
        localHostL.setForeground(Color.RED);
        
        secondPlayer = new JCheckBox("Two Player");
        secondPlayer.setBounds((int)(width*0.6), (int)(height*0.75), (int)Math.max(width*0.1, 170), (int)(Math.max(height*0.02, 20)));
        secondPlayer.addItemListener(this);
        secondPlayer.setBackground(Color.BLACK);
        secondPlayer.setForeground(Color.RED);
        
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
        
        game = makeButton();
        game.setText("Game");
        game.setLocation((int)(width/2-bhlx),(int)(height/2-bhly*3.2));
      
        settings = makeButton();
        settings.setText("Settings");         
        settings.setLocation((int)(width/2-bhlx),(int)(height/2-bhly));                          
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
