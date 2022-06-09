package gameobject;

import java.awt.Color;

import component.RenderableGeometric;
import event.Event;
import event.EventType;
import input.Input;
import rendering.Screen;

public class DurationLength extends GameObject {
	RenderableGeometric rect;
	static int rect_width=10;
	double speed;
	double start;
	int player;
	double duration;
	Arrow durationArrow;
	Direction direction;
	
	boolean beingGot=false;
	boolean gottable=false;
	InputArrow playerInput;
	
	public DurationLength(double speed, double start, int player, double duration, Direction direction, Arrow dur) {
		this.speed=speed;
		this.start=start;
		this.player=player;
		this.duration=duration;
		this.direction=direction;
		durationArrow=dur;
		
		rect=new RenderableGeometric(this);
		rect.width=rect_width;
		rect.height=(int)(duration*speed);
		rect.visible=true;
		rect.filled=true;
		
		transform.coordinates.y=this.speed*start+InputArrows.inputArrowsY+(Arrow.size/2);
		transform.coordinates.x=player<=0?InputArrows.player0XPadding:Screen.LARGURA_TELA-4*Arrow.size-InputArrows.player1XPadding;
		transform.coordinates.x+=(Arrow.size-rect_width)/2;
		
		switch(direction) {
		case LEFT:
			rect.color=Color.PINK;
			break;
		case DOWN:
			transform.coordinates.x+=Arrow.size;
			rect.color=Color.CYAN;
			break;
		case UP:
			transform.coordinates.x+=2*Arrow.size;
			rect.color=Color.GREEN;
			break;
		case RIGHT:
			transform.coordinates.x+=3*Arrow.size;
			rect.color=Color.RED;
			break;
		}
		
	}
	@Override
	public void OnEnterTree() {
		AddComponent(rect);
		super.OnEnterTree();
	}
	private boolean Capturable() {
		double upper_point=InputArrows.topY;
		double lower_point=InputArrows.bottomY;
		double myTopY=transform.coordinates.y;
		double myBottomY=myTopY+rect.height;
		if((myTopY<=upper_point && upper_point<=myBottomY || myTopY<=lower_point && lower_point<=myBottomY))
		return true;
		else return false;
	}
	private void UpdateGottable() {
		if(Capturable()) {
			if(!gottable) {
				gottable=true;
				SignalRoot(new Event(EventType.Gottable, new Object[] {this.player, this.direction, this.duration, true}));
			}
		}
		else if(gottable) {
			gottable=false;
			SignalRoot(new Event(EventType.Gottable, new Object[] {this.player, this.direction, this.duration, false}));
		}
	}
	private void Getting(double deltaTime) {
		SignalMother(new Event(EventType.PlayerHittingDuration, new Object[] {this.player}));
		//place the top of the rect in the input
		transform.coordinates.y=InputArrows.inputArrowsY+(InputArrow.size/2);
		if(durationArrow.direction==Direction.DOWN  || durationArrow.direction==Direction.RIGHT)
		rect.height=(int) (durationArrow.transform.coordinates.y+Arrow.size/2-transform.coordinates.y);
		else rect.height=(int) (durationArrow.transform.coordinates.y-Arrow.size/2-transform.coordinates.y);
		if(rect.height<=0) {
			if(playerInput!=null) playerInput.OnGameObjectEvent(new Event(EventType.Died, null), this);
			Die();
		}
	}
	private void PlayBot(double deltaTime) {
		if(transform.coordinates.y<=InputArrows.inputArrowsY+(InputArrow.size/2)) Getting(deltaTime);
	}
	@Override
	protected void SelfUpdate(long deltaTime) {
		if(mother instanceof SextouLevel && ((SextouLevel) mother).paused) return;
		if(durationArrow.dead || transform.coordinates.y+rect.height<0) {
			if(playerInput!=null) playerInput.OnGameObjectEvent(new Event(EventType.Died, null), this);
			Die();
		}
		transform.coordinates.y-=speed*deltaTime;
		if(beingGot) Getting(deltaTime);
		UpdateGottable();
		if(player==0 || (mother instanceof SextouLevel && ((SextouLevel)mother).botPlay)) PlayBot(deltaTime);
	}
	@Override
	protected void OnGameObjectEvent(Event e, GameObject sender) {
		if(e.Type()==EventType.PlayerHittingDuration && sender instanceof InputArrow) {
			playerInput=(InputArrow)sender;
			beingGot=(boolean)e.Args()[0];
		}
		super.OnGameObjectEvent(e, sender);
	}
	public void Delay(int milliseconds) {
		transform.coordinates.y+=milliseconds*speed;
	}
}
