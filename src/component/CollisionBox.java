package component;

import java.awt.Color;
import gameobject.GameObject;

public class CollisionBox extends RenderableGeometric {
	//public Transform relativeTo
	public CollisionBox(GameObject mine) {
		super(mine, Shape.Rect);
		color=Color.GREEN;
		filled=false;
	}
	
}
