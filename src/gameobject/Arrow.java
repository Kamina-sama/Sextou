package gameobject;

import java.awt.Color;

import component.CollisionBox;
import component.Component;
import component.RenderableGeometric;
import component.Shape;
import event.Event;
import event.EventType;
import input.Input;
import rendering.Screen;

public class Arrow extends GameObject {
	
	static int size=80;
	
	RenderableGeometric sprite=new RenderableGeometric(this, Shape.Arrow, Color.BLUE, size, size);
	CollisionBox box=new CollisionBox(this);
	
	double speed;
	double start;
	int player;
	double duration;
	Direction direction;
	boolean isDuration=false;
	
	boolean gottable=false;
	boolean gotted=false;
	boolean failed=false;
	DurationLength durationLength;
	
	public Arrow(double speed, double start, int player, double duration, Direction direction) {
		super();
		this.speed=speed;
		this.start=start;
		this.player=player;
		this.duration=duration;
		this.direction=direction;
		
		box.width=size;
		box.height=size;
		
		transform.coordinates.y=this.speed*start+InputArrows.inputArrowsY;
		transform.coordinates.x=player<=0?InputArrows.player0XPadding:Screen.LARGURA_TELA-4*size-InputArrows.player1XPadding;
		
		switch(direction) {
		case LEFT:
			sprite=new RenderableGeometric(this, Shape.Arrow, Color.PINK, size, size);
			transform.rotation=Math.PI;
			transform.coordinates.x+=size;
			transform.coordinates.y+=size;
			break;
		case DOWN:
			sprite=new RenderableGeometric(this, Shape.Arrow, Color.CYAN, size, size);
			transform.rotation=Math.PI/2;
			transform.coordinates.x+=2*size;
			break;
		case UP:
			sprite=new RenderableGeometric(this, Shape.Arrow, Color.GREEN, size, size);
			transform.rotation=-Math.PI/2;
			transform.coordinates.y+=size;
			transform.coordinates.x+=2*size;
			break;
		case RIGHT:
			sprite=new RenderableGeometric(this, Shape.Arrow, Color.RED, size, size);
			transform.coordinates.x+=3*size;
			break;
		}
	}
	
	public Arrow(double speed, double start, int player, double duration, Direction direction, boolean durationNote) {
		this(speed, start+duration, player, 0, direction);
		sprite.filled=false;
		isDuration=true;
	}
	@Override
	public void OnEnterTree() {
		if(duration>0) {		
			Arrow dur=new Arrow(speed, start, player, duration, direction, true);
			durationLength=new DurationLength(speed, start, player, duration, direction, dur);
			SignalMother(new Event(EventType.DurationNote, new Object[] { dur, durationLength}));
		}
		AddComponent(sprite);
		//AddComponent(box);
		super.OnEnterTree();
	}
	@Override
	protected void SelfUpdate(long deltaTime) {
		if(mother instanceof SextouLevel && ((SextouLevel) mother).paused) return;
		if(transform.coordinates.y>-size) transform.coordinates.y-=speed*deltaTime;
		if(isDuration) {
			if((direction==Direction.DOWN || direction==Direction.RIGHT) && transform.coordinates.y<=InputArrows.inputArrowsY) sprite.visible=false;
			if((direction==Direction.LEFT || direction==Direction.UP) && transform.coordinates.y-size<=InputArrows.inputArrowsY) sprite.visible=false;
		}
		UpdateGottable();
		CheckFailure();
		if(player==0 || (mother instanceof SextouLevel && ((SextouLevel)mother).botPlay)) PlayBot();
	}
	private boolean Capturable() {
		double upper_point=0;
		double lower_point=0;
		if(direction==Direction.DOWN || direction==Direction.RIGHT) {
			upper_point=transform.coordinates.y;
			lower_point=transform.coordinates.y+size;}
		else {
			upper_point=transform.coordinates.y-size;
			lower_point=transform.coordinates.y;
		}
		if((InputArrows.topY<=upper_point && upper_point<=InputArrows.bottomY || InputArrows.topY<=lower_point && lower_point<=InputArrows.bottomY))
		return true;
		else return false;
	}
	private void UpdateGottable() {
		if(Capturable()) {
			if(!gottable) {
				gottable=true;
				SignalMother(new Event(EventType.Gottable, new Object[] {this.player, this.direction, this.duration, this.isDuration, true}));
				box.color=Color.BLUE;
			}
		}
		else if(gottable) {
			gottable=false;
			SignalMother(new Event(EventType.Gottable, new Object[] {this.player, this.direction, this.duration, this.isDuration, false}));
			box.color=Color.RED;
		}
	}
	private void PlayBot() {
		if((direction==Direction.DOWN || direction==Direction.RIGHT) && transform.coordinates.y<=InputArrows.inputArrowsY) Git();
		if((direction==Direction.LEFT || direction==Direction.UP) && transform.coordinates.y-size<=InputArrows.inputArrowsY) Git();
	}
	private void Git() {
		gotted=true;
		SignalMother(new Event(EventType.PlayerHitNote, new Object[] {this.player, direction}));
		Die();
	}
	private void CheckFailure() {
		if(transform.coordinates.y<=-size) {
			if(!isDuration) SignalMother(new Event(EventType.MissedNote, new Object[] {this.player}));
			Die();
		}
	}
	@Override
	protected void OnGameObjectEvent(Event e, GameObject sender) {
		if(e.Type()==EventType.PlayerHitNote) {
			sender.OnGameObjectEvent(new Event(EventType.Died, null), this);
			Git();
		}
	}
	public void Delay(int milliseconds) {
		transform.coordinates.y+=milliseconds*speed;
	}
}
