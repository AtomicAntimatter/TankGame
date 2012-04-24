package Tanks.Types;

import Game.GUI;
import Tanks.Bullets.MageBullet;
import Tanks.Tank;
import java.awt.*;
import java.awt.geom.*;

public class MageTank extends Tank
{    
    private static final int MAX_TIER = 2;
    private static final Shape BARREL_D = makeBarrel(), BODY_D = makeBody();
    /*auto*/ {
        tankDefinition = BODY_D;
        barrelDefinition = BARREL_D;
    }
    private final long BULLET_TIMEOUT = 200;
    private final long BULLET_HEAT = 800;
    private final long BULLET_COOL = 400;
    private long bulletT = 0;
    private long bulletTHeat = 0;
    private long bulletTCool;
       
    public MageTank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds, 10, 400);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition; 
    }
    
    private static Shape makeBody()
    {
        double xDif = (tankHeight - tankWidth)/2;
        double xPoints[] = {tankHeight/2,tankHeight,tankHeight*0.809017,tankHeight*0.190983, 0, tankHeight*0.3, tankHeight*0.7, tankHeight*0.3};        
        double yPoints[] = {0,tankHeight*0.363271,tankHeight*0.951057,tankHeight*0.951057,tankHeight*0.363271, tankHeight*0.145309, tankHeight*0.145309, tankHeight*0.145309};
        
        Path2D tankBody = new Path2D.Double(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        tankBody.moveTo(xPoints[0]-xDif, yPoints[0]);  
        
        for(int i = 1; i < xPoints.length; i++)
        {
            tankBody.lineTo(xPoints[i]-xDif, yPoints[i]);
        }
        
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
        
        return tankBarrel.createTransformedShape(AffineTransform.getRotateInstance(Math.PI/2, tankWidth*.2, tankWidth*.5));
    }
    
    @Override
    protected void specialDraw(Graphics2D g)
    {
        double xDif = tankHeight/4;
        double xPoints[] = {tankHeight/2,tankHeight*0.190983,tankHeight,0,tankHeight*0.809017};        
        double yPoints[] = {0,tankHeight*0.951057,tankHeight*0.363271,tankHeight*0.363271,tankHeight*0.951057};
        
        Graphics2D myG = (Graphics2D)g.create();
        myG.transform(tankTrans);
        Color c = myG.getColor();
        myG.setColor(new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()));        
        
        if(specialDrawSequence < 500*tankHeight)
        {
            specialDrawSequence += tankHeight;
        }
        double seqPos = specialDrawSequence/100d;
        if(seqPos > 4*tankHeight)
        {
            Point nextPoint = getNextPoint(xPoints[4], yPoints[4], xPoints[0], yPoints[0], seqPos-4*tankHeight);
            myG.drawLine((int)(xPoints[4]-xDif), (int)(yPoints[4]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));       
        }
        else if(seqPos > 3*tankHeight)
        {
            Point nextPoint = getNextPoint(xPoints[3], yPoints[3], xPoints[4], yPoints[4], seqPos-3*tankHeight);
            myG.drawLine((int)(xPoints[3]-xDif), (int)(yPoints[3]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));
        }
        else if(seqPos > 2*tankHeight)
        {
            Point nextPoint = getNextPoint(xPoints[2], yPoints[2], xPoints[3], yPoints[3], seqPos-2*tankHeight);
            myG.drawLine((int)(xPoints[2]-xDif), (int)(yPoints[2]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));
        }
        else if(seqPos > tankHeight)
        {
            Point nextPoint = getNextPoint(xPoints[1], yPoints[1], xPoints[2], yPoints[2], seqPos-tankHeight);
            myG.drawLine((int)(xPoints[1]-xDif), (int)(yPoints[1]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));
        }
        else if(seqPos > 0)
        {
            Point nextPoint = getNextPoint(xPoints[0], yPoints[0], xPoints[1], yPoints[1], seqPos);
            myG.drawLine((int)(xPoints[0]-xDif), (int)(yPoints[0]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));
        }
        
        switch((int)Math.floor(seqPos/tankHeight))
        {
            case 5: myG.drawLine((int)(xPoints[4]-xDif), (int)(yPoints[4]), (int)(xPoints[0]-xDif), (int)(yPoints[0]));
            case 4: myG.drawLine((int)(xPoints[3]-xDif), (int)(yPoints[3]), (int)(xPoints[4]-xDif), (int)(yPoints[4]));
            case 3: myG.drawLine((int)(xPoints[2]-xDif), (int)(yPoints[2]), (int)(xPoints[3]-xDif), (int)(yPoints[3]));
            case 2: myG.drawLine((int)(xPoints[1]-xDif), (int)(yPoints[1]), (int)(xPoints[2]-xDif), (int)(yPoints[2]));
            case 1: myG.drawLine((int)(xPoints[0]-xDif), (int)(yPoints[0]), (int)(xPoints[1]-xDif), (int)(yPoints[1]));
        }
    }
    
    private Point getNextPoint(double originX, double originY, double destX, double destY, double step)
    {
        double dX = destX-originX;  
        double dY = originY-destY; 

        double tempAngle = Math.atan2(dY,dX);

        if(tempAngle < 0)
        {
            tempAngle += 2*Math.PI;
        }
        if((tempAngle == 0)&&(destX<originX))
        {
            tempAngle = Math.PI;
        }

        double newX = step*Math.sin(tempAngle + Math.PI/2) + originX;
        double newY = step*Math.cos(tempAngle + Math.PI/2) + originY;
        
        return new Point((int)newX, (int)newY);
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
                GUI.theGUI.launchBullet(MageBullet.make(centerPoint.x, centerPoint.y, this, tier));
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
    
    @Override
    public void cooldown()
    {
        if(System.currentTimeMillis() > bulletTCool)
        {
            bulletTHeat = System.currentTimeMillis() + BULLET_HEAT;
        }
    }
}
