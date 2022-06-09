package gameobject;

import event.Event;
import event.EventType;
import input.Input;

public class InputArrows extends GameObject {
	static int inputArrowsY=20;
	
	static int extraTopY=20;
	static int extraBottomY=30;
	
	static int bottomY=inputArrowsY+InputArrow.size+extraBottomY;
	static int topY=inputArrowsY-extraTopY;
	
	static int player0XPadding=20;
	static int player1XPadding=20;
	
	int player;
	InputArrow[] inputArrows;
	
	public InputArrows(int player) {
		this.player=player;
		inputArrows=new InputArrow[] {
				new InputArrow(player,Direction.LEFT), 
				new InputArrow(player,Direction.DOWN), 
				new InputArrow(player,Direction.UP), 
				new InputArrow(player,Direction.RIGHT)
		};
	}
	@Override
	public void OnEnterTree() {
		for(InputArrow iA: inputArrows) Birth(iA);
		super.OnEnterTree();
	}
	@Override
	protected void OnGameObjectEvent(Event e, GameObject sender) {
		if(e.Type()==EventType.Gottable) {
			Direction column=(Direction)e.Args()[1];
			inputArrows[column.ordinal()].OnGameObjectEvent(e, sender);
		}
		else switch(e.Type()) {
		case MissInput:
		case PlayerHitNote:
		case PlayerHittingDuration:
			SignalMother(e);
			break;
		default:
			break;
		}
	}
}
