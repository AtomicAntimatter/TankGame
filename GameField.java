import java.awt.geom.*;
import java.awt.*;
import java.util.Arrays;

public class GameField 
{
    private Rectangle2D boundary;
    private Color boundaryColor;
    private Point screenPoint;
    private Point[] tankPoints;
    private Dimension d;
    private final int moveSpeed = 8;
    
    public GameField(Color _boundaryColor, Rectangle2D _boundary)
    {
        boundary = _boundary;
        boundaryColor = _boundaryColor;
        screenPoint = new Point(0,0);
        tankPoints = new Point[2];
        tankPoints[0] = new Point(0,0);
        tankPoints[1] = new Point(0,0);
    }
    
    public void drawField(Graphics2D g)
    {
        g.setColor(Color.DARK_GRAY.darker());
        for(int i = 1; i < boundary.getWidth()/50; i++)
        {
            g.drawLine(i*50,(int)boundary.getY(),i*50, (int)(boundary.getHeight()+boundary.getY()));
        }
        for(int i = 1; i < boundary.getHeight()/50; i++)
        {
            g.drawLine((int)(boundary.getX()),i*50,(int)(boundary.getX()+boundary.getWidth()), i*50);
        }
        g.setColor(boundaryColor);
        g.draw(boundary);
    }
    
    public Rectangle2D getBounds()
    {
        return boundary;
    }
    
    public void setScreenInfo(Point _screenPoint, Dimension a)
    {
        d = a;
        screenPoint = _screenPoint;
    }
    
    public void setTankPoint(Point centerPoint)
    {
        if(tankPoints[0] == null)
        {
            tankPoints[0] = centerPoint;
        }
        else
        {
            tankPoints[1] = centerPoint;
        }
    }
    
    public void done()
    {
        if(tankPoints[1] == null)
        {
            int dx = d.width/2 - (tankPoints[0].x+screenPoint.x);
            int dy = d.height/2 - (tankPoints[0].y+screenPoint.y);
            
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
        }
        Arrays.fill(tankPoints, null);
    }
    
    public Point getScreenPoint()
    {
        return screenPoint;
    }
}
