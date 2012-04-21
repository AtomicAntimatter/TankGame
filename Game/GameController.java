package Game;

import java.awt.Dimension;

public class GameController 
{
    private String[] names;
    private int[] scores;
    private boolean gameOn, menuOn;
    private boolean oldGameOn, oldMenuOn;
    private boolean windowMode, oldWindowMode, soundOn;
    
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
        int[] tankType;
        int[] tankCntrl;
        Dimension fd;
        int portNum;
        String hostname;
        int mode;
        
        public GameSettings(boolean _wM, boolean _sO, Dimension _fd, int[] _tankType, int[] _tankCntrl, int _portNum, String _hostname, int _mode)
        {
            //0 = heavy, 1 = range, 2 = mage:: 0 = human, 1 = AI, 2 = NET:: 0 = noNET, 1 = SERVER, 2 = CLIENT
            wM = _wM;
            sO = _sO;
            fd = _fd;
            tankType = _tankType;
            tankCntrl = _tankCntrl;
            portNum = _portNum;
            hostname = _hostname;
            mode = _mode;
        }
    }
}
