package Agents;

import java.io.IOException;
import java.security.SecureRandom;

import Messages.FireMessage;
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
	
	protected void setup() {
		this.fireCoordX = new SecureRandom().nextInt(101);
		this.fireCoordY = new SecureRandom().nextInt(101);
		
		addBehaviour( new StartAFire(this));
	}
	
	
	class StartAFire extends OneShotBehaviour {
		
		StartAFire(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			//enviar info ao world a dizer onde foi posto o fogo
			//TODO
			//enviar info ao quartel
			FireMessage fm = new FireMessage(fireCoordX, fireCoordY);
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			try {
				msg.setContentObject(fm);
				msg.addReceiver(new AID("FirestationAgent", AID.ISLOCALNAME));
				send(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			doDelete();
		}
		
	}
}
