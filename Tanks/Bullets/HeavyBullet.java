package Tanks.Bullets;

import Tanks.Tank;

public class HeavyBullet extends Bullet 
{
    public HeavyBullet (double _x, double _y, double _a, Tank _p) 
    {
        super(_x,_y,   //position
              1.0d,_a, //velocity
              1000,     //lifetime
              3,       //size
              _p);     //parent
    }
}
