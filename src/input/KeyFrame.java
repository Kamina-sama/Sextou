package input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.Timer;

public class KeyFrame extends JFrame implements KeyListener
{
    public boolean t = true;
    //private final HashSet<Integer> pressed = new HashSet<Integer>();
    private Input i;
    public KeyFrame(String name, Input i) {
        super(name);
        this.i=i;
     }

    public void keyTyped(KeyEvent e) {
        return;
    }

    public void keyPressed(KeyEvent e) {
    	switch(e.getKeyCode()) {
    	case KeyEvent.VK_W:
    	case KeyEvent.VK_UP:
    		i.up=true;
    		break;
    	case KeyEvent.VK_A:
    	case KeyEvent.VK_LEFT:
    		i.left=true;
    		break;
    	case KeyEvent.VK_S:
    	case KeyEvent.VK_DOWN:
    		i.down=true;
    		break;
    	case KeyEvent.VK_D:
    	case KeyEvent.VK_RIGHT:
    		i.right=true;
    		break;
    	}
    }

    public void keyReleased(KeyEvent e) {
    	switch(e.getKeyCode()) {
    	case KeyEvent.VK_W:
    	case KeyEvent.VK_UP:
    		i.up=false;
    		break;
    	case KeyEvent.VK_A:
    	case KeyEvent.VK_LEFT:
    		i.left=false;
    		break;
    	case KeyEvent.VK_S:
    	case KeyEvent.VK_DOWN:
    		i.down=false;
    		break;
    	case KeyEvent.VK_D:
    	case KeyEvent.VK_RIGHT:
    		i.right=false;
    		break;
    	}
    }
}
