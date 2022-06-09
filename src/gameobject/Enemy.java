package gameobject;

import component.Component;
import component.RenderableSprites;
import component.Timer;
import event.Event;
import event.EventType;
import input.Input;

public class Enemy extends GameObject {
	RenderableSprites sprites;
	Timer hitNoteTimeToIdle;
	String enemyName;
	public Enemy(String enemy) {
		enemyName=enemy;
		sprites=new RenderableSprites(this); 
		LoadEnemy(enemy);
		hitNoteTimeToIdle=new Timer(this, 350, true);
	}
	@Override
	public void OnEnterTree() {
		AddComponent(hitNoteTimeToIdle);
		AddComponent(sprites);
		sprites.SetAnimation("idle");
		sprites.Play(true);
		switch(enemyName) {
		case "Pico":
			if(sprites.GetAnimation()!="idle") {
				sprites.width=250;
				sprites.height=200;
				
				transform.coordinates.x=250;
				transform.coordinates.y=375;				
			}
			else if(sprites.GetAnimation()=="idle") {
				sprites.width=175;
				sprites.height=200;
				transform.coordinates.x=275;
				transform.coordinates.y=375;
			}
			break;
		case "DaddyDearest":
			sprites.width=300;
			sprites.height=500;
			transform.coordinates.x=200;
			transform.coordinates.y=70;
			break;
		case "MommyMearest":
			sprites.width=400;
			sprites.height=500;
			
			transform.coordinates.x=200;
			transform.coordinates.y=70;
			break;
		}
	}
	@Override
	protected void SelfUpdate(long deltaTime) {
		if(enemyName=="Pico") {
			if(sprites.GetAnimation()!="idle") {
				sprites.width=250;
				sprites.height=200;
				
				transform.coordinates.x=250;
				transform.coordinates.y=375;				
			}
			else if(sprites.GetAnimation()=="idle") {
				sprites.width=175;
				sprites.height=200;
				transform.coordinates.x=275;
				transform.coordinates.y=375;
			}
		}
		if(enemyName=="MommyMearest") {
			if(sprites.GetAnimation()!="idle") {
				sprites.width=400;
				sprites.height=500;
				
				transform.coordinates.x=200;
				transform.coordinates.y=70;			
			}
			else if(sprites.GetAnimation()=="idle") {
				sprites.width=225;
				sprites.height=500;
				transform.coordinates.x=275;
				transform.coordinates.y=70;	
			}
		}
	}
	@Override
	protected void OnGameObjectEvent(Event e, GameObject sender) {
		switch (e.Type()){
		case PlayerHitNote:
			switch((Direction)e.Args()[1]) {
			case LEFT:
				sprites.SetAnimation("leftHit");
				break;
			case DOWN:
				sprites.SetAnimation("downHit");
				break;
			case UP:
				sprites.SetAnimation("upHit");
				break;
			case RIGHT:
				sprites.SetAnimation("rightHit");
				break;
			}
			hitNoteTimeToIdle.Reset();
			sprites.Play(true);
		break;
		case PlayerHittingDuration:
		
		default:
		break;
		}
	}
	@Override
	public void OnComponentEvent(Event e, Component sender) {
		switch(e.Type()) {
		case Timer:
			sprites.SetAnimation("idle");
			break;
		}
	}
	private void LoadEnemy(String enemy) {
		switch(enemy) {
		case "DaddyDearest":
			sprites.AddAnimation("leftHit", new String[] {enemy+"/Left1.gif"});
			sprites.AddAnimation("downHit", new String[] {enemy+"/Down1.gif",enemy+"/Down2.gif"});
			sprites.AddAnimation("upHit", new String[] {enemy+"/Up1.gif",enemy+"/Up2.gif", enemy+"/Up3.gif"});
			sprites.AddAnimation("rightHit", new String[] {enemy+"/Right1.gif",enemy+"/Right2.gif", enemy+"/Right3.gif"});
			sprites.AddAnimation("idle", new String[] {enemy+"/Idle1.gif"});
			break;
		case "Pico":
			sprites.AddAnimation("leftHit", new String[] {enemy+"/Left1.gif",enemy+"/Left2.gif",enemy+"/Left3.gif"});
			sprites.AddAnimation("downHit", new String[] {enemy+"/Down1.gif",enemy+"/Down2.gif"});
			sprites.AddAnimation("upHit", new String[] {enemy+"/Up1.gif",enemy+"/Up2.gif", enemy+"/Up3.gif"});
			sprites.AddAnimation("rightHit", new String[] {enemy+"/Right1.gif",enemy+"/Right2.gif", enemy+"/Right3.gif"});
			sprites.AddAnimation("idle", new String[] {enemy+"/Idle1.gif",enemy+"/Idle2.gif",enemy+"/Idle3.gif",enemy+"/Idle4.gif",enemy+"/Idle5.gif", enemy+"/Idle6.gif", enemy+"/Idle7.gif", enemy+"/Idle8.gif", enemy+"/Idle9.gif", enemy+"/Idle10.gif", enemy+"/Idle11.gif",enemy+"/Idle12.gif"});
			break;
		case "MommyMearest":
			sprites.AddAnimation("leftHit", new String[] {enemy+"/Left1.png",enemy+"/Left2.png",enemy+"/Left3.png",enemy+"/Left4.png"});
			sprites.AddAnimation("downHit", new String[] {enemy+"/Down1.png",enemy+"/Down2.png",enemy+"/Down3.png",enemy+"/Down4.png"});
			sprites.AddAnimation("upHit", new String[] {enemy+"/Up1.png",enemy+"/Up2.png", enemy+"/Up3.png",enemy+"/Up4.png"});
			sprites.AddAnimation("rightHit", new String[] {enemy+"/Right1.png",enemy+"/Right2.png", enemy+"/Right3.png",enemy+"/Right4.png"});
			sprites.AddAnimation("idle", new String[] {enemy+"/Idle1.png",enemy+"/Idle2.png",enemy+"/Idle3.png",enemy+"/Idle4.png",enemy+"/Idle5.png",enemy+"/Idle6.png",enemy+"/Idle7.png",enemy+"/Idle8.png",enemy+"/Idle9.png",enemy+"/Idle10.png",enemy+"/Idle11.png",enemy+"/Idle12.png"});
			break;
		}
	}
}
