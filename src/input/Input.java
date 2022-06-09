package input;

public class Input {
	public boolean shouldQuit, pause;
	public boolean left, right, up, down;
	private static Input input=null;
	public void Input() {}
	public static Input GetInput() {
		if(input==null) input=new Input();
		return input;
	}
}
