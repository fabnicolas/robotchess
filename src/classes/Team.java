package classes;
import java.util.ArrayList;

public class Team {
	private ArrayList<Robot> list_robot;
	private Workbench wb;
	private int nexus_actual_life,nexus_max_life;
	
	public Team()
	{
		list_robot = new ArrayList<Robot>();
		nexus_actual_life=nexus_max_life=1000;
	}
	public Robot getRobot(int i) {
		return list_robot.get(i);
	}
	
	public int searchRobot(int x, int y){
		boolean found=false;
		int robot_index=-1;
		for(int i=0;i<length() && !found;i++)
		{
			final Robot robot_temp = getRobot(i);
			if(robot_temp.x == x && robot_temp.y == y)
			{
				robot_index=i;
				found=true;
			}
		}
		
		if(found) return robot_index;
		else 		return -1;
	}
	
	public void addRobot(Robot obj,int x,int y)
	{
		obj.x = x;
		obj.y = y;
		list_robot.add(obj);
	}
	public void setWorkbench(Workbench workbench)
	{
		wb=workbench;
	}
	public Workbench getWorkbench(){return wb;}
	public void removeRobot(int x){list_robot.remove(x);}
	public int length(){return list_robot.size();}
	public int getNexusLife(){return nexus_actual_life;}
	public int getNexusMaxLife(){return nexus_max_life;}
	public void setNexusLife(int x){nexus_actual_life=x;}
	public void setNexusMaxLife(int x){nexus_max_life=x;}
	
}