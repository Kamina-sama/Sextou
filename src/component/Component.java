package component;

import coordinate.Transform;
import event.Event;
import gameobject.GameObject;
import input.Input;
import rendering.IScreen;

public class Component {
	public GameObject obj;
	public Transform relToObj;
	public Component(GameObject mine) {
		obj=mine;
		relToObj=new Transform();
	}
	public Component(GameObject mine, Transform t) {
		obj=mine;
		relToObj=t;
	}
	//Public needed. GameObject uses it.
	public void Update(double deltaTime) {}
	public void OnEnterTree() {}
	public void OnExitTree() {}
	
	public void OnAdded() {}
	public void OnRemoved() {}
	
	protected final void SignalObject(Event e) {
		obj.OnComponentEvent(e, this);
	}
	public void OnGameObjectEvent(Event e) {}
	public void Accept(IScreen rendererVisitor) {}
	
	public Transform GlobalTransform() {
		Transform t=new Transform(obj.transform);
		t.coordinates.AddMut(relToObj.coordinates);
		t.rotation+=relToObj.rotation;
		return t;
	}
}
