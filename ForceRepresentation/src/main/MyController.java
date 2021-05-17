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


	@Override
	public void initialize(URL location, ResourceBundle resources) {

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


		//initialize toggle group
		/* radio_cube.setToggleGroup(group);
		radio_cylinder.setToggleGroup(group);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				RadioButton button = (RadioButton) group.getSelectedToggle();
				if (button.getText().equals("Cube")) {
					input_radius.setVisible(false);
					input_sizeLength.setVisible(true);
					txt_radius.setVisible(false);
					txt_sizeLength.setVisible(true);
					bg_cylinder.setVisible(false);
					bg_cube.setVisible(true);
					horizontal_line.setVisible(false);
					vertical_line.setVisible(false);
				} else if (button.getText().equals("Cyclinder")) {
					input_radius.setVisible(true);
					input_sizeLength.setVisible(false);
					txt_radius.setVisible(true);
					txt_sizeLength.setVisible(false);
					bg_cylinder.setVisible(true);
					bg_cube.setVisible(false);
					horizontal_line.setVisible(true);
					vertical_line.setVisible(true);
				}
			}
		}); */

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

	private class ParallaxAnimation extends AnimationTimer {
		@Override
		public void handle(long now) {
			if (lastUpdateAnimation.get() > 0) {
				long elastedNanoSecond = now - lastUpdateAnimation.get();
				// to do
				if (bg_cube.isVisible()) {
					updateTransition(cube, elastedNanoSecond);
				} else if (bg_cylinder.isVisible()) {
					updateTransition(cylinder, elastedNanoSecond);
				}
			}
			lastUpdateAnimation.set(now);
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



	public void handleDragOver(MouseEvent event) {
	}


	public void display(ActedObject object) {
		//Phần lực phải hiện thị bằng mũi tên, nên sẽ thay thế sau

		if (valuesBox.isSelected() == true) {
			gravitationalForceDisplay.setVisible(true);
			normalForceDisplay.setVisible(true);
			frictionalForceDisplay.setVisible(true);
			actorForceDisplay.setVisible(true);

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


	//
	//	public static void start(ActedObject obj, ChangeableForce force, Surface surface) {
	//
	//		if (obj instanceof Cube) {
	//			Cube cube = (Cube) obj;
	//			double t = 0;
	//			while (t < 10) {
	//				if (t > 5) {
	//					force.setMagnitude(1000.0);
	//				}
	//				cube.proceed(0.001);
	//				System.out.println("Time (s): " + t);
	//				System.out.println("Position (x, y): " + "(" + cube.getX() + ", " + cube.getY() + ")\n");
	//				System.out.println("Total force magnitude: " + cube.getSumForce());
	//				System.out.println("--------------------------------------");
	//				display(obj);
	//				t += 0.001;
	//			}
	//		} else if (obj instanceof Cylinder) {
	//			Cylinder cyclinder = (Cylinder) obj;
	//			double t = 0;
	//			while (t < 10) {
	//				if (t > 5) {
	//					force.setMagnitude(1000.0);
	//				}
	//				cyclinder.proceed(0.001);
	//				System.out.println("Time (s): " + t);
	//				System.out.println("Position (x, y): " + "(" + cyclinder.getX() + ", " + cyclinder.getY() + ")\n");
	//				System.out.println("Total force magnitude: " + cyclinder.getSumForce());
	//				System.out.println("--------------------------------------");
	//				display(obj);
	//				t += 0.001;
	//
	//			}
	//			lastUpdateAnimation.set(now);
	//		}
	//	}

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