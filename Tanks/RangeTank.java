package Tanks;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.HashSet;
import java.util.Iterator;

public class RangeTank extends Tank
{
    public RangeTank(Color _tankColor, String _tankName, String _tankNumber, Point2D _centerPoint, double _tankAngle, Rectangle2D _bounds)
    {
        super(_tankColor, _tankName, _tankNumber, _centerPoint, _tankAngle, _bounds, 10);
           
        makeBody();
        makeBarrel();
        tankShape = tankDefinition;
        barrelShape = barrelDefinition;  
        
        tankSphere = (int)Math.max(1.4*tankWidth/2, 1.2*tankHeight/2);
        tankPixels = getMask(makeImage(tankDefinition));
    }
    
    private void makeBody()
    {
        int xPoints[] = {(int)(tankWidth*0.3),(int)(tankWidth*-0.2),(int)(tankWidth*1.2), (int)(tankWidth*0.7)};        
        int yPoints[] = {(int)(tankHeight*-0.2),(int)tankHeight,(int)tankHeight, (int)(tankHeight*-0.2)};
        
        GeneralPath tankBody = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
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
    
    private BufferedImage makeImage(Shape s) 
    {
        Rectangle r = s.getBounds();
        BufferedImage image = new BufferedImage(r.width, r.height, BufferedImage.BITMASK);
        Graphics2D gr = (Graphics2D)image.getGraphics();

        gr.translate(-r.x, -r.y);
        gr.draw(s);
        gr.dispose();
        
        return image;
    }
    
    private HashSet getMask(BufferedImage image)
    {
        HashSet mask = new HashSet();

        int pixel, a;

        for(int i = 0; i < image.getWidth(); i++)
        { 
            for( int j = 0; j < image.getHeight(); j++)
            {
                pixel = image.getRGB(i, j);
                a = (pixel >> 24) & 0xff;
                if(a != 0)
                {
                    String pix = i + " " + j;
                    mask.add(pix);
                }
            }
        }
        return mask;
    }
    
    private void printPixelLocations(HashSet pixLoc)
    {
        Iterator i = pixLoc.iterator();
        while(i.hasNext())
        {
            String pixStr = (String)i.next();
            System.out.println(pixStr);
        }
    }
    
    public boolean didHit()
    {
        if((centerPoint.getX()-tankSphere < bounds.getX())
            ||(centerPoint.getX()+tankSphere > bounds.getX()+bounds.getWidth())
            ||(centerPoint.getY()-tankSphere < bounds.getY())
            ||(centerPoint.getY()+tankSphere > bounds.getY()+bounds.getHeight()))
        {
            return true;
        }
        return false;
    }
}
