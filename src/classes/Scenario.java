package classes;
import java.util.ArrayList;

public class Scenario {
	private Team player1;
	private Team player2;
	public ArrayList<Obstacle> list_ost;
	
	public Scenario()
	{
		player1 = new Team();
		player2 = new Team();
		list_ost=new ArrayList<Obstacle>();
		//player2.aggiungiRobotC(new RobotCombattente(), 9, 1);
	}
	
	public int collisionDetect(Team controller, int xn, int yn, int type)
	{
		boolean enemy_found=false;
		int i=-1;
		int i_max;
		if(type==1)			i_max=lengthO();
		else if(type==2)	i_max=1;
		else 				i_max=controller.length(); //type==0
		while(i<i_max-1 && !enemy_found)
		{
			i++;
			ExistingEntity entity;
			if(type==0) 			entity=controller.getRobot(i);
			else if(type==1)		entity=getDataObstacle(i);
			else					entity=controller.getWorkbench(); //type==2
			if(xn == entity.x && yn==entity.y) enemy_found=true;
		}
		if(enemy_found) return i;
		return -1;
	}
	
	public Team getP1(){ return player1;}
	public Team getP2(){ return player2;}
	public Team getTeam(int i){
		if(i==0)	return player1;
		else		return player2;
	}
	public Team getEnemyTeam(int i){
		if(i==0) return getTeam(1);
		else	 return getTeam(0);
	}
	public int lengthO(){return list_ost.size();}
	public Obstacle getDataObstacle(int i){return list_ost.get(i);}
	public void removeObstacle(int x){list_ost.remove(x);}
	
	
}