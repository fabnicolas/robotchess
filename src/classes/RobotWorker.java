package classes;
import interfaces.*;
import java.util.ArrayList;

import extra.Utility;

public class RobotWorker extends Robot implements RobotActionsInterface<Tool>, RenderizableInterface{
	public ArrayList<Tool> list_tools;
	public int tool_equip;
	private boolean hasRepairPcs;
	
	public RobotWorker() {
		super(400,-1);
		super.actual_life = super.max_life = 150;
		
		list_tools = new ArrayList<Tool>();
		list_tools.add(new Tool("Martello",15,1,-1));
		tool_equip=0;
		hasRepairPcs=true;
		super.render_team=0;
		this.setLookPosition("E");
	}
	
	public RobotWorker(int vita_attuale, int vita_max, int energia_attuale, int energia_max, int tool_equipaggiato, int renderteam)
	{
		this();
		super.setEnergy(energia_attuale);
		super.setMaxEnergy(energia_max);
		super.setLife(vita_attuale);
		super.setMaxLife(vita_max);
		tool_equip=tool_equipaggiato;
		if(renderteam==0) this.setLookPosition("E");
		else			  this.setLookPosition("W");
		super.render_team=renderteam;
	}
	
	public void repairLife(Robot r)
	{
		if(hasRepairPcs)
		{
			r.setLife(r.getLife()+this.getObjectEquipped().getLifeHealed());
			System.out.println(this.getObjectEquipped().getLifeHealed());
			hasRepairPcs=false;
			if(this.getObjectEquipped().getResistance()!=-1)
			{
				this.getObjectEquipped().damageItem();
				checkResistanceObjectEquipped();
			}
		}
	}
	
	public void repairEnergy(Robot r)
	{
		int eccesso=0;
		r.setEnergy(r.getEnergy() + this.getEnergy());
		if(r.getEnergy() > r.getMaxEnergy())
		{
			eccesso = r.getEnergy() - r.getMaxEnergy();
			r.setEnergy(r.getMaxEnergy());
		}
		this.setEnergy(eccesso);
		
		if(this.getObjectEquipped().getResistance()!=-1)
		{
			this.getObjectEquipped().damageItem();
			checkResistanceObjectEquipped();
		}
	}
	
	public void rechargeSelfEnergy(){
		this.setEnergy(this.getMaxEnergy());
	}
	
	private void checkResistanceObjectEquipped()
	{
		if(this.getObjectEquipped().getResistance()==0)
		{
			this.removeObject(tool_equip);
			tool_equip=0;
		}
	}
	
	public int heal(String where, Scenario sc, int id_team, int type_healing)
	{
		int ally_index=-1;
		int[] xy_new=Utility.getDirectionXY(where, x, y);
		int x_new=xy_new[0];
		int y_new=xy_new[1];
		
		Team controller;
		if(id_team==0) controller=sc.getP1();
		else		   controller=sc.getP2();
		
		if(ally_index==-1){ally_index = sc.collisionDetect(controller,x_new,y_new,0);}
		if(ally_index>-1)
		{
			if(type_healing==0)	repairLife(controller.getRobot(ally_index));
			else				repairEnergy(controller.getRobot(ally_index));
		}
		return ally_index;
	}
	
	public void addTool(Tool x)
	{
		list_tools.add(x);
	}
	
	@Override public String toString()
	{
		return "Robot["+super.x+","+super.y+","+super.actual_life+","+super.max_life+","+getEnergy()+","+getMaxEnergy()+","+tool_equip+","+hasRepairPcs+"]";
	}
	
	@Override public void takeObjectFromWB(Workbench wb)
	{
		list_tools.add(wb.giveTool());
		tool_equip=list_tools.size()-1;
	}
	@Override public Tool getObjectEquipped(){ return list_tools.get(tool_equip); }
	@Override public Tool getObject(int i){ return list_tools.get(i); }
	@Override public void removeObject(int i){list_tools.remove(i);}
	@Override public int getObjectLength(){return list_tools.size();}
	@Override public void giveObject(Tool t){ list_tools.add(t); }
	@Override public void equipObject(int i){tool_equip=i;}
	@Override public String getRenderImage(){return "robot_team"+(super.render_team)+"_worker_"+(super.look_at)+".gif";}
	@Override public void setLookPosition(String lookat){super.look_at=lookat;}
}
