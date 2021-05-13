package objects;

import force.*;

public abstract class ActedObject {

	private double mass;
	protected GravitationalForce gravitationalForce;
	protected NormalForce normalForce;
	protected ActorForce actorForce;
	protected FrictionalForce frictionalForce;
	protected Surface surface;
	private double x;
	private double y;
	private double velocity;
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

	
	public ActedObject(double mass, double x, double y, ActorForce actorForce, Surface surface) {
		//(x,y) is the coordinates of the center of the object
		//Must specify the actor force and the surface related to the object
		super();
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.gravitationalForce = new GravitationalForce(this.x, this.y, this.mass*10);
		this.normalForce = new NormalForce(this.x, this.y, this.gravitationalForce.getMagnitude());
		this.frictionalForce = new FrictionalForce(this.x, 0, 0);
		this.actorForce = actorForce;
		this.surface = surface;
	}
	
	
	public double getSumForce() {
		//Base direction: Right
		return this.actorForce.getMagnitude() - this.frictionalForce.getMagnitude();
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
