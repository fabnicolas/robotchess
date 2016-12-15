package classes;
import interfaces.*;
import extra.*;
import java.util.ArrayList;

public class RobotWarrior extends Robot implements RobotActionsInterface<Weapon>, RenderizableInterface{
	public ArrayList<Weapon> list_weapons;
	public int weapon_equip;
	
	
	public RobotWarrior() {
		super(150,-1);
		super.setLife(400);
		super.setMaxLife(400);
		
		list_weapons = new ArrayList<Weapon>();
		this.giveObject(new Weapon("Spada",300,1,-1));
		weapon_equip=0;
		super.render_team=0;
		this.setLookPosition("E");
	}
	
	public RobotWarrior(int actual_life, int max_life, int actual_energy, int max_energy, int weapon_equipped, int renderteam)
	{
		this();
		super.setEnergy(actual_energy);
		super.setMaxEnergy(max_energy);
		super.setLife(actual_life);
		super.setMaxLife(max_life);
		weapon_equip=weapon_equipped;
		if(renderteam==0) this.setLookPosition("E");
		else			  this.setLookPosition("W");
		super.render_team=renderteam;
	}
	
	public int[] attack(String where, Scenario sc, int id_team)
	{
		int enemy_index=-1,result_attack=0;
		int[] xy_new=Utility.getDirectionXY(where, x, y);
		int x_new=xy_new[0];
		int y_new=xy_new[1];
		
		Team controller_enemy;
		if(id_team==0) controller_enemy=sc.getP2();
		else		   controller_enemy=sc.getP1();
		
		int tipo=-1;
		if(enemy_index==-1){enemy_index = sc.collisionDetect(controller_enemy,x_new,y_new,0); tipo=0;}
		if(enemy_index==-1){enemy_index = sc.collisionDetect(null,x_new,y_new,1);			  tipo=1;}
		if(enemy_index>-1)
		{
			int danno=getObjectEquipped().getDmg();
			if(tipo==0) 	 
			{
				Robot r_enemy = controller_enemy.getRobot(enemy_index);
				if(r_enemy instanceof RobotWarrior)
				{
					danno=danno-(int)((double)(danno*((RobotWarrior)r_enemy).getObjectEquipped().getProtection())/100);
				}
				r_enemy.setLife(r_enemy.getLife() - danno);
				
				if(r_enemy.getLife()<=0)
				{
					controller_enemy.removeRobot(enemy_index);
					result_attack=1;
				}else if(((((float)r_enemy.getLife())/((float)r_enemy.getMaxLife()))*100) <= 25.0f)
				{
					result_attack=2;
				}
			}
			else if(tipo==1)
			{
				sc.getDataObstacle(enemy_index).setLife(sc.getDataObstacle(enemy_index).getLife() - danno);
				if(sc.getDataObstacle(enemy_index).getLife()<=0) sc.removeObstacle(enemy_index);
			}
			setEnergy(getEnergy()-15);
		}
		return new int[]{enemy_index,result_attack};
	}
	
	public int attackNexus(Scenario sc, int id_team)
	{
		boolean condition;
		Team controller_enemy;
		if(id_team==0)
		{
			condition = (x==9) && (y==1 || y==2 || y==3);
			controller_enemy=sc.getP2();
		}else{
			condition = (x==0) && (y==1 || y==2 || y==3);
			controller_enemy=sc.getP1();
		}
		
		if(condition)
		{
			int danno=getObjectEquipped().getDmg();
			controller_enemy.setNexusLife(controller_enemy.getNexusLife()-danno);
			setEnergy(getEnergy()-15);
			if(controller_enemy.getNexusLife()<0)
			{
				controller_enemy.setNexusLife(0);
				return 1;
			}
			
			return 0;
		}else{
			return -1;
		}
	}
	
	public void addWeapon(Weapon x)
	{
		list_weapons.add(x);
	}
	
	@Override public String toString()
	{
		return "Robot["+super.x+","+super.y+","+super.actual_life+","+super.max_life+","+getEnergy()+","+getMaxEnergy()+","+weapon_equip+"]";
	}
	
	@Override public void takeObjectFromWB(Workbench wb)
	{
		list_weapons.add(wb.giveWeapon());
		weapon_equip=list_weapons.size()-1;
	}
	@Override public Weapon getObjectEquipped(){ return list_weapons.get(weapon_equip); }
	@Override public Weapon getObject(int i){ return list_weapons.get(i);}
	@Override public int getObjectLength(){return list_weapons.size();}
	@Override public void giveObject(Weapon a){ list_weapons.add(a); }
	@Override public void equipObject(int i){weapon_equip=i;}
	@Override public void removeObject(int i){list_weapons.remove(i);}
	@Override public String getRenderImage(){return "robot_team"+(super.render_team)+"_warrior_"+(super.look_at)+".gif";}
	@Override public void setLookPosition(String lookat){super.look_at=lookat;}
	
}
