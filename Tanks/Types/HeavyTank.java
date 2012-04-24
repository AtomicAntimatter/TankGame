package Tanks.Types;

import Game.GUI;
import Tanks.Bullets.HeavyBullet;
import Tanks.Tank;
import java.awt.*;
import java.awt.geom.*;

public class HeavyTank extends Tank
{   
    private static final Shape BARREL_D = makeBarrel(), TANK_D = makeBody();
    /*auto*/ { 
        barrelDefinition = BARREL_D; tankDefinition = TANK_D; 
    }
    private static final int MAX_TIER = 2;
    private final long BULLET_TIMEOUT = 100;
    private final long BULLET_HEAT = 500;
    private final long BULLET_COOL = 200;
    private long bulletT = 0;
    private long bulletTHeat = 0;
    private long bulletTCool;
    
    public HeavyTank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds, 5.5d, 1000);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition;
        shieldShape = shieldDefinition;
    }
      
    private static Shape makeBody()
    {
        double xPoints[] = {0,0,tankWidth,tankWidth,0,0,tankWidth,tankWidth};        
        double yPoints[] = {0,tankHeight,tankHeight,0,0,tankHeight*0.1,tankHeight*0.1, 0};
        
        GeneralPath tankBody = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        tankBody.moveTo(xPoints[0], yPoints[0]);
        
        for(int i = 1; i < xPoints.length; i++)
        {
            tankBody.lineTo(xPoints[i], yPoints[i]);
        }
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
        
        return tankBarrel.createTransformedShape(AffineTransform.getRotateInstance(Math.PI/2, tankWidth*.2, tankWidth*.5));
    }
       
    protected void specialDraw(Graphics2D g)
    {
        Graphics2D myG = (Graphics2D)g.create();
        Color c = myG.getColor();
        AffineTransform myAT = (AffineTransform)tankTrans.clone();
        
        if(specialDrawSequence == 0)
        {
            specialDrawSequence = 1000;
        }
        
        if(specialDrawSequence > 200)
        {
            specialDrawSequence -= 1;
        }

        if(specialDrawSequence < 400)
        {
            double seqPos = (double)specialDrawSequence/200d;
            myG.setColor(new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue(), 255 - (int)((2*(seqPos-.99))*127)));
            myAT.scale(seqPos, seqPos);
            myAT.translate(-(seqPos-1)/4*tankWidth, -(seqPos-1)/4*tankHeight);
            myG.setStroke(new BasicStroke((float)(6-seqPos*2)));

            myG.transform(myAT);   
            myG.draw(tankDefinition);
        }
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
                GUI.theGUI.launchBullet(HeavyBullet.make(centerPoint.x, centerPoint.y, this, tier));
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
