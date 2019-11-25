package Messages;

public class OrderMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1805288644572998364L;
	
	private int fireCoordX, fireCoordY;
	private String fireId;

	
	public OrderMessage(int fx, int fy, String fireId) {
		this.fireCoordX = fx;
		this.fireCoordY = fy;
		this.setFireId(fireId);
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

	public String getFireId() {
		return fireId;
	}

	public void setFireId(String fireId) {
		this.fireId = fireId;
	}

	

	
}
