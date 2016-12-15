package extra;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import classes.Robot;
import classes.RobotWarrior;
import classes.RobotWorker;

public class Utility {
	public static int[] getDirectionXY(String where, int x, int y)
	{
		int x_new,y_new,n_statement;
		switch(where)
		{
			case "E": x_new = x+1;
					  y_new = y;
					  n_statement=0;
					  break;
			case "W": x_new = x-1;
					  y_new = y;
					  n_statement=1;
					  break;
			case "N": x_new = x;
					  y_new = y-1;
					  n_statement=2;
					  break;
			case "S": x_new = x;
					  y_new = y+1;
					  n_statement=3;
					  break;
			default: x_new=-1; y_new=-1; n_statement=-1; break;	
		}
		return new int[]{x_new,y_new,n_statement};
	}
	
	public static String getDirection(int code){
		switch(code)
		{
			case 0: return "E";
			case 1: return "W";
			case 2: return "N";
			case 3: return "S";
			default: return null;
		}
	}
	
	public static String[] getPossibleActions(Robot robot){
		if(robot instanceof RobotWarrior)
			return new String[]{"Move", "Attack", "Attack nexus", "Move obstacle", "Use Workbench", "Info Robot"};
		else if(robot instanceof RobotWorker)
			return new String[]{"Move", "Heal ally LIFE", "Heal ally ENERGY", "Recharge self energy", "Move obstacle", "Use Workbench", "Info Robot"};
		else return null;
	}
	
	public static int rand(int min,int max)
    {
        return min + (int)(Math.random() * ((max-min) + 1));
    }
	
	// inizio metodi di collocazione dei componenti
	public static void setLocationComponent (Component c, int x, int y)
	{
		c.setBounds(new Rectangle(new Point(x,y), c.getPreferredSize()));
	}
			
	public static void alignAtRightOf(Component c, Component c_left, int dx)
	{
		setLocationComponent(c,dx+c_left.getX()+c_left.getWidth(),c_left.getY());
	}
			
	public static void alignAtRightOf(Component c, Component c_left)
	{
		alignAtRightOf(c,c_left,10);
	}
			
	public static void alignAtBottomOf(Component c, Component c_top, int dy)
	{
		setLocationComponent(c,c_top.getX(),c_top.getY()+c_top.getHeight()+dy);
	}
			
	public static void alignAtBottomOf(Component c, Component c_top)
	{
		alignAtBottomOf(c,c_top,10);
	}
			// fine metodi di collocazione dei componenti
			
}
