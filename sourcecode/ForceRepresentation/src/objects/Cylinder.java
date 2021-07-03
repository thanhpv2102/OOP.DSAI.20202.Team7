package objects;

import force.*;
import java.lang.Math;

import exception.InvalidInputException;

public class Cylinder extends ActedObject {
	private double radius;
	private static final double MAX_RADIUS = 30.0;
	
	
	public double getRadius() {
		return radius;
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


	@Override
	public void updateAngularAcceleration() {
		// TODO Auto-generated method stub
		this.angularAcceleration = this.getAcceleration()/this.radius;

		
	}
	
}