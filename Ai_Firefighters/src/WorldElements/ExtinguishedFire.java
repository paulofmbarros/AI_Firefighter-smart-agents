package WorldElements;


public class ExtinguishedFire {
	// Global Instance Variables:
	
		/**
		 * The Fire Extinguished.
		 */
		private Fire extinguishedFire;
		
		/**
		 * The TimeStamp of the extinguish of the Fire.
		 */
		private long extinguishedFireTimeStamp;
		
		/**
		 * The ID of the Vehicle responsible for the extinguish of the Fire.
		 */
		private int idVehicleResponsibleForExtinguishFire;
		
		
		// Constructors:
		
		/**
		 * The constructor #1 of the Extinguished Fire.
		 * 
		 * @param fireExtinguished the Fire that was extinguished
		 * @param idVehicleResponsibleForExtinguishFire the ID of the vehicle responsible for the extinguish of the Fire
		 */
		public ExtinguishedFire(Fire extinguishedFire, int idVehicleResponsibleForExtinguishFire) {
			this.extinguishedFire = extinguishedFire;
			this.extinguishedFireTimeStamp = System.currentTimeMillis();
			this.idVehicleResponsibleForExtinguishFire = idVehicleResponsibleForExtinguishFire;
		}
		
		// Methods/Functions:
		
		/**
		 * Returns the Fire Extinguished.
		 * 
		 * @return the Fire Extinguished
		 */
		public Fire getExtinguishedFire() {
			return this.extinguishedFire;
		}
		
		/**
		 * Returns the TimeStamp of the extinguish of the Fire.
		 * 
		 * @return the TimeStamp of the extinguish of the Fire
		 */
		public long getExtinguishedFireTimeStamp() {
			return this.extinguishedFireTimeStamp;
		}
		
		/**
		 * Returns the ID of the Vehicle responsible for the extinguish of the Fire.
		 * 
		 * @return the ID of the Vehicle responsible for the extinguish of the Fire
		 */
		public int getIdVehicleResponsibleForExtinguishFire() {
			return this.idVehicleResponsibleForExtinguishFire;
		
}
}
