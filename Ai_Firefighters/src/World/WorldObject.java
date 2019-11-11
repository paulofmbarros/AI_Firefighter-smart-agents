package World;

import java.awt.Point;

import Enums.WorldObjectEnum;


public class WorldObject {
	// Global Instance Variables:
		
		/**
		 * The type of the World's object.
		 */
		private WorldObjectEnum worldObjectEnum;
		
		/**
		 * The position/point of the World's object.
		 */
		private Point worldObjectPosition;
		
		
		// Constructors:
		
		/**
		 * The constructor #1 of the World's object.
		 * 
		 * @param worldObjectType the type of the World's object
		 * @param worldObjectPosition the position of the World's object
		 */
		public WorldObject(WorldObjectEnum worldObjectType, Point worldObjectPosition) {
			this.worldObjectEnum = worldObjectType;
			this.worldObjectPosition = worldObjectPosition;
		}

		// Methods/Functions:
		
		/**
		 * Returns the type of the World's object.
		 * 
		 * @return the type of the World's object
		 */
		public WorldObjectEnum getWorldObjectType() {
			return this.worldObjectEnum;
		}
		
		/**
		 * Returns the position/point of the World's object.
		 * 
		 * @return the position/point of the World's object
		 */
		public Point getPosition() {
			return this.worldObjectPosition;
		}

		/**
		 * Returns the coordinate X of the position/point of the World's object.
		 * 
		 * @return the coordinate X of the position/point of the World's object
		 */
		public int getPositionX() {
			return this.worldObjectPosition.x;
		}

		/**
		 * Returns the coordinate Y of the position/point of the World's object.
		 * 
		 * @return the coordinate Y of the position/point of the World's object
		 */
		public int getPositionY() {
			return this.worldObjectPosition.y;
		}

		/**
		 * Updates the both coordinates of the current position/point of the World's object.
		 * 
		 * @param x the coordinate X of the position/point of the World's object
		 * @param y the coordinate Y of the position/point of the World's object
		 */
		public void updatePosition(int x, int y) {
			this.worldObjectPosition.x = x;
			this.worldObjectPosition.y = y;
		}
}
