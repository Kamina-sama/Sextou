package component;

import event.Event;
import event.EventType;
import gameobject.GameObject;
import input.Input;

public class Timer extends Component {
	private int originalTime;
	private boolean activated=false;
	public int millisecondsLeft;
	public boolean repeat=false;
	
	public Timer(GameObject mine, int millisecondsLeft, boolean repeat) {
		super(mine);
		this.repeat=repeat;
		this.millisecondsLeft=millisecondsLeft;
		if(repeat) originalTime=millisecondsLeft;
	}
	@Override
	public void Update(double deltaTime) {
		if(activated) return;
		millisecondsLeft-=deltaTime;
		if(millisecondsLeft<=0) {
			activated=true;
			SignalObject(new Event(EventType.Timer,null));
			if(repeat) {
				activated=false;
				millisecondsLeft=originalTime;
			}
		}
	}
	public void Reset() {
		millisecondsLeft=originalTime;
	}
}
