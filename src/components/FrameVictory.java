package components;
import javax.swing.*;
import java.awt.event.*;

public class FrameVictory extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private JPanel panel_frame;
	private JLabel label_name;
	private JButton button_ok;
	
	private String txt;
	private FrameMain frame_father;
	
	public FrameVictory(FrameMain ff, String text)
	{
		txt=text;
		frame_father=ff;
		panel_frame = new JPanel();
		this.add(panel_frame);
		initComponents();
		setSize(400,100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null); // to center frame
		setResizable(false);
	}
	
	private void initComponents()
	{
		label_name=new JLabel(txt);
		panel_frame.add(label_name);
		
		button_ok = new JButton("Restart game");
		panel_frame.add(button_ok);
		
		button_ok.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getSource()==button_ok)
		{
			frame_father.initGame();
			frame_father.setEnabled(true);
			this.setVisible(false);
		}
	}
	
	
}