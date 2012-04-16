
public class GameController 
{
    private String[] names;
    private int[] scores;
    private boolean gameOn, menuOn;
    private boolean oldGameOn, oldMenuOn;
    
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
}
