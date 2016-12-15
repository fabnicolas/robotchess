package classes;

public abstract class Item {
	public String name_object;
	public int shift_delay,actual_resistance;
	
	public String getName(){return name_object;}
	public int getShiftDelay(){return shift_delay;}
	public int getResistance(){return actual_resistance;}
	public void damageItem(){
		if(actual_resistance != -1) actual_resistance--;
	}
}
