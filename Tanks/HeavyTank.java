package Tanks;

import java.awt.*;
import java.awt.geom.*;

public class HeavyTank extends Tank
{   
    public HeavyTank(Color _tankColor, String _tankName, String _tankNumber, Point2D _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition;     
    }
      
    private void makeBarrel()
    {
        int x1Points[] = {0, 0, (int)(tankWidth*0.4), (int)(tankWidth*0.4), 
            (int)(tankWidth*0.35), (int)(tankWidth*0.3), (int)(tankWidth*0.1), 
            (int)(tankWidth*0.05)};
             
        int y1Points[] = {0, (int)(tankWidth*0.5), (int)(tankWidth*0.5), 0, 0, 
            (int)(tankWidth*0.5), (int)(tankWidth*0.5), 0};
               
        GeneralPath tankBarrel = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x1Points.length);  
        tankBarrel.moveTo(x1Points[0], y1Points[0]);
        tankBarrel.lineTo(x1Points[1], y1Points[1]); 
        tankBarrel.curveTo((int)(tankWidth*0.1), (int)(tankWidth*0.75), 
                (int)(tankWidth*0.3), (int)(tankWidth*0.75),x1Points[2], 
                y1Points[2]);
        
        for(int i = 3; i < x1Points.length; i++)
        {
            tankBarrel.lineTo(x1Points[i], y1Points[i]);
        }
        
        tankBarrel.closePath();
        
        barrelDefinition = tankBarrel;
    }
    
    private void makeBody()
    {
        tankDefinition = new Rectangle2D.Double(0, 0, tankWidth, tankHeight);
        //tankFront = new Rectangle2D.Double(0,0,tankWidth,tankHeight*0.1);
    }
}
