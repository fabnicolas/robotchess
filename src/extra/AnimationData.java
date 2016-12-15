package extra;

public class AnimationData{
	private int team,entity_index;
	private int[] animation_start_xy;
	private int[] animation_delayed_xy;
	private int animation_code;

	public AnimationData(int team,int robot_index,int n_statement){
		this.team=team;
		this.entity_index=robot_index;
		this.animation_start_xy=new int[2];
		this.animation_delayed_xy=new int[2];
		switch(n_statement)
		{
			case 0: animation_start_xy[0]=-1; animation_start_xy[1]=0; break;
			case 1: animation_start_xy[0]=1; animation_start_xy[1]=0; break;
			case 2: animation_start_xy[0]=0; animation_start_xy[1]=1; break;
			case 3: animation_start_xy[0]=0; animation_start_xy[1]=-1; break;
		}
		this.animation_code=n_statement;
		animation_delayed_xy[0]=0;
		animation_delayed_xy[1]=0;
	}

	public void setTeam(int team){
		this.team = team;
	}
	public void setAnimationStartXY(int x,int y){
		animation_start_xy[0]=x;
		animation_start_xy[1]=y;
	}
	public void setRobotIndex(int entity_index){
		this.entity_index = entity_index;
	}
	public void setAnimationDelayedXY(int x,int y){
		animation_delayed_xy[0]=x;
		animation_delayed_xy[1]=y;
	}
	public void setAnimationCode(int n_statement){
		animation_code=n_statement;
	}
	public int getTeam(){return team;}
	public int[] getAnimationStartXY(){return animation_start_xy;}
	public int getEntityIndex(){return entity_index;}
	public int[] getAnimationDelayedXY(){return animation_delayed_xy;}
	public int getAnimationCode(){return animation_code;}
}
