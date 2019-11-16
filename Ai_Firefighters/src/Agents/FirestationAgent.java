package Agents;

import Messages.FireMessage;
import Messages.OrderMessage;
import Messages.StatusMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;

import java.util.ArrayList;
import java.util.HashMap;


public class FirestationAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, ArrayList<ACLMessage>> firesToProcess;
	private int numberOfVehicles;
	
	protected void setup() {
		this.firesToProcess = new HashMap<String, ArrayList<ACLMessage>>();
		this.numberOfVehicles = 0;
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new CountNumberOfVehicles(this));
		sb.addSubBehaviour(new ReceiveMessages(this));
		addBehaviour(sb);
	}
	
	
	class CountNumberOfVehicles extends OneShotBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public CountNumberOfVehicles(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ServiceDescription sd = new ServiceDescription();
			sd.setType("VEHICLE_AGENT");
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.addServices(sd);
			try {
				numberOfVehicles = DFService.search(myAgent, dfd).length;
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	

	class MessageAllVehicles extends OneShotBehaviour {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private FireMessage fm;
		public MessageAllVehicles(Agent a, FireMessage fm) {
			super(a);
			this.fm = fm;
		}

		@Override
		public void action() {
			ArrayList<ACLMessage> vehicles = new ArrayList<>();
			//se esta key ja existe, e porque foi de uma tentativa anterior e a lista deve ser ignorada
			firesToProcess.put(fm.getFireId(), vehicles);
			
			ServiceDescription sd = new ServiceDescription();
			sd.setType("VEHICLE_AGENT");
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.addServices(sd);
			
			try {
				DFAgentDescription[] dfds = DFService.search(myAgent, dfd);
				if (dfds.length > 0) {
					System.out.println("Localized vehicles");
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					for (DFAgentDescription dfad : dfds) {
						msg.addReceiver(dfad.getName());
					}
					msg.setContentObject(fm);
					send(msg);
				} else {
					System.out.println("Couldn't localize vehicles!");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Failed searching in the DF!");
			}
		}
	}
	
	class SendBestVehicle extends OneShotBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String fireId;
		
		public SendBestVehicle(Agent a, String fireId) {
			super(a);
			this.fireId = fireId;
		}
		@Override
		public void action() {
			System.out.println("For fire id " + fireId + " I can choose from " + firesToProcess.get(fireId).size() + " vehicles");
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
				case (ACLMessage.INFORM):
					if(content instanceof FireMessage) {
						FireMessage fm = (FireMessage) content;
						System.out.println("Fire msg received. Checking all vehicles...");
						addBehaviour(new MessageAllVehicles(myAgent, fm));
						/*
						ACLMessage requestVehicleMsg = new ACLMessage(ACLMessage.REQUEST);
						requestVehicleMsg.setContentObject(fm);
						requestVehicleMsg.addReceiver(new AID("Firetruck1", AID.ISLOCALNAME));
						send(requestVehicleMsg);
						*/
					}
					else if(content instanceof StatusMessage) {
						
						StatusMessage sm = (StatusMessage) content;
						/*System.out.println("Reply received from vehicle. Vehicle coords: " + sm.getCoordX() + ", " + sm.getCoordY());
						if(sm.isAvailable()) {
							//verificar combustivel e assim
							//neste caso assumo que pode ir apagar o fogo
							ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
							OrderMessage om = new OrderMessage(sm.getFireX(), sm.getFireY());
							reply.setContentObject(om);
							reply.addReceiver(msg.getSender());
							send(reply);
						}
						*/
						firesToProcess.get(sm.getFireId()).add(msg);
						if(firesToProcess.get(sm.getFireId()).size() == numberOfVehicles) {
							addBehaviour(new SendBestVehicle(myAgent, sm.getFireId()));
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
