package components;
import classes.*;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class ImagePanel_nexus extends JPanel{
	private static final long serialVersionUID = 1L;
	private Graphics2D g2d; 
	private Team nexus_team;
	private Image bande;
	
	public ImagePanel_nexus(Team nt)
	{
		setSize(100,640);
		try
		{
			bande = ImageIO.read(new File("images\\bande.jpg"));
		}catch(Exception e){e.printStackTrace();}
		nexus_team=nt;
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d=(Graphics2D)g;
        drawImagePanel();
    }
	
	private void drawImagePanel()
	{
		g2d.setBackground(Color.GRAY);
		g2d.setColor(Color.BLACK);
		g2d.drawImage(bande, 0, 0, getWidth(), getHeight(), null);
		try
		{
			Image test=ImageIO.read(new File("images/nexus.gif"));
			int imgx=this.getWidth()/2 - test.getWidth(null)/2;
			int imgy=this.getHeight()/2 - test.getHeight(null)/2;
			g2d.drawImage(test, imgx, imgy, null);
			this.fillRectC(imgx+2, imgy+test.getHeight(null), test.getWidth(null), 20, Color.BLACK);
			this.drawStringC(nexus_team.getNexusLife()+"/"+nexus_team.getNexusMaxLife(), imgx+5, imgy+test.getHeight(null)+15, Color.GREEN);
		}catch(IOException e)
		{
			System.out.println("Errore nel nexus: "+e.getMessage());
		}
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
	
	private void drawStringC(String s, int x, int y, Color c)
	{
		final Color temp = g2d.getColor();
		g2d.setColor(c);
		g2d.drawString(s,x,y);
		g2d.setColor(temp);
	}

	
}