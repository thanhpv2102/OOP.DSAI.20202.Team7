package main;

import force.ChangeableForce;
import objects.*;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MyController implements Initializable {

	private ChangeableForce force = new ChangeableForce(0, 0, 0);
	private Surface surface = new Surface(0.25, 0.25);
	private final LongProperty lastUpdateAnimation = new SimpleLongProperty(0);
	private Cube cube;
	private Cylinder cylinder;

	private double mass;
	private double sideLength;

	private boolean cubeChosen = false;
	private boolean cylinderChosen = false; 
	@FXML
	private Slider staticSlider;

	@FXML
	private Slider kineticSlider;

	private int BACKGROUND_WIDTH = 1653;

	@FXML
	private Slider forceSlider;

	@FXML
	private ImageView bg1;

	@FXML
	private ImageView bg2;

	@FXML
	private ImageView ls1;

	@FXML
	private ImageView ls2;

	@FXML
	private Rectangle bg_cube;

	@FXML
	private Circle bg_cylinder;

	@FXML
	private RadioButton radio_cube;

	@FXML
	private RadioButton radio_cylinder;

	ToggleGroup group = new ToggleGroup();

	@FXML
	private TextField input_mass;

	@FXML
	private TextField input_radius;

	@FXML
	private TextField input_sizeLength;

	@FXML
	private Text txt_radius;

	@FXML
	private Text txt_sizeLength;

	@FXML
	private Line horizontal_line;

	@FXML
	private Line vertical_line;

	@FXML
	private TextArea sumForcesDisplay;

	@FXML
	private TextArea gravitationalForceDisplay;

	@FXML
	private TextArea normalForceDisplay;

	@FXML
	private TextArea actorForceDisplay;

	@FXML
	private TextArea frictionalForceDisplay;

	@FXML
	private TextField massDisplay;

	@FXML
	private TextField speedDisplay;

	@FXML
	private  TextField accelerationDisplay;

	@FXML
	private CheckBox forcesBox;

	@FXML
	private  CheckBox sumForcesBox;

	@FXML
	private CheckBox valuesBox;

	@FXML
	private CheckBox massBox;

	@FXML
	private CheckBox speedBox;

	@FXML
	private  CheckBox accelerationBox;

	@FXML 
	private Rectangle choiceCube;

	@FXML 
	private Circle choiceCylinder;
	
	@FXML 
	private SVGPath actorArrow;
	
	@FXML 
	private SVGPath normalArrow;
	
	@FXML 
	private SVGPath sumForceArrow;
	
	@FXML 
	private SVGPath gravitationalArrow;
	
	@FXML 
	private SVGPath frictionalArrow;
	
	@FXML 
	private Label actorForceLabel;
	
	@FXML 
	private Label normalForceLabel;
	
	@FXML 
	private Label gravitationalForceLabel;
	
	@FXML 
	private Label frictionalForceLabel;
	
	@FXML 
	private Label sumForceLabel;
	
	@FXML
	private Button pauseButton;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		actorArrow.setVisible(false);
		normalArrow.setVisible(false);
		gravitationalArrow.setVisible(false);
		frictionalArrow.setVisible(false);		
		sumForceArrow.setVisible(false);

		actorForceLabel.setVisible(false);
		normalForceLabel.setVisible(false);
		gravitationalForceLabel.setVisible(false);
		frictionalForceLabel.setVisible(false);
		sumForceLabel.setVisible(false);

		cube = new Cube(Double.parseDouble(input_mass.getText()), Double.parseDouble(input_sizeLength.getText()), force, surface);
		cylinder = new Cylinder(Double.parseDouble(input_mass.getText()), Double.parseDouble(input_radius.getText()), force, surface);

		//Alter static friction coef with staticSlider
		massDisplay.setVisible(false);
		bg_cube.setVisible(false);
		bg_cylinder.setVisible(false);
		staticSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (staticSlider.getValue() >= kineticSlider.getValue()) {
					staticSlider.adjustValue(kineticSlider.getValue());
				}
				surface.setStaticFrictionCoef((double) staticSlider.getValue());
				//			System.out.println("static:" + surface.getStaticFrictionCoef());
			}
		});

		//Alter kinetic friction coef with kineticSlider
		kineticSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				if (kineticSlider.getValue() <= staticSlider.getValue()) {
					kineticSlider.adjustValue(staticSlider.getValue());
				}
				surface.setKineticFrictionCoef((double) kineticSlider.getValue());
				//			System.out.println("kinetic:" + surface.getKineticFrictionCoef());
			}

		});

		//force slider
		forceSlider.valueProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				force.setMagnitude(forceSlider.getValue());
				AnimationTimer parallaxAnimation = new ParallaxAnimation();
				parallaxAnimation.start();
					
			}
		});

	}

	public void submitMass(ActionEvent event) {
		try {
			mass = Double.parseDouble(input_mass.getText());
		} catch(Exception e) {
			input_mass.setText("Please enter a number for the mass!");
		}
	}
	
	public void pauseHandle(ActionEvent event) {
		if (pauseButton.getText().equals("Pause")) {
			pauseButton.setText("Resume");
		} else if (pauseButton.getText().equals("Resume")) {
			pauseButton.setText("Pause");

		}

	}

	private class ParallaxAnimation extends AnimationTimer {
		@Override
		public void handle(long now) {
			if (pauseButton.getText().equals("Pause")) {
				if (lastUpdateAnimation.get() > 0) {
					long elastedNanoSecond = now - lastUpdateAnimation.get();
					// to do
					if (bg_cube.isVisible()) {
						display(cube);
						updateTransition(cube, elastedNanoSecond);
					} else if (bg_cylinder.isVisible()) {
						display(cylinder);
						updateTransition(cylinder, elastedNanoSecond);
					}
				}
				lastUpdateAnimation.set(now);
			}
			
			
		}
	}

	public void cubeDragged(MouseEvent event) {
		choiceCube.setTranslateX(event.getX() + choiceCube.getTranslateX() );
		choiceCube.setTranslateY(event.getY() + choiceCube.getTranslateY() );
	}

	public void cubeReleased(MouseEvent event) {
		if ( choiceCube.getTranslateY() < ( 400 - choiceCube.getLayoutY()) )   {
			cylinderChosen = false;
			cubeChosen = true;
			bg_cylinder.setVisible(false);
			horizontal_line.setVisible(false);
			vertical_line.setVisible(false);
			bg_cube.setVisible(true);
			choiceCube.setTranslateX(0);
			choiceCube.setTranslateY(0);
			choiceCube.setVisible(false);
		} else {
			choiceCube.setTranslateX(0);
			choiceCube.setTranslateY(0);
		}	
		if (cubeChosen == true) {
			choiceCylinder.setVisible( true);
			choiceCube.setVisible(false);
		}
		if (cylinderChosen == true) {
			choiceCube.setVisible( true);
			choiceCylinder.setVisible( false);
		}
	}

	public void cylinderDragged(MouseEvent event) {
		choiceCylinder.setTranslateX(event.getX() + choiceCylinder.getTranslateX() );
		choiceCylinder.setTranslateY(event.getY() + choiceCylinder.getTranslateY() );
	}

	public void cylinderReleased(MouseEvent event) {
		if ( choiceCylinder.getTranslateY() < ( 400 - choiceCylinder.getLayoutY()) )   {
			cylinderChosen = true;
			cubeChosen = false;	
			bg_cube.setVisible(false);
			bg_cylinder.setVisible(true);
			horizontal_line.setVisible(true);
			vertical_line.setVisible(true);
		}
		choiceCylinder.setTranslateX(0);
		choiceCylinder.setTranslateY(0);
		if (cubeChosen == true) {
			choiceCylinder.setVisible( true);
			choiceCube.setVisible(false);
		}
		if (cylinderChosen == true) {
			choiceCube.setVisible( true);
			choiceCylinder.setVisible( false);
		}
	}



	public void display(ActedObject object) {
		//Phần lực phải hiện thị bằng mũi tên, nên sẽ thay thế sau

		if (valuesBox.isSelected() == true) {
			gravitationalForceDisplay.setVisible(true);
			normalForceDisplay.setVisible(true);
			frictionalForceDisplay.setVisible(true);
			actorForceDisplay.setVisible(true);
			
			
			actorArrow.setVisible(true);
			normalArrow.setVisible(true);
			gravitationalArrow.setVisible(true);
			frictionalArrow.setVisible(true);		
			sumForceArrow.setVisible(true);

			actorForceLabel.setVisible(true);
			normalForceLabel.setVisible(true);
			gravitationalForceLabel.setVisible(true);
			frictionalForceLabel.setVisible(true);
			sumForceLabel.setVisible(true);
			
			actorArrow.setScaleX(object.getActorForceMagnitude() / actorArrow.boundsInLocalProperty().get().getWidth());
			//actorArrow.setTranslateX(actorArrow.getTranslateX());
			String roundedActor = String.format("%.2f", Math.abs(object.getActorForceMagnitude()) );
			actorForceLabel.setText(roundedActor + " N");
			
			normalArrow.setScaleX(object.getNormalForceMagnitude() / normalArrow.boundsInLocalProperty().get().getWidth());
			//normalArrow.setTranslateX(normalArrow.getTranslateX());
			String roundedNormal = String.format("%.2f", Math.abs(object.getNormalForceMagnitude()));
			normalForceLabel.setText(roundedNormal + " N");
			
			
			frictionalArrow.setScaleX(object.getFrictionalForceMagnitude() / frictionalArrow.boundsInLocalProperty().get().getWidth());
		    //frictionalArrow.setTranslateX(frictionalArrow.getTranslateX());
			String roundedFriction = String.format("%.2f", object.getFrictionalForceMagnitude());
			frictionalForceLabel.setText(roundedFriction + " N");
			
			gravitationalArrow.setScaleX(object.getNormalForceMagnitude() / gravitationalArrow.boundsInLocalProperty().get().getWidth());
			//gravitationalArrow.setTranslateX(gravitationalArrow.getTranslateX());
			String roundedGravitational = String.format("%.2f", Math.abs( object.getGravitationalForceMagnitude() ));
			gravitationalForceLabel.setText(roundedGravitational + " N");
			
			sumForceArrow.setScaleX(object.getSumForce() / sumForceArrow.boundsInLocalProperty().get().getWidth());
			//gravitationalArrow.setTranslateX(gravitationalArrow.getTranslateX());
			String roundedSum = String.format("%.2f", Math.abs(object.getSumForce()));
			sumForceLabel.setText(roundedSum + " N");
		


			gravitationalForceDisplay.setText(Double.toString( object.getGravitationalForceMagnitude() ) + " N");
			normalForceDisplay.setText(Double.toString( object.getNormalForceMagnitude() ) + " N");
			frictionalForceDisplay.setText(Double.toString( object.getFrictionalForceMagnitude() ) + " N");

			String rounded = String.format("%.2f", object.getActorForceMagnitude());
			actorForceDisplay.setText(rounded + " N");
		} else {
			gravitationalForceDisplay.setVisible(false);
			normalForceDisplay.setVisible(false);
			frictionalForceDisplay.setVisible(false);
			actorForceDisplay.setVisible(false);
		}

		if (sumForcesBox.isSelected() == true) {
			sumForcesDisplay.setVisible(true);
			sumForcesDisplay.setText(Double.toString(object.getSumForce()) + " N");
		} else {
			sumForcesDisplay.setVisible(false);
		}

		if (massBox.isSelected() == true) {
			massDisplay.setVisible(true);
			String rounded = String.format("%.2f", object.getMass());
			massDisplay.setText(rounded + " Kg");
		} else {
			massDisplay.setVisible(false);

		}
		if (speedBox.isSelected() == true) {
			speedDisplay.setVisible(true);
			String rounded = String.format("%.2f", object.getVelocity());
			speedDisplay.setText(rounded + " m/s");
		} else {
			speedDisplay.setVisible(false);

		}
		if (accelerationBox.isSelected() == true) {
			accelerationDisplay.setVisible(true);
			String rounded = String.format("%.2f", object.getAcceleration());
			accelerationDisplay.setText(rounded + " m/s\u00b2");

		} else {
			accelerationDisplay.setVisible(false);
		}
	}

	public void updateTransition(ActedObject obj, long elastedNanoSecond) {
		double elastedSecond = elastedNanoSecond  / 1_000_000_000.0;
		double old_x;
		if (obj instanceof Cube) {
			old_x = cube.getX();
			cube.proceed(elastedSecond);

			//reset back to original position to prevent backgrounds run out of scene
			//multiply by 50 to make the animation looks faster
			if (ls1.getX() - (cube.getX() - old_x)*50 < -BACKGROUND_WIDTH) {
				ls1.setX(0);
				ls2.setX(0);
			} else if (ls1.getX() - (cube.getX() - old_x)*40 > 0) {
				ls1.setX(-BACKGROUND_WIDTH);
				ls2.setX(-BACKGROUND_WIDTH);
			} else {
				ls1.setX(ls1.getX() - (cube.getX() - old_x)*40);
				ls2.setX(ls2.getX() - (cube.getX() - old_x)*40);
			}
			if (bg1.getX() - (cube.getX() - old_x)*10 < -BACKGROUND_WIDTH) {
				bg1.setX(0);
				bg2.setX(0);
			} else if (bg1.getX() - (cube.getX() - old_x)*10 > 0) {
				bg1.setX(-BACKGROUND_WIDTH);
				bg2.setX(-BACKGROUND_WIDTH);
			} else {
				bg1.setX(bg1.getX() - (cube.getX() - old_x)*10);
				bg2.setX(bg2.getX() - (cube.getX() - old_x)*10);
			}
		} else {
			old_x = cylinder.getX();
			cylinder.proceed(elastedSecond);
			if (ls1.getX() - (cylinder.getX() - old_x)*40 < -BACKGROUND_WIDTH) {
				ls1.setX(0);
				ls2.setX(0);
			} else if (ls1.getX() - (cylinder.getX() - old_x)*40 > 0) {
				ls1.setX(-BACKGROUND_WIDTH);
				ls2.setX(-BACKGROUND_WIDTH);
			} else {
				ls1.setX(ls1.getX() - (cylinder.getX() - old_x)*40);
				ls2.setX(ls2.getX() - (cylinder.getX() - old_x)*40);
			}
			if (bg1.getX() - (cylinder.getX() - old_x)*10 < -BACKGROUND_WIDTH) {
				bg1.setX(0);
				bg2.setX(0);
			} else if (bg1.getX() - (cylinder.getX() - old_x)*10 > 0) {
				bg1.setX(-BACKGROUND_WIDTH);
				bg2.setX(-BACKGROUND_WIDTH);
			} else {
				bg1.setX(bg1.getX() - (cylinder.getX() - old_x)*10);
				bg2.setX(bg2.getX() - (cylinder.getX() - old_x)*10);
			}
			//multiply by 25 because the rotation is faster than transition
			horizontal_line.setRotate(horizontal_line.getRotate() + (cylinder.getX() - old_x)*20);
			vertical_line.setRotate(vertical_line.getRotate() + (cylinder.getX() - old_x)*20);
		}
	}
}