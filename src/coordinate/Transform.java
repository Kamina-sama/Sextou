package coordinate;

public class Transform  {
	public Vector2D coordinates;
	public double rotation=0;
	public double scale=1;
	public boolean absolute=true;
	
	public Transform() {
		 coordinates=new Vector2D();
	}
	public Transform(double x, double y) {
		coordinates=new Vector2D(x,y);
	}
	public Transform(double x, double y, double rotation) {
		coordinates=new Vector2D(x,y);
		this.rotation=rotation;
		scale=1;
	}
	public Transform(Vector2D v, double rotation) {
		coordinates=v;
		this.rotation=rotation;
	}
	public Transform(Transform t) {
		this(t.coordinates, t.rotation);
	}
	//TODO:WORLD TO SCREEN CONVERSION AND VICE VERSA (USING MATRIX MULTIPLICATION)
}
