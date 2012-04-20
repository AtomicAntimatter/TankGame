package Tanks;

import java.awt.*;
import java.awt.geom.*;

public class RangeTank extends Tank
{
    public RangeTank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds, 20);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition;  
    }
    
    private void makeBody()
    {
        int xPoints[] = {(int)(tankWidth*0.3),(int)(tankWidth*-0.2),(int)(tankWidth*1.2), (int)(tankWidth*0.7)};        
        int yPoints[] = {(int)(tankHeight*-0.2),(int)tankHeight,(int)tankHeight, (int)(tankHeight*-0.2)};
        
        Path2D tankBody = new Path2D.Double(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        tankBody.moveTo(xPoints[0], yPoints[0]);  
        tankBody.lineTo(xPoints[1], yPoints[1]);
        tankBody.curveTo(tankWidth/2,tankHeight/2+tankHeight*0.2,tankWidth/2,tankHeight/2+tankHeight*0.2,xPoints[2], yPoints[2]);
        tankBody.lineTo(xPoints[3], yPoints[3]);
        tankBody.curveTo(tankWidth/2, tankHeight*-0.1, tankWidth/2, tankHeight*-0.1, xPoints[0], yPoints[0]);
        
        tankBody.closePath();
        
        tankDefinition = tankBody;
    }
    
    private void makeBarrel()
    {
        int xPoints[] = {0, 0, (int)(tankWidth*0.4), (int)(tankWidth*0.4), 
            (int)(tankWidth*0.35), (int)(tankWidth*0.3), (int)(tankWidth*0.1), 
            (int)(tankWidth*0.05)};
             
        int yPoints[] = {0, (int)(tankWidth*0.5), (int)(tankWidth*0.5), 0, 0, 
            (int)(tankWidth*0.5), (int)(tankWidth*0.5), 0};
               
        GeneralPath tankBarrel = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);  
        tankBarrel.moveTo(xPoints[0], yPoints[0]);
        tankBarrel.lineTo(xPoints[1], yPoints[1]); 
        tankBarrel.curveTo((int)(tankWidth*0.1), (int)(tankWidth*0.75), 
                (int)(tankWidth*0.3), (int)(tankWidth*0.75),xPoints[2], 
                yPoints[2]);
        
        for(int i = 3; i < xPoints.length; i++)
        {
            tankBarrel.lineTo(xPoints[i], yPoints[i]);
        }
        
        tankBarrel.closePath();
        
        barrelDefinition = tankBarrel;
    }
    
    public void fire() {}
}
