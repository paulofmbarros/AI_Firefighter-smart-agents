package Agents;

import java.awt.Point;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;

import Classes.Fire;
import Enums.WorldObjectEnum;
import Messages.FireMessage;
import World.WorldObject;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;


public class FireStarterAgent  extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int fireCoordX, fireCoordY;
	public WorldAgent worldAgent;
	
	protected void setup() {
		this.fireCoordX = new SecureRandom().nextInt(101);
		this.fireCoordY = new SecureRandom().nextInt(101);
		/*
		 * DEBUG
		 if(new SecureRandom().nextInt() >= 0.5) {
			this.fireCoordX = 100;
			this.fireCoordY = 100;
		}
		else {
			this.fireCoordX = 0;
			this.fireCoordY = 0;
		}
		*/
		addBehaviour( new StartAFire(this));
	}
	
	//TODO: TEMOS DE TRANSFORMAR ISTO NUM TICKERBEHAVIOUR PORQUE VAMOS TER MAIS QUE UM FOGO A SER GERADO
	
	class StartAFire extends OneShotBehaviour {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		StartAFire(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			//NOTA DO PAULO: Criei dentro do package classses, uma classe fire, se conseguires passa o teu fireMessage para dentro da classe fire
			//enviar info ao world a dizer onde foi posto o fogo
			//TODO
			//enviar info ao quartel
			FireMessage fm = new FireMessage(fireCoordX, fireCoordY);
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			try {
				
				//-------------Codigo para enviar mensagem ao Firestation ----------
				msg.setContentObject(fm);
				msg.addReceiver(new AID("FirestationAgent", AID.ISLOCALNAME));
				msg.addReceiver(new AID("WorldAgent", AID.ISLOCALNAME));
				
				
				
				
				send(msg);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			doDelete();
		}
		
	}
}
