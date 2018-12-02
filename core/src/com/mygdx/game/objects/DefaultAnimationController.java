package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.PlayerTweener;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.mygdx.game.helper.Helper;

public abstract class DefaultAnimationController {
	
	protected StateMachine<State, Trigger> state;
	public float timeInState = 0;
	GameObject personagem;
	protected PlayerTweener tweener;
	
	public interface State<T extends DefaultAnimationController>{
		abstract Player anim(T controller);
		abstract String name();
	}
	
	public interface Trigger<T extends GameObject>{
		abstract boolean accept(T gameObject, Animation stateAnimation);
		abstract String name();
	}
	
	protected StateMachineConfig<State, Trigger> config;
	
	public DefaultAnimationController(GameObject personagem) {
		this.personagem = personagem;
		//Configura as stateMachines
		config = new StateMachineConfig<State, Trigger>();
	}
	
	float transitionAlpha = 0;
	protected float timeToTransition = 0.5f;
	State initialState;
	
	public void readXML(String fileName, EnumReader<State> stateReader, EnumReader<Trigger> transitionReader) {
		
		XmlReader reader = new XmlReader();
		Element root = reader.parse(Gdx.files.internal(fileName));
		root = root.getChildByName("statemachine");
		
		Action1<Transition<State, Trigger>> resetAnimation = new Action1<Transition<State, Trigger>>() {
			public void doIt(Transition<State, Trigger> transition) {
				transition.getDestination().anim(DefaultAnimationController.this).setTime(0);
				transition.getSource().anim(DefaultAnimationController.this).setTime(0);
				tweener.setTime(0);
				
				
				tweener.setPlayers(
						transition.getSource().anim(DefaultAnimationController.this),
						transition.getDestination().anim(DefaultAnimationController.this));
				tweener.setWeight(0);
				transitionAlpha = 1;
			}
		};

		initialState = stateReader.read(root.getChildByName("initialstate").getText());
		
		for(Element ele : root.getChildrenByName("state")) {
			String name = ele.getChildByName("name").getText();
			
			for(Element trans : ele.getChildByName("transitions").getChildrenByName("transition")) {
				String transitionName = trans.getChildByName("name").getText();
				String destination = trans.getChildByName("destination").getText();
				
				config
				.configure(stateReader.read(name))
				.permit(transitionReader.read(transitionName), stateReader.read(destination))
				.onEntry(resetAnimation);
			}
		}
		
		state = new StateMachine<DefaultAnimationController.State, DefaultAnimationController.Trigger>(initialState, config);
	}
	
	public Player getCurrentPlayer() {
		return tweener;
	}
	
	public void finishConstructor() {
		tweener = new PlayerTweener(initialState.anim(this).getEntity());
	}
	
	public abstract class EnumReader<T> {
		public abstract T read(String text);
	}
	
	public void update(float delta) {
		timeInState += delta * 1000f;
		
		if(timeToTransition != 0) {
			transitionAlpha -= delta / timeToTransition;
			transitionAlpha = Helper.clamp(transitionAlpha, 0, 1);			
		}
		else {
			transitionAlpha = 0;
		}
		
		for(Trigger t : state.getPermittedTriggers()) {
			if(t.accept(personagem, state.getState().anim(this).getAnimation())) {
				timeInState = 0;
				state.fire(t);
				break;
			}
		}
		
		tweener.setWeight(1 - transitionAlpha);
	}
	
	public State getCurrentState() {
		return state.getState();
	}

}
