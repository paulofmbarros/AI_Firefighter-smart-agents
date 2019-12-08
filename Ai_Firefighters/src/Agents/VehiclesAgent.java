package Agents;

import java.awt.Point;
import java.util.ArrayList;

import Classes.FuelResource;
import Classes.WaterResource;
import Enums.WorldObjectEnum;
import Messages.FireMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class VehiclesAgent extends Agent {
	private static final long serialVersionUID = 1L;
	protected boolean isAvailable = true;
	protected ArrayList<WaterResource> waterResources;
	protected ArrayList<FuelResource> fuelResources;
	protected int coordX = 0;
	protected int coordY = 0;
	protected int waterTank = 0;
	protected int fuelTank = 0;
	protected int maxWater = 0;
	protected int maxFuel = 0;
	protected String localName = "";
	protected int speed = 0; // delete for real traveling speed (slow and boring!) Configurations.BASE_VEHICLE_SPEED*Configurations.FIRE_TRUCK_SPEED_MULTIPLIER;

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
	
	public void travel(int x, int y) {
		System.out.println("Traveling to " + x + "x, " + y + "y");
		while(coordX != x || coordY != y) {
			System.out.println(coordX + "x, " + coordY + "y" + "   --   " + fuelTank + "l fuel, " + waterTank +"l water");
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
		while(x1 != x2 || y1 != y2) {
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
	
	public Point getClosest(Point p, WorldObjectEnum worldObject) {
		int closestDistance = 100000;
		int closestIndex = 0;
		switch (worldObject) {
		case WATER_RESOURCE:
			for (int i = 0; i < waterResources.size(); i++) {
				if (simulateDistance((int) p.getX(), (int) p.getY(),
						waterResources.get(i).getWorldObject().getPositionX(),
						waterResources.get(i).getWorldObject().getPositionY()) < closestDistance) {
					closestIndex = i;
					closestDistance = simulateDistance((int) p.getX(), (int) p.getY(),
							waterResources.get(i).getWorldObject().getPositionX(),
							waterResources.get(i).getWorldObject().getPositionY());
				}
			}
			return new Point(waterResources.get(closestIndex).getWorldObject().getPositionX(),
					waterResources.get(closestIndex).getWorldObject().getPositionY());
		case FUEL_RESOURCE:
			for (int i = 0; i < fuelResources.size(); i++) {
				if (simulateDistance((int) p.getX(), (int) p.getY(),
						fuelResources.get(i).getWorldObject().getPositionX(),
						fuelResources.get(i).getWorldObject().getPositionY()) < closestDistance) {
					closestIndex = i;
					closestDistance = simulateDistance((int) p.getX(), (int) p.getY(),
							waterResources.get(i).getWorldObject().getPositionX(),
							waterResources.get(i).getWorldObject().getPositionY());
				}
			}
			return new Point(fuelResources.get(closestIndex).getWorldObject().getPositionX(),
					fuelResources.get(closestIndex).getWorldObject().getPositionY());
		default:
			return new Point();
		}
	}

}
