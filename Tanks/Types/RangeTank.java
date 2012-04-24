package Tanks.Types;

import Tanks.Tank;
import Game.GUI;
import Tanks.Bullets.RangeBullet;
import java.awt.*;
import java.awt.geom.*;

public class RangeTank extends Tank
{
    private static final int MAX_TIER = 2;
    private static final Shape BARREL_D = makeBarrel(), BODY_D = makeBody();
    /*auto*/ {
        tankDefinition = BODY_D;
        barrelDefinition = BARREL_D;
    }
    private final long BULLET_TIMEOUT = 80;
    private final long BULLET_HEAT = 600;
    private final long BULLET_COOL = 100;
    private long bulletT = 0;
    private long bulletTHeat = 0;
    private long bulletTCool = 0;
    
    public RangeTank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds, 20, 600);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition;  
    }
    
    private static Shape makeBody()
    {
        double xPoints[] = {tankWidth*0.3,tankWidth*-0.2,tankWidth*1.2,tankWidth*0.7};        
        double yPoints[] = {tankHeight*-0.2,tankHeight,tankHeight,tankHeight*-0.2};
        
        Path2D tankBody = new Path2D.Double(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        tankBody.moveTo(xPoints[0], yPoints[0]);  
        tankBody.lineTo(xPoints[1], yPoints[1]);
        tankBody.curveTo(tankWidth/2,tankHeight/2+tankHeight*0.2,tankWidth/2,tankHeight/2+tankHeight*0.2,xPoints[2], yPoints[2]);
        tankBody.lineTo(xPoints[3], yPoints[3]);
        tankBody.curveTo(tankWidth/2, tankHeight*-0.1, tankWidth/2, tankHeight*-0.1, xPoints[0], yPoints[0]);
        
        tankBody.closePath();
        
        return tankBody;
    }
    
    private static Shape makeBarrel()
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
        
        return tankBarrel.createTransformedShape(AffineTransform.getQuadrantRotateInstance(1, tankWidth*.2, tankWidth*.5));
    }
    
    protected void specialDraw(Graphics2D g)
    {
        
    }

    @Override 
    public void fire() 
    {
        if(System.currentTimeMillis() < bulletTHeat)
        {
            if(System.currentTimeMillis() - bulletT > BULLET_TIMEOUT)
            {
                int tier = Math.min(power/200 + 1, MAX_TIER);
                power = Math.max(--power, 0);
                GUI.theGUI.launchBullet(RangeBullet.make(centerPoint.x, centerPoint.y, this, tier));
                bulletT = System.currentTimeMillis();
            }
            bulletTCool = System.currentTimeMillis() + BULLET_COOL;
        }
        else
        {
            if(System.currentTimeMillis() > bulletTCool)
            {
                bulletTHeat = System.currentTimeMillis() + BULLET_HEAT;
            }
        }
    }
    
    public void cooldown()
    {
        if(System.currentTimeMillis() > bulletTCool)
        {
            bulletTHeat = System.currentTimeMillis() + BULLET_HEAT;
        }
    }
}
