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
        int x1Points[] = {0,0,(int)tankWidth,(int)tankWidth,0,0,(int)tankWidth,(int)tankWidth};        
        int y1Points[] = {0,(int)tankHeight,(int)tankHeight,0,0,(int)(tankHeight*0.1),(int)(tankHeight*0.1), 0};
        
        GeneralPath tankBody = new GeneralPath(GeneralPath.WIND_EVEN_ODD, x1Points.length);
        tankBody.moveTo(x1Points[0], y1Points[0]);
        
        for(int i = 1; i < x1Points.length; i++)
        {
            tankBody.lineTo(x1Points[i], y1Points[i]);
        }
        tankDefinition = tankBody;
    }
}
