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
    private int moveSpeed;
    
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
    
    public void setTankPoint(Point centerPoint, double tankSpeed)
    {
        if(tankPoints[0] == null)
        {
            tankPoints[0] = centerPoint;
        }
        else
        {
            tankPoints[1] = centerPoint;
        }
        
        moveSpeed = Math.max(moveSpeed, (int)tankSpeed);
    }
    
    public void done()
    {
        if(tankPoints[1] == null)
        {
            if(tankPoints[0].x+screenPoint.x > d.width/2)
            {
                screenPoint.translate(-moveSpeed, 0);
            }
            if(tankPoints[0].x+screenPoint.x < d.width/2)
            {
                screenPoint.translate(moveSpeed, 0);
            }
            if(tankPoints[0].y+screenPoint.y > d.height/2)
            {
                screenPoint.translate(0, -moveSpeed);
            }
            if(tankPoints[0].y+screenPoint.y < d.height/2)
            {
                screenPoint.translate(0, moveSpeed);
            }
        }
        Arrays.fill(tankPoints, null);
    }
    
    public Point getScreenPoint()
    {
        return screenPoint;
    }
}
