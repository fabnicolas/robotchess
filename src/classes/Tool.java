package classes;

public class Tool extends Item{
	private int life_healed;
	
	public Tool(String x, int hp, int delay, int res)
	{
		super.name_object=x;
		life_healed=hp;
		super.shift_delay=delay;
		super.actual_resistance=res;
	}
	
	public int getLifeHealed(){return life_healed;}
}
