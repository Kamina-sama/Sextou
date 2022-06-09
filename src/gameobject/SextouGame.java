package gameobject;

import java.util.Vector;

import event.Event;
import input.Input;

public class SextouGame extends GameObject {
	Vector<SextouLevel> levels;
	int currentLevelIndex=0;
	SextouLevel current;
	boolean gameOver=false;
	
	private String[] filesInst=new String[] {"Dadbattle_Inst.wav", "Blammed_Inst.wav", "Satin-Panties_Inst.wav"};
	private String[] filesVoices=new String[] {"Dadbattle_Voices.wav", "Blammed_Voices.wav", "Satin-Panties_Voices.wav"};
	private String[] charts=new String[] {"charts/dadbattle.json", "charts/blammed.json", "charts/satin-panties.json"};
	private String[] enemies=new String[] {"DaddyDearest", "Pico", "MommyMearest"};
	
	public SextouGame(boolean botPlay) {
		levels=new Vector<SextouLevel>();
		for(int i=0;i<filesInst.length;++i) {
			levels.add(new SextouLevel(filesInst, filesVoices, charts, enemies, i, botPlay));
		}
	}
	@Override
	public void OnEnterTree() {
		current=levels.elementAt(currentLevelIndex);
		Birth(current);
		super.OnEnterTree();
	}
	@Override
	protected void OnGameObjectEvent(Event e, GameObject sender) {
		switch(e.Type()) {
		case GameOver:
			boolean won=(boolean)e.Args()[0];
			if(won) {
				current.Die();
				if(currentLevelIndex<levels.size()-1) {
					current=levels.elementAt(++currentLevelIndex);
					Birth(current);
				}
				else {
					
				}
			}
			else gameOver=true;
			break;
		default:
			break;
		}
	}
}
