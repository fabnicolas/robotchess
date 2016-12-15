package interfaces;

import classes.Workbench;

public interface RobotActionsInterface<A> {
	public void takeObjectFromWB(Workbench wb);
	public void removeObject(int i);
	public int getObjectLength();
	public void equipObject(int i);
	public A getObject(int i);
	public void giveObject(A x);
}
