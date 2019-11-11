package Config;

public abstract class Configurations {
	
	public static int GRID_WIDTH = 7;

	/**
	 * Defines the World's map/grid height.
	 */
	public static int GRID_HEIGHT = 8;
	
	/**
	 * Defines the maximum number of Water Resources that can exist in the World.
	 */
	public static int NUM_MAX_WATER_RESOURCES = 3;
	
	/**
	 * The complete class name of the Aircraft Agent's instance.
	 * e.g. some.package.object-class-name.
	 */
	public static final String INSTANCE_OF_AIRCRAFT_CLASS_NAME = "agents.vehicles.aircraft.AircraftAgent";
	
	/**
	 * The complete class name of the Drone Agent's instance.
	 * e.g. some.package.object-class-name.
	 */
	public static final String INSTANCE_OF_DRONE_CLASS_NAME = "agents.vehicles.drone.DroneAgent";
	
	/**
	 * The complete class name of the Fire Truck Agent's instance.
	 * e.g. some.package.object-class-name.
	 */
	public static final String INSTANCE_OF_FIRE_TRUCK_CLASS_NAME = "agents.vehicles.firetruck.FireTruckAgent";
	
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
	 * Defines the maximum number of Aircrafts that can exist in the World.
	 */
	public static int NUM_MAX_VEHICLES = NUM_MAX_AIRCRAFTS + NUM_MAX_DRONES + NUM_MAX_FIRE_TRUCKS;
	
	/**
	 * Defines the maximum number of Fires that can occur/be presented in the World.
	 */
	public static int NUM_MAX_FIRES = 8;
	
	/**
	 * Defines the maximum water tank's capacity of an Aircraft Agent.
	 */
	public final static int AIRCRAFT_MAX_WATER_TANK_CAPACITY = 6;
	
	/**
	 * Defines the maximum water tank's capacity of a Drone Agent.
	 */
	public final static int DRONE_MAX_WATER_TANK_CAPACITY = 3;
	
	/**
	 * Defines the maximum water tank's capacity of a Fire Truck Agent.
	 */
	public final static int FIRE_TRUCK_MAX_WATER_TANK_CAPACITY = 9;
	
	/**
	 * Defines the initial maximum fuel tank's capacity of an Aircraft Agent.
	 */
	public final static int AIRCRAFT_MAX_INITIAL_FUEL_TANK_CAPACITY = (int) (3 * Math.round((2 * (Math.sqrt( (GRID_WIDTH ^ 2) + (GRID_HEIGHT ^ 2) )))));
	
	/**
	 * Defines the initial maximum fuel tank's capacity of a Drone Agent.
	 */
	public final static int DRONE_MAX_INITIAL_FUEL_TANK_CAPACITY = (int) Math.round((2 * (Math.sqrt( (GRID_WIDTH ^ 2) + (GRID_HEIGHT ^ 2) ))));
	
	/**
	 * Defines the initial maximum fuel tank's capacity of a Fire Truck Agent.
	 */
	public final static int FIRE_TRUCK_MAX_INITIAL_FUEL_TANK_CAPACITY = (int) (2 * Math.round((2 * (Math.sqrt( (GRID_WIDTH ^ 2) + (GRID_HEIGHT ^ 2) )))));
	
	/**
	 * Defines the final maximum fuel tank's capacity of an Aircraft Agent.
	 */
	public final static int AIRCRAFT_MAX_FINAL_FUEL_TANK_CAPACITY = GRID_WIDTH * GRID_HEIGHT;
	
	/**
	 * Defines the final maximum fuel tank's capacity of a Drone Agent.
	 */
	public final static int DRONE_MAX_FINAL_FUEL_TANK_CAPACITY = (int) (2 * Math.round((2 * (Math.sqrt( (GRID_WIDTH ^ 2) + (GRID_HEIGHT ^ 2) )))));
	
