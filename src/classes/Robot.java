package classes;
import extra.*;
import components.*;
import interfaces.*;

public abstract class Robot extends ExistingEntity implements RenderizableInterface{
	public int actual_energy,max_energy;
	public String look_at;
	public String render;
	public int render_team;
	private int strength;
	private ExistingEntity target;
	private boolean IA_enabled;
	
	public Robot(int e,int s){
		actual_energy=e;
		max_energy=e;
		strength=s;
		target=null;
		IA_enabled=false;
	}
	public int getEnergy(){return actual_energy;}
	public int getMaxEnergy(){return max_energy;}
	public void setEnergy(int x){actual_energy=x;}
	public void setMaxEnergy(int x){max_energy=x;}
	public int getLife(){return super.actual_life;}
	public void setLife(int x){super.actual_life=x;}
	public int getMaxLife(){return super.max_life;}
	public void setMaxLife(int x){super.max_life=x;}
	public int getStrength(){return strength;}
	public void setTarget(ExistingEntity x){target=x;}
	public ExistingEntity getTarget(){return target;}
	public void enableIA(){IA_enabled=true;}
	public void disableIA(){IA_enabled=false;}
	public boolean isIAEnabled(){return IA_enabled;}
	
	public int move(String where, GamePanel gp, int id_team, int n_robot)
	{
		int[] xy_new=Utility.getDirectionXY(where, x, y);
		int x_new=xy_new[0];
		int y_new=xy_new[1];
		int n_statement=xy_new[2];
		
		Team controller,controller_enemy;
		if(id_team==0){
			controller=gp.scenario.getP1();
			controller_enemy=gp.scenario.getP2();
		}else{
			controller=gp.scenario.getP2();
			controller_enemy=gp.scenario.getP1();
		}
		
		if(x_new >= 0 && x_new < 10 && y_new >= 0 && y_new < 5){
			int enemy_index=-1;
			if(enemy_index==-1) enemy_index = gp.scenario.collisionDetect(controller,x_new,y_new,0);
			if(enemy_index==-1) enemy_index = gp.scenario.collisionDetect(controller_enemy,x_new,y_new,0);
			if(enemy_index==-1) enemy_index = gp.scenario.collisionDetect(null,x_new,y_new,1);
			if(enemy_index==-1) enemy_index = gp.scenario.collisionDetect(controller,x_new,y_new,2);
			if(enemy_index==-1) enemy_index = gp.scenario.collisionDetect(controller_enemy,x_new,y_new,2);
			
			if(enemy_index==-1){
				switch(n_statement){
					case 0: x++; break;
					case 1: x--; break;
					case 2: y--; break;
					case 3: y++; break;
				}
				if(n_statement>-1){
					gp.stackAnimation(id_team,n_robot,n_statement);
				}
				setEnergy(getEnergy()-10);
				return 0; // 0 = tutt appost
			}else{return -2;}	// c'è qualcosa che occupa quella direzione
		}else{return -1;}	// esci fuori dalla mappa
	}
	
	public int moveObstacle(String where, GamePanel gp)
	{
		int[] xy_new=Utility.getDirectionXY(where, x, y);
		int x_new=xy_new[0];
		int y_new=xy_new[1];
		
		if(x_new >= 0 && x_new < 10 && y_new >= 0 && y_new < 5)
		{
			int obstacle_index=-1;
			if(obstacle_index==-1) obstacle_index = gp.scenario.collisionDetect(null,x_new,y_new,1);
			
			if(obstacle_index>-1){
				Obstacle obs=gp.scenario.getDataObstacle(obstacle_index);
				int movement_index=obs.move(where, gp, obstacle_index);
				if(movement_index==-1) return -3;	// errore: l'ostacolo non può finire fuori dalla mappa!
				if(movement_index==-2) return -4;	// errore: c'è qualcosa che blocca il movimento dell'ostacolo
				
				setEnergy(getEnergy()-10);
				return 0; // 0 = tutt appost
			}else{return -2;}	// errore: l'ostacolo non c'è
		}else{return -1;}	// errore: il robot non può eseguire quest'azione fuori dalla mappa
	}
	
	
	@Override public String toString(){
		return "Robot["+super.x+","+super.y+","+super.actual_life+","+super.max_life+","+getEnergy()+","+getMaxEnergy()+"]";
	}
	
	public abstract Item getObjectEquipped();
	public abstract Item getObject(int i);
	public abstract int getObjectLength();
	//public abstract void giveObject(Item x);
	public abstract void takeObjectFromWB(Workbench wb);
	public abstract void equipObject(int i);
	public abstract void removeObject(int i);
	public abstract String getRenderImage();
	public abstract void setLookPosition(String lookat);
	
}
