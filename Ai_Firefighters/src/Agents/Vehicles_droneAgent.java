package Agents;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Classes.FuelResource;
import Classes.WaterResource;
import Config.Configurations;
import Enums.WorldObjectEnum;
import Messages.FireMessage;
import Messages.OrderMessage;
import Messages.ResourcesMessage;
import Messages.StatusMessage;
import jade.core.AID;
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

public class Vehicles_droneAgent extends VehiclesAgent { // MUDAR AQUI
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String vehicleEnum = "DRONE"; // MUDAR AQUI
	
	public Vehicles_droneAgent(){ //MUDAR AQUI
		isAvailable=true;
		coordX=200;
		coordY=200;
		waterTank = maxWater = Configurations.DRONE_MAX_WATER_TANK_CAPACITY; // MUDAR AQUI
		fuelTank = maxFuel = Configurations.DRONE_MAX_FUEL_TANK_CAPACITY;  // MUDAR AQUI
				
		speed=100;
		
		waterResources = new ArrayList<WaterResource>();
		fuelResources = new ArrayList<FuelResource>();
	}

	protected void setup() {
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this));
		sb.addSubBehaviour(new InformInterfaceBehaviour(this));
		sb.addSubBehaviour(new GetResourcesPositions(this));
		sb.addSubBehaviour(new ReceiveMessages(this));
		localName = this.getLocalName();
		addBehaviour(sb);
		System.out.println(localName + ": Vehicle agent started");
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
				System.out.println(localName + ": Vehicle failed registering with DF! Shutting down...");
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
			this.tick = 0; // debug
			try {
				this.order = (OrderMessage) request.getContentObject();
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

			if (order != null) {
				System.out.println(localName + ": " +
						coordX + "x, " + coordY + "y" + "   --   " + fuelTank + "l fuel, " + waterTank + "l water");
				if (waterTank <= 0) {
					if (fuelTank <= simulateDistance(coordX, coordY, 30, 30)
							+ simulateDistance(30, 30, order.getFireCoordX(), order.getFireCoordY())) {
						System.out.println(localName + ": No water no fuel");
						this.travelingToWater = true;
						this.travelingToFire = true;
					} else {
						System.out.println(localName + ": No water, has fuel");
						this.travelingToWater = true;
					}
				} else if (fuelTank <= simulateDistance(coordX, coordY, order.getFireCoordX(), order.getFireCoordY())) {
					System.out.println(localName + ": No fuel, has water");
					this.travelingToFuel = true;
				} else {
					System.out.println(
							localName + ": Has everything - " + fuelTank + " in fuel tank, " + waterTank + " in water tank. Cost is "
									+ simulateDistance(coordX, coordY, order.getFireCoordX(), order.getFireCoordY())
									+ " moves");
					travelingToFire = true;
				}
			}
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected void onTick() {
			if (travelingToWater && travelingToFuel) {
				if (destX != 50 && destY != 50 && tick != 0) {
					System.out.println(localName + ": Traveling to " + destX + "x, " + destY + "y");
				}
				this.destX = 50;
				this.destY = 50;
				if (coordX == this.destX && coordY == this.destY) {
					fuelTank = Configurations.DRONE_MAX_FUEL_TANK_CAPACITY;
					this.travelingToFuel = false;
				}
			} else if (travelingToWater) {
				if (destX != 30 && destY != 30 && tick != 0) {
					System.out.println(localName + ": Traveling to " + destX + "x, " + destY + "y");
				}
				this.destX = 30;
				this.destY = 30;
				if (coordX == this.destX && coordY == this.destY) {
					waterTank = Configurations.DRONE_MAX_WATER_TANK_CAPACITY;
					this.travelingToWater = false;
				}
			} else if (travelingToFire) {
				if (destX != order.getFireCoordX() && destY != order.getFireCoordY() && tick != 0) {
					System.out.println(localName + ": Traveling to " + destX + "x, " + destY + "y");
				}
				this.destX = order.getFireCoordX();
				this.destY = order.getFireCoordY();
				if (coordX == this.destX && coordY == this.destY) {
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

			System.out.println(localName + ": " + coordX + "x, " + coordY + "y" + "   --   " + "dest: " + destX + "x, " + destY + "y "
					+ " -- " + fuelTank + "l fuel, " + waterTank + "l water");
			if (travelingToFire || travelingToFuel || travelingToWater) {
				fuelTank--;
				myAgent.addBehaviour(new InformInterfaceBehaviour(myAgent));
			} else {
				System.out.println(localName + ": Traveling done. Final stats: " + waterTank + " liters of water, " + fuelTank
						+ " liters of fuel");
				try {
					ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
					msg.setContentObject((OrderMessage) request.getContentObject());
					msg.addReceiver(request.getSender());
					// A enviar a mensagem a dizer que apagou o fogo ao Firestation
					send(msg);
					isAvailable = true;
				} catch (IOException | UnreadableException e) {
					System.out.println(localName + ": Erro ao enviar msg de confirmacao de fogo apagado");
					e.printStackTrace();
				}

				// wait a few seconds, if new fire message not received, travel to resources and
				// refill fuel (and water if needed)
				Timer t = new Timer();
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						if (isAvailable) {
							System.out.println(localName + ": Fire message not received in a while, traveling to resources");
							myAgent.addBehaviour(new TravelToResources(myAgent));
						}
						t.cancel();
					}
				}, 5000);
				stop();
			}
		}
	}
	
	class TravelToResources extends TickerBehaviour {

		private static final long serialVersionUID = 1L;
		private Point closestFuelResource;
		private Point closestWaterResource;
		private int destX, destY;
		private boolean needsWater;
		private boolean fetchingFuel;

		public TravelToResources(Agent a) {
			super(a, speed);
			isAvailable = false;
			needsWater = waterTank <= 0;
			fetchingFuel = true;
			closestFuelResource = getClosest(new Point(coordX, coordY), WorldObjectEnum.FUEL_RESOURCE);
			destX = (int) closestFuelResource.getX();
			destY = (int) closestFuelResource.getY();
		}

		@Override
		protected void onTick() {
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

			fuelTank--;
			myAgent.addBehaviour(new InformInterfaceBehaviour(myAgent));
			System.out.println(localName + ": " + coordX + "x, " + coordY + "y" + "   --   " + "dest: " + destX + "x, " + destY + "y "
					+ " -- " + fuelTank + "l fuel, " + waterTank + "l water");
			if (coordX == destX && coordY == destY) {

				if (fetchingFuel && needsWater) {
					fuelTank = maxFuel;
					fetchingFuel = false;
					closestWaterResource = getClosest(new Point(coordX, coordY), WorldObjectEnum.WATER_RESOURCE);
					destX = (int) closestWaterResource.getX();
					destY = (int) closestWaterResource.getY();
					System.out.println(localName + ": " + coordX + "x, " + coordY + "y" + "   --   " + "dest: " + destX + "x, " + destY
							+ "y " + " -- " + fuelTank + "l fuel, " + waterTank + "l water");
				} else if (fetchingFuel && !needsWater) {
					fuelTank = maxFuel;
					System.out.println(localName + ": " + coordX + "x, " + coordY + "y" + "   --   " + "dest: " + destX + "x, " + destY
							+ "y " + " -- " + fuelTank + "l fuel, " + waterTank + "l water");
					isAvailable = true;
					
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {
							if (isAvailable  && fuelTank < maxFuel - 10) {
								System.out.println(localName + ": Fire message not received in a while, traveling to resources");
								myAgent.addBehaviour(new TravelToResources(myAgent));
							}
							t.cancel();
						}
					}, 5000);
					stop();
				} else if (!fetchingFuel && needsWater) {
					waterTank = maxWater;
					System.out.println(localName + ": " + coordX + "x, " + coordY + "y" + "   --   " + "dest: " + destX + "x, " + destY
							+ "y " + " -- " + fuelTank + "l fuel, " + waterTank + "l water");
					isAvailable = true;
					
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						@Override
						public void run() {
							if (isAvailable && fuelTank < maxFuel - 10) {
								System.out.println(localName + ": Fire message not received in a while, traveling to resources");
								myAgent.addBehaviour(new TravelToResources(myAgent));
							}
							t.cancel();
						}
					}, 5000);
					stop();
				}
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
					StatusMessage sm = new StatusMessage(coordX, coordY, fireId, myAgent.getAID(), isAvailable, vehicleEnum, fuelTank, waterTank);
					msg.setContentObject(sm);
					msg.addReceiver(request.getSender());
					send(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class InformInterfaceBehaviour extends OneShotBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InformInterfaceBehaviour(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setOntology("info-drone");
			msg.setContent(coordX + "::" + coordY +"::");
			msg.addReceiver(new AID("Interface", AID.ISLOCALNAME));
			send(msg);
		}
	}
	
	class GetResourcesPositions extends OneShotBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public GetResourcesPositions(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ResourcesMessage rm = new ResourcesMessage(waterResources, fuelResources);

			// AQUI ENVIA A MENSAGEM AO WORLDAGENT PARA OBTER WATER AND FUEL RESOURCES
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			try {
				message.setContentObject(rm);
				message.addReceiver(new AID("WorldAgent", AID.ISLOCALNAME));
				send(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Request of Water and Fuel Resources");
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
			if(msg == null  || msg.getOntology() == "info-drone") {
				block();
				return;
			}
			
			try {
				Object content = msg.getContentObject();
				switch(msg.getPerformative()) {
				case (ACLMessage.REQUEST):
					if(content instanceof FireMessage) {
						FireMessage fm = (FireMessage) content;
						System.out.println(localName + ": Get status msg received in vehicle. "
								+ "Coords of corresponding fire: X: "
								+ "" + fm.getFireCoordX() + " Y: " + fm.getFireCoordY());
						StatusMessage sm = new StatusMessage(coordX, coordY, fm.getFireId(), myAgent.getAID(), isAvailable, vehicleEnum, fuelTank, waterTank);
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
				case (ACLMessage.INFORM):
					if (content instanceof ResourcesMessage) {
						ResourcesMessage rm = (ResourcesMessage) content;
						waterResources = rm.getWaterResources();
						fuelResources = rm.getFuelResources();
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
