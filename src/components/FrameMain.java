package components;
import classes.*;
import extra.*;
import exceptions.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class FrameMain extends JFrame implements ActionListener, MouseListener{
	private static final long serialVersionUID = 1L;
	
	private GamePanel gp;
	private ImagePanel_nexus b1,b2;
	private JButton button_start,button_action,button_save;
	private JLabel label_team,label_robot,label_action,label_action_extra;
	private JComboBox<String> combobox_team,combobox_action,combobox_action_extra,combobox_robot;
	private int cod_scenario,type_loading;
	
	private int team;
	
	public FrameMain(int n_scenario, int type_load)
	{
		initComponents();
		this.setLayout(null);
		gp = new GamePanel();
		cod_scenario=n_scenario;
		type_loading=type_load;
		this.initGame();
		b1=new ImagePanel_nexus(gp.scenario.getTeam(0));
		b2=new ImagePanel_nexus(gp.scenario.getTeam(1));
		this.add(b1);
		this.add(b2);
		this.add(gp);
		b1.setLocation(0,0);
		gp.setLocation(b1.getX()+b1.getWidth(),0);
		b2.setLocation(gp.getX()+gp.getWidth(),0);
		
		gp.add(button_start);
		
		setSize(1200,640);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to close program with X
		setLocationRelativeTo(null); // to center frame
		setResizable(false);
	}
	
	private void initComponents()
	{
		button_start=new JButton("Start play");
		button_action=new JButton("Execute");
		button_save=new JButton("Save game!");
		
		button_start.addActionListener(this);
		button_action.addActionListener(this);
		button_save.addActionListener(this);
		
		label_team=new JLabel("Team: ");
		label_robot=new JLabel("Robot N°: ");
		label_action=new JLabel("Action: ");
		label_action_extra=new JLabel(">");
		
		combobox_team=new JComboBox<String>(new String[]{"0", "1"});
		combobox_robot=new JComboBox<String>(new String[]{"-1"});
		combobox_action=new JComboBox<String>(new String[]{"Pass turn"});
		combobox_action_extra=new JComboBox<String>(new String[]{"--","N","S","E","W"});
		
		combobox_action.addActionListener(this);
		
		label_team.setVisible(false);
		label_robot.setVisible(false);
		label_action.setVisible(false);
		label_action_extra.setVisible(false);
		combobox_team.setVisible(false);
		combobox_robot.setVisible(false);
		combobox_action.setVisible(false);
		button_action.setVisible(false);

		combobox_team.setEnabled(false);
		combobox_robot.setEnabled(false);
		combobox_action.setEnabled(false);
		combobox_action_extra.setEnabled(false);
		
		combobox_action.setPreferredSize(new java.awt.Dimension(200,combobox_action.getPreferredSize().height));
		combobox_action_extra.setPreferredSize(new java.awt.Dimension(50,combobox_action_extra.getPreferredSize().height));
	}
	
	public void initGame()
	{
		gp.removeAll();
		gp.add(button_start);
		button_start.setVisible(true);
		
		for(int i=0;i<gp.scenario.getTeam(0).length();i++)
			gp.scenario.getTeam(0).removeRobot(i);
		for(int i=0;i<gp.scenario.getTeam(1).length();i++)
			gp.scenario.getTeam(1).removeRobot(i);
		for(int i=0;i<gp.scenario.lengthO();i++)
			gp.scenario.list_ost.remove(i);
		
		gp.scenario.getTeam(0).setWorkbench(new Workbench(0, 4));
		gp.scenario.getTeam(1).setWorkbench(new Workbench(9, 0));
		
		team=-1;
		
		try
		{
			String path,ext;
			if(type_loading==0){path="levels\\level";ext="lvl";}
			else			   {path="quicksaves\\save";ext="savefile";}
			FileReader reader = new FileReader(path+cod_scenario+"."+ext);
			Scanner in = new Scanner(reader);
			
			Team team_building=null;
			int n_team=-1,default_warrior_maxlife=-1,default_warrior_maxenergy=-1,default_lavoratore_maxlife=-1,default_lavoratore_maxenergy=-1;
			while(in.hasNextLine()){
				String cl = in.nextLine(); // cl=current line
				if(!cl.isEmpty()){
					if(cl.charAt(0)!='/'){
						if(cl.equals("P1")){
							n_team=0;
							team_building=gp.scenario.getTeam(0);
						}else if(cl.equals("P2")){
							n_team=1;
							team_building=gp.scenario.getTeam(1);
						}else if(cl.charAt(0)=='V'){
							String[] values=cl.split(":");
							team_building.setNexusLife(Integer.parseInt(values[1]));
							team_building.setNexusMaxLife(Integer.parseInt(values[2]));
							default_warrior_maxlife=Integer.parseInt(values[3]);
							default_warrior_maxenergy=Integer.parseInt(values[4]);
							default_lavoratore_maxlife=Integer.parseInt(values[5]);
							default_lavoratore_maxenergy=Integer.parseInt(values[6]);
						}else if(cl.charAt(0)=='C'){
							String[] values=cl.split(":");
							if(team_building!=null) team_building.addRobot(
									new RobotWarrior(Integer.parseInt(values[1]), default_warrior_maxlife, Integer.parseInt(values[2]), default_warrior_maxenergy, Integer.parseInt(values[5]), n_team),
									Integer.parseInt(values[3]),
									Integer.parseInt(values[4])
																						);
						}else if(cl.charAt(0)=='L'){
							String[] values=cl.split(":");
							if(team_building!=null) team_building.addRobot(
									new RobotWorker(Integer.parseInt(values[1]), default_lavoratore_maxlife, Integer.parseInt(values[2]), default_lavoratore_maxenergy, Integer.parseInt(values[5]), n_team),
									Integer.parseInt(values[3]),
									Integer.parseInt(values[4])
																						);
						}else if(cl.charAt(0)=='A'){
							String[] values=cl.split(":");
							if(team_building.getRobot(Integer.parseInt(values[1])) instanceof RobotWarrior)
							{
								RobotWarrior r=(RobotWarrior)team_building.getRobot(Integer.parseInt(values[1]));
								r.addWeapon(new Weapon(values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5])));
							}
							
						}else if(cl.charAt(0)=='T'){
							String[] values=cl.split(":");
							if(team_building.getRobot(Integer.parseInt(values[1])) instanceof RobotWorker)
							{
								RobotWorker r=(RobotWorker)team_building.getRobot(Integer.parseInt(values[1]));
								r.addTool(new Tool(values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5])));
							}
						}else if(cl.charAt(0)=='O'){
							String[] values=cl.split(":");
							if(team_building!=null) gp.scenario.list_ost.add(new Obstacle(values[1], values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]), Integer.parseInt(values[6]), Integer.parseInt(values[7])));
						}else if(cl.charAt(0)=='W'){
							String[] values=cl.split(":");
							if(values[1].equals("A"))
							{
								Weapon x=new Weapon(values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]));
								gp.scenario.getTeam(0).getWorkbench().addWeapon(x);
								gp.scenario.getTeam(1).getWorkbench().addWeapon(x);
							}
							else if(values[1].equals("T"))
							{
								Tool x=new Tool(values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]));
								gp.scenario.getTeam(0).getWorkbench().addTool(x);
								gp.scenario.getTeam(1).getWorkbench().addTool(x);
							}
						}else if(cl.charAt(0)=='S'){
							String[] values=cl.split(":");
							team=Integer.parseInt(values[1]);
						}else if(cl.charAt(0)=='B'){
							String[] values=cl.split(":");
							gp.setBackgroundImage(values[1]);
						}
					}
				}
			}
			in.close();
			reader.close();
			repaint();
		}catch(Exception e){e.printStackTrace();}
		
		gp.scenario.getTeam(0).getRobot(0).setTarget(gp.scenario.getEnemyTeam(0).getRobot(3));
		gp.scenario.getTeam(0).getRobot(1).setTarget(gp.scenario.getEnemyTeam(0).getRobot(0));
		gp.scenario.getTeam(0).getRobot(3).setTarget(gp.scenario.getEnemyTeam(0).getRobot(1));
		gp.scenario.getTeam(1).getRobot(0).setTarget(gp.scenario.getEnemyTeam(1).getRobot(3));
		gp.scenario.getTeam(1).getRobot(1).setTarget(gp.scenario.getEnemyTeam(1).getRobot(0));
		gp.scenario.getTeam(1).getRobot(3).setTarget(gp.scenario.getEnemyTeam(1).getRobot(1));
		
		
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getSource()==button_start)
		{
			button_start.setVisible(false);
			
			gp.add(label_team);
			label_team.setVisible(true);
			
			gp.add(combobox_team);
			combobox_team.setVisible(true);
			
			gp.add(label_robot);
			label_robot.setVisible(true);
			
			gp.add(combobox_robot);
			combobox_robot.setVisible(true);
			
			gp.add(label_action);
			label_action.setVisible(true);
			
			gp.add(combobox_action);
			combobox_action.setVisible(true);
			
			gp.add(label_action_extra);
			label_action_extra.setVisible(true);
			
			gp.add(combobox_action_extra);
			combobox_action_extra.setVisible(true);

			
			gp.add(button_action);
			button_action.setVisible(true);
			
			gp.add(button_save);
			button_save.setVisible(true);
			
			if(team==-1)
			{
				team=Utility.rand(0, 1);
			}
			setShiftOf(team);
			this.addMouseListener(this);
		}else if(evt.getSource()==button_action){
			final String str_team=(String)combobox_team.getSelectedItem();
			final int n_robot = Integer.parseInt((String)combobox_robot.getSelectedItem());
			final String str_action = (String)combobox_action.getSelectedItem();
			final String str_action_extra = (String)combobox_action_extra.getSelectedItem();
			
			boolean action_completed=false;
			int id_team=Integer.parseInt(str_team);
			Team controller=gp.scenario.getTeam(id_team);
			
			try{
				if(n_robot >= 0 && n_robot < controller.length()){
					// check energy depending on action
					int required_energy;
					switch(str_action)
					{
						case "Move":
						case "Move obstacle":
											 required_energy=10; break;
						case "Attack":
						case "Attack nexus": required_energy=15; break;
						default: required_energy=0;
					}
					Robot robot=controller.getRobot(n_robot);
					if((robot.getEnergy()-required_energy) < 0)
						throw new InsufficientEnergyException("NOT ENOUGH ENERGY");
					if(str_action.equals("--"))
						throw new ActionException("Error: no action decided!");
					
					String allowed_actions[]=Utility.getPossibleActions(robot);
					boolean action_allowed=false;
					for(String x : allowed_actions) if(x.equals(str_action)){action_allowed=true;break;}
					
					
					if(action_allowed){
						if(str_action.equals("Move")){
							if(str_action_extra.equals("--")) throw new ActionException("Error: no direction decided!");
							int enemy_index=robot.move(str_action_extra, gp, id_team, n_robot);
							if(enemy_index == 0){
								gp.showNMFrames("Movement OK!");
								action_completed=true;
							}else throw new ActionException("Error: cannot move in that position!");
						}else if(str_action.equals("Attack")){
							if(str_action_extra.equals("--")) throw new ActionException("Error: no direction decided!");
							if(robot instanceof RobotWarrior){
								RobotWarrior robotC=(RobotWarrior)robot;
								int[] attackResults = robotC.attack(str_action_extra, gp.scenario, id_team);
								if(attackResults[0] > -1){
									action_completed=true;
									try{
										if(attackResults[1] == 1)
											gp.showNMFrames("Attack successful: robot enemy DESTROYED!!!");
										else if(attackResults[1] == 2)
											throw new CriticalStatusException("ROBOT(S) IN CRITICAL CONDITIONS");
									}catch(CriticalStatusException cse){
										gp.showCSEErrorFrames();
									}
								}else throw new ActionException("Error: there isn't any enemy or obstacle to attack!");
							}
						}else if(str_action.equals("Attack nexus")){
							if(robot instanceof RobotWarrior){
								RobotWarrior robotC=(RobotWarrior)robot;
								int result = robotC.attackNexus(gp.scenario, id_team);
								if(result > -1){
									gp.showNMFrames("Enemy nexus attacked!!!");
									if(result==1){
										this.setEnabled(false);
										new FrameVictory(this,"Team "+(id_team+1)+" WON! Congratulations!!!").setVisible(true);
									}else action_completed=true;
								}else throw new ActionException("Error: robot is NOT near to enemy nexus!");
							}
						}else if(str_action.equals("Heal ally LIFE")){
							if(robot instanceof RobotWorker){
								RobotWorker robotL=(RobotWorker)robot;
								int result = robotL.heal(str_action_extra, gp.scenario, id_team, 0);
								if(result > -1)
								{
									 gp.showNMFrames("Life healed successfully!");
									 action_completed=true;
								}else throw new ActionException("Error: there isn't any robot ally to heal!");
							}
						}else if(str_action.equals("Heal ally ENERGY")){
							if(controller.getRobot(n_robot) instanceof RobotWorker){
								RobotWorker robotL=(RobotWorker)robot;
								int result = robotL.heal(str_action_extra, gp.scenario, id_team, 1);
								if(result > -1)
								{
									gp.showNMFrames("Energy healed successfully!");
									action_completed=true;
								}else{
									throw new ActionException("Error: there isn't any robot ally to heal!");
								}
							}
						}else if(str_action.equals("Move obstacle")){
							if(str_action_extra.equals("--")) throw new ActionException("Error: no direction decided!");
							int enemy_index=robot.moveObstacle(str_action_extra, gp);
							if(enemy_index == 0)
							{
								gp.showNMFrames("Obstacle movement OK!");
								action_completed=true;
							}else{
								if(enemy_index==-1) throw new ActionException("Error: cannot do this action in that position!");
								else if(enemy_index==-2) throw new ActionException("Error: no such obstacle!");
								else if(enemy_index==-3) throw new ActionException("Error: designed obstacle cannot reach that position!");
								else if(enemy_index==-4) throw new ActionException("Error: something blocks obstacle movement!");
							}	
						}else if(str_action.equals("Use Workbench")){
							int workbench_found=-1;
							int xy_new[]=Utility.getDirectionXY(str_action_extra, robot.x, robot.y);
							int x_new=xy_new[0];
							int y_new=xy_new[1];
							workbench_found = gp.scenario.collisionDetect(controller,x_new,y_new,2);
							if(workbench_found==0)
							{
								robot.takeObjectFromWB(controller.getWorkbench());
								gp.showNMFrames("Item took from workbench (and equipped): "+robot.getObjectEquipped().getName());
								action_completed=true;
							}else throw new ActionException("Error: workbench not found!");
						}else if(str_action.equals("Recharge self energy"))
						{
							int workbench_found=-1;
							int xy_new[]=Utility.getDirectionXY(str_action_extra, robot.x, robot.y);
							int x_new=xy_new[0];
							int y_new=xy_new[1];
							workbench_found = gp.scenario.collisionDetect(controller,x_new,y_new,2);
							if(workbench_found==0)
							{
								if(robot instanceof RobotWorker){
									((RobotWorker)robot).rechargeSelfEnergy();
									gp.showNMFrames("Robot worker energy recharged at workbench!");
									action_completed=true;
								}else throw new ActionException("Error: this robot is not a worker!");
							}else throw new ActionException("Error: workbench not found!");
						}else if(str_action.equals("Info Robot")){
							this.setEnabled(false);
							FrameInfoRobot fir=new FrameInfoRobot(this,robot);
							fir.setVisible(true);
						}
					}else{
						if(!str_action.equals("Pass turn"))
							throw new ActionException("Error: this robot cannot do this kind of action!");
					}
				}
				if(!action_completed && str_action.equals("Pass turn")){
					gp.showNMFrames("Player "+(id_team+1)+" passed turn to the enemy!");
					action_completed=true;
				}
			}catch(ActionException ae){
				gp.showAEErrorFrames(ae.getMessage());
			}catch(InsufficientEnergyException iee){
				gp.showIEEErrorFrames();
			}catch(Exception e){
				System.out.println("Errore #1: "+e.getMessage());
				e.printStackTrace();
			}finally{
				if(action_completed)
				{
					gp.resetIEE();
					gp.sf=1.0f;
					if(id_team==0){
						IA(id_team,0);
						IA(id_team,1);
						IA(id_team,3);
					}
					if(id_team==0)	  setShiftOf(1);
					else			  setShiftOf(0);
					for(int team=0;team<2;team++){
						for(int i=0;i<gp.scenario.getTeam(team).length();i++){
							Robot r=gp.scenario.getTeam(team).getRobot(i);
							if(r instanceof RobotWorker){
								RobotWorker robot=(RobotWorker)r;
								robot.setEnergy(robot.getEnergy()+5);
								if(robot.getEnergy()>robot.getMaxEnergy())
									robot.setEnergy(robot.getMaxEnergy());
							}
						}
					}
					gp.execAnimations();
				}
				repaint();
			}
		}else if(evt.getSource()==combobox_action){
			combobox_action_extra.removeAllItems();
			switch(String.valueOf(combobox_action.getSelectedItem()))
			{
			case "Move": 	
			case "Move obstacle":
			case "Attack":
			case "Heal ally LIFE":
			case "Heal ally ENERGY":
			case "Use Workbench":
							combobox_action_extra.addItem("--");
							combobox_action_extra.addItem("N");
							combobox_action_extra.addItem("S");
							combobox_action_extra.addItem("E");
							combobox_action_extra.addItem("W");
							break;
			case "--":
			case "Attack nexus":
			case "Info Robot":
			case "Pass turn":
							combobox_action_extra.addItem("--");
							break;
			}
		}else if(evt.getSource()==button_save){
			try{
				int numero_saves = new File("quicksaves//").listFiles().length;
				PrintWriter pw = new PrintWriter("quicksaves//save"+(numero_saves+1)+".savefile");
				pw.println("P1");
				pw.print("V:"+gp.scenario.getTeam(0).getNexusLife());
				pw.print(":"+gp.scenario.getTeam(0).getNexusMaxLife());
				RobotWarrior temp1=new RobotWarrior();
				pw.print(":"+temp1.getMaxLife());
				pw.print(":"+temp1.getMaxEnergy());
				RobotWorker temp2=new RobotWorker();
				pw.print(":"+temp2.getMaxLife());
				pw.print(":"+temp2.getMaxEnergy());
				pw.println();
				for(int i=0;i<gp.scenario.getTeam(0).length();i++)
				{
					Robot r=gp.scenario.getTeam(0).getRobot(i);
					if(r instanceof RobotWarrior)
						pw.print("C");
					else pw.print("L");
					pw.print(":"+r.getLife());
					pw.print(":"+r.getEnergy());
					pw.print(":"+r.x);
					pw.print(":"+r.y);
					if(r instanceof RobotWarrior)
					{
						pw.print(":"+((RobotWarrior)r).weapon_equip);
					}else if(r instanceof RobotWorker)
					{
						pw.print(":"+((RobotWorker)r).tool_equip);
					}
					pw.println();
				}
				for(int i=0;i<gp.scenario.getTeam(0).length();i++)
				{
					Robot r=gp.scenario.getTeam(0).getRobot(i);
					if(r instanceof RobotWarrior)
					{
						for(int j=1;j<((RobotWarrior)r).list_weapons.size();j++)
						{
							Weapon x = ((RobotWarrior)r).list_weapons.get(j);
							pw.println("A:"+i+":"+x.getName()+":"+x.getDmg()+":"+x.getShiftDelay()+":"+x.getResistance());
						}
					}
					else if(r instanceof RobotWorker)
					{
						for(int j=1;j<((RobotWorker)r).list_tools.size();j++)
						{
							Tool x = ((RobotWorker)r).list_tools.get(j);
							pw.println("A:"+i+":"+x.getName()+":"+x.getLifeHealed()+":"+x.getShiftDelay()+":"+x.getResistance());
						}
					}
				}
				pw.println("P2");
				pw.print("V:"+gp.scenario.getTeam(1).getNexusLife());
				pw.print(":"+gp.scenario.getTeam(1).getNexusMaxLife());
				RobotWarrior temp3=new RobotWarrior();
				pw.print(":"+temp3.getMaxLife());
				pw.print(":"+temp3.getMaxEnergy());
				RobotWorker temp4=new RobotWorker();
				pw.print(":"+temp4.getMaxLife());
				pw.print(":"+temp4.getMaxEnergy());
				pw.println();
				for(int i=0;i<gp.scenario.getTeam(1).length();i++)
				{
					Robot r=gp.scenario.getTeam(1).getRobot(i);
					if(r instanceof RobotWarrior)
						pw.print("C");
					else pw.print("L");
					pw.print(":"+r.getLife());
					pw.print(":"+r.getEnergy());
					pw.print(":"+r.x);
					pw.print(":"+r.y);
					if(r instanceof RobotWarrior)
					{
						pw.print(":"+((RobotWarrior)r).weapon_equip);
					}else if(r instanceof RobotWorker)
					{
						pw.print(":"+((RobotWorker)r).tool_equip);
					}
					pw.println();
				}
				for(int i=0;i<gp.scenario.getTeam(1).length();i++)
				{
					Robot r=gp.scenario.getTeam(1).getRobot(i);
					if(r instanceof RobotWarrior)
					{
						for(int j=1;j<((RobotWarrior)r).list_weapons.size();j++)
						{
							Weapon x = ((RobotWarrior)r).list_weapons.get(j);
							pw.println("A:"+i+":"+x.getName()+":"+x.getDmg()+":"+x.getShiftDelay()+":"+x.getResistance());
						}
					}
					else if(r instanceof RobotWorker)
					{
						for(int j=1;j<((RobotWorker)r).list_tools.size();j++)
						{
							Tool x = ((RobotWorker)r).list_tools.get(j);
							pw.println("A:"+i+":"+x.getName()+":"+x.getLifeHealed()+":"+x.getShiftDelay()+":"+x.getResistance());
						}
					}
				}
				for(int i=0;i<gp.scenario.lengthO();i++){
					Obstacle temp = gp.scenario.list_ost.get(i);
					pw.println("O:"+temp.getObstacleName()+":"+temp.getRenderImage()+":"+temp.getLife()+":"+temp.getMaxLife()+":"+temp.getWeight()+":"+temp.x+":"+temp.y);
				}
				for(int i=0;i<gp.scenario.getTeam(0).getWorkbench().lengthW();i++){
					Weapon temp=gp.scenario.getTeam(0).getWorkbench().getWeapon(i);
					pw.println("W:A:"+temp.getName()+":"+temp.getDmg()+":"+temp.getShiftDelay()+":"+temp.getResistance());
				}
				for(int i=0;i<gp.scenario.getTeam(0).getWorkbench().lengthT();i++)
				{
					Tool temp=gp.scenario.getTeam(0).getWorkbench().getTool(i);
					pw.println("W:T:"+temp.getName()+":"+temp.getLifeHealed()+":"+temp.getShiftDelay()+":"+temp.getResistance());
				}
				pw.println("S:"+String.valueOf(combobox_team.getSelectedItem()));
				pw.println("B:"+gp.getBackgroundImage());
				pw.close();
				System.out.println("File creato!");
			}catch(Exception e){e.printStackTrace();}
		}
	}
	
	private void setShiftOf(int team){
		combobox_team.setSelectedItem(String.valueOf(team));
		combobox_robot.removeAllItems();
		combobox_robot.addItem("-1");
		combobox_robot.setSelectedItem("-1");
		combobox_action.setSelectedItem("Pass turn");
		combobox_action_extra.setSelectedItem("--");
		int x_sel_robot=-1,y_sel_robot=-1;
		Team team_control=gp.scenario.getTeam(team);
		for(int i=0;i<team_control.length();i++)
		{
			combobox_robot.addItem(String.valueOf(i));
			if(i==0)
			{
				x_sel_robot=team_control.getRobot(0).x;
				y_sel_robot=team_control.getRobot(0).y;
				combobox_robot.setSelectedItem("0");
			}
		}
		gp.showPMFrames("It's player "+(team+1)+" turn!");
		gp.setSelectedCell(x_sel_robot, y_sel_robot, 0);
		gp.forceRefresh();
	}

	@Override	public void mouseClicked(MouseEvent e){}
	@Override	public void mouseEntered(MouseEvent e){}
	@Override	public void mouseExited(MouseEvent e){}
	@Override	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){
		// TODO Auto-generated method stub
		final int cella_x=(int)(((double)(e.getX()-gp.getX()-gp.blg-3))/((double)90));
		final int cella_y=(int)(((double)(e.getY()-gp.getY()-gp.blg-26))/((double)108));
		if(cella_x>=0 && cella_x<10 && cella_y>=0 && cella_y<5)
		{
			gp.forceRefresh();
			int team=Integer.parseInt((String)combobox_team.getSelectedItem());
			int robot_index=-1;
			if(team==0 || team==1)
				robot_index = gp.scenario.getTeam(team).searchRobot(cella_x, cella_y);
			
			if(robot_index > -1)
			{
				boolean selectCell=false;
				int[] old_selection = gp.getSelectedCell();
				if(cella_x == old_selection[0] && cella_y == old_selection[1])
				{
					if(gp.getSelectionAreaN()==0)
					{
						gp.setSelectionArea(cella_x,cella_y);
						combobox_action.removeAllItems();
						Robot robot = gp.scenario.getTeam(team).getRobot(robot_index);
						String allowed_actions[]=Utility.getPossibleActions(robot);
						for(String x: allowed_actions){
							combobox_action.addItem(x);
						}
						combobox_action.addItem("Pass turn");
						combobox_action.setSelectedItem("Move");
						combobox_action.showPopup();
					}
					else selectCell=true;
				}else{
					if(gp.getSelectionAreaN()==0) // se la selezione è disabilitata
						selectCell=true;
				}
				if(selectCell){
					combobox_robot.setSelectedItem(String.valueOf(robot_index));
					gp.setSelectedCell(cella_x,cella_y,0);
					combobox_action.removeAllItems();
					combobox_action.addItem("Pass turn");
				}
					
			}
			
			int[] old_selection = gp.getSelectedCell(); // cella gialla
			int[] sel_found=null;
			boolean found=false;
			for(int i=0;i<gp.getSelectionAreaN() && (!found);i++)
			{
				int[] sel_area_xy=gp.getSelectionArea()[i]; // cella blu alla riga i
				if(cella_x == sel_area_xy[0] && cella_y == sel_area_xy[1])
				{
					// la cella che sto cliccando è nella selezione
					sel_found=sel_area_xy;
					found=true;
				}
			}
			if(found) // è stata trovata la cella
			{
				if(old_selection[0] == sel_found[0])
				{
					if(old_selection[1] < sel_found[1]) combobox_action_extra.setSelectedItem("S");
					else								combobox_action_extra.setSelectedItem("N");
				}else if(old_selection[1] == sel_found[1]){
					if(old_selection[0] < sel_found[0]) combobox_action_extra.setSelectedItem("E");
					else								combobox_action_extra.setSelectedItem("W");
				}
			}
		}
	}
	
	private void IA(int team,int n_robot){
		//System.out.print("IA("+team+","+n_robot+") executed: ");
		Team controller=gp.scenario.getTeam(team);
		Robot r = controller.getRobot(n_robot);
		if(r.getTarget() instanceof Robot)
		{
			Robot r_enemy = (Robot)r.getTarget();
			if(r.y == r_enemy.y){
				if(r.x < r_enemy.x){
					int code=r.move("E", gp, team, n_robot);
					if(code!=0){	//qualcosa non va
						int rand = Utility.rand(0, 1);
						String direction=null;
						if(rand==0) direction="N";
						else		direction="S";
						code=r.move(direction, gp, team, n_robot);
						if(code!=0){
							if(direction=="N") direction="S";
							else			   direction="N";
							code=r.move(direction, gp, team, n_robot);
							if(code!=0){
								r.move("W", gp, team, n_robot);
							}
						}
					}
				}else{
					int code=r.move("W", gp, team, n_robot);
					if(code!=0){	//qualcosa non va
						int rand = Utility.rand(0, 1);
						String direction=null;
						if(rand==0) direction="N";
						else		direction="S";
						code=r.move(direction, gp, team, n_robot);
						if(code!=0){
							if(direction=="N") direction="S";
							else			   direction="N";
							code=r.move(direction, gp, team, n_robot);
							if(code!=0){
								r.move("E", gp, team, n_robot);
							}
						}
					}
				}
			}else{
				if(r.y < r_enemy.y){//sta + in alto
					String direction=null;
					int rand = Utility.rand(0, 1);
					if(rand==0) direction="S";
					else		direction="E";
					int code=r.move(direction, gp, team, n_robot);
					if(code!=0){
						if(direction=="S") direction="E";
						else			   direction="S";
						r.move(direction, gp, team, n_robot);
					}
				}else{
					String direction=null;
					int rand = Utility.rand(0, 1);
					if(rand==0) direction="N";
					else		direction="E";
					int code=r.move(direction, gp, team, n_robot);
					if(code!=0){
						if(direction=="N") direction="E";
						else			   direction="N";
						r.move(direction, gp, team, n_robot);
					}
				}
			}
		}
	}
	
	
}