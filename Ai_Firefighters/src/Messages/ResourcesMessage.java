package Messages;

import java.util.ArrayList;

import Classes.FuelResource;
import Classes.WaterResource;

public class ResourcesMessage implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<WaterResource> waterResources;
	private ArrayList<FuelResource> fuelResources;
	
	public ResourcesMessage(ArrayList<WaterResource> waterResources,ArrayList<FuelResource> fuelResources) {
		this.waterResources = waterResources;
		this.fuelResources = fuelResources;
	}
	
	public ArrayList<FuelResource> getFuelResources() {
		return fuelResources;
	}
	public void setFuelResources(ArrayList<FuelResource> fuelResources) {
		this.fuelResources = fuelResources;
	}
	public ArrayList<WaterResource> getWaterResources() {
		return waterResources;
	}
	public void setWaterResources(ArrayList<WaterResource> waterResources) {
		this.waterResources = waterResources;
	}
}
