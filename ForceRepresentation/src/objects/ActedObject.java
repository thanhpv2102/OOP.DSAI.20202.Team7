package objects;

import force.*;

public abstract class ActedObject {

	private double mass;
	public GravitationalForce gravitationalForce;
	public NormalForce normalForce;
	public ActorForce actorForce;
	public FrictionalForce frictionalForce;
	public Surface surface;
	private double x;
	private double y;
	private double velocity;
	private double acceleration;

	
	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
		this.gravitationalForce = new GravitationalForce(this.x, this.y, this.mass*10);
		this.normalForce = new NormalForce(this.x,this.y,this.mass * 10);
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
		System.out.println(this.gravitationalForce.getMagnitude());
		this.normalForce = new NormalForce(this.x, this.y, this.gravitationalForce.getMagnitude());
		this.frictionalForce = new FrictionalForce(this.x, 0, 0);
		this.actorForce = actorForce;
		this.surface = surface;
	}
	
	
	public double getGravitationalForceMagnitude() {
		return this.gravitationalForce.getMagnitude();
	}
	public double  getNormalForceMagnitude() {
		return this.normalForce.getMagnitude();
	}
	public double getActorForceMagnitude() {
		return this.actorForce.getMagnitude();
	}
	public double getFrictionalForceMagnitude() {
		return this.frictionalForce.getMagnitude();
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
	
	public void setSurface(Surface surface) {
		this.surface = surface;
	}
	public void setFrictionalForce(FrictionalForce frictionalForce) {
		this.frictionalForce = frictionalForce;
	}
	
	
	
	
	
}
