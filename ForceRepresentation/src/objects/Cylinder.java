package objects;

import force.*;
import java.lang.Math;

import exception.InvalidInputException;

public class Cylinder extends ActedObject {
	private double radius;
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
		}
		this.radius = radius;
		// TODO Auto-generated constructor stub
	}
	
	
	public void updateFrictionalForce() {
		double magnitude;
		if ( Math.abs(this.actorForce.getMagnitude()) <= 3 * Math.abs(this.normalForce.getMagnitude()) * this.surface.getStaticFrictionCoef() 
			&& this.getVelocity() == 0) {
			magnitude = Math.abs(this.actorForce.getMagnitude()) / 3;
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
		this.angularAcceleration = 2 * this.frictionalForce.getMagnitude() / (this.getMass() * this.radius * this.radius);
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
