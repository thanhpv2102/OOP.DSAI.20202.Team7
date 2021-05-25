package objects;

import force.*;
import java.lang.Math;

import exception.InvalidInputException;

public class Cylinder extends ActedObject {
	private double radius;
	private static final double MAX_RADIUS = 30.0;
	//Base direction: clock-wise
	private double angularPosition;
	private double angularVelocity;
	private double angularAcceleration;
	
	
	public double getRadius() {
		return radius;
	}

	public double getAngularPosition() {
		return angularPosition;
	}

	public double getAngularVelocity() {
		return angularVelocity;
	}

	public double getAngularAcceleration() {
		return angularAcceleration;
	}
	
	public Cylinder(double mass, double radius, ChangeableForce actorForce, Surface surface) throws InvalidInputException {
		super(mass, 0, radius, actorForce, surface);
		if (radius <= 0) {
			throw new InvalidInputException("Radius of cylinder must be positive");
		} else if (radius > MAX_RADIUS) {
			throw new InvalidInputException("Maximum radius is 30m");
		}
		this.radius = radius;
		// TODO Auto-generated constructor stub
	}
	
	//Assume rolling without slipping
	public void updateFrictionalForce() {
		double magnitude;
		if ( Math.abs(this.actorForce.getMagnitude()) <= Math.abs(this.normalForce.getMagnitude()) * this.surface.getStaticFrictionCoef() 
			&& Math.abs(this.getVelocity()) <= 0.01) {
			magnitude = Math.abs(this.actorForce.getMagnitude());
		} else {
			magnitude = Math.abs(this.normalForce.getMagnitude() * this.surface.getKineticFrictionCoef());
		}
		
		if (this.getVelocity() >= 0) {
			this.frictionalForce.setMagnitude(magnitude);
		} else {
			this.frictionalForce.setMagnitude(-magnitude);
		}
	}
	
	
	//Update angular acceleration
	public void updateAngularAcceleration() {
		this.angularAcceleration = this.getAcceleration()/this.radius;
	}
	
	public void updateAngularVelocity(double deltaT) {
		this.angularVelocity = this.angularVelocity + this.angularAcceleration * deltaT;
	}
	
	public void updateAngularPosition(double deltaT) {
		this.angularPosition = this.angularPosition + this.angularVelocity * deltaT;
	}
	
	public void proceed(double deltaT) {
		this.validateSpeedThreshold();
		this.updateFrictionalForce();
		this.updateAcceleration();
		this.updateAngularAcceleration();
		this.updatePosition(deltaT);
		this.updateAngularPosition(deltaT);
		this.updateVelocity(deltaT);
		this.updateAngularVelocity(deltaT);
		this.updateForcesRoots();
	}
	
}