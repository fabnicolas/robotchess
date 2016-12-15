package components;
import classes.*;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;

public class FrameInfoRobot extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JPanel panel_frame;
	private JTextArea textarea_robot;
	private JButton button_ok;
	
	private Robot robot;
	private FrameMain frame_father;
	
	public FrameInfoRobot(FrameMain ff, Robot r)
	{
		robot=r;
		frame_father=ff;
		panel_frame = new JPanel();
		this.add(panel_frame);
		initComponents();
		buildInfo();
		setSize(150,200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null); // to center frame
		setResizable(false);
	}
	
	private void initComponents()
	{
		textarea_robot=new JTextArea();
		textarea_robot.setPreferredSize(new Dimension(140,130));
		button_ok = new JButton("OK");
		
		panel_frame.add(textarea_robot);
		panel_frame.add(button_ok);
		
		button_ok.addActionListener(this);
	}

	private void buildInfo()
	{
		textarea_robot.append("-----DATA ROBOT-----\n");
		textarea_robot.append("> Life: "+robot.getLife()+"/"+robot.getMaxLife()+"\n");
		textarea_robot.append("> Energy: "+robot.getEnergy()+"/"+robot.getMaxEnergy()+"\n");
		textarea_robot.append("> Objects held: \n");
		for(int i=0;i<robot.getObjectLength();i++){
			textarea_robot.append(robot.getObject(i).getName());
			textarea_robot.append("(Res.: "+robot.getObject(i).getResistance()+")\n");
		}
		textarea_robot.append("Object equipped: "+robot.getObjectEquipped().getName());
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource()==button_ok)
		{
			frame_father.setEnabled(true);
			this.setVisible(false);
		}
	}
	
	
}