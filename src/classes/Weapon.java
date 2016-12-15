package classes;

public class Weapon extends Item{
	private int damage,protection;
	
	public Weapon(String x, int dmg, int delay, int res)
	{
		super.name_object=x;
		damage=dmg;
		super.shift_delay=delay;
		super.actual_resistance=res;
		protection=0;
	}
	
	public Weapon(String x, int dmg, int def, int delay, int res)
	{
		this(x,dmg,delay,res);
		protection=def;
	}
	
	public int getDmg(){return damage;}
	public int getProtection(){return protection;}
}
