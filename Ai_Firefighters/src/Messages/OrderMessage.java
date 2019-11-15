package Messages;

public class OrderMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1805288644572998364L;
	
	private int fireCoordX, fireCoordY;
	
	public OrderMessage(int fx, int fy) {
		this.fireCoordX = fx;
		this.fireCoordY = fy;
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
	
	
}
