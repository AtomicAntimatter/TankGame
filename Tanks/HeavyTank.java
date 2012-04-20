package Tanks;

import Game.GUI;
import Tanks.Bullets.HeavyBullet;
import java.awt.*;
import java.awt.geom.*;

public class HeavyTank extends Tank
{   
    public HeavyTank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds, 5.5d);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition;  
    }
      
    private void makeBody()
    {
        double xPoints[] = {0,0,tankWidth,tankWidth,0,0,tankWidth,tankWidth};        
        double yPoints[] = {0,tankHeight,tankHeight,0,0,tankHeight*0.1,tankHeight*0.1, 0};
        
        GeneralPath tankBody = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        tankBody.moveTo(xPoints[0], yPoints[0]);
        
        for(int i = 1; i < xPoints.length; i++)
        {
            tankBody.lineTo(xPoints[i], yPoints[i]);
        }
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
        Graphics2D myG = (Graphics2D)g.create();
        Color c = myG.getColor();
        AffineTransform myAT = (AffineTransform)tankTrans.clone();
        
        if(specialDrawSequence == 0)
        {
            specialDrawSequence = 2;
        }
        
        if(specialDrawSequence > 1)
        {
            specialDrawSequence -= 0.005d;
        }

        myG.setColor(new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue(), 255 - (int)((2*(specialDrawSequence-.99))*127)));
        myAT.scale(specialDrawSequence, specialDrawSequence);
        myAT.translate(-(specialDrawSequence-1)/4*tankWidth, -(specialDrawSequence-1)/4*tankHeight);
        myG.setStroke(new BasicStroke((float)(6-specialDrawSequence*2)));

        myG.transform(myAT);   
        myG.draw(tankDefinition);
    }
    
    @Override 
    public void fire() 
    {
        GUI.theGUI.launchBullet(new HeavyBullet(centerPoint.x, centerPoint.y, barrelAngle-0.5*Math.PI, this));
    }
}
