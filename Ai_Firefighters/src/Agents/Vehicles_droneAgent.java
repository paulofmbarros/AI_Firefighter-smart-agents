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
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Vehicles_droneAgent extends VehiclesAgent{
	private static final long serialVersionUID = 1L;

	public Vehicles_droneAgent() {
		 isAvailable = true;
		 coordX = 50;
		 coordY = 50;
		 waterTank = Configurations.DRONE_MAX_WATER_TANK_CAPACITY;
		 fuelTank = Configurations.DRONE_MAX_WATER_TANK_CAPACITY;
		 speed = 100; // delete for real traveling speed (slow and boring!) Configurations.BASE_VEHICLE_SPEED*Configurations.FIRE_TRUCK_SPEED_MULTIPLIER;
	}
	
	
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
			
			//ver se o nome deve permanecer 
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

	
	class TravelToFireTicker extends TickerBehaviour {

		/**
		 * 
		 */
		private boolean travelingToFire, travelingToWater, travelingToFuel;
		private int destX, destY, tick;
		private OrderMessage order;
		private ACLMessage request;
		public TravelToFireTicker(Agent a, ACLMessage request) {
			super(a, speed);
			this.travelingToFire = false;
			this.travelingToFuel = false;
			this.travelingToWater = false;
			this.request = request;
			this.tick = 0; //debug
			try {
				this.order = (OrderMessage) request.getContentObject();
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
			if(order != null) {
				System.out.println(coordX + "x, " + coordY + "y" + "   --   " + fuelTank + "l fuel, " + waterTank +"l water");
				if(waterTank <= 0) {
					if(fuelTank <= simulateDistance(coordX, coordY, 30, 30) + simulateDistance(30, 30, order.getFireCoordX(), order.getFireCoordY())) {
						System.out.println("No water no fuel");
						this.travelingToWater = true;
						this.travelingToFire = true;
					}
					else {
						System.out.println("No water, has fuel");
						this.travelingToWater = true;
					}
				}
				else if(fuelTank <= simulateDistance(coordX, coordY, order.getFireCoordX(), order.getFireCoordY())) {
					System.out.println("No fuel, has water");
					this.travelingToFuel = true;
				}
				else {
					System.out.println("Has everything - " + fuelTank +" in fuel tank, " + waterTank + " in water tank. Cost is " + simulateDistance(coordX, coordY, order.getFireCoordX(), order.getFireCoordY()) + " moves");
					travelingToFire = true;
				}
			}
		}
		private static final long serialVersionUID = 1L;

		@Override
		protected void onTick() {
			if(travelingToWater && travelingToFuel) {
				if(destX != 50 && destY != 50 && tick != 0) {
					System.out.println("Traveling to " + destX + "x, " + destY + "y");
				}
				this.destX = 50;
				this.destY = 50;
				if(coordX == this.destX && coordY == this.destY) {
					fuelTank = Configurations.DRONE_MAX_FUEL_TANK_CAPACITY;
					this.travelingToFuel = false;
				}
			}
			else if(travelingToWater) {
				if(destX != 30 && destY != 30 && tick != 0) {
					System.out.println("Traveling to " + destX + "x, " + destY + "y");
				}
				this.destX = 30;
				this.destY = 30;
				if(coordX == this.destX && coordY == this.destY) {
					waterTank = Configurations.DRONE_MAX_WATER_TANK_CAPACITY;
					this.travelingToWater = false;
				}
			}
			else if (travelingToFire) {
				if(destX !=  order.getFireCoordX() && destY != order.getFireCoordY() && tick != 0) {
					System.out.println("Traveling to " + destX + "x, " + destY + "y");
				}
				this.destX = order.getFireCoordX();
				this.destY = order.getFireCoordY();
				if(coordX == this.destX && coordY == this.destY) {
					waterTank--;
					this.travelingToFire = false;
				}
			}
			
			if (coordX < destX) {
				coordX++;
			} else if (coordX > destX) {
				coordX--;
			}
			if (coordY < destY) {
				coordY++;
			} else if (coordY > destY) {
				coordY--;
			}

			System.out.println(coordX + "x, " + coordY + "y" + "   --   " + fuelTank + "l fuel, " + waterTank +"l water");
			if(travelingToFire || travelingToFuel || travelingToWater) {
				fuelTank--;
			}
			else {
				System.out.println("Traveling done. Final stats: " + waterTank + " liters of water, " + fuelTank + " liters of fuel");
				try {
					ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
					msg.setContentObject((OrderMessage) request.getContentObject());
					msg.addReceiver(request.getSender());
					//A enviar a mensagem a dizer que apagou o fogo ao Firestation
					send(msg);
					isAvailable = true;
				} catch (IOException | UnreadableException e) {
					System.out.println("Erro ao enviar msg de confirmacao de fogo apagado");
					e.printStackTrace();
				}
				
				stop();
			}
		}
		
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
				if(fuelTank <= simulateDistance(coordX, coordY, 30, 30) + simulateDistance(30, 30, om.getFireCoordX(), om.getFireCoordY())) {
					System.out.println("No water no fuel");
					travel(50, 50);
					fuelTank = Configurations.DRONE_MAX_FUEL_TANK_CAPACITY;
					travel(30, 30);
					waterTank = Configurations.DRONE_MAX_WATER_TANK_CAPACITY;
					travel(om.getFireCoordX(), om.getFireCoordY());
					waterTank--;
				}
				else {
					System.out.println("No water, has fuel");
					travel(30,30);
					waterTank = Configurations.DRONE_MAX_WATER_TANK_CAPACITY;
					travel(om.getFireCoordX(), om.getFireCoordY());
					waterTank--;
				}
			}
			else if(fuelTank <= simulateDistance(coordX, coordY, om.getFireCoordX(), om.getFireCoordY())) {
				System.out.println("No fuel, has water");
				travel(50, 50);
				fuelTank = Configurations.DRONE_MAX_FUEL_TANK_CAPACITY;
				travel(om.getFireCoordX(), om.getFireCoordY());
				waterTank--;
			}
			else {
				System.out.println("Has everything - " + fuelTank +" in fuel tank, " + waterTank + " in water tank. Cost is " + simulateDistance(coordX, coordY, om.getFireCoordX(), om.getFireCoordY()) + " moves");
				travel(om.getFireCoordX(), om.getFireCoordY());
				waterTank--;
			}
			System.out.println("Traveling done. Final stats: " + waterTank + " liters of water, " + fuelTank + " liters of fuel");
			
			try {
				ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
				msg.setContentObject((OrderMessage) request.getContentObject());
				msg.addReceiver(request.getSender());
				//A enviar a mensagem a dizer que apagou o fogo ao Firestation
				send(msg);
				isAvailable = true;
			} catch (IOException | UnreadableException e) {
				System.out.println("Erro ao enviar msg de confirmacao de fogo apagado");
				e.printStackTrace();
			}
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
				addBehaviour(new TravelToFireTicker(myAgent, request));
			}
			else {
				try {
					fireId = ((OrderMessage) request.getContentObject()).getFireId();
					ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
					StatusMessage sm = new StatusMessage(coordX, coordY, fireId, myAgent.getAID(), isAvailable, "DRONE", fuelTank, waterTank);
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
						StatusMessage sm = new StatusMessage(coordX, coordY, fm.getFireId(), myAgent.getAID(), isAvailable, "DRONE", fuelTank, waterTank);
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