	/**
	 * Defines the final maximum fuel tank's capacity of a Fire Truck Agent.
	 */
	public final static int FIRE_TRUCK_MAX_FINAL_FUEL_TANK_CAPACITY = 2 * (GRID_WIDTH * GRID_HEIGHT);
	
	/**
	 * Defines the maximum quantity of a Water Resource.
	 */
	public final static int WATER_RESOURCE_INITIAL_MAX_CAPACITY = 8;
	
	/**
	 * Defines the total number of Weather Seasons.
	 */
	public final static int NUM_WEATHER_SEASONS = 4;
	
	/**
	 * Defines the factor value of water provided by rain to the Water Resources in Spring Season,
	 * to calculate the amount of rain.
	 */
	public final static int RAIN_FACTOR_SPRING_SEASON = 4;
	
	/**
	 * Defines the factor value of water provided by rain to the Water Resources in Summer Season,
	 * to calculate the amount of rain.
	 */
	public final static int RAIN_FACTOR_SUMMER_SEASON = 2;
	
	/**
	 * Defines the factor value of water provided by rain to the Water Resources in Autumn Season,
	 * to calculate the amount of rain.
	 */
	public final static int RAIN_FACTOR_AUTUMN_SEASON = 4;
	
	/**
	 * Defines the factor value of water provided by rain to the water resources in Winter Season,
	 * to calculate the amount of rain.
	 */
	public final static int RAIN_FACTOR_WINTER_SEASON = 8;
	
	/**
	 * Defines the total number of types of wind.
	 */
	public final static int NUM_TYPE_WINDS = 4;
	
	/**
	 * Defines the penalty time that will affect the movement time of all the aerial Agents (Aircrafts/Drones),
	 * in environments that have no wind.
	 */
	public final static long AIRCRAFT_DRONE_MOVEMENT_PENALTY_TIME_NO_WIND = 0L;

	/**
	 * Defines the penalty time that will affect the movement time of all the aerial Agents (Aircrafts/Drones),
	 * in environments that have weak wind.
	 */
	public final static long AIRCRAFT_DRONE_MOVEMENT_PENALTY_TIME_WEAK_WIND = 100;

	/**
	 * Defines the penalty time that will affect the movement time of all the aerial Agents (Aircrafts/Drones),
	 * in environments that have normal wind.
	 */
	public final static long AIRCRAFT_DRONE_MOVEMENT_PENALTY_TIME_NORMAL_WIND = 250;

	/**
	 * Defines the penalty time that will affect the movement time of all the aerial Agents (Aircrafts/Drones),
	 * in environments that have strong wind.
	 */
	public final static long AIRCRAFT_DRONE_MOVEMENT_PENALTY_TIME_STRONG_WIND = 600;

	/**
	 * Defines the penalty value of Water Resources, during drought situations, when it happens.
	 */
	public final static int DROUGHT_SITUATION_PENALTY = 6;
	
	/**
	 * Defines the maximum initial intensity of a Fire.
	 */
	public final static int FIRE_MAX_INITIAL_INTENSITY = 6;
	
	/**
	 * Defines the maximum final intensity of a Fire.
	 */
	public final static int FIRE_MAX_FINAL_INTENSITY = 10;
	
	/**
	 * Defines the a factor value to be used to calculate a new timeout,
	 * to verifies if the Fire increases its intensity or not,
	 * verifying if the current time it's greater than the value of timeout
	 * for each number of intensity increases
	 * (30 seconds of timeout for each number of intensity increases).
	 */
	public final static long FIRE_ACTIVE_FACTOR_TIMEOUT = 30000;
	
	/**
	 * Defines the maximum fire's intensity penalty, for every time that it's active
	 * for more time than the current timeout
	 * (30 seconds of timeout for each number of intensity increases)
	 */
	public final static int FIRE_ACTIVE_INTENSITY_MAX_PENALTY = 4;
	
	/**
	 * Defines a detected Fire alarm's ontology to be used in Alarm Detected Fire ACL Messages.
	 */
	public final static String DETECTED_FIRE_ALARM_ONTOLOGY = "Detected Fire Alarm";
}

