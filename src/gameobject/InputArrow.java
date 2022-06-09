package gameobject;

import java.awt.Color;
import java.io.Console;

import component.CollisionBox;
import component.RenderableGeometric;
import component.Shape;
import event.Event;
import event.EventType;
import input.Input;
import rendering.Screen;

public class InputArrow extends GameObject {
	
	static int size=Arrow.size;
	
	
	RenderableGeometric sprite;
	CollisionBox box=new CollisionBox(this);
	int player;
	double duration;
	Direction direction;
	Color color;
	
	int iPC=0, iRC=0;
	
	Arrow toBeGotted=null;
	Arrow previousGotted=null;
	
	DurationLength toBeGetting=null;
	
	public InputArrow(int player, Direction direction) {
		super();
		this.player=player;
		this.direction=direction;
		
		box.width=size;
		box.height=size;
		
		transform.coordinates.y=InputArrows.inputArrowsY;
		transform.coordinates.x=player<=0?InputArrows.player0XPadding:Screen.LARGURA_TELA-4*size-InputArrows.player1XPadding;
		
		switch(direction) {
		case LEFT:
			color=Color.PINK;
			sprite=new RenderableGeometric(this, Shape.Arrow, color, size, size);
			transform.rotation=Math.PI;
			transform.coordinates.x+=size;
			transform.coordinates.y+=size;
			break;
		case DOWN:
			color=Color.CYAN;
			sprite=new RenderableGeometric(this, Shape.Arrow, color, size, size);
			transform.rotation=Math.PI/2;
			transform.coordinates.x+=2*size;
			break;
		case UP:
			color=Color.GREEN;
			sprite=new RenderableGeometric(this, Shape.Arrow, color, size, size);
			transform.rotation=-Math.PI/2;
			transform.coordinates.y+=size;
			transform.coordinates.x+=2*size;
			break;
		case RIGHT:
			color=Color.RED;
			sprite=new RenderableGeometric(this, Shape.Arrow, color, size, size);
			transform.coordinates.x+=3*size;
			break;
		}
		sprite.filled=false;
	}
	@Override
	public void OnEnterTree() {
		AddComponent(sprite);
		//AddComponent(box);
		super.OnEnterTree();
	}
	@Override
	protected void SelfUpdate(long deltaTime) {
		if(mother!=null && mother.mother!=null && mother.mother instanceof SextouLevel && ((SextouLevel)mother.mother).paused) return;
		if(player==1) {
			UpdateInputCounter();
			if(iPC==1 && toBeGotted!=null && toBeGotted.isDuration==false) {
				sprite.filled=true;
				sprite.color=color;
				toBeGotted.OnGameObjectEvent(new Event(EventType.PlayerHitNote, new Object[] {player,direction}), this);
				if(toBeGetting!=null) toBeGetting.beingGot=true;
			}
			else if(iPC==1 && toBeGotted==null && toBeGetting==null) {
				sprite.filled=true;
				sprite.color=Color.GRAY;
				SignalMother(new Event(EventType.MissInput,new Object[] {player, direction}));
			}
			else if(iRC>0) {
				sprite.filled=false;
				sprite.color=color;
				if(toBeGotted!=null && toBeGotted.isDuration) 
					toBeGotted.OnGameObjectEvent(new Event(EventType.PlayerHitNote, new Object[] {player,direction}), this);
			}
			if(iPC>0 && toBeGetting!=null) {
				sprite.filled=true;
				sprite.color=color;
				toBeGetting.OnGameObjectEvent(new Event(EventType.PlayerHittingDuration, new Object[] {true, direction}), this);
			}
			if(iPC==0 && toBeGetting!=null) {
				toBeGetting.OnGameObjectEvent(new Event(EventType.PlayerHittingDuration, new Object[] {false, direction}), this);
			}
		}
	}
	private void UpdateInputCounter() {
		Input input=Input.GetInput();
		switch(direction) {
		case LEFT:
			if(input.left) {
				iPC+=1;
				iRC=0;
			}
			else {
				iPC=0;
				iRC+=1;
			}
			break;
		case DOWN:
			if(input.down) {
				iPC+=1;
				iRC=0;
			}
			else {
				iPC=0;
				iRC+=1;
			}
			break;
		case UP:
			if(input.up) {
				iPC+=1;
				iRC=0;
			}
			else {
				iPC=0;
				iRC+=1;
			}
			break;
		case RIGHT:
			if(input.right) {
				iPC+=1;
				iRC=0;
			}
			else {
				iPC=0;
				iRC+=1;
			}
			break;
		}
	}
	@Override
	protected void OnGameObjectEvent(Event e, GameObject sender) {
		if(e.Type()==EventType.Gottable) {
			if(sender instanceof Arrow) {
				boolean gottable=(boolean)e.Args()[4];
				toBeGotted=gottable?(Arrow)sender:null;
				if(toBeGotted!=null && toBeGotted.durationLength!=null) {
					toBeGetting=toBeGotted.durationLength;
				}
			}
			else if(sender instanceof DurationLength) {
				boolean gottable=(boolean)e.Args()[3];
				if(toBeGetting!=null) toBeGetting.beingGot=false;
				toBeGetting=gottable?(DurationLength)sender:null;
			}
		}
		if(e.Type()==EventType.Died) {
			if(sender instanceof Arrow && sender==toBeGotted) toBeGotted=null;
			else if(sender instanceof DurationLength && sender==toBeGetting) toBeGetting=null;
		}
	}
}
