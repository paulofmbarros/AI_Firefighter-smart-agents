package WorldElements;

import java.util.Random;

import World.WorldObject;
import Config.Configurations;
import Enums.WorldObjectEnum;
public class Fire {
	// Global Instance Variables:
	
		/**
		 * The ID of the Fire.
		 */
		private int id;
		
		/**
		 * The World's object of the Fire.
		 */
		private WorldObject worldObject;
		
		/**
		 * The timeStamp of the Fire's creation.
		 */
		private long creationTimeStamp;
		
		/**
		 * The current intensity of the Fire.
		 */
		private int currentIntensity;
		
		/**
		 * The original intensity of the Fire.
		 */
		private final int originalIntensity;
		
		/**
		 * The probability of spreading of the Fire. 
		 */
		private float spreadProbability;
		
		/**
		 * The number of spreads of the Fire.
		 */
		private int numSpreads;
		
		/**
		 * The number of increases of the intensity of the Fire.
		 */
		private int numIntensityIncreases;
		
		/**
		 * The number of decreases of the intensity of the Fire.
		 */
		private int numIntensityDecreases;
		
		/**
		 * The status to inform that the Fire is currently active or not.
		 */
		private boolean active;
		
		/**
		 * The status to inform that the Fire is currently attended or not.
		 */
		private boolean attended;
		
		
		// Constructors:
		
		/**
		 * The constructor #1 of the Fire.
		 * 
		 * @param id the ID of the Fire
		 * @param worldObject the World's object associated to the Fire
		 */
		public Fire(int id, WorldObject worldObject) {
			Random random = new Random();
			
			this.worldObject = worldObject;
			
			this.creationTimeStamp = System.currentTimeMillis();
			
			this.currentIntensity = random.nextInt(Configurations.FIRE_MAX_INITIAL_INTENSITY) + 1;
			this.originalIntensity = currentIntensity;
			
			this.spreadProbability = random.nextFloat();
			
			this.numSpreads = 0;
			this.numIntensityIncreases = 0;
			this.numIntensityDecreases = 0;
			
			this.active = true;
			this.attended = false;
		}
		
		
		// Methods/Functions:
		
		/**
		 * Returns the ID of the Fire.
		 * 
		 * @return the ID of the Fire
		 */
		public int getID() {
			return this.id;
		}
		
		/**
		 * Returns the World's object of the Fire.
		 * 
		 * @return the World's object of the Fire
		 */
		public WorldObject getWorldObject() {
			return this.worldObject;
		}
		
		/**
		 * Returns the type of the World's object of the Fire.
		 * 
		 * @return the type of the World's object of the Fire
		 */
		public WorldObjectEnum getWorldObjectType() {
			return this.worldObject.getWorldObjectType();
		}
		
		/**
		 * Returns the TimeStamp of the Fire's creation.
		 * 
		 * @return the TimeStamp of the Fire's creation
		 */
		public long getCreationTimeStamp() {
			return this.creationTimeStamp;
		}
		
		/**
		 * Returns the current intensity of the Fire.
		 * 
		 * @return the current intensity of the Fire
		 */
		public int getCurrentIntensity() {
			return this.currentIntensity;
		}
		
		/**
		 * Returns the original intensity of the Fire.
		 * 
		 * @return the original intensity of the Fire
		 */
		public int getOriginalIntensity() {
			return this.originalIntensity;
		}
		
		/**
		 * Returns the probability of spreading of the Fire.
		 * 
		 * @return the probability of spreading of the Fire
		 */
		public float getSpreadProbability() {
			return this.spreadProbability;
		}
		
		/**
		 * Returns the number of spreads of the Fire.
		 * 
		 * @return the number of spreads of the Fire
		 */
		public int getNumSpreads() {
			return this.numSpreads;
		}
		
		/**
		 * Increases the number of spreads of the Fire.
		 */
		public void getIncreaseNumSpreads() {
			this.numSpreads++;
		}
		
		/**
		 * Returns the number of increases of the intensity of the Fire.
		 * 
		 * @return the number of increases of the intensity of the Fire
		 */
		public int getNumIntensityIncreases() {
			return this.numIntensityIncreases;
		}
		
		/**
		 * Increases the intensity of the Fire, given a value of increasing.
		 * 
		 * @param value value of increasing of the intensity of the Fire
		 */
		public void increaseIntensity(int value) {
			
			if((this.currentIntensity + value) > Configurations.FIRE_MAX_FINAL_INTENSITY) {
				this.currentIntensity += value;
				this.numIntensityIncreases++;
			}
		}
		
		/**
		 * Returns the number of decreases of the intensity of the Fire.
		 * 
		 * @return the number of decreases of the intensity of the Fire
		 */
		public int getNumIntensityDecreases() {
			return this.numIntensityDecreases;
		}
		
		/**
		 * Increases the intensity of the Fire, given a value of increasing.
		 * 
		 * @param value value of increasing of the intensity of the Fire
		 */
		public void decreaseIntensity(int value) {
			
			this.currentIntensity = this.currentIntensity - value;
			
			if(this.currentIntensity <= 0)
				this.active = false;
			
			this.numIntensityDecreases++;
		}
		
		/**
		 * Returns the status to inform that the Fire is currently active or not.
		 * 
		 * @return the status to inform that the Fire is currently active or not
		 */
		public boolean isActive() {
			return this.active;
		}
		
		/**
		 * Returns the status to inform that the Fire is currently attended or not.
		 * 
		 * @return the status to inform that the Fire is currently attended or not
		 */
		public boolean isAttended() {
			return this.attended;
		}

		/**
		 * Returns the current information about the Fire.
		 * 
		 * @return the current information about the Fire
		 */
		public String getFireInfo() {
			// TODO
			
			return "Fire " + this.id;
		}
		
		/**
		 * Returns the basic information to be displayed in a graphic user interface about the Fire.
		 */
		@Override
		public String toString() {
			return "F - i: " + this.currentIntensity + ";";
		}

}
