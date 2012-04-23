package Game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import Tanks.Tank;
import Tanks.Types.*;
import TankController.*;
import java.awt.Color;
import java.util.List;
import java.util.LinkedList;

public class GameController 
{
    private String[] names;
    private int[] scores;
    private boolean gameOn, menuOn, oldGameOn, oldMenuOn, windowMode, oldWindowMode, soundOn;
    
    public GameController(boolean initialMenu)
    {
        oldMenuOn = initialMenu;
        menuOn = initialMenu;
    }
    
    public void setStatus(boolean _gameOn, boolean _menuOn)
    {
        oldGameOn = gameOn;
        oldMenuOn = menuOn;
        gameOn = _gameOn;
        menuOn = _menuOn;
    }
    public int loadPanel()
    {
        if(oldGameOn != gameOn)
        {
            oldMenuOn = !menuOn;
            menuOn = !menuOn;
            return 1;
        }
        if(oldMenuOn != menuOn)
        {      
            oldGameOn = !gameOn;
            gameOn = !gameOn;
            return 2;
        }
        return 0;
    }
    public void setResults(String[] _names, int[] _scores)
    {
        names = _names;
        scores = _scores;
    }
    public int[] getScores()
    {
        return scores;
    }
    public String[] getNames()
    {
        return names;
    }
    
    public void setSettings(GameSettings gs)
    {
        soundOn = gs.sO;
        windowMode = gs.wM;
    }
    
    public boolean changeWindowMode()
    {
        if(windowMode != oldWindowMode)
        {
            oldWindowMode = windowMode;
            return true;
        }
        return false;
    }
    
    public static class GameSettings
    {
        boolean wM, sO;
        int portNum, mode;
        String hostname;
        
        public GameSettings(boolean _wM, boolean _sO, int _portNum, String _hostname, int _mode)
        {
            wM = _wM;
            sO = _sO;          
            portNum = _portNum;
            hostname = _hostname;
            mode = _mode;
        }
    }
    
    public static class TankManager
    {
        private List tankList, controlList;
        private static Rectangle2D boundary;
        public GameField gf;
     
        public TankManager(Dimension fd, Dimension d)
        {
            tankList = new LinkedList();
            controlList = new LinkedList();
            boundary = new Rectangle2D.Double(fd.width*0.005,fd.height*0.005,fd.width*0.99,fd.height*0.99);
            Point tempPoint = new Point(d.width/2-fd.width/2, d.height/2-fd.height/2);    
            gf = new GameField(Color.CYAN,boundary,tempPoint,d);
        }
    
        public void addTank(TankStyle t, TankController c)
        {
            tankList.add(t);
            controlList.add(c);
        }
        
        public Tank getTankType(int i)
        {
            return ((TankStyle)tankList.get(i)).getTank();
        }
        
        public TankController getTankController(int i)
        {
            return (TankController)controlList.get(i);
        }
        
        public boolean isHuman(int i)
        {
            return ((TankController)controlList.get(i)).getClass().equals(HumanController.class);
        }
        
        public boolean isMouse(int i)
        {
            return ((HumanController)controlList.get(i)).isMouse();
        }
        
        public int getSize()
        {
            return tankList.size();
        }
        
        public static class TankStyle
        {
            private String name, num;
            private Point location;
            private double initialAngle;   
            private Color c;
            private Tank t;
            
            public TankStyle(String _name, Color _c, String _num, Point _location, double _initialAngle, int type)
            {
                name = _name;
                num = _num;
                location = _location;
                initialAngle = _initialAngle;
                c = _c;
                
                if(type == 0)
                {
                    t = new HeavyTank(c,name,num,location,initialAngle,boundary.getBounds());      
                }
                if(type == 1)
                {              
                    t = new RangeTank(c,name,num,location,initialAngle,boundary.getBounds());      
                }
                if(type == 2)
                {
                    t = new MageTank(c,name,num,location,initialAngle,boundary.getBounds());      
                }
            }
            
            public Tank getTank()
            {
                return t;
            }
        }        
    }
}
