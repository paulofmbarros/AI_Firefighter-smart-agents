package Classes;

import java.util.Random;

import World.WorldObject;
import Config.Configurations;;


/**
 * The class responsible for a Water Resource.
 */
public class WaterResource implements java.io.Serializable{
	
	// Global Instance Variables:
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The ID of the Water Resource.
	 */
	private byte id;
	
	/**
	 * The World's object of the Water Resource.
	 */
	private WorldObject worldObject;
	
	/**
	 * The Water Quantity currently contained in the Water Resource.
	 */
	private int waterQuantity;
	
	
	// Constructors:
	
	/**
	 * The constructor #1 of the Water Resource.
	 * 
	 * @param id the ID of the Water Resource
	 * @param worldObject the World's object associated to the Water Resource
	 */
	public WaterResource(byte id, WorldObject worldObject) {
		Random random = new Random();
		
		this.id = id;
		this.worldObject = worldObject;
		this.waterQuantity = random.nextInt(Configurations.WATER_RESOURCE_INITIAL_MAX_CAPACITY) + 1;
	}
	
	
	// Methods/Functions:
	
	/**
	 * Returns the ID of the Water Resource.
	 * 
	 * @return the ID of the Water Resource
	 */
	public byte getID() {
		return this.id;
	}
	
	/**
	 * Returns the World's object of the Water Resource.
	 * 
	 * @return the World's object of the Water Resource
	 */
	public WorldObject getWorldObject() {
		return this.worldObject;
	}
	
	/**
	 * Returns the water quantity of the Water Resource.
	 * 
	 * @return the water quantity of the Water Resource
	 */
	public int getWaterQuantity() {
		return this.waterQuantity;
	}
	
	/**
	 * Decreases the current water quantity of the Water Resource, given a value of water quantity.
	 * 
	 * @param value a value of water quantity to decrease in the Water Resource
	 */
	public void decreaseQuantity(int value) {
		
		if((this.waterQuantity - value) < 0) {
			this.waterQuantity = 0;
		}
		
		this.waterQuantity -= value;
	}
	
	/**
	 * Increases the current water quantity of the Water Resource, given a value of water quantity.
	 * 
	 * @param value a value of water quantity to increase in the Water Resource
	 */
	public void increaseQuantity(int value) {
		this.waterQuantity += value;
	}
	
	/**
	 * Returns the basic information to be displayed in a graphic user interface about the Water Resource.
	 */
	@Override
	public String toString() {
		return "WR - wq: " + this.waterQuantity + ";";
	}
}
