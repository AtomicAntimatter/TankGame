package Game;

import TankController.AI.SimplisticAI;
import TankController.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.net.*;

public final class MainMenu extends JPanel implements ActionListener, ListSelectionListener, ItemListener
{
    private final Dimension d;      
    private final int width, height, bhlx, bhly; 
    private static int tankType, mode;
    private static final JButton play;
    private static final JLabel hostL, portL, aiL;
    private static final JCheckBox serverON, clientON, secondPlayer;
    private static final JList optionList, tankList, fieldList;
    private static final JTextField portT, hostNameT, aiT;   
    private static final String[] optionStr = {"Field Size", "Tank Type", "AI", "Multiplayer Client", "Multiplayer Server"};
    private static final String[] tankStr = {"Heavy", "Range", "Mage"};
    private static final String[] fieldStr = {"20000x20000", "10000x10000", "5000x5000", "1000x1000"};
    private static Dimension fd;
    private static JLabel loadoutIMGL, localHostL;
    private static TankController.GenericConfiguration a, b;
    private static boolean playB;
    private static final ExitField ef;
    
    static
    {
        play = new JButton("Play");
        optionList = new JList(optionStr);
        tankList = new JList(tankStr);
        fieldList = new JList(fieldStr);
        portL = new JLabel("PORT NUMBER");
        hostL = new JLabel("HOSTNAME");
        aiL = new JLabel("Number of AIs");
        portT = new JTextField("0");
        hostNameT = new JTextField("X");
        aiT = new JTextField("0");
        serverON = new JCheckBox("Server");
        clientON = new JCheckBox("Client");
        secondPlayer = new JCheckBox("Two Player");   
        
        ef = new ExitField();
        
        try
        {
            localHostL = new JLabel("Hostname: " + InetAddress.getLocalHost().getHostName());
        }catch(Exception e){}
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
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
        this.addKeyListener(ef);
		
        width = this.getWidth();
        height = this.getHeight();
        
        try
        { 
            BufferedImage buttonIMG = ImageIO.read(this.getClass().getResource("/Resources/Button.png"));           
            play.setVerticalTextPosition(JButton.CENTER);
            play.setHorizontalTextPosition(JButton.CENTER);
            play.setSize(Math.max((int)(width*0.1),200),Math.max((int)(height*0.05),50));
            play.setIcon(new ImageIcon(buttonIMG.getScaledInstance(play.getWidth(), play.getHeight(), Image.SCALE_SMOOTH)));
            play.setForeground(Color.RED);      
            play.addActionListener(this);
            play.setSize(Math.max((int)(width*0.1),200),Math.max((int)(height*0.05),50));
        }catch(Exception e) {}
        
        try
        { 
            BufferedImage menuIMG = ImageIO.read(this.getClass().getResource("/Resources/MegaTanks.png")); 
            loadoutIMGL = new JLabel(new ImageIcon(menuIMG.getScaledInstance(width,height, Image.SCALE_SMOOTH)));
            loadoutIMGL.setBounds(0,0,width,height);
        }catch(Exception e) {}
        
        bhlx = play.getWidth()/2;
        bhly = play.getHeight()/2;
        play.setLocation(width/2-bhlx,height/2-bhly);
        
        makeLoadout();        
    }
     
