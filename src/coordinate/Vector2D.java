package coordinate;

public class Vector2D {
	public double x, y;
	public Vector2D() {
		x=0;
		y=0;
	}
	public Vector2D(double x, double y) {
		this.x=x;
		this.y=y;
	}
	public Vector2D(Vector2D other) {
		this.x=other.x;
		this.y=other.y;
	}
	public Vector2D ScalarMultiplyMut(double scalar) {
		x*=scalar;
		y*=scalar;
		return this;
	}
	public Vector2D ScalarMultiply(double scalar) {
		Vector2D copy=new Vector2D(this);
		copy.x*=scalar;
		copy.y*=scalar;
		return copy;
	}
	public Vector2D Versor2D() {
		Vector2D copy=new Vector2D(this);
		return copy.ScalarMultiplyMut(1/copy.Magnitude());
	}
	public double Magnitude() {
		return Math.sqrt(x*x+y*y);
	}
	public double MagnitudeSquared() {
		return x*x+y*y;
	}
	public static Vector2D Add(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x+v2.x,v1.y+v2.y);
	}
	public void AddMut(Vector2D other) {
		this.x+=other.x;
		this.y+=other.y;
	}
	public static Vector2D Sub(Vector2D v1, Vector2D v2) {
		return new Vector2D(v1.x-v2.x,v1.y-v2.y);
	}
	public static double DotProduct(Vector2D v1, Vector2D v2) {
		return v1.x*v2.x+v1.y*v2.y;
	}
	//TODO: MATRIX OPERATIONS
}
