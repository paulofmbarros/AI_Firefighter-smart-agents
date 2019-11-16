package Messages;

public class StatusMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int coordX, coordY;
	private int fireX, fireY;
	private int fuelTank, waterTank;
	private String fireId;
	private boolean available;
	
	public StatusMessage(int x, int y, int fx, int fy, String fireId, boolean available, int fuelTank, int waterTank) {
		this.coordX = x;
		this.coordY = y;
		this.fireX = fx;
		this.fireY = fy;
		this.fireId = fireId;
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

	public int getFireX() {
		return fireX;
	}

	public void setFireX(int fireX) {
		this.fireX = fireX;
	}

	public int getFireY() {
		return fireY;
	}

	public void setFireY(int fireY) {
		this.fireY = fireY;
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
	
	
}
