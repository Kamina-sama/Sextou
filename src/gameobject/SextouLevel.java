package gameobject;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import component.Component;
import component.RenderableSprites;
import component.ScoreBar;
import component.Timer;
import event.Event;
import event.EventType;
import input.Input;
import parser.ChartParser;
import rendering.Screen;
import sound.SoundManager;


public class SextouLevel extends GameObject {
	SoundManager sm;
	InputArrows p0, p1;
	public boolean botPlay;
	ScoreBar score;
	Timer timer;
	boolean paused=false;
	boolean started=false;
	boolean won=false;
	int initialDelayMilliseconds=2000;
	int stage=0;
	BF bf;
	Enemy enemy;
	String enemyName;
	
	RenderableSprites bg;
	
	Vector<Arrow> arrows;
	public SextouLevel(String[] files_inst, String[] files_voices, String[] files_charts, String[] enemyNames, int index, boolean botPlay) {
		this.botPlay=botPlay;
		timer=new Timer(this, 400, true);
		bf=new BF();
		enemyName=enemyNames[index];
		enemy=new Enemy(enemyName);
		score=new ScoreBar(this, enemyName);
		
		bg=new RenderableSprites(this);
		bg.AddAnimation("bg", new String[] {"/bg.jpg"});
		bg.SetAnimation("bg");
		bg.width=Screen.LARGURA_TELA;
		bg.height=Screen.ALTURA_TELA;
		
		sm=new SoundManager();
		try {
			sm.addClip("musics/"+files_inst[index]);
			sm.addClip("musics/"+files_voices[index]);
			sm.addClip("sounds/missnote1.wav");
			sm.addClip("sounds/missnote2.wav");
			sm.addClip("sounds/missnote3.wav");
			sm.addClip("sounds/intro1.wav");
			sm.addClip("sounds/intro2.wav");
			sm.addClip("sounds/intro3.wav");
			sm.addClip("sounds/introGo.wav");
			sm.addClip("sounds/gameOver.wav");
			sm.addClip("sounds/gameOverEnd.wav");
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			//e.printStackTrace();
		}
		ChartParser cp=new ChartParser();
		arrows=cp.Chart(files_charts[index]);
		p0=new InputArrows(0);
		p1=new InputArrows(1);
	}
	@Override
	protected void SelfUpdate(long deltaTime) {
		
		if(sm.clipEnded(0)) {
			if(!won) {
				won=true;
				sm.clip(10).start();
			}
			if(sm.clipEnded(10)) SignalMother(new Event(EventType.GameOver, new Object[] {true}));
		}
	}
	@Override
	public void OnEnterTree() {
		AddComponent(bg);
		Birth(bf);
		Birth(enemy);
		for(Arrow arrow: arrows) {
			arrow.Delay(initialDelayMilliseconds);
			Birth(arrow);
		}
		AddComponent(score);
		AddComponent(timer);
		Birth(p0);
		Birth(p1);
		
		super.OnEnterTree();
	}
	@Override
	protected void OnGameObjectEvent(Event e, GameObject sender) {
		switch(e.Type()) {
		case DurationNote:
			Arrow da=(Arrow)e.Args()[0];
			da.Delay(initialDelayMilliseconds);
			DurationLength dl=(DurationLength)e.Args()[1];
			dl.Delay(initialDelayMilliseconds);
			Birth(da);
			Birth(dl);
			break;
		case Gottable:
			if((int)e.Args()[0]>0) p1.OnGameObjectEvent(e, sender);
			break;
		case PlayerHitNote:
			if((int)e.Args()[0]==1) bf.OnGameObjectEvent(e, sender);
			else enemy.OnGameObjectEvent(e, sender);
			if(sender instanceof Arrow) {
				score.OnGameObjectEvent(e);
				sm.setVolume(1.0, 1);
			}
			break;
		case MissedNote:
			score.OnGameObjectEvent(e);
			sm.setVolume(0, 1);
			break;
		case MissInput:
			int randomNum = ThreadLocalRandom.current().nextInt(2, 5);
			sm.clip(randomNum).setMicrosecondPosition(0);
			sm.clip(randomNum).start();
			score.OnGameObjectEvent(e);
			if((int)e.Args()[0]==1) bf.OnGameObjectEvent(e, sender);
			break;
		default:
			break;
		}
	}
	@Override
	public void OnComponentEvent(Event e, Component sender) {
		switch(e.Type()) {
		case GameOver:
			boolean won=(boolean)e.Args()[0];
			if(!won) {
				SignalMother(e);
				bf.OnGameObjectEvent(e, this);
				GameOverLost();
			}
			break;
		case Timer:
			switch(stage++) {
			case 0:
				sm.clip(5).start();
				break;
			case 1:
				sm.clip(6).start();
				break;
			case 2:
				sm.clip(7).start();
				break;
			case 3:
				sm.clip(8).start();
				break;
			case 4:
				sm.clip(0).start();
				sm.clip(1).start();
				break;
			default:
				break;
			}
			break;
		default:
			break;
			
		}
	}
	private void GameOverLost() {
		paused=true;
		sm.clip(0).stop();
		sm.clip(1).stop();
		sm.clip(9).start();
	}
}
