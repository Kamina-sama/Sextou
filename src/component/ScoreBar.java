package component;

import java.awt.Color;

import event.Event;
import event.EventType;
import gameobject.GameObject;
import input.Input;
import rendering.IScreen;

public class ScoreBar extends Component {
	public int hp=50; //from the player perspective: 0 is im ded, 100 is i killed the guy (never actually happens)	
	public int hpVictory=100;
	public int hpDefeat=0;
	
	public int score=0;
	public int scorePenalty=100;
	
	public int hitStreak=0;

	public int hitHPGain=2;
	public int missHPGain=-5;
	
	
	public int width=450;
	public int height=20;
	
	public Color playerColor=Color.BLUE;
	public Color opponentColor=Color.RED;
	
	public String enemyName;
	public ScoreBar(GameObject mine, String enemyName) {
		super(mine);
		this.enemyName=enemyName;
	}
	@Override
	public void Update(double deltaTime) {
	}
	@Override
	public void OnGameObjectEvent(Event e) {
		switch(e.Type()) {
		case MissInput:
		case MissedNote:
			int player=(int)e.Args()[0];
			if(player==1) {
				hp+=player==1?missHPGain:-missHPGain;
				if(hp<=hpDefeat) SignalObject(new Event(EventType.GameOver, new Object[] {false}));
				score-=scorePenalty;
				score=Math.max(score, 0);
				hitStreak=0;
			}
			break;
		case PlayerHitNote:
			int player2=(int)e.Args()[0];
			if(player2==1) {
				hitStreak+=1;
				score+=1000; //Todo: Better gains based on how close the arrow was when hit
				if(hp+hitHPGain<hpVictory) hp+=hitHPGain;
			}
			break;
		default:
			break;
		}
	}
	@Override
	public final void Accept(IScreen rendererVisitor) {
		rendererVisitor.Visit(this);
	}
}
