package Messages;

import jade.core.AID;

public class StatusMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int coordX, coordY;
	private int fuelTank, waterTank;
	private String fireId;
	private AID vehicleName;
	private boolean available;
	
	public StatusMessage(int x, int y, String fireId, AID vehicleName, boolean available, int fuelTank, int waterTank) {
		this.coordX = x;
		this.coordY = y;
		this.fireId = fireId;
		this.vehicleName = vehicleName;
		this.available = available;
		this.setFuelTank(fuelTank);
		this.setWaterTank(waterTank);
	}

	public int getCoordX() {
		return coordX;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFireId() {
		return fireId;
	}

	public int getFuelTank() {
		return fuelTank;
	}

	public void setFuelTank(int fuelTank) {
		this.fuelTank = fuelTank;
	}

	public int getWaterTank() {
		return waterTank;
	}

	public void setWaterTank(int waterTank) {
		this.waterTank = waterTank;
	}

	public AID getVehicleName() {
		return vehicleName;
	}
	
	
}
