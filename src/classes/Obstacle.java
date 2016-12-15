package classes;
import components.GamePanel;
import extra.Utility;
import interfaces.*;

public class Obstacle extends ExistingEntity implements RenderizableInterface{
	private String name_obstacle;
	private int weight;
	private String render_image;
	
	public Obstacle(String n, String renderimg, int life, int p, int a, int b){
		name_obstacle=n;
		render_image=renderimg;
		super.actual_life=super.max_life=life;
		weight=p;
		super.x=a;
		super.y=b;
	}
	public Obstacle(String n, String renderimg, int actual_life, int max_life, int p, int a, int b){
		this(n,renderimg,max_life,p,a,b);
		super.actual_life=actual_life;
	}
	public int move(String where, GamePanel gp, int n_obstacle){
		int[] xy_new=Utility.getDirectionXY(where, x, y);
		int x_new=xy_new[0];
		int y_new=xy_new[1];
		int n_statement=xy_new[2];
		
		if(x_new >= 0 && x_new < 10 && y_new >= 0 && y_new < 5){
			int blocker_index = gp.scenario.collisionDetect(gp.scenario.getP1(),x_new,y_new,0);
			if(blocker_index==-1) blocker_index = gp.scenario.collisionDetect(gp.scenario.getP2(),x_new,y_new,0);
			if(blocker_index==-1) blocker_index = gp.scenario.collisionDetect(null,x_new,y_new,1);
			if(blocker_index==-1) blocker_index = gp.scenario.collisionDetect(gp.scenario.getP1(),x_new,y_new,2);
			if(blocker_index==-1) blocker_index = gp.scenario.collisionDetect(gp.scenario.getP2(),x_new,y_new,2);
			
			if(blocker_index==-1){
				switch(n_statement){
					case 0: x++; break;
					case 1: x--; break;
					case 2: y--; break;
					case 3: y++; break;
				}
				if(n_statement>-1) gp.stackAnimation(-1,n_obstacle,n_statement);
				return 0; // puoi spostarlo
			}else{return -2;}	// errore: c'è qualcosa che blocca il movimento dell'ostacolo
		}else{return -1;}	// errore: l'ostacolo non può finire fuori dalla mappa!
	}
	@Override public String getRenderImage(){ return render_image; }
	public String getObstacleName(){return name_obstacle;}
	public int getWeight(){return weight;}
	public int getLife(){return super.actual_life;}
	public int getMaxLife(){return super.max_life;}
	public void setLife(int x){super.actual_life=x;}
	public void setMaxLife(int x){super.max_life=x;}
}
