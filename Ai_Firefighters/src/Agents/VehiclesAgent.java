package Agents;

import java.security.SecureRandom;

import Config.Configurations;
import Messages.FireMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

public class VehiclesAgent extends Agent {
	private static final long serialVersionUID = 1L;
	protected boolean isAvailable = true;
	protected int coordX = 50;
	protected int coordY = 50;
	protected int waterTank = 0;
	protected int fuelTank = 0;
	protected int speed = 100; // delete for real traveling speed (slow and boring!) Configurations.BASE_VEHICLE_SPEED*Configurations.FIRE_TRUCK_SPEED_MULTIPLIER;

	protected void setup() {
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
						System.out.println("Fire msg received in vehicle. X: " + fm.getFireCoordX() + " Y: " + fm.getFireCoordY());
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
