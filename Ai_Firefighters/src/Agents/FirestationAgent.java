package Agents;

import Messages.FireMessage;
import Messages.OrderMessage;
import Messages.StatusMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import java.util.HashMap;

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
						System.out.println("Fire msg receivd. Contacting fire truck...");
						ACLMessage requestVehicleMsg = new ACLMessage(ACLMessage.REQUEST);
						requestVehicleMsg.setContentObject(fm);
						requestVehicleMsg.addReceiver(new AID("Firetruck1", AID.ISLOCALNAME));
						send(requestVehicleMsg);
					}
					else if(content instanceof StatusMessage) {
						StatusMessage sm = (StatusMessage) content;
						System.out.println("Reply received from vehicle. Vehicle coords: " + sm.getCoordX() + ", " + sm.getCoordY());
						if(sm.isAvailable()) {
							//verificar combustivel e assim
							//neste caso assumo que pode ir apagar o fogo
							ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
							OrderMessage om = new OrderMessage(sm.getFireX(), sm.getFireY());
							reply.setContentObject(om);
							reply.addReceiver(msg.getSender());
							send(reply);
						}
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
