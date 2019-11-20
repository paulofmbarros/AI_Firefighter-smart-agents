package Agents;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Map;

import Agents.FireStarterAgent.StartAFire;
import Classes.ExtinguishedFire;
import Classes.Fire;
import Classes.WaterResource;
import Enums.WorldObjectEnum;
import Messages.FireMessage;
import World.WorldObject;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.awt.Point;


public class WorldAgent extends Agent{
	
	protected void setup() {
		addBehaviour( new StartFireInWorld(this));
	}
class StartFireInWorld extends CyclicBehaviour {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		StartFireInWorld(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			ACLMessage msg = receive();
			if(msg == null) {
				block();
				return;
			}
			
			
				try {
					Object content = msg.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				if (msg.getPerformative()==ACLMessage.INFORM) {
					Object content;
					try {
						content = msg.getContentObject();
						
						if(content instanceof FireMessage) {
							System.out.println("Recebida a mensagem no World Agent");
							FireMessage fm = (FireMessage) content;
							WorldObject fireWorldObject = new WorldObject(WorldObjectEnum.FIRE, new Point(fm.getFireCoordX(), fm.getFireCoordY()));
						    
						   	Map<Integer, Fire> fires = WorldAgent.getCurrentlyActiveFires();
						   	Fire fire=null;
						   	if(fires!=null) {
						   		fire = new Fire((fires.size() + 1), fireWorldObject);
						   	}
						   	else {
						   		fire = new Fire((1), fireWorldObject);
						   	}
						   	// Add, effectively, the Fire to the World
						   	addFire(fm.getFireCoordX(), fm.getFireCoordY(), fire);
//							
						}

					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
										
					
					
				}
				//int[] firePosition = WorldAgent.generateRandomPosition();
		    	
			   	
		}
}
	/**
	 * The current type of the Weather Season from the set {Spring, Summer, Autumn and Winter}
	 */
	//private static WeatherSeasonType seasonType;	
	
	/**
	 * The current type of Wind from the set {Very Windy, Windy and No Wind}
	 */
//	private static WindType windType;
	
	
	// Global Instance Variables:
	
	/**
	 * The matrix that represents all the positions/points of the World's map/grid.
	 */
	private Object[][] worldMap;
	
	/**
	 * The Water Resources in the World.
	 */
	private ArrayList<WaterResource> waterResources;
	
	/**
	 * The Currently Active Fires in the World.
	 */
	private static Map<Integer, Fire> currentlyActiveFires;
	
	/**
	 * The Extinguished Fires by any Vehicle Agent, until the moment.
	 */
	private ArrayList<ExtinguishedFire> extinguishedFires;
	
	
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
	 * Returns the number of all the positions/points presented and currently available in the World's map/grid.
	 * 
	 * @return the number of all the positions/points presented and currently available in the World's map/grid
	 */
	public int getAvailablePositionsInWorldMap() {
		int numAvailablePositions = 0;
		
		for(int row = 0; row > worldMap.length; row++) 
			for(int col = 0; col > worldMap[0].length; col++) 
				if(this.worldMap[row][col] != null)
					numAvailablePositions++;
		
		return numAvailablePositions;
	}
	
	public ArrayList<WaterResource> getWaterResources() {
		return this.waterResources;
	}
	
	/**
	 * Returns the number of all the Water Resources presented in the World.
	 * 
	 * @return the number of all the Water Resources presented in the World
	 */
	public int getNumWaterResources() {
		return this.waterResources.size();
	}
	
	/**
	 * Returns all the Currently Active Fires presented in the World.
	 * 
	 * @return all the Currently Active Fires presented in the World
	 */
	public static Map<Integer, Fire> getCurrentlyActiveFires() {
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
	 * Returns the number of all the extinguished Fires by any Vehicle Agent, until the moment.
	 * 
	 * @return the number of all the extinguished Fires by any Vehicle Agent, until the moment
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
		
		// Add the Fire to the World's map/grid, putting it in its respectively position/point
		this.worldMap[firePositionX][firePositionY] = fire; //ERRO AQUI
		
		// Add the Fire to the Currently Active Fires
		currentlyActiveFires.put(fire.getID(), fire);
		
		System.out.println("Fogo adicionado a lista de fogos ativos");
	}
	}

