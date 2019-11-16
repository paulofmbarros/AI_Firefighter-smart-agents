package Agents;

import java.util.UUID;

import javax.security.auth.login.Configuration;

import Config.Configurations;
import Messages.FireMessage;
import Messages.OrderMessage;
import Messages.StatusMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Vehicles_firetruckAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isAvailable = true;
	private int coordX = 50;
	private int coordY = 50;
	private int fireStationCoordX = 50;
	private int fireStationCoordY = 50;
	private int waterTank = Configurations.FIRE_TRUCK_MAX_WATER_TANK_CAPACITY;
	private int fuelTank = Configurations.FIRE_TRUCK_MAX_FUEL_TANK_CAPACITY;


	protected void setup() {
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this));
		sb.addSubBehaviour(new ReceiveMessages(this));
		addBehaviour(sb);
		System.out.println("Vehicle agent started");
	}
	
	class RegisterInDF extends OneShotBehaviour {


		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RegisterInDF(Agent a) {
			super(a);
		}

		public void action() {

			ServiceDescription sd = new ServiceDescription();
			sd.setType("VEHICLE_AGENT");
			sd.setName(getName());
			sd.setOwnership("AI2019");
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			dfd.addServices(sd);
			try {
				DFAgentDescription[] dfds = DFService.search(myAgent, dfd);
				if (dfds.length > 0) {
					DFService.deregister(myAgent, dfd);
				}
				DFService.register(myAgent, dfd);
			} catch (Exception ex) {
				System.out.println("Vehicle failed registering with DF! Shutting down...");
				ex.printStackTrace();
				doDelete();
			}
		}
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
				case (ACLMessage.REQUEST):
					if(content instanceof FireMessage) {
						FireMessage fm = (FireMessage) content;
						System.out.println("Get status msg received in vehicle. "
								+ "Coords of corresponding fire: X: "
								+ "" + fm.getFireCoordX() + " Y: " + fm.getFireCoordY());
						StatusMessage sm = new StatusMessage(coordX, coordY, fm.getFireCoordX(), fm.getFireCoordY(), fm.getFireId(), isAvailable);
						ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
						reply.setContentObject(sm);
						reply.addReceiver(msg.getSender());
						send(reply);
					}
					break;
				case (ACLMessage.PROPOSE):
					if(content instanceof OrderMessage) {
						OrderMessage om = (OrderMessage) content;
						System.out.println("Vehicle should go to " + om.getFireCoordX() + ", " + om.getFireCoordY());
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
