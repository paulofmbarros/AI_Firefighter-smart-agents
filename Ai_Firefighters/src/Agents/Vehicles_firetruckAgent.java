package Agents;

import java.io.IOException;

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
import jade.lang.acl.UnreadableException;

public class Vehicles_firetruckAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isAvailable = true;
	private int coordX = 50;
	private int coordY = 50;
	private int waterTank = Configurations.FIRE_TRUCK_MAX_WATER_TANK_CAPACITY;
	private int fuelTank = Configurations.FIRE_TRUCK_MAX_FUEL_TANK_CAPACITY;
	private int speed = 100; // delete for real traveling speed (slow and boring!) Configurations.BASE_VEHICLE_SPEED*Configurations.FIRE_TRUCK_SPEED_MULTIPLIER;


	protected void setup() {
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this));
		sb.addSubBehaviour(new ReceiveMessages(this));
		addBehaviour(sb);
		System.out.println("Vehicle agent started");
	}
	
	public int getSpeed() {
		return speed;
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
	
	private void travel(int x, int y) {
		System.out.println("Traveling to " + x + "x, " + y + "y");
		while(coordX != x || coordY != y) {
			System.out.println(coordX + "x, " + coordY + "y" + "   --   " + fuelTank + " liters of fuel");
			if(coordX < x) {
				coordX++;
			}
			else if(coordX > x) {
				coordX--;
			}
			if(coordY < y) {
				coordY++;
			}
			else if(coordY > y) {
				coordY--;
			}
			fuelTank--;
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Arrived at " + x + "x, " + y + "y");
	}
	
	
	public int simulateDistance(int x1, int y1, int x2, int y2) {
		int moves = 0;
		while(x1 != x2 && x2 != y2) {
			if (x1 < x2) {
				x1++;
			} else if (x1 > x2) {
				x1--;
			}
			if (y1 < y2) {
				y1++;
			} else if (y1 > y2) {
				y1--;
			}
			moves++;
		}
		return moves;
	}
	
	class TravelToFire extends OneShotBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ACLMessage request;
			
		public  TravelToFire(Agent a, ACLMessage request) {
			super(a);
			this.request = request;
		}
		@Override
		public void action() {
			OrderMessage om = null;
			try {
				om = ((OrderMessage) request.getContentObject());
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			if(waterTank <= 0) {
				if(fuelTank <= simulateDistance(coordX, coordY, 30, 30) + simulateDistance(coordX, coordY, om.getFireCoordX(), om.getFireCoordY())) {
					travel(50, 50);
					fuelTank = Configurations.FIRE_TRUCK_MAX_FUEL_TANK_CAPACITY;
					travel(30, 30);
					waterTank = Configurations.FIRE_TRUCK_MAX_WATER_TANK_CAPACITY;
					travel(om.getFireCoordX(), om.getFireCoordY());
					waterTank--;
				}
				else {
					travel(30,30);
					waterTank = Configurations.FIRE_TRUCK_MAX_WATER_TANK_CAPACITY;
					travel(om.getFireCoordX(), om.getFireCoordY());
					waterTank--;
				}
			}
			else if(fuelTank <= simulateDistance(coordX, coordY, om.getFireCoordX(), om.getFireCoordY())) {
				travel(50, 50);
				fuelTank = Configurations.FIRE_TRUCK_MAX_FUEL_TANK_CAPACITY;
				travel(om.getFireCoordX(), om.getFireCoordY());
				waterTank--;
			}
			else {
				travel(om.getFireCoordX(), om.getFireCoordY());
				waterTank--;
			}
			System.out.println("Traveling done. Final stats: " + waterTank + " liters of water, " + fuelTank + " liters of fuel");
		}
	}
	
	
	
	class CheckCanTravel extends OneShotBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ACLMessage request;
		
		public CheckCanTravel(Agent a, ACLMessage request) {
			super(a);
			this.request = request;
		}

		@Override
		public void action() {
			String fireId;
			
			if(isAvailable) {
				//can travel
				isAvailable = false;
				ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				msg.addReceiver(request.getSender());
				send(msg);
				
				//add behaviour travel to fire and put it out
				addBehaviour(new TravelToFire(myAgent, request));
			}
			else {
				try {
					fireId = ((OrderMessage) request.getContentObject()).getFireId();
					ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
					StatusMessage sm = new StatusMessage(coordX, coordY, fireId, myAgent.getAID(), isAvailable, "FIRETRUCK", fuelTank, waterTank);
					msg.setContentObject(sm);
					msg.addReceiver(request.getSender());
					send(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
						StatusMessage sm = new StatusMessage(coordX, coordY, fm.getFireId(), myAgent.getAID(), isAvailable, "FIRETRUCK", fuelTank, waterTank);
						ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
						reply.setContentObject(sm);
						reply.addReceiver(msg.getSender());
						send(reply);
					}
					break;
				case (ACLMessage.PROPOSE):
					if(content instanceof OrderMessage) {
						addBehaviour(new CheckCanTravel(myAgent, msg));
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
