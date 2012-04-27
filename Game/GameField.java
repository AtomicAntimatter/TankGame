package Game;

import java.awt.geom.*;
import java.awt.*;

public class GameField 
{
    private final Rectangle2D boundary;
    private final Color boundaryColor;
    private final Point screenPoint;
    private final Dimension d;
    
    public GameField(Color _boundaryColor, Rectangle2D _boundary, Point _screenPoint, Dimension a)
    {
        d = a;
        boundary = _boundary;
        boundaryColor = _boundaryColor;
        screenPoint = _screenPoint;
    }
    
    public void drawField(Graphics2D g)
    {
        g.setColor(Color.DARK_GRAY.darker());
        for(int i = 1; i < boundary.getWidth()/50 +1; i++)
        {
            g.drawLine(i*50,(int)boundary.getY(),i*50, (int)(boundary.getHeight()+boundary.getY()));
        }
        for(int i = 1; i < boundary.getHeight()/50 +1; i++)
        {
            g.drawLine((int)(boundary.getX()),i*50,(int)(boundary.getX()+boundary.getWidth()), i*50);
        }
        g.setColor(boundaryColor);
        g.draw(boundary);
    }
       
    public Point getScreenPoint(Point centerPoint)
    {
        int dx = d.width/2 - (screenPoint.x+centerPoint.x);
        int dy = d.height/2 - (screenPoint.y+centerPoint.y);

        if(((boundary.getWidth()+screenPoint.x+boundary.getX() < d.width)&&(dx < 0))
                ||((screenPoint.x+boundary.getX() > 0)&&(dx > 0)))
        {
            dx = 0;
        }

        if(((boundary.getHeight()+screenPoint.y+boundary.getY() < d.height)&&(dy < 0))
                ||((screenPoint.y+boundary.getY() > 0)&&(dy > 0)))
        {
            dy = 0;
        }

        screenPoint.translate(dx, dy);
        return screenPoint;
    }
}
