package classes;
import extra.*;
import java.util.ArrayList;

public class Workbench extends ExistingEntity{
	private ArrayList<Weapon> list_weapons;
	private ArrayList<Tool> list_tools;
	
	
	public Workbench(int a, int b){
		super.actual_life = -1;
		super.max_life = -1;
		super.x = a;
		super.y = b;
		
		list_weapons=new ArrayList<Weapon>();
		list_tools=new ArrayList<Tool>();
	}
	
	public void addWeapon(Weapon x){list_weapons.add(x);}
	public void addTool(Tool x){list_tools.add(x);}
	public Weapon getWeapon(int x){return list_weapons.get(x);}
	public Tool getTool(int x){return list_tools.get(x);}
	public int lengthW(){return list_weapons.size();}
	public int lengthT(){return list_tools.size();}
	
	
	public Weapon giveWeapon(){
		return list_weapons.get(Utility.rand(0, this.list_weapons.size()-1));
	}
	public Tool giveTool(){
		return list_tools.get(Utility.rand(0, this.list_tools.size()-1));
	}
	public String getRenderImage(){
		return "workbench.gif";
	}
	
}
