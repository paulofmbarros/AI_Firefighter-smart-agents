package Messages;

public class FireMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fireCoordX, fireCoordY;
	
	public FireMessage(int x, int y) {
		this.fireCoordX = x;
		this.fireCoordY = y;
	}

	public int getFireCoordX() {
		return fireCoordX;
	}

	public void setFireCoordX(int fireCoordX) {
		this.fireCoordX = fireCoordX;
	}

	public int getFireCoordY() {
		return fireCoordY;
	}

	public void setFireCoordY(int fireCoordY) {
		this.fireCoordY = fireCoordY;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
