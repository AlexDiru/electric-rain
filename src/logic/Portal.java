package logic;

import java.awt.Point;

/**
 * Entrance to a map
 * 
 * @author Alex
 * 
 */
public class Portal {

	/**
	 * The tile position of the portal on the initial map
	 */
	private Point position;

	/**
	 * The new tile position of the player on the target map
	 */
	private Point targetPosition;

	/**
	 * The index of the target map
	 */
	private int targetMapId;

	/**
	 * Creates a portal to another map
	 * 
	 * @param position
	 *            The position of the portal to walk into
	 * @param targetMapId
	 *            The target map id to move to
	 * @param targetPosition
	 *            The position on the new map to position the player
	 */
	public Portal(Point position, int targetMapId, Point targetPosition) {
		this.position = position;
		this.targetPosition = targetPosition;
		this.targetMapId = targetMapId;
	}

	/**
	 * Creates a portal to the same map
	 * 
	 * @param position
	 *            The position of the portal to walk into
	 * @param targetPosition
	 *            The position on the same map to move the player to
	 */
	public Portal(Point position, Point targetPosition) {
		this.position = position;
		this.targetPosition = targetPosition;
		targetMapId = -1;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Point getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(Point targetPosition) {
		this.targetPosition = targetPosition;
	}

	public int getTargetMapId() {
		return targetMapId;
	}

	public void setTargetMapId(int targetMapId) {
		this.targetMapId = targetMapId;
	}
}
