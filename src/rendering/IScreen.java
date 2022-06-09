package rendering;
import java.awt.Graphics2D;

import component.RenderableGeometric;
import component.RenderableSprites;
import component.ScoreBar;
import gameobject.GameObject;
public interface IScreen {
	public void RenderGame(GameObject root);
	//public void Visit(RenderableSprite component);
	public void Visit(RenderableGeometric component);
	public void Visit(RenderableSprites component);
	public void Visit(ScoreBar component);
	public Graphics2D GetGraphics();
}
