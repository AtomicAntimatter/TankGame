package Tanks;

import java.awt.*;
import java.awt.geom.*;

public class MageTank extends Tank
{
    private int drawSequence;
    
    public MageTank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds, 20);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition;
        drawSequence = 0;
    }
    
    private void makeBody()
    {
        double xDif = (tankHeight - tankWidth)/2;
        double xPoints[] = {tankHeight/2,tankHeight,tankHeight*0.809017,tankHeight*0.190983, 0};        
        double yPoints[] = {0,tankHeight*0.363271,tankHeight*0.951057,tankHeight*0.951057,tankHeight*0.363271};
        
        Path2D tankBody = new Path2D.Double(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        tankBody.moveTo(xPoints[0]-xDif, yPoints[0]);  
        
        for(int i = 1; i < xPoints.length; i++)
        {
            tankBody.lineTo(xPoints[i]-xDif, yPoints[i]);
        }
        
        tankBody.closePath();

        tankDefinition = tankBody;
    }
    
    private void makeBarrel()
    {
        double xPoints[] = {0,0,tankWidth*0.4,tankWidth*0.4,tankWidth*0.35,tankWidth*0.3,tankWidth*0.1,tankWidth*0.05};           
        double yPoints[] = {0,tankWidth*0.5,tankWidth*0.5,0,0,tankWidth*0.5,tankWidth*0.5, 0};
               
        GeneralPath tankBarrel = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);  
        tankBarrel.moveTo(xPoints[0], yPoints[0]);
        tankBarrel.lineTo(xPoints[1], yPoints[1]); 
        tankBarrel.curveTo(tankWidth*0.1,tankWidth*0.75,tankWidth*0.3,tankWidth*0.75,xPoints[2],yPoints[2]);
        
        for(int i = 3; i < xPoints.length; i++)
        {
            tankBarrel.lineTo(xPoints[i], yPoints[i]);
        }
        
        tankBarrel.closePath();
        
        barrelDefinition = tankBarrel;
    }
    
    protected void specialDraw(Graphics2D g)
    {
        double dif = tankHeight/2;
        int xPoint1 = (int)(tankHeight/2 - dif) + centerPoint.x;
        int yPoint1 = (int)(-dif) + centerPoint.y;
        int xPoint2 = (int)(tankHeight*0.190983 - dif) + centerPoint.x;
        int yPoint2 = (int)(tankHeight*0.951057 - dif) + centerPoint.y;
        int xPoint3 = (int)(tankHeight - dif) + centerPoint.x;
        int yPoint3 = (int)(tankHeight*0.363271 - dif) + centerPoint.y;
        g.drawLine(xPoint1, yPoint1, xPoint2, yPoint2);
        g.drawLine(xPoint2, yPoint2, xPoint3, yPoint3);
    }
}
