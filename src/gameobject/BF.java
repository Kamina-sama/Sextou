package gameobject;

import component.RenderableSprites;
import component.Timer;
import event.Event;
import event.EventType;
import input.Input;

public class BF extends GameObject {
	RenderableSprites bfSprites;
	boolean isDed=false;
	
	public BF() {
		bfSprites=new RenderableSprites(this); 
		bfSprites.AddAnimation("leftHit", new String[] {"BF/LeftHit1.gif","BF/LeftHit2.gif"});
		bfSprites.AddAnimation("downHit", new String[] {"BF/DownHit1.gif","BF/DownHit2.gif"});
		bfSprites.AddAnimation("upHit", new String[] {"BF/UpHit1.gif","BF/UpHit2.gif"});
		bfSprites.AddAnimation("rightHit", new String[] {"BF/RightHit1.gif","BF/RightHit2.gif"});
		bfSprites.AddAnimation("leftMiss", new String[] {"BF/LeftMiss1.gif","BF/LeftMiss2.gif"});
		bfSprites.AddAnimation("downMiss", new String[] {"BF/DownMiss1.gif","BF/DownMiss2.gif"});
		bfSprites.AddAnimation("upMiss", new String[] {"BF/UpMiss1.gif","BF/UpMiss2.gif"});
		bfSprites.AddAnimation("rightMiss", new String[] {"BF/RightMiss1.gif","BF/RightMiss2.gif"});
		bfSprites.AddAnimation("idle", new String[] {"BF/Idle.gif"});
		bfSprites.AddAnimation("gameOver", new String[] {"BF/GameOver.gif"});
		bfSprites.SetAnimation("idle");
		
		transform.coordinates.x=600;
		transform.coordinates.y=400;
	}
	@Override
	public void OnEnterTree() {
		AddComponent(bfSprites);
		super.OnEnterTree();
	}
	@Override
	protected void SelfUpdate(long deltaTime) {
		Input input=Input.GetInput();
		if(!input.left && !input.down && !input.up && !input.right && !isDed) {
			bfSprites.SetAnimation("idle");
		}
	}
	@Override
	protected void OnGameObjectEvent(Event e, GameObject sender) {
		switch (e.Type()){
		case PlayerHitNote:
			switch((Direction)e.Args()[1]) {
			case LEFT:
				bfSprites.SetAnimation("leftHit");
				break;
			case DOWN:
				bfSprites.SetAnimation("downHit");
				break;
			case UP:
				bfSprites.SetAnimation("upHit");
				break;
			case RIGHT:
				bfSprites.SetAnimation("rightHit");
				break;
			}
			bfSprites.Play(true);
		break;
		case PlayerHittingDuration:
			
		break;
		case MissInput: 
			switch((Direction)e.Args()[1]) {
			case LEFT:
				bfSprites.SetAnimation("leftMiss");
				break;
			case DOWN:
				bfSprites.SetAnimation("downMiss");
				break;
			case UP:
				bfSprites.SetAnimation("upMiss");
				break;
			case RIGHT:
				bfSprites.SetAnimation("rightMiss");
				break;
			}
			bfSprites.Play(true);
		break;
		case GameOver:
			bfSprites.SetAnimation("gameOver");
			isDed=true;
		break;
		default:
		break;
		}
	}
}
