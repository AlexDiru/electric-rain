package statistics;

import entity.IEntity;

/**
 * Stores all of the data for an attack being used in battle
 * @author Alex
 *
 */
public class AttackAction {
	/**
	 * The recipient IEntity
	 */
	private IEntity recipient;
	
	/**
	 * The attack being used
	 */
	private Attack attack;
	
	private IEntity user;
	
	public AttackAction(IEntity user,IEntity recipient, Attack attack) {
		super();
		this.recipient = recipient;
		this.attack = attack;
		this.user = user;
	}

	public IEntity getRecipient() {
		return recipient;
	}

	public Attack getAttack() {
		return attack;
	}
	
	public IEntity getUser() {
		return user;
	}
}
