package parser;

public class OrphanArrow {
	public double position;
	public double speed;
	public int column;
	public double duration;
	public OrphanArrow(double position, long column, double duration, double speed) {
		this.position=position;
		this.column=(int)column;
		this.duration=duration;
		this.speed=speed;
	}
}
