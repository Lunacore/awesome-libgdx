package com.mygdx.game.test;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.PlayerTweener;
import com.github.oxo42.stateless4j.delegates.Action;
import com.mygdx.game.animation.DefaultAnimationController;

public class PersonagemAnimationController extends DefaultAnimationController{
	
	
	public enum MyState implements State<PersonagemAnimationController>{
		IDLE_RUN{
			public Player anim(PersonagemAnimationController controller) {
				controller.idle_run.setWeight(controller.personagem.endVelocityX / controller.personagem.getSpeed());
				return controller.idle_run;
			}
		},
		JUMP_BEGIN{
			public Player anim(PersonagemAnimationController controller) {
				return controller.jumpBegin;
			}
		},
		JUMP_LOOP{
			public Player anim(PersonagemAnimationController controller) {
				return controller.jumpLoop;
			}
		},
		JUMP_END{
			public Player anim(PersonagemAnimationController controller) {
				return controller.jumpEnd;
			}
		};
	}
	
	public enum MyTrigger implements Trigger<Personagem>{
		REQUEST_JUMP {
			public boolean accept(Personagem personagem, Animation stateAnimation) {
				return personagem.requestJump;
			}
		},
		BEGIN_FINISH{
			public boolean accept(Personagem personagem, Animation stateAnimation) {
				return personagem.getPlayer().getTime() + personagem.getPlayer().speed*2 >= stateAnimation.length;
			}
		},
		CLOSE_FLOOR{
			public boolean accept(Personagem personagem, Animation stateAnimation) {
				return personagem.getDistanceToFloor() < 1;
			}
		},
		END_FINISH{
			public boolean accept(Personagem personagem, Animation stateAnimation) {
				return personagem.getPlayer().getTime() + personagem.getPlayer().speed*2 >= stateAnimation.length;
			}
		},
		ON_AIR{
			public boolean accept(Personagem personagem, Animation stateAnimation) {
				return personagem.getDistanceToFloor() > 1.5;
			}
		},
		STEP_ON_FLOOR{
			public boolean accept(Personagem personagem, Animation stateAnimation) {
				return personagem.isOnFloor();
			}
		};
	}
	
	//Animações
	
	PlayerTweener idle_run;
	Player jumpBegin;
	Player jumpEnd;
	Player jumpLoop;
	Personagem personagem;
	
	public PersonagemAnimationController(final Personagem personagem) {
		super(personagem);
		this.personagem = personagem;
		timeToTransition = 0;
		
		//Carrega a maquina de estados
		readXML("spriter/controller.fsm",
		new DefaultAnimationController.EnumReader<State>(){
			public State<?> read(String text) {
				return MyState.valueOf(text);
			}
		},
		new EnumReader<DefaultAnimationController.Trigger>() {
			public Trigger<?> read(String text) {
				return MyTrigger.valueOf(text);
			}
		});
		
		state.configure(MyState.JUMP_BEGIN).onEntry(new Action() {
			public void doIt() {
				personagem.jump();
			}
		});
		
		//Cria as animações
		Player idle = new Player(personagem.getPlayer().getEntity());
		idle.setAnimation("Idle");
		Player run = new Player(personagem.getPlayer().getEntity());
		run.setAnimation("Run");
		idle_run = new PlayerTweener(idle, run);
				
		jumpBegin = new Player(personagem.getPlayer().getEntity());
		jumpBegin.setAnimation("JumpBegin");
		jumpBegin.setTime(0);
		
		jumpLoop = new Player(personagem.getPlayer().getEntity());
		jumpLoop.setAnimation("JumpLoop");
		
		jumpEnd = new Player(personagem.getPlayer().getEntity());
		jumpEnd.setAnimation("JumpEnd");
		
		finishConstructor();
	}

}
