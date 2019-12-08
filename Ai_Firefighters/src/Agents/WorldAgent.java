package Agents;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

import Classes.ExtinguishedFire;
import Classes.Fire;
import Classes.FuelResource;
import Classes.WaterResource;
import Config.Configurations;
import Enums.WorldObjectEnum;
import Messages.FireMessage;
import Messages.OrderMessage;
import Messages.ResourcesMessage;
import World.WorldObject;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Map;
import java.awt.Point;
import java.io.IOException;
import jess.Fact;
import jess.RU;
import jess.Value;

public class WorldAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private jess.Rete jess;

	protected void setup() {
		currentlyActiveFires = new HashMap<Integer, Fire>();
		this.generateResourcesPositions();
		SequentialBehaviour sb = new SequentialBehaviour();
		this.jess = new jess.Rete();
		sb.addSubBehaviour(new InformInterfaceBehaviour(this));
		sb.addSubBehaviour(new ReceiveMessages(this));
		addBehaviour(sb);
	}
	
	class ReceiveMessages extends CyclicBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ReceiveMessages(Agent a) {
			super(a);
		}

		boolean newMsg(ACLMessage msg) {
			try {
				String sender = msg.getSender().getLocalName();
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!PAAASSSOOOOUUUUU AAAAQUUUUIIIIIII!!!!!!!!!!!!!!!!!!!!!!!!!!");

				Object content = msg.getContentObject();
				FireMessage fm = (FireMessage) content;
				String fireId = fm.getFireId();
				int posX = fm.getFireCoordX();
				int posY = fm.getFireCoordY();
				Fact f = new Fact("Fire", jess);
				f.setSlotValue("sender", new Value(sender, RU.STRING));
				f.setSlotValue("fireId", new Value(fireId, RU.INTEGER));
				f.setSlotValue("posX", new Value(posX, RU.INTEGER));
				f.setSlotValue("posY", new Value(posY, RU.INTEGER));
				jess.assertFact(f);
				return true;
			} catch (Exception ex) {
				return false;
			}

		}

		public void action() {
			ACLMessage msg = receive();
			if (msg == null || msg.getOntology() == "info-resources" || msg.getOntology() == "info-extinguishedFire") {
				block();
				return;
			}

			try {
				Object content = msg.getContentObject();
				switch (msg.getPerformative()) {
				case (ACLMessage.INFORM):
					if (content instanceof FireMessage) {
						FireMessage fm = (FireMessage) content;
						WorldObject fireWorldObject = new WorldObject(WorldObjectEnum.FIRE,
								new Point(fm.getFireCoordX(), fm.getFireCoordY()));

						Map<Integer, Fire> fires = getCurrentlyActiveFires();
						Fire fire = null;
						if (fires != null) {
							fire = new Fire((fires.size() + 1), fireWorldObject);
						} else {
							fire = new Fire((1), fireWorldObject);
						}
						newMsg(msg);
						// Add, effectively, the Fire to the World
						addFire(fm.getFireCoordX(), fm.getFireCoordY(), fire);

					}

					break;
				case (ACLMessage.CONFIRM):
					if (content instanceof OrderMessage) {
						OrderMessage om = (OrderMessage) content;
						//int x = getNumCurrentlyActiveFires();
						if (getNumCurrentlyActiveFires() > 0) {
							removeFire(om.getFireCoordX(), om.getFireCoordY());
						} else {
							System.out.println("Todos os fogos já se encontram extintos");
						}
					}
					break;
				case (ACLMessage.REQUEST):
					if (content instanceof ResourcesMessage) {
						System.out.println("Reources's positions request received.");
						ResourcesMessage rm = new ResourcesMessage(waterResources, fuelResources);
						ACLMessage message = new ACLMessage(ACLMessage.INFORM);
						try {
							message.setContentObject(rm);
							message.addReceiver(msg.getSender());
							send(message);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Reources's positions sent.");
					}
					break;
				default:
					break;

				}
			} catch (Exception e) {
				System.out.println(e);
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
			try {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setOntology("info-resources");
				ArrayList<WaterResource> waterResource = new ArrayList<WaterResource> (waterResources);
				waterResource.remove(waterResource.size() - 1);
				ArrayList<FuelResource> fuelResource = new ArrayList<FuelResource>(fuelResources);
				fuelResource.remove(fuelResource.size() - 1);
				ResourcesMessage rm = new ResourcesMessage(waterResource, fuelResource);
				msg.setContentObject(rm);
				msg.addReceiver(new AID("Interface", AID.ISLOCALNAME));
				myAgent.send(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	class extinguishedFireInteface extends OneShotBehaviour {


		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int cordX, cordY;
		
		public extinguishedFireInteface(Agent a, int x, int y) {
			super(a);
			this.cordX=x;
			this.cordY=y;
		}

		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setOntology("info-extinguishedFire");
			msg.setContent(cordX + "::" + cordY +"::");
			msg.addReceiver(new AID("Interface", AID.ISLOCALNAME));
			myAgent.send(msg);
		}
	}


	/**
	 * The current type of the Weather Season from the set {Spring, Summer, Autumn
	 * and Winter}
	 */
	// private static WeatherSeasonType seasonType;

	/**
	 * The current type of Wind from the set {Very Windy, Windy and No Wind}
	 */
//	private static WindType windType;

	// Global Instance Variables:

	/**
	 * The matrix that represents all the positions/points of the World's map/grid.
	 */
	private Object[][] worldMap = new Object[Configurations.GRID_WIDTH][Configurations.GRID_HEIGHT];

	/**
	 * The Water Resources in the World.
	 */
	private ArrayList<WaterResource> waterResources;

	/**
	 * The Water Resources in the World.
	 */
	private ArrayList<FuelResource> fuelResources;

	/**
	 * The Currently Active Fires in the World.
	 */
	private HashMap<Integer, Fire> currentlyActiveFires;

	/**
	 * The Extinguished Fires by any Vehicle Agent, until the moment.
	 */
	private ArrayList<ExtinguishedFire> extinguishedFires = new ArrayList<ExtinguishedFire>();

	// Fixed Agents (without/with no movement)

	/**
	 * The Fire Station Agent in the World.
	 */
	private FirestationAgent fireStationAgent;

	// Mobile Agents (with movement)

	/**
	 * The Vehicle Agents presented in the world.
	 */
	private Map<Integer, VehiclesAgent> vehicleAgents;

	/**
	 * The Aircraft Agents presented in the world.
	 */
	private ArrayList<Vehicles_aircraftAgent> aircraftAgents;

	/**
	 * The Drone Agents presented in the world.
	 */
	private ArrayList<Vehicles_droneAgent> droneAgents;

	/**
	 * The Fire Truck Agents presented in the world.
	 */
	private ArrayList<Vehicles_firetruckAgent> fireTruckAgents;

	public int getSizeWorldMap() {
		return this.worldMap.length * this.worldMap[0].length;
	}

	/**
	 * Returns the number of all the positions/points presented and currently
	 * available in the World's map/grid.
	 * 
	 * @return the number of all the positions/points presented and currently
	 *         available in the World's map/grid
	 */
	public int getAvailablePositionsInWorldMap() {
		int numAvailablePositions = 0;

		for (int row = 0; row < worldMap.length; row++)
			for (int col = 0; col < worldMap[0].length; col++)
				if (this.worldMap[row][col] != null)
					numAvailablePositions++;

		return numAvailablePositions;
	}

	/**
	 * Generate all the positions/points of water and fuel resources in the World's
	 * map/grid.
	 */
	public void generateResourcesPositions() {
		waterResources = new ArrayList<WaterResource>();
		fuelResources = new ArrayList<FuelResource>();

		// Water Resources
		Point p0 = new Point(new SecureRandom().nextInt(250), new SecureRandom().nextInt(250));
		WorldObject wo0 = new WorldObject(WorldObjectEnum.WATER_RESOURCE, p0);
		WaterResource wr0 = new WaterResource((byte) 0, wo0);
		this.worldMap[(int) p0.getX()][(int) p0.getY()] = wr0;

		Point p1 = new Point(new SecureRandom().nextInt(250), new SecureRandom().nextInt(500 - 250) + 250);
		WorldObject wo1 = new WorldObject(WorldObjectEnum.WATER_RESOURCE, p1);
		WaterResource wr1 = new WaterResource((byte) 1, wo1);
		this.worldMap[(int) p1.getX()][(int) p1.getY()] = wr1;

		Point p2 = new Point(new SecureRandom().nextInt(500 - 250) + 250, new SecureRandom().nextInt(250));
		WorldObject wo2 = new WorldObject(WorldObjectEnum.WATER_RESOURCE, p2);
		WaterResource wr2 = new WaterResource((byte) 2, wo2);
		this.worldMap[(int) p2.getX()][(int) p2.getY()] = wr2;

		Point p3 = new Point(new SecureRandom().nextInt(500 - 250) + 250, new SecureRandom().nextInt(500 - 250) + 250);
		WorldObject wo3 = new WorldObject(WorldObjectEnum.WATER_RESOURCE, p3);
		WaterResource wr3 = new WaterResource((byte) 3, wo3);
		this.worldMap[(int) p3.getX()][(int) p3.getY()] = wr3;

		Point p4 = new Point(250, 250);
		WorldObject wo4 = new WorldObject(WorldObjectEnum.WATER_RESOURCE, p4);
		WaterResource wr4 = new WaterResource((byte) 4, wo4);
		this.worldMap[(int) p4.getX()][(int) p4.getY()] = wr4;

		waterResources.add(wr0);
		waterResources.add(wr1);
		waterResources.add(wr2);
		waterResources.add(wr3);
		waterResources.add(wr4);

		// Fuel Resources
		Point p5 = new Point(new SecureRandom().nextInt(250), new SecureRandom().nextInt(250));
		WorldObject wo5 = new WorldObject(WorldObjectEnum.FUEL_RESOURCE, p5);
		FuelResource fr0 = new FuelResource((byte) 0, wo5);
		this.worldMap[(int) p5.getX()][(int) p5.getY()] = fr0;

		Point p6 = new Point(new SecureRandom().nextInt(250), new SecureRandom().nextInt(500 - 250) + 250);
		WorldObject wo6 = new WorldObject(WorldObjectEnum.FUEL_RESOURCE, p6);
		FuelResource fr1 = new FuelResource((byte) 1, wo6);
		this.worldMap[(int) p6.getX()][(int) p6.getY()] = fr1;

		Point p7 = new Point(new SecureRandom().nextInt(500 - 250) + 250, new SecureRandom().nextInt(250));
		WorldObject wo7 = new WorldObject(WorldObjectEnum.FUEL_RESOURCE, p7);
		FuelResource fr2 = new FuelResource((byte) 2, wo7);
		this.worldMap[(int) p7.getX()][(int) p7.getY()] = fr2;

		Point p8 = new Point(new SecureRandom().nextInt(500 - 250) + 250, new SecureRandom().nextInt(500 - 250) + 250);
		WorldObject wo8 = new WorldObject(WorldObjectEnum.FUEL_RESOURCE, p8);
		FuelResource fr3 = new FuelResource((byte) 3, wo8);
		this.worldMap[(int) p8.getX()][(int) p8.getY()] = fr3;

		Point p9 = new Point(250, 250);
		WorldObject wo9 = new WorldObject(WorldObjectEnum.FUEL_RESOURCE, p9);
		FuelResource fr4 = new FuelResource((byte) 4, wo9);
		this.worldMap[(int) p9.getX()][(int) p9.getY()] = fr4;

		fuelResources.add(fr0);
		fuelResources.add(fr1);
		fuelResources.add(fr2);
		fuelResources.add(fr3);
		fuelResources.add(fr4);
	}

	public ArrayList<WaterResource> getWaterResources() {
		return this.waterResources;
	}

	public ArrayList<FuelResource> getFuelResources() {
		return this.fuelResources;
	}

	/**
	 * Returns all the Currently Active Fires presented in the World.
	 * 
	 * @return all the Currently Active Fires presented in the World
	 */
	public Map<Integer, Fire> getCurrentlyActiveFires() {
		return currentlyActiveFires;
	}

	/**
	 * Returns the number of all the Currently Active Fires presented in the World.
	 * 
	 * @return the number of all the Currently Active Fires presented in the World
	 */
	public int getNumCurrentlyActiveFires() {
		return this.currentlyActiveFires.size();
	}

	/**
	 * Returns all the Extinguished Fires by any Vehicle Agent, until the moment.
	 * 
	 * @return all the Extinguished Fires by any Vehicle Agent, until the moment
	 */
	public ArrayList<ExtinguishedFire> getExtinguishedFires() {
		return this.extinguishedFires;
	}

	/**
	 * Returns the number of all the extinguished Fires by any Vehicle Agent, until
	 * the moment.
	 * 
	 * @return the number of all the extinguished Fires by any Vehicle Agent, until
	 *         the moment
	 */
	public int getNumExtinguishedFires() {
		return this.extinguishedFires.size();
	}

	/**
	 * Returns all the Fire Station Agents presented in the World.
	 * 
	 * @return all the Fire Station Agents presented in the World
	 */
	public FirestationAgent getFireStationAgent() {
		return this.fireStationAgent;
	}

	/**
	 * Returns the number of all the Fire Station Agents presented in the World.
	 * 
	 * @return the number of all the Fire Station Agents presented in the World
	 */
	public int getNumFireStationAgents() {
		return 1;
	}

	/**
	 * Returns all the Vehicle Agents presented in the World.
	 * 
	 * @return all the Vehicle Agents presented in the World
	 */
	public Map<Integer, VehiclesAgent> getVehicleAgents() {
		return this.vehicleAgents;
	}

	/**
	 * Returns the number of all the Vehicle Agents presented in the World.
	 * 
	 * @return the number of all the Vehicle Agents presented in the World
	 */
	public int getNumTotalVehicleAgents() {
		return this.vehicleAgents.size();
	}

	/**
	 * Returns all the Aircraft Agents presented in the World.
	 * 
	 * @return all the Aircraft Agents presented in the World
	 */
	public ArrayList<Vehicles_aircraftAgent> getAircraftAgents() {
		return this.aircraftAgents;
	}

	/**
	 * Returns the number of all the Aircraft Agents presented in the World.
	 * 
	 * @return the number of all the Aircraft Agents presented in the World
	 */
	public int getNumAircraftAgents() {
		return this.vehicleAgents.size();
	}

	/**
	 * Returns all the Drone Agents presented in the World.
	 * 
	 * @return all the Drone Agents presented in the World
	 */
	public ArrayList<Vehicles_droneAgent> getDroneAgents() {
		return this.droneAgents;
	}

	/**
	 * Returns the number of all the Drone Agents presented in the World.
	 * 
	 * @return the number of all the Drone Agents presented in the World
	 */
	public int getNumDroneAgents() {
		return this.droneAgents.size();
	}

	/**
	 * Returns all the Fire Truck Agents presented in the World.
	 * 
	 * @return all the Fire Truck Agents presented in the World
	 */
	public ArrayList<Vehicles_firetruckAgent> getFireTruckAgent() {
		return this.fireTruckAgents;
	}

	/**
	 * Returns the number of all the Fire Truck Agents presented in the World.
	 * 
	 * @return the number of all the Fire Truck Agents presented in the World
	 */
	public int getNumFireTruckAgents() {
		return this.fireTruckAgents.size();
	}

	public void addFire(int firePositionX, int firePositionY, Fire fire) {

		// Add the Fire to the World's map/grid, putting it in its respectively
		// position/point
		this.worldMap[firePositionX][firePositionY] = fire;

		// Add the Fire to the Currently Active Fires
		currentlyActiveFires.put(fire.getID(), fire);

		System.out.println("Fogo adicionado a lista de fogos ativos no mapa");
	}

	/**
	 * Removes a Fire from a given position/point in the World's map/grid.
	 * 
	 * @param firePositionX                         coordinate X of the
	 *                                              position/point of the World's
	 *                                              map/grid
	 * @param firePositionY                         coordinate Y of the
	 *                                              position/point of the World's
	 *                                              map/grid
	 * @param idVehicleResponsibleForExtinguishFire the ID of the Vehicle Agent
	 *                                              responsible for extinguish the
	 *                                              Fire
	 */
	public void removeFire(int firePositionX, int firePositionY) {

		// The position of the pretended Fire to be removed can't be null
		if (this.worldMap[firePositionX][firePositionY] != null) {

			// The position of the pretended Fire to be removed must be, as obvious, an
			// instance of a Fire
			if (this.worldMap[firePositionX][firePositionY] instanceof Fire) {

				Fire fireToBeExtinguished = (Fire) this.worldMap[firePositionX][firePositionY];

				// Remove the Fire to the World's map/grid, changing its position/point to null
				this.worldMap[firePositionX][firePositionY] = null;
				this.addBehaviour(new extinguishedFireInteface(this,firePositionX,firePositionY));
				// Remove the Fire from the Currently Active Fires
				this.currentlyActiveFires.remove(fireToBeExtinguished.getID());
				
				ExtinguishedFire extinguishedFire = new ExtinguishedFire(fireToBeExtinguished);

				// Add the Fire to the Extinguished Fires
				this.extinguishedFires.add(extinguishedFire);
			}
		}

		System.out.println("FOGO REMOVIDO DA LISTA DE FOGOS ATIVOS DO MAPA");
	}
}
