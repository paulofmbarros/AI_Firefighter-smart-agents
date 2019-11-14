package Agents;

import Messages.FireMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;

public class FirestationAgent  extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		addBehaviour(new ReceiveMessages(this));
	}
	
	class ReceiveMessages extends CyclicBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ReceiveMessages(Agent a) {
			super(a);
		}

		
		public void action() {
			ACLMessage msg = receive();
			if(msg == null) {
				block();
				return;
			}
			
			try {
				Object content = msg.getContentObject();
				switch(msg.getPerformative()) {
				case (ACLMessage.INFORM):
					if(content instanceof FireMessage) {
						FireMessage fm = (FireMessage) content;
						System.out.println("Fire msg receivd. X: " + fm.getFireCoordX() + " Y: " + fm.getFireCoordY());
					}
					break;
				default:
					break;
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		
	}

}
