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
        for(int i = 0; i < 101; i++)
        {
            g.drawLine((int)(i*boundary.getWidth()/100),(int)boundary.getY(),(int)(i*boundary.getWidth()/100), (int)(boundary.getHeight()+boundary.getY()));
        }
        for(int i = 0; i < 101; i++)
        {
            g.drawLine((int)(boundary.getX()),(int)(i*boundary.getHeight()/100),(int)(boundary.getX()*boundary.getWidth()), (int)(i*boundary.getHeight()/100));
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
            int relativeX = tankPoints[0].x+screenPoint.x;
            int relativeY = tankPoints[0].y+screenPoint.y;
                    
            if((relativeX > d.width/2+30)&&(boundary.getWidth()+screenPoint.x+boundary.getX() > d.width))
            {
                screenPoint.translate(-moveSpeed, 0);
            }
            if((relativeX < d.width/2-30)&&(screenPoint.x+boundary.getX() < 0))
            {
                screenPoint.translate(moveSpeed, 0);
            }
            if((relativeY > d.height/2+30)&&(boundary.getHeight()+screenPoint.y+boundary.getY() > d.height))
            {
                screenPoint.translate(0, -moveSpeed);
            }
            if((relativeY < d.height/2-30)&&(screenPoint.y+boundary.getY() < 0))
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
