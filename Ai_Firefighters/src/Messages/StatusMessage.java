package Messages;

public class StatusMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int coordX, coordY;
	private int fireX, fireY;
	private boolean available;
	
	public StatusMessage(int x, int y, int fx, int fy, boolean available) {
		this.coordX = x;
		this.coordY = y;
		this.fireX = fx;
		this.fireY = fy;
		this.available = available;
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
	
	
}
