package Classes;

import java.util.Random;

import World.WorldObject;
import Config.Configurations;;


public class FuelResource implements java.io.Serializable{
	// Global Instance Variables:
	
		/**
		 * The ID of the Fuel Resource.
		 */
		private byte id;
		
		/**
		 * The World's object of the Fuel Resource.
		 */
		private WorldObject worldObject;
		
		/**
		 * The Fuel Quantity currently contained in the Fuel Resource.
		 */
		private int fuelQuantity;
		
		
		// Constructors:
		
		/**
		 * The constructor #1 of the Fuel Resource.
		 * 
		 * @param id the ID of the Fuel Resource
		 * @param worldObject the World's object associated to the Fuel Resource
		 */
		public FuelResource(byte id, WorldObject worldObject) {
			Random random = new Random();
			
			this.id = id;
			this.worldObject = worldObject;
			this.fuelQuantity = random.nextInt(Configurations.FUEL_RESOURCE_INITIAL_MAX_CAPACITY) + 1;
		}
		
		
		// Methods/Functions:
		
		/**
		 * Returns the ID of the Fuel Resource.
		 * 
		 * @return the ID of the Fuel Resource
		 */
		public byte getID() {
			return this.id;
		}
		
		/**
		 * Returns the World's object of the Fuel Resource.
		 * 
		 * @return the World's object of the Fuel Resource
		 */
		public WorldObject getWorldObject() {
			return this.worldObject;
		}
		
		/**
		 * Returns the Fuel quantity of the Fuel Resource.
		 * 
		 * @return the Fuel quantity of the Fuel Resource
		 */
		public int getFuelQuantity() {
			return this.fuelQuantity;
		}
		
		/**
		 * Decreases the current Fuel quantity of the Fuel Resource, given a value of Fuel quantity.
		 * 
		 * @param value a value of Fuel quantity to decrease in the Fuel Resource
		 */
		public void decreaseQuantity(int value) {
			
			if((this.fuelQuantity - value) < 0) {
				this.fuelQuantity = 0;
			}
			
			this.fuelQuantity -= value;
		}
		
		/**
		 * Increases the current Fuel quantity of the Fuel Resource, given a value of Fuel quantity.
		 * 
		 * @param value a value of Fuel quantity to increase in the Fuel Resource
		 */
		public void increaseQuantity(int value) {
			this.fuelQuantity += value;
		}
		
		/**
		 * Returns the basic information to be displayed in a graphic user interface about the Fuel Resource.
		 */
		@Override
		public String toString() {
			return "WR - wq: " + this.fuelQuantity + ";";
		}
}
