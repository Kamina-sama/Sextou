package event;

public final class Event {
	private EventType type;
	private Object[] args;
	public Event(EventType type, Object[] args) {
		this.type=type;
		this.args=args;
	}
	public EventType Type() {
		return type;
	}
	public Object[] Args() {
		return args;
	}
}
