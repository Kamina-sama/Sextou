package rendering;
import javax.swing.JPanel;

import component.Component;
import component.RenderableGeometric;
import component.RenderableSprites;
import component.ScoreBar;
import coordinate.Transform;
import coordinate.Vector2D;
import gameobject.GameObject;
import input.Input;
import input.KeyFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

public class Screen extends JPanel implements IScreen  {
	public static int LARGURA_TELA=1024;
	public static int ALTURA_TELA=768;
	private GameObject root;
	private Graphics2D graphics;
	private Font font;
	private HashMap<String, BufferedImage> icons=new HashMap<String,BufferedImage>();
	
	public Screen(Input i) {
		font=new Font(Font.SANS_SERIF, Font.PLAIN, 24);
		addKeyListener(new KeyFrame("Custom",i));
		JFrame janela = new JFrame("Sextou");
		setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
		setFocusable(true);
		setLayout(null);
		janela.getContentPane().add(this);
		janela.setResizable(false);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLocation(100, 100);
		janela.setVisible(true);
		janela.pack();
		
		try {
			icons.put("DaddyDearest", ImageIO.read(loadStream(new FileInputStream("sprites/DaddyDearest/Icon.png"))));
			icons.put("Pico", ImageIO.read(loadStream(new FileInputStream("sprites/Pico/Icon.png"))));
			icons.put("MommyMearest", ImageIO.read(loadStream(new FileInputStream("sprites/MommyMearest/Icon.png"))));
			icons.put("BF", ImageIO.read(loadStream(new FileInputStream("sprites/BF/Icon.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void ClearScreen() {
		setBackground(Color.black);
		graphics.fillRect(0, 0, LARGURA_TELA, ALTURA_TELA);
	}
	@Override
	public void paint(Graphics g) {
		graphics = (Graphics2D) g;
		ClearScreen();
		Visit(root);
	}
	public void RenderGame(GameObject rootObject) {
		root=rootObject;
		repaint();
	}
	public Graphics2D GetGraphics() {
		return graphics;
	}
	private void Visit(GameObject obj) {
		if(obj==null) return;
		try {
			for(Component c : obj.Components()) c.Accept(this);
			for(GameObject child: obj.Children()) if(child!=null && !child.dead) Visit(child);
		}
		catch(ConcurrentModificationException e) {
			
		}
	}
	@Override
	public void Visit(RenderableGeometric component) {
		if(!component.visible) return;
		Transform globalComponentTransform=component.GlobalTransform();
		Vector2D globalLocation=globalComponentTransform.coordinates;
		AffineTransform old = graphics.getTransform();
		graphics.translate(globalLocation.x, globalLocation.y);
		graphics.rotate(globalComponentTransform.rotation);
		graphics.setColor(component.color);
		int width=component.width;
		int height=component.height;
		switch(component.shape) {
		case Circle:
			if(component.filled) graphics.fillOval(0, 0, width, height);
			else graphics.drawOval(0, 0, width, height);
			break;
		case Rect:
			if(component.filled) graphics.fillRect(0,  0, width, height);
			else graphics.drawRect(0,  0, width, height);
			break;
		case Arrow:
			double k=(double)3/5;
			double m=(double)1/2;
			
			int rect_w=(int)(k*width);
			int rect_h=(int)(m*height);
			
			int h1=(int)(height*(1-m)/2);
			
			Polygon triangle=new Polygon(new int[]{rect_w,rect_w, width}, new int[]{0,height,height/2}, 3);
			if(component.filled) {
				graphics.fillRect(0, h1, rect_w, rect_h);
				graphics.fillPolygon(triangle);
			}
			else {
				graphics.drawRect(0, h1, rect_w, rect_h);
				graphics.drawPolygon(triangle);
			}
			break;
		}
		graphics.setTransform(old);
	}
	public void Visit(ScoreBar scoreBar) {
		graphics.setFont(font);
		int x=(LARGURA_TELA - scoreBar.width)/2;
		int y=ALTURA_TELA-scoreBar.height-60;
		double percentageOfPlayer=(double)scoreBar.hp/scoreBar.hpVictory;
		double percentageOfOpponent=(double)(1-percentageOfPlayer); 
		int xPlayer=x+(int)(percentageOfOpponent*scoreBar.width);
		graphics.setColor(scoreBar.opponentColor);
		graphics.fillRect(x, y, (int)(percentageOfOpponent*scoreBar.width), scoreBar.height);
		
		
		graphics.setColor(scoreBar.playerColor);
		graphics.fillRect(xPlayer, y, (int)(percentageOfPlayer*scoreBar.width), scoreBar.height);
		graphics.setColor(Color.WHITE);
		graphics.drawString("score: "+scoreBar.score, LARGURA_TELA/2-160, ALTURA_TELA-10);
		graphics.drawString("hit streak: "+scoreBar.hitStreak,  LARGURA_TELA/2+40, ALTURA_TELA-10);
		graphics.drawImage(icons.get(scoreBar.enemyName), x+(int)(percentageOfOpponent*scoreBar.width)-100, y-60, null);
		graphics.drawImage(icons.get("BF"), xPlayer, y-60, null);
		if(scoreBar.hp<=scoreBar.hpDefeat) {
			graphics.setColor(Color.RED);
			graphics.drawString("GAME OVER", LARGURA_TELA/2-70, 75);
		}
	}
	public void Visit(RenderableSprites sprite) {
		if(!sprite.visible) return;
		Transform globalComponentTransform=sprite.GlobalTransform();
		Vector2D globalLocation=globalComponentTransform.coordinates;
		AffineTransform old = graphics.getTransform();
		graphics.translate(globalLocation.x, globalLocation.y);
		graphics.rotate(globalComponentTransform.rotation);
		if(sprite.height!=0 && sprite.width!=0) {
			graphics.drawImage(sprite.CurrentSprite(), 0, 0, sprite.width, sprite.height,null);
		}
		else {
			graphics.drawImage(sprite.CurrentSprite(), 0, 0,null);			
		}
		graphics.setTransform(old);
	}
	private ByteArrayInputStream loadStream(InputStream inputstream)
            throws IOException
    {
          ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
          byte data[] = new byte[1024];
          for(int i = inputstream.read(data); i != -1; i = inputstream.read(data))
                bytearrayoutputstream.write(data, 0, i);

          inputstream.close();
          bytearrayoutputstream.close();
          data = bytearrayoutputstream.toByteArray();
          return new ByteArrayInputStream(data);
    }
}