    public void launchMenu()
    {
        playB = false;
        showGameLoadout();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == play)
        {
            this.removeAll();
            this.revalidate();
            repaint();
            playB = true;     
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
       if((e.getSource() == optionList)&&(!e.getValueIsAdjusting()))
       {    
           if(optionList.getSelectedValue().equals("Field Size"))
           {
               this.removeAll();   
               this.add(fieldList);
               this.add(play);
               this.add(optionList);
               this.add(secondPlayer);
               this.add(loadoutIMGL);
               repaint();
           }
           if(optionList.getSelectedValue().equals("Tank Type"))
           {
               this.removeAll();
               this.add(tankList);
               this.add(play);
               this.add(optionList);
               this.add(secondPlayer);
               this.add(loadoutIMGL);
               repaint();
           }
           if(optionList.getSelectedValue().equals("Multiplayer Client"))
           {
               this.removeAll();
               this.add(clientON);
               this.add(hostL);
               this.add(portL);
               this.add(hostNameT);
               this.add(portT);
               this.add(play);
               this.add(optionList);
               this.add(secondPlayer);
               this.add(loadoutIMGL);
               repaint();
           }
           if(optionList.getSelectedValue().equals("Multiplayer Server"))
           {
               this.removeAll();
               this.add(serverON);
               this.add(localHostL);
               this.add(portL);
               this.add(portT);
               this.add(play);
               this.add(optionList);
               this.add(secondPlayer);
               this.add(loadoutIMGL);
               repaint();
           }
           if(optionList.getSelectedValue().equals("AI"))
           {
               this.removeAll();
               this.add(aiL);
               this.add(aiT);
               this.add(play);
               this.add(optionList);
               this.add(secondPlayer);
               this.add(loadoutIMGL);
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
    
    @Override
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
                  
        return new GameController.GameSettings(portNum, hostNameT.getText(), mode);
    }
    
    public GameController.TankManager getTankSetup()
    {
        int aiNum;
        try
        {
            aiNum = Integer.parseInt(aiT.getText());
        }
        catch(Exception e)
        {
            aiNum = 6;
        }
        
        GameController.TankManager tm = new GameController.TankManager(fd, d);
        if(secondPlayer.isSelected())
        {
            a = new HumanMouseController.Configuration(1);
            b = new HumanKeyboardController.Configuration();
     
            GameController.TankManager.TankStyle t1 = new GameController.TankManager.TankStyle("Player One", Color.CYAN, "1", new Point(fd.width/2-60,fd.height/2), 0, tankType);
            TankController h1 = a.instantiate(t1);
            tm.addTank(t1, h1);
            
            GameController.TankManager.TankStyle t2 = new GameController.TankManager.TankStyle("Player Two", Color.CYAN, "2", new Point(fd.width/2+60,fd.height/2), 0, tankType);
            TankController h2 = b.instantiate(t2);
            tm.addTank(t2, h2);
        }
        else
        {        
            a = new HumanMouseController.Configuration(0);
            GameController.TankManager.TankStyle t1 = new GameController.TankManager.TankStyle("Player One", Color.CYAN, "1", new Point(fd.width/2-60,fd.height/2), 0, tankType);
            TankController h1 = a.instantiate(t1);
            tm.addTank(t1, h1);
        }
        
        for(int i = 0; i < aiNum; i++)
        {
            int aiType = (int)(Math.random()*3);
            Point randPoint = new Point((int)(Math.random()*fd.width*0.9 +0.05),(int)(Math.random()*fd.height*0.9 +0.05));
            GameController.TankManager.TankStyle at = new GameController.TankManager.TankStyle("AI " + String.valueOf(i+1), Color.RED.darker(), "A" + String.valueOf(i+1), randPoint, 0, aiType);
            SimplisticAI ac = new SimplisticAI(at.getTank());
            tm.addTank(at, ac);
        }
        
        return tm;
    }
    
    private void showGameLoadout()
    {
        this.removeAll();
        this.add(play);
        this.add(optionList);
        this.add(secondPlayer);
        this.add(loadoutIMGL);
        repaint();
    }
    
    private void makeLoadout()
    {      
        optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        optionList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        optionList.setBounds((int)(width*0.1),(int)(height*0.35), (int)Math.max(width*0.1, 200), (int)(Math.max(height*0.2, 300)));
        optionList.setBackground(Color.BLACK);
        optionList.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker()));
        optionList.setForeground(Color.RED);
        optionList.addListSelectionListener(this);            
        tankList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tankList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        tankList.setBounds((int)(width*0.65),(int)(height*0.35), (int)Math.max(width*0.1, 200), (int)(Math.max(height*0.2, 300)));
        tankList.setBackground(Color.BLACK);
        tankList.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker()));
        tankList.setForeground(Color.RED);
        tankList.addListSelectionListener(this);           
        fieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fieldList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fieldList.setBounds((int)(width*0.65),(int)(height*0.35), (int)Math.max(width*0.1, 200), (int)(Math.max(height*0.2, 300)));
        fieldList.setBackground(Color.BLACK);
        fieldList.setBorder(BorderFactory.createLineBorder(Color.CYAN.darker()));
        fieldList.setForeground(Color.RED);
        fieldList.addListSelectionListener(this);          
        portT.setBounds((int)(width*0.65),(int)(height*0.4 - Math.max(height*0.02, 20)),(int)Math.max(width*0.1, 170),(int)(Math.max(height*0.02,20)));         
        portT.addKeyListener(ef);
        hostNameT.setBounds((int)(width*0.65),(int)(height*0.4 + Math.max(height*0.02, 20)*2),(int)Math.max(width*0.1, 170),(int)(Math.max(height*0.02,20)));         
        hostNameT.addKeyListener(ef);
        aiT.setBounds((int)(width*0.65),(int)(height*0.4 - Math.max(height*0.02, 20)),(int)Math.max(width*0.1, 170),(int)(Math.max(height*0.02,20)));            
        aiT.addKeyListener(ef);
        portL.setBounds((int)(width*0.65),(int)(height*0.4 - Math.max(height*0.02, 20)*2),(int)Math.max(width*0.1, 170),(int)(Math.max(height*0.02,20)));
        portL.setForeground(Color.RED);             
        hostL.setBounds((int)(width*0.65),(int)(height*0.4 + Math.max(height*0.02, 20)),(int)Math.max(width*0.1, 170),(int)(Math.max(height*0.02,20)));
        hostL.setForeground(Color.RED);            
        aiL.setBounds((int)(width*0.65),(int)(height*0.4 - Math.max(height*0.02, 20)*2),(int)Math.max(width*0.1, 170),(int)(Math.max(height*0.02,20)));
        aiL.setForeground(Color.RED);                     
        serverON.setBounds((int)(width*0.65),(int)(height*0.4 + Math.max(height*0.02, 20)*4),(int)Math.max(width*0.1, 170),(int)(Math.max(height*0.02,20)));
        serverON.addItemListener(this);
        serverON.setBackground(Color.BLACK);
        serverON.setForeground(Color.RED);         
        clientON.setBounds((int)(width*0.65),(int)(height*0.4 + Math.max(height*0.02, 20)*4),(int)Math.max(width*0.1, 170),(int)(Math.max(height*0.02,20)));
        clientON.addItemListener(this);
        clientON.setBackground(Color.BLACK);
        clientON.setForeground(Color.RED);          
        localHostL.setBounds((int)(width*0.65),(int)(height*0.4 + Math.max(height*0.02, 20)),(int)Math.max(width*0.1, 250),(int)(Math.max(height*0.02,20)));
        localHostL.setForeground(Color.RED);          
        secondPlayer.setBounds((int)(width*0.5-Math.max(width*0.1, 200)/2),(int)(height*0.6), (int)Math.max(width*0.1, 200),(int)(Math.max(height*0.02, 20)));
        secondPlayer.addItemListener(this);
        secondPlayer.setBackground(Color.BLACK);
        secondPlayer.setForeground(Color.RED);     
    } 

    private static final class ExitField implements KeyListener
    {      
        @Override
        public void keyReleased(KeyEvent e){}
        @Override
        public void keyTyped(KeyEvent e){}

        @Override
        public void keyPressed(KeyEvent e)
        {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
                System.exit(0);
            }
            else if(e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                playB = true;
            }
        }
    }
}
