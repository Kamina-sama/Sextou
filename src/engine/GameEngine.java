package engine;

import gameobject.GameObject;
import gameobject.SextouGame;
import gameobject.SextouLevel;
import input.Input;
import rendering.IScreen;
import rendering.Screen;


public class GameEngine {
	private IScreen screen;
	private GameObject rootObject;
	private GameEngine(IScreen screen, GameObject rootObject) {
		this.screen=screen;
		this.rootObject=rootObject;
		GameLoop();
	}
	private void GameLoop() {
		long deltaTime=0;
		long timeNow=System.currentTimeMillis();
		rootObject.root=rootObject;
		rootObject.OnEnterTree();
		while(!Input.GetInput().shouldQuit) {
			deltaTime=System.currentTimeMillis()-timeNow;
			rootObject.Update(deltaTime);
			timeNow+=deltaTime;
			screen.RenderGame(rootObject);
		}
		rootObject.OnExitTree();
	}
	public static void Test() {

	}
	public static void main(final String[] args) {
		GameObject game=new SextouGame(false);
		GameEngine engine=new GameEngine(new Screen(Input.GetInput()), game);
	}
}