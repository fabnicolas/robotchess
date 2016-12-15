package components;
import classes.*;
import classes.Robot;
import extra.AnimationData;
import extra.Utility;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class GamePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public final int blg = 50; // border dx dy of the grid
	private final int dxw, dxh;
	private Graphics2D g2d; 
	public final Scenario scenario;
	private boolean in_movement=false;
	public int mousex,mousey;
	
	// animations
	public Timer timer_animation;
	private boolean timer_animation_firstexec=true;
	private ArrayList<AnimationData> animation_system;
	
	// selection area
	public int coloredrect_x=-1,coloredrect_y=-1,coloredarea_n=0;
	public int[][] coloredarea_xy;
	
	//background
	private Image background;
	private String background_str;
	
	// exceptions variables
	public float sf=1.00f; // shader for darkening for IEE
	private Timer timer_remove_IEE,timer_remove_CSE,timer_remove_AE,timer_remove_PM,timer_remove_NM;
	private boolean show_IEE_error=false,show_CSE_error=false,show_AE_error=false,show_player_msg=false,show_normal_msg=false;
	
	private String AE_errortxt,PM_txt,NM_txt;
	private Image warningbanner;
	
	public GamePanel(){
		//lista_img = new ArrayList<Image>();
		/* BLG = bordo del layout della griglia, determina la posizione iniziale di x ed y, va sommata sempre
		 * dxw = costante che indica la posizione x di una iesima cella (dxw*i)
		 * dxh = costante che indica la posizione y di una iesima cella (dxh*i)
		 */
		setSize(1000,640);
		dxw = (getWidth()-blg*2) / 10;
		dxh = (getHeight()-blg*2) / 5;
		scenario = new Scenario();
		animation_system=new ArrayList<AnimationData>();
		timer_animation=new Timer(1, new ActionListener() {
			private boolean condition1,condition2;
            @Override
            public void actionPerformed(ActionEvent e){
            	if(timer_animation_firstexec){
            		timer_animation_firstexec=false;
            		for(AnimationData x : animation_system)
            			x.setAnimationDelayedXY(0,0);
            	}
            	for(int i=0;i<animation_system.size();i++){
            		AnimationData x = animation_system.get(i);
            		switch(x.getAnimationCode())
                    {	// 0=est, 1=west, 2=north, 3=south
                    	case 0: x.getAnimationDelayedXY()[0]++;
                    			condition1=(x.getAnimationDelayedXY()[0] <= dxw);
                    			condition2=(x.getAnimationDelayedXY()[0] > dxw);
                    			break;
                    	case 1: x.getAnimationDelayedXY()[0]--;
                    			condition1=(x.getAnimationDelayedXY()[0] >= -dxw);
                    			condition2=(x.getAnimationDelayedXY()[0] < dxw);
                    			break;
                    	case 2: x.getAnimationDelayedXY()[1]--;
                    			condition1=(x.getAnimationDelayedXY()[1] >= -dxh);
                    			condition2=(x.getAnimationDelayedXY()[1] < dxh);
                    			break;
                    	case 3: x.getAnimationDelayedXY()[1]++;
                    			condition1=(x.getAnimationDelayedXY()[1] <= dxh);
                    			condition2=(x.getAnimationDelayedXY()[1] > dxh);
                    			break;
                    	default:condition1=false; condition2=true; break;
                    }
            		if(!condition1 && condition2){
            			// remove this animation
                    	animation_system.remove(i);
                    	i--;
                    }
            	}
            	if(condition1){
                	try{Thread.sleep(2);}catch(Exception exc){exc.printStackTrace();}
                	repaint();
                }
            	if(animation_system.isEmpty()){
            		in_movement=false;
            		repaint();
            		try{Thread.sleep(200);}catch(Exception exc){exc.printStackTrace();}
                    ((Timer)e.getSource()).stop(); // FERMA IL TIMER
            	}
            }

        });
		timer_remove_IEE = new Timer(2000, new ActionListener() {
			@Override public void actionPerformed(ActionEvent e)
			{
				resetIEE();
				((Timer)e.getSource()).stop();
			}
	    });
		timer_remove_CSE = new Timer(5000, new ActionListener(){
			@Override public void actionPerformed(ActionEvent e)
			{
				show_CSE_error=false;
				forceRefresh();
				((Timer)e.getSource()).stop();
			}
		});
		timer_remove_AE = new Timer(1500, new ActionListener(){
			@Override public void actionPerformed(ActionEvent e)
			{
				show_AE_error=false;
				AE_errortxt="";
				forceRefresh();
				((Timer)e.getSource()).stop();
			}
		});
		timer_remove_PM = new Timer(1500, new ActionListener(){
			@Override public void actionPerformed(ActionEvent e)
			{
				show_player_msg=false;
				PM_txt="";
				forceRefresh();
				((Timer)e.getSource()).stop();
			}
		});
		timer_remove_NM = new Timer(2000, new ActionListener(){
			@Override public void actionPerformed(ActionEvent e)
			{
				show_normal_msg=false;
				NM_txt="";
				forceRefresh();
				((Timer)e.getSource()).stop();
			}
		});
		try{
			warningbanner = ImageIO.read(new File("images\\warning.gif"));
		}catch(Exception e){e.printStackTrace();}
	}
	
	@Override public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2d=(Graphics2D)g;
        drawGame();
    }
	
	private void drawGame()
	{
		g2d.setBackground(Color.WHITE);
		g2d.setColor(Color.BLACK);
		this.fillRectC(0, 0, getWidth(), getHeight(), Color.GRAY);
		
		/* building grid
		 * BLG = border layout of grid, set to 50, used to determine the distance from the whole grid to the GamePanel itself
		 * dxw = constant that when multiplied properly, determines x position of index cell (dwx*i)
		 * dxh = constant that when multiplied properly, determines y position of index cell (dwh*i)
		 */
		
		
		// Area di gioco: sfondo bianco, griglia a linee nere
		//this.fillRectC(blg, blg, getWidth()-(blg*2), getHeight()-(blg*2), Color.WHITE);
		g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		//g2d.drawImage(background, blg, blg, getWidth()-(blg*2), getHeight()-(blg*2), null);
		
		
		boolean isWhiteCell=false;
		Color c;
		for(int i=0;i<10;i++) for(int j=0;j<5;j++)
		{
			if(!isWhiteCell)
			{
				c=new Color((int)(220*sf),(int)(220*sf),(int)(220*sf), 200); // grigio
			}else{
				c=new Color((int)(255*sf),(int)(255*sf),(int)(255*sf), 200); // bianco
			}
			isWhiteCell = !isWhiteCell;
			this.fillRectC(blg + dxw*i, blg + dxh*j, dxw, dxh, c);
		}
		
		if(coloredrect_x != -1 && coloredrect_y != -1)
		{
			this.fillRectC(blg + dxw*coloredrect_x, blg + dxh*coloredrect_y, dxw, dxh, Color.YELLOW);
			for(int i=0;i<coloredarea_n;i++)
			{
				this.fillRectC(blg + dxw*coloredarea_xy[i][0], blg + dxh*coloredarea_xy[i][1], dxw, dxh, new Color(48,213,200));
			}
		}

		for(int i=0;i<11;i++)
			g2d.drawLine(blg + dxw*i, blg, blg + dxw*i, getHeight()-blg);
		for(int j=0;j<6;j++)
			g2d.drawLine(blg, blg + dxh*j, getWidth()-blg, blg + dxh*j);
		
		// Sopra l'area di gioco le stat dei giocatori
		this.drawStringC("Player 1", 30, 20, Color.YELLOW);
		this.drawStringC("                   Player 2", getWidth()-130, 20, Color.YELLOW);
		
		// disegno le barre della vita delle basi
		final int percNexusLifeP1=(int)(((double)scenario.getP1().getNexusLife()/(double)scenario.getP1().getNexusMaxLife())*100);
		this.fillRectC(30, 25, 100, 20, Color.RED);
		this.fillRectC(30, 25, percNexusLifeP1, 20, Color.GREEN);
		
		final int percNexusLifeP2=(int)(((double)scenario.getP2().getNexusLife()/(double)scenario.getP2().getNexusMaxLife())*100);
		this.fillRectC(getWidth()-130, 25, 100, 20, Color.RED);
		this.fillRectC(getWidth()-130, 25, percNexusLifeP2, 20, Color.GREEN);
		
		try{
			this.drawRobots(scenario.getP1(),0);
			this.drawRobots(scenario.getP2(),1);
			this.drawWorkbenchs();
			this.drawObstacles();
		}catch(IOException e){
			System.out.println("Error in drawImage: "+e.getMessage());
			e.printStackTrace();
		}
		
		if(show_IEE_error)
		{
			try
			{	g2d.drawImage(warningbanner, getWidth()/2-80, getHeight()/2-150, 200, 174, null);
			}catch(Exception e){e.printStackTrace();}
			
			this.drawStringC("WARNING!!!", getWidth()/2-100, getHeight()/2+65, 40, Color.RED);
			this.drawStringC("Insufficient energy to make this action!", getWidth()/2-150, getHeight()/2+90, 20, Color.ORANGE);
		}
		
		if(show_AE_error) 		this.drawStringC(AE_errortxt, getWidth()/2-300, getHeight()-55, 20, Color.RED);
		else if(show_CSE_error) this.drawStringC("DANGER! Robot(s) in CRITICAL CONDITIONS! Life under 25%!", getWidth()/2-300, getHeight()-35, 20, Color.RED);
		
		if(show_normal_msg) this.drawStringC(NM_txt, 300, blg, 15, Color.WHITE);
		if(show_player_msg) this.drawStringC(PM_txt, getWidth()/2-100, blg+20, 20, Color.RED);
		
		this.drawStringC("mousex="+this.mousex+";mousey="+this.mousey, getWidth()-150, getHeight()-35, Color.WHITE);
	}
	
	private void drawRobots(Team controller, int id_team_ctrl) throws IOException
	{
		Robot robot=null;
		
		for(int i=0;i<controller.length();i++)
		{
			robot=controller.getRobot(i);
			Image test = ImageIO.read(new File("images\\"+robot.getRenderImage()));
			int actual_life = robot.getLife();
			int max_life = robot.getMaxLife();
			int actual_energy = robot.getEnergy();
			int max_energy = robot.getMaxEnergy();
			Color c_life=new Color(41,105,52);
			if(((((float)actual_life)/((float)max_life))*100) <= 25.0f)
			{
				c_life=Color.RED;
			}
			
			int index_ad_robot=-1;
			boolean animation_exists=false;
			if(in_movement){
				index_ad_robot=findAnimationInStack(id_team_ctrl,i);
				if(index_ad_robot!=-1) animation_exists=true;
			}
			if(animation_exists){
				AnimationData ad_robot=animation_system.get(index_ad_robot);
				robot.setLookPosition(Utility.getDirection(ad_robot.getAnimationCode()));
				g2d.drawImage(test, (blg+14) + dxw*(robot.x+ad_robot.getAnimationStartXY()[0])+ad_robot.getAnimationDelayedXY()[0], (blg+14) + dxh*(robot.y+ad_robot.getAnimationStartXY()[1])+ad_robot.getAnimationDelayedXY()[1], null);
				this.drawStringC(actual_life+"/"+max_life, 50+dxw*(robot.x+ad_robot.getAnimationStartXY()[0])+ad_robot.getAnimationDelayedXY()[0], (blg+14) + dxh*(robot.y+ad_robot.getAnimationStartXY()[1])+ad_robot.getAnimationDelayedXY()[1], c_life);
				this.drawStringC(actual_energy+"/"+max_energy, 50+dxw*(robot.x+ad_robot.getAnimationStartXY()[0])+ad_robot.getAnimationDelayedXY()[0], 75+(blg+14) + dxh*(robot.y+ad_robot.getAnimationStartXY()[1])+ad_robot.getAnimationDelayedXY()[1], Color.BLUE);
			}else{
				robot.setLookPosition((id_team_ctrl==0 ? "E" : "W"));
				this.drawSprite(test, robot.x, robot.y);
				this.drawStringC(actual_life+"/"+max_life, 50+dxw*(robot.x), (blg+14) + dxh*(robot.y), c_life);
				this.drawStringC(actual_energy+"/"+max_energy, 50+dxw*(robot.x), 75+(blg+14) + dxh*(robot.y), Color.BLUE);
			}
		}
	}

	private void drawObstacles() throws IOException
	{
		for(int i=0;i<scenario.lengthO();i++)
		{
			final Obstacle obstacle = scenario.getDataObstacle(i);
			Image test = ImageIO.read(new File("images\\"+obstacle.getRenderImage()));
			int index_ad_obstacle=findAnimationInStack(-1,i);
			if(index_ad_obstacle > -1){
				AnimationData ad_obstacle=animation_system.get(index_ad_obstacle);
				g2d.drawImage(test, (blg+14) + dxw*(obstacle.x+ad_obstacle.getAnimationStartXY()[0])+ad_obstacle.getAnimationDelayedXY()[0], (blg+14) + dxh*(obstacle.y+ad_obstacle.getAnimationStartXY()[1])+ad_obstacle.getAnimationDelayedXY()[1], null);
				this.drawStringC(obstacle.getLife()+"/"+obstacle.getMaxLife(), 50+dxw*(obstacle.x+ad_obstacle.getAnimationStartXY()[0])+ad_obstacle.getAnimationDelayedXY()[0], (blg+14) + dxh*(obstacle.y+ad_obstacle.getAnimationStartXY()[1])+ad_obstacle.getAnimationDelayedXY()[1], new Color(41,105,52));
			}else{
				this.drawSprite(test, obstacle.x, obstacle.y);
				this.drawStringC(obstacle.getLife()+"/"+obstacle.getMaxLife(), 50+dxw*(obstacle.x), (blg+14) + dxh*(obstacle.y), new Color(41,105,52));
			}
		}
	}
	
	private void drawWorkbenchs() throws IOException
	{
		this.drawWorkbench(scenario.getP1());
		this.drawWorkbench(scenario.getP2());
	}
	
	private void drawWorkbench(Team controller) throws IOException
	{
		Workbench wb=controller.getWorkbench();
		Image test = ImageIO.read(new File("images\\"+wb.getRenderImage()));
		this.drawSprite(test, wb.x, wb.y);
	}
	
	private void drawSprite(Image img, int i, int j)
	{
		g2d.drawImage(img, (blg+14) + dxw*i, (blg+14) + dxh*j, null);
	}
	
	private void drawStringC(String s, int x, int y, int size, Color c)
	{
		final Color ctemp = g2d.getColor();
		final Font ftemp = g2d.getFont();
		g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, size));
		g2d.setColor(c);
		g2d.drawString(s,x,y);
		g2d.setFont(ftemp);
		g2d.setColor(ctemp);
	}
	
	private void drawStringC(String s, int x, int y, Color c)
	{
		final Color temp = g2d.getColor();
		g2d.setColor(c);
		g2d.drawString(s,x,y);
		g2d.setColor(temp);
	}
	
	private void fillRectC(int x, int y, int width, int height, Color c)
	{
		final Color temp = g2d.getColor();
		final Color tempbck = g2d.getBackground();
		g2d.setColor(c);
		g2d.setBackground(c);
		g2d.fillRect(x,y,width,height);
		g2d.setColor(temp);
		g2d.setBackground(tempbck);
	}
	
	public void stackAnimation(int id_team, int id_entity, int code){
		animation_system.add(new AnimationData(id_team,id_entity,code));
	}
	
	public void execAnimations(){
		timer_animation_firstexec=true;
		in_movement=true;
		for(AnimationData x : animation_system){
			System.out.println("TEAM:"+x.getTeam()+",ENTITY_INDEX:"+x.getEntityIndex()+",ANIMATION_CODE:"+x.getAnimationCode()+",ANIMATION_START:("+x.getAnimationStartXY()[0]+";"+x.getAnimationStartXY()[1]+"),ANIMATION_DELAYED:("+x.getAnimationDelayedXY()[0]+";"+x.getAnimationDelayedXY()[1]+")");
		}
		System.out.println("");
		timer_animation.restart();
	}
	
	private int findAnimationInStack(int team, int i_entity){
		boolean found=false;
		int animation_index=-1;
		for(int i=0;i<animation_system.size() && !found;i++)
		{
			final AnimationData ad = animation_system.get(i);
			if(ad.getTeam() == team && ad.getEntityIndex() == i_entity){
				animation_index=i;
				found=true;
			}
		}
		if(found) return animation_index;
		else 		return -1;
	}
	
	public void showIEEErrorFrames()
	{
		show_IEE_error=true;
		sf=0.25f;
		timer_remove_IEE.restart();
		forceRefresh();
	}
	
	public void showCSEErrorFrames()
	{
		show_CSE_error=true;
		timer_remove_CSE.restart();
		forceRefresh();
	}
	
	public void showAEErrorFrames(String text)
	{
		show_AE_error=true;
		AE_errortxt=text;
		timer_remove_AE.restart();
		forceRefresh();
	}
	
	public void showPMFrames(String text)
	{
		show_player_msg=true;
		PM_txt=text;
		timer_remove_PM.restart();
		forceRefresh();
	}
	
	public void showNMFrames(String text)
	{
		show_normal_msg=true;
		NM_txt=text;
		timer_remove_NM.restart();
		forceRefresh();
	}
	
	public void resetIEE()
	{
		if(show_IEE_error)
		{
			show_IEE_error=false;
			if(getSelectionAreaN()==0) sf=1.00f;
			forceRefresh();
		}
	}
	
	public void forceRefresh()
	{
		this.repaint();
	}
	
	public void setSelectedCell(int x,int y,int type)
	{
		if(type==0) // if cell is robot that should do one action
		{
			this.coloredrect_x=this.mousex=x;
			this.coloredrect_y=this.mousey=y;
			this.sf=1.0f;
			this.coloredarea_n=0;
		}else if(type==1){
			this.coloredarea_xy[this.coloredarea_n][0]=x;
			this.coloredarea_xy[this.coloredarea_n][1]=y;
			this.coloredarea_n++;
		}
	}
	
	public int[] getSelectedCell(){
		return new int[]{this.coloredrect_x, this.coloredrect_y};
	}
	
	public void setSelectionArea(int x,int y)
	{
		this.sf=0.25f;
		this.coloredarea_n=0;
		this.coloredarea_xy=new int[4][2];
		if((x+1)<10) this.setSelectedCell(x+1, y, 1);
		if((x-1)>=0) this.setSelectedCell(x-1, y, 1);
		if((y-1)>=0) this.setSelectedCell(x, y-1, 1);
		if((y+1)<5) this.setSelectedCell(x, y+1, 1);
	}
	
	public void setBackgroundImage(String background_str) throws IOException{
		this.background_str = background_str;
		this.background = ImageIO.read(new File("images\\"+background_str));
	}
	
	public String getBackgroundImage(){return this.background_str;}
	public int[][]	getSelectionArea(){return this.coloredarea_xy;}
	public int		getSelectionAreaN(){return this.coloredarea_n;}

	

	
}