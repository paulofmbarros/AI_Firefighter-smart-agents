package Messages;

public class OrderMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1805288644572998364L;
	
	private int fireCoordX, fireCoordY, idVehicleResponsibleForExtinguishFire;
	private String fireId;

	
	public OrderMessage(int fx, int fy, String fireId) {
		this.fireCoordX = fx;
		this.fireCoordY = fy;
		this.setFireId(fireId);
	}
	public OrderMessage(int fx, int fy, String fireId, int _idVehicleResponsibleForExtinguishFire) {
		this.fireCoordX = fx;
		this.fireCoordY = fy;
		this.setFireId(fireId);
		this.idVehicleResponsibleForExtinguishFire=_idVehicleResponsibleForExtinguishFire;
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

	public int getIdVehicleResponsibleForExtinguishFire() {
		return idVehicleResponsibleForExtinguishFire;
	}

	public void setIdVehicleResponsibleForExtinguishFire(int idVehicleResponsibleForExtinguishFire) {
		this.idVehicleResponsibleForExtinguishFire = idVehicleResponsibleForExtinguishFire;
	
	}
}
