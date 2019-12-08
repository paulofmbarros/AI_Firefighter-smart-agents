package Config;


public abstract class Configurations {
	
	public static int GRID_WIDTH = 500;

	/**
	 * Defines the World's map/grid height.
	 */
	public static int GRID_HEIGHT = 500;
	
	/**
	 * Defines the maximum number of Aircrafts that can exist in the World.
	 */
	public static int NUM_MAX_WATER_RESOUCES = 4;	
	/**
	 * Defines the maximum number of Aircrafts that can exist in the World.
	 */
	public static int NUM_MAX_AIRCRAFTS = 1;
	
	/**
	 * Defines the maximum number of Drones that can exist in the World.
	 */
	public static int NUM_MAX_DRONES = 1;
	
	/**
	 * Defines the maximum number of Fire Trucks that can exist in the World.
	 */
	public static int NUM_MAX_FIRE_TRUCKS = 1;
	
	/**
	 * Defines the default velocity (timeout in ms) in which the vehicle moves to a new coordinate
	 */
	public final static int BASE_VEHICLE_SPEED = 1000;
	
	
	/**
	 * Defines the maximum number of Fires that can occur/be presented in the World.
	 */
	public static int NUM_MAX_FIRES = 8;
	
	/**
	 * Defines the maximum water tank's capacity of an Aircraft Agent.
	 */
	public final static int AIRCRAFT_MAX_WATER_TANK_CAPACITY = 6;
	
	/**
	 * Defines the maximum water tank's capacity of an Aircraft Agent.
	 */
	public final static int AIRCRAFT_MAX_FUEL_TANK_CAPACITY = 1000;
	
	/**
	 * Defines the maximum water tank's capacity of a Drone Agent.
	 */
	public final static int DRONE_MAX_WATER_TANK_CAPACITY = 9;
	
	
	/**
	 * Defines the maximum FUEL tank's capacity of a Drone Agent.
	 */
	public final static int DRONE_MAX_FUEL_TANK_CAPACITY = 500;
	
	/**
	 * Defines the maximum water tank's capacity of a Fire Truck Agent.
	 */
	public final static int FIRE_TRUCK_MAX_WATER_TANK_CAPACITY = 9;
	
	/*
	 * Fire truck max fuel tank capacity
	 */
	public final static int FIRE_TRUCK_MAX_FUEL_TANK_CAPACITY = 1250;
	
	/*
	 * Fire truck velocity (timeout (ms) in which it moves to a new coordinate - less is faster)
	 */
	public final static int FIRE_TRUCK_VELOCITY = 1000;
	/**
	 * Defines the maximum quantity of a Water Resource.
	 */
	public final static int WATER_RESOURCE_INITIAL_MAX_CAPACITY = 8;
	/**
	 * Defines the maximum quantity of a Water Resource.
	 */
	public final static int FUEL_RESOURCE_INITIAL_MAX_CAPACITY = 8;	
	/**
	 * Defines the maximum initial intensity of a Fire.
	 */
	public final static int FIRE_MAX_INITIAL_INTENSITY = 6;
	/**
	 * Defines the maximum final intensity of a Fire.
	 */
	public final static int FIRE_MAX_FINAL_INTENSITY = 10;

	public static final int FIRE_TRUCK_SPEED_MULTIPLIER = 1;
	

	public static final float DRONE_SPEED_MULTIPLIER = (float) 0.3;

	public static final float AIRCRAFT_SPEED_MULTIPLIER = (float) 0.5;
	
	
}

