package Tanks.Types;

import Tanks.Bullets.TierOne;
import Game.GUI;
import Tanks.Tank;
import java.awt.*;
import java.awt.geom.*;

public class MageTank extends Tank
{    
    private final long BULLET_TIMEOUT = 100;
    private final long BULLET_HEAT = 500;
    private final long BULLET_COOL = 200;
    private long bulletT = 0;
    private long bulletTHeat = 0;
    private long bulletTCool;
       
    public MageTank(Color _tankColor, String _tankName, String _tankNumber, Point _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds, 10);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition; 
    }
    
    private void makeBody()
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
        double xDif = tankHeight/4;
        double xPoints[] = {tankHeight/2,tankHeight*0.190983,tankHeight,0,tankHeight*0.809017};        
        double yPoints[] = {0,tankHeight*0.951057,tankHeight*0.363271,tankHeight*0.363271,tankHeight*0.951057};
        
        Graphics2D myG = (Graphics2D)g.create();
        myG.transform(tankTrans);
        Color c = myG.getColor();
        myG.setColor(new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()));
        
        if(specialDrawSequence < 5*tankHeight)
        {
            specialDrawSequence += 0.01d*tankHeight;
        }
        if(specialDrawSequence > 4*tankHeight)
        {
            Point nextPoint = getNextPoint(xPoints[4], yPoints[4], xPoints[0], yPoints[0], specialDrawSequence-4*tankHeight);
            myG.drawLine((int)(xPoints[4]-xDif), (int)(yPoints[4]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));       
        }
        else if(specialDrawSequence > 3*tankHeight)
        {
            Point nextPoint = getNextPoint(xPoints[3], yPoints[3], xPoints[4], yPoints[4], specialDrawSequence-3*tankHeight);
            myG.drawLine((int)(xPoints[3]-xDif), (int)(yPoints[3]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));
        }
        else if(specialDrawSequence > 2*tankHeight)
        {
            Point nextPoint = getNextPoint(xPoints[2], yPoints[2], xPoints[3], yPoints[3], specialDrawSequence-2*tankHeight);
            myG.drawLine((int)(xPoints[2]-xDif), (int)(yPoints[2]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));
        }
        else if(specialDrawSequence > tankHeight)
        {
            Point nextPoint = getNextPoint(xPoints[1], yPoints[1], xPoints[2], yPoints[2], specialDrawSequence-tankHeight);
            myG.drawLine((int)(xPoints[1]-xDif), (int)(yPoints[1]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));
        }
        else if(specialDrawSequence > 0)
        {
            Point nextPoint = getNextPoint(xPoints[0], yPoints[0], xPoints[1], yPoints[1], specialDrawSequence);
            myG.drawLine((int)(xPoints[0]-xDif), (int)(yPoints[0]), (int)(nextPoint.x-xDif), (int)(nextPoint.y));
        }
        
        switch((int)Math.floor(specialDrawSequence/tankHeight))
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
    
    public void fire() 
    {
        if(System.currentTimeMillis() < bulletTHeat)
        {
            if(System.currentTimeMillis() - bulletT > BULLET_TIMEOUT)
            {
                GUI.theGUI.launchBullet(new TierOne(centerPoint.x, centerPoint.y, barrelAngle-0.5*Math.PI, this));
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
