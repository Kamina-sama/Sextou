package gameobject;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Vector;

import component.Component;
import coordinate.Transform;
import event.Event;
import event.EventType;
import input.Input;

public class GameObject {
	public Transform transform=new Transform();
	protected GameObject mother=null;
	public GameObject root=null;
	private Vector<Component> components=new Vector<Component>();
	private Vector<GameObject> children=new Vector<GameObject>();
	public boolean dead=false;
	
	Vector<GameObject> toRemove=new Vector<GameObject>();
	
	public GameObject GetRoot() {
		return root;
	}
	//--------Customizable "Constructor" and "Destructor"-----------
	public void OnEnterTree() {
		for (Component component : components) component.OnEnterTree();
	}
	public void OnExitTree() {
		for (Component component : components) component.OnExitTree();
	}
	//-----------------------------UPDATE---------------------------
	//Public needed. GameEngine uses it
	public final void Update(long deltaTime) {
		for (Component component : components) component.Update(deltaTime);
		SelfUpdate(deltaTime);
		try {
			for(GameObject child:children) {
				child.Update(deltaTime);
				if(child.dead) toRemove.add(child);
			}
		}
		catch(ConcurrentModificationException e) {
			
		}
		children.removeAll(toRemove);
		toRemove.clear();
	}
	//Customizable part of Update
	protected void SelfUpdate(long deltaTime) {}
	//-----------Component Adding/Removing------------ WHEN ADDING OR REMOVING COMPONENTS TO THE OBJECT, ALWAYS USE THESE
	protected final void AddComponent(Component component) {
		components.add(component);
		component.obj=this;
		component.OnAdded();
	}
	protected final void RemoveComponent(Component component) {
		component.OnRemoved();
		component.obj=null;
		components.remove(component);
	}
	public Vector<Component> Components() { // FOR SCREEN ONLY
		return components;
	}
	//-----------Game Object Children Adding/Removing------------ WHEN ADDING OR REMOVING GAMEOBJECTS TO A SCENE, ALWAYS USE THESE
	protected final void Birth(GameObject obj) {
		if(obj==null) return;
		children.add(obj);
		obj.mother=this;
		obj.root=root;
		obj.OnEnterTree();
		SignalMother(new Event(EventType.Birthed, new Object[] {this, obj}));
	}
	protected final void KillChild(GameObject obj) {
		if(obj!=null) obj.Die();
	}
	protected final void Die() {
		if(!dead && this.root!=this) {
			OnExitTree();
			components.clear();
			for(GameObject child: children) child.Die();
			children.clear();
			SignalMother(new Event(EventType.Died, new Object[] {this}));
			dead=true;
		}
	}
	public Vector<GameObject> Children() {
		return children;
	}
	//-------------------------Event handling-------------------------------
	protected final void SignalEventToChildren(Event e, GameObject sender, boolean recursive) {
		if(dead) return;
		for (GameObject child : children) {
			child.OnGameObjectEvent(e, sender);
			if(recursive) child.SignalEventToChildren(e, sender, recursive);
		}
	}
	protected final void SignalEventToChildren(Event e, boolean recursive) {
		if(dead) return;
		for (GameObject child : children) {
			child.OnGameObjectEvent(e, this);
			if(recursive) child.SignalEventToChildren(e, this, recursive);
		}
	}
	protected final void SignalEventToChildren(Event e) {
		if(dead) return;
		for (GameObject child : children) {
			child.OnGameObjectEvent(e, this);
		}
	}
	protected final void SignalMother(Event e) {
		if(!dead && this.root!=this) mother.OnGameObjectEvent(e, this);
	}
	protected final void SignalRoot(Event e) {
		if(!dead) root.OnGameObjectEvent(e, this);
	}
	protected final void SignalEventToComponents(Event e) {
		if(dead) return;
		for (Component component : components) component.OnGameObjectEvent(e);
	}
	//-----------------Customizable Event Handlers--------------------------------------- (Mediator pattern should be used here)
	protected void OnGameObjectEvent(Event e, GameObject sender) {}
	//Public needed. Component uses it.
	public void OnComponentEvent(Event e, Component sender) {}
}
