package objects;

import exception.InvalidInputException;
import force.*;

public abstract class ActedObject {

	private double mass;
	protected Force gravitationalForce;
	protected Force normalForce;
	protected ChangeableForce actorForce;
	protected ChangeableForce frictionalForce;
	protected Surface surface;
	private double x;
	private double y;
	private double velocity;
	private final double MAX_SPEED = 60;
	private double acceleration;

	
	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	public double getVelocity() {
		return velocity;
	}
	public double getAcceleration() {
		return acceleration;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}

	
	public ActedObject(double mass, double x, double y, ChangeableForce actorForce, Surface surface) throws InvalidInputException {
		//(x,y) is the coordinates of the center of the object
		//Must specify the actor force and the surface related to the object
		super();
		if (mass <= 0) {
			throw new InvalidInputException("Mass of object must be positive");
		}
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.gravitationalForce = new Force(this.x, this.y, this.mass*10);
		this.normalForce = new Force(this.x, this.y, this.gravitationalForce.getMagnitude());
		this.frictionalForce = new ChangeableForce(this.x, 0, 0);
		this.actorForce = actorForce;
		this.surface = surface;
	}
	
	
	public double getGravitationalForceMagnitude() {
		return gravitationalForce.getMagnitude();
	}
	public double getNormalForceMagnitude() {
		return normalForce.getMagnitude();
	}
	public double getActorForceMagnitude() {
		return actorForce.getMagnitude();
	}
	public double getFrictionalForceMagnitude() {
		return frictionalForce.getMagnitude();
	}
	public double getSumForce() {
		//Base direction: Right
		return this.actorForce.getMagnitude() - this.frictionalForce.getMagnitude();
	}
	
	//If the speed of the object surpasses a threshold, then we have to stop applying force on it
	//otherwise the speed can reach infinity at some point
	public boolean validateSpeedThreshold() {
		if ( Math.abs(this.velocity) >= this.MAX_SPEED ) {
			return true;
		}
		return false;
	}
	
	//Method to update frictional force
	public abstract void updateFrictionalForce();
		
	//Method to update the acceleration
	public void updateAcceleration() {
		this.acceleration = this.getSumForce() / this.mass;
	}
	
	//Method to update velocity after a time interval deltaT
	public void updateVelocity(double deltaT) {
		this.velocity = this.velocity + this.acceleration * deltaT;
	}
	
	//Method to update position after a time interval deltaT
	public void updatePosition(double deltaT) {
		this.x = this.x + this.velocity * deltaT;
	}
	
	public void updateForcesRoots() {
		this.gravitationalForce.setRootX(this.x);
		this.normalForce.setRootX(this.x);
		this.actorForce.setRootX(this.x);
		this.frictionalForce.setRootX(this.x);
	}
	
	//Method to update the state of the object after a time interval deltaT
	public abstract void proceed(double deltaT);
	
}
