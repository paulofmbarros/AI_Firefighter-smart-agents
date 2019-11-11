package Enums;

import Config.Configurations;

public enum WeatherSeasonEnum {
	// Enumeration definitions:
	
	/**
	 * The possible enumerations and their parameters.
	 */
	SPRING((byte) 0, "Spring", Configurations.RAIN_FACTOR_SPRING_SEASON, false),
	SUMMER((byte) 1, "Summer", Configurations.RAIN_FACTOR_SUMMER_SEASON, true),
	AUTUMN((byte) 2, "Autumn", Configurations.RAIN_FACTOR_AUTUMN_SEASON, false),
	WINTER((byte) 3, "Winter", Configurations.RAIN_FACTOR_WINTER_SEASON, false);

	
	// Global Instance Variables:
	
	/*
	 * The ID of the type of the Weather Season.
	 */
    private final byte id;
    
    /**
     * The name of the type of the Weather Season.
     */
    private final String name;
    
    /**
     * The rain factor associated to the type of the Weather Season.
     */
    private final int rainFactor;

    /**
     * The boolean value that keeps the information about the possibility of 
     * droughts (extreme dry situations) occurrence, associated to the type of Weather Season.
     */
    private final boolean occurrenceOfDroughts;
	    
  
    // Constructors:
	    
    /**
     * The constructor #1 of the type of the Weather Season.
     * 
     * @param id the ID of the type of the Weather Season
     * @param name the name of the type of the Weather Season
     * @param rainFactor the rain factor associated to the type of the Weather Season
     * @param occurrenceOfDroughts the boolean value that keeps the information about the possibility of 
     * 		  droughts (extreme dry situations) occurrence, associated to the type of the Weather Season
     */
    private WeatherSeasonEnum(byte id, String name, int rainFactor, boolean occurrenceOfDroughts) {
        this.id = id;
        this.name = name;
        this.rainFactor = rainFactor;
        this.occurrenceOfDroughts = occurrenceOfDroughts;
    }
    
	        
    // Methods/Functions:
	    
    /**
     * Returns the ID of the type of the Weather Season.
     * 
     * @return the ID of the type of the Weather Season
     */
    public byte getID() {
    	return this.id;
    }

    /**
     * Returns the name of the type of the Weather Season.
     * 
     * @return the name of the type of the Weather Season
     */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the rain factor associated to the type of the Weather Season.
	 * 
	 * @return the rain factor associated to the type of the Weather Season
	 */
	public int getRainFactor() {
		return this.rainFactor;
	}

	/**
	 * Returns the boolean value that keeps the information about the possibility of 
     * droughts (extreme dry situations) occurrence, associated to the type of the
     * Weather Season.
	 * 
	 * @return the boolean value that keeps the information about the possibility of 
     * 		   droughts (extreme dry situations) occurrence, associated to the type of the
     * 		   Weather Season
	 */
	public boolean canOccurDroughts() {
		return this.occurrenceOfDroughts;
	}
}
