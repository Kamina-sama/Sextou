package component;

import java.awt.Color;
import gameobject.GameObject;
import rendering.IScreen;

public class RenderableGeometric extends Component {
	
	public Shape shape;
	public Color color;
	public boolean visible=true;
	public boolean filled=true;
	public int width;
	public int height;
	public int priority=0;
	
	public RenderableGeometric(GameObject mine) {
		super(mine);
		this.shape=Shape.Rect;
		this.color=Color.RED;
		width=10;
		height=10;
	}
	public RenderableGeometric(GameObject mine, int width, int height) {
		this(mine);
		this.width=width;
		this.height=height;
	}
	public RenderableGeometric(GameObject mine, Shape shape) {
		this(mine);
		this.shape=shape;
		this.color=Color.RED;
	}
	public RenderableGeometric(GameObject mine, Shape shape, int width, int height) {
		this(mine, shape);
		this.width=width;
		this.height=height;
	}
	public RenderableGeometric(GameObject mine, Shape shape, Color color) {
		this(mine, shape);
		this.color=color;
	}
	public RenderableGeometric(GameObject mine, Shape shape, Color color, int width, int height) {
		this(mine, shape, color);
		this.width=width;
		this.height=height;
	}
	@Override
	public final void Accept(IScreen rendererVisitor) {
		rendererVisitor.Visit(this);
	}
}
