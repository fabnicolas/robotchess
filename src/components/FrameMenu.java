package components;
import extra.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class FrameMenu extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JPanel panel_frame;
	private JLabel label_nome,label_start_sel,label_load_sel;
	private JComboBox<String> combobox_start_sel,combobox_load_sel;
	private JButton button_menu_start,button_start_sel,button_menu_load,button_load_sel;
	
	
	public FrameMenu()
	{
		initComponents();
		this.setLayout(null);
		setSize(150,150);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to close program with X
		setLocationRelativeTo(null); // to center frame
		setResizable(false);
		
		this.add(panel_frame);
		panel_frame.setLayout(null);
		panel_frame.add(label_nome);
		panel_frame.add(button_menu_start);
		panel_frame.add(button_menu_load);
		panel_frame.setBounds(0,0,500,150);

		Utility.setLocationComponent(label_nome,5,0);
		Utility.setLocationComponent(button_menu_start,30,label_nome.getY()+label_nome.getHeight()+20);
		Utility.setLocationComponent(button_menu_load,20,button_menu_start.getY()+button_menu_start.getHeight()+10);
		
	}
	
	private void initComponents()
	{
		panel_frame = new JPanel();
		label_nome=new JLabel("Welcome in RobotIDK");
		button_menu_start = new JButton("START");
		button_menu_start.addActionListener(this);
		button_start_sel=new JButton("Start play");
		button_start_sel.addActionListener(this);
		button_menu_load=new JButton("LOAD GAME");
		button_menu_load.addActionListener(this);
		button_load_sel=new JButton("Load save");
		button_load_sel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt){
		// TODO Auto-generated method stub
		if(evt.getSource()==button_menu_start){	
			label_start_sel=new JLabel("Select scenario: ");
			combobox_start_sel=new JComboBox<String>();
			int numero_livelli = new File("levels//").listFiles().length;			
			for(int i=0;i<numero_livelli;i++)
				combobox_start_sel.addItem(String.valueOf(i+1));
			
			button_start_sel.setSize(50,20);
			
			panel_frame.removeAll();
			panel_frame.add(label_start_sel);
			panel_frame.add(combobox_start_sel);
			panel_frame.add(button_start_sel);
			
			Utility.setLocationComponent(label_start_sel,5,0);
			Utility.alignAtRightOf(combobox_start_sel,label_start_sel);
			Utility.alignAtRightOf(button_start_sel,combobox_start_sel);
			
			setSize(300,75);
			setLocationRelativeTo(null);
			repaint();
			
		}else if(evt.getSource()==button_start_sel){
			try{
				int scenario = Integer.parseInt(String.valueOf(combobox_start_sel.getSelectedItem()));
				new FrameMain(scenario,0).setVisible(true);
				this.setVisible(false);
			}catch(Exception e){e.printStackTrace();}
			
		}else if(evt.getSource()==button_menu_load){
			label_load_sel=new JLabel("Select savefile: ");
			int numero_saves = new File("quicksaves//").listFiles().length;
			combobox_load_sel=new JComboBox<String>();		
			for(int i=0;i<numero_saves;i++)
				combobox_load_sel.addItem(String.valueOf(i+1));
			
			button_load_sel.setSize(50,20);
			
			panel_frame.removeAll();
			panel_frame.add(label_load_sel);
			panel_frame.add(combobox_load_sel);
			panel_frame.add(button_load_sel);
			
			Utility.setLocationComponent(label_load_sel,5,0);
			Utility.alignAtRightOf(combobox_load_sel,label_load_sel);
			Utility.alignAtRightOf(button_load_sel,combobox_load_sel);
			
			setSize(300,75);
			setLocationRelativeTo(null);
			repaint();
		}else if(evt.getSource()==button_load_sel){
			try{
				int scenario = Integer.parseInt(String.valueOf(combobox_load_sel.getSelectedItem()));
				new FrameMain(scenario,1).setVisible(true);
				this.setVisible(false);
			}catch(Exception e){e.printStackTrace();}
			
		}
			
	}
	
	
}