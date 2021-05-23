package main;

import force.ChangeableForce;
import objects.*;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

public class MyController implements Initializable {

	private ChangeableForce force = new ChangeableForce(0, 0, 0);
	private Surface surface = new Surface(0.25, 0.25);
	private final LongProperty lastUpdateAnimation = new SimpleLongProperty(0);
	AnimationTimer parallaxAnimation = new ParallaxAnimation();
	private ActedObject obj;

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
	private TextField forceValue;

	@FXML
	private ImageView bg1;

	@FXML
	private ImageView bg2;

	@FXML
	private ImageView ls1;

	@FXML
	private ImageView ls2;

	@FXML
	private Rectangle bgCube;

	@FXML
	private Circle bgCylinder;

	@FXML
	private RadioButton radio_cube;

	@FXML
	private RadioButton radio_cylinder;

	ToggleGroup group = new ToggleGroup();

	@FXML
	private Line horizontalLine;

	@FXML
	private Line verticalLine;

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
		
		resetBtnPressed();
		
		//Alter static friction coef with staticSlider
		staticSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (staticSlider.getValue() >= kineticSlider.getValue()) {
					staticSlider.adjustValue(kineticSlider.getValue());
				}
				surface.setStaticFrictionCoef((double) staticSlider.getValue());
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
			}
		});
		
		//the user can input applied force value in this text field
		forceValue.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					try {
						Double inputValue = Double.parseDouble(forceValue.getText());
						forceSlider.adjustValue(inputValue);
					} catch (NumberFormatException e) {
						forceValue.setText(String.valueOf(forceSlider.getValue()));
					}
				}
			}
		});

		//force slider
		forceSlider.valueProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				force.setMagnitude(forceSlider.getValue());
				forceValue.setText(String.valueOf(forceSlider.getValue()));
				if (obj != null) {
					parallaxAnimation.start();	
				}
			}
		});

	}
	
	@FXML
	private void resetBtnPressed() {
		valuesBox.setSelected(false);
		forcesBox.setSelected(false);
		sumForcesBox.setSelected(false);
		massBox.setSelected(false);
		speedBox.setSelected(false);
		accelerationBox.setSelected(false);
		
		parallaxAnimation.stop();
		obj = null;
		display(obj);
		
		//Why though
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
		
		bgCube.setVisible(false);
		bgCylinder.setVisible(false);
		
		choiceCube.setVisible(true);
		choiceCylinder.setVisible(true);
		
		horizontalLine.setRotate(0);
		verticalLine.setRotate(0);
		horizontalLine.setVisible(false);
		verticalLine.setVisible(false);
		
		forceSlider.adjustValue(0.0);
		staticSlider.adjustValue(0.25);
		kineticSlider.adjustValue(0.25);
		
		bg1.setX(0);
		bg2.setX(0);
		ls1.setX(0);
		ls2.setX(0);
		
		pauseButton.setText("Pause");
	}

	public void submitMass(ActionEvent event) {
//		try {
//			mass = Double.parseDouble(inputMass.getText());
//		} catch(Exception e) {
//			inputMass.setText("Please enter a number for the mass!");
//		}
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
			if (lastUpdateAnimation.get() > 0) {
				long elastedNanoSecond = now - lastUpdateAnimation.get();
				if (pauseButton.getText().equals("Pause")) {
					display(obj);
					updateTransition(obj, elastedNanoSecond);
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
			bgCylinder.setVisible(false);
			horizontalLine.setVisible(false);
			verticalLine.setVisible(false);
			bgCube.setVisible(true);
			choiceCube.setTranslateX(0);
			choiceCube.setTranslateY(0);
			choiceCube.setVisible(false);
			forceSlider.adjustValue(0.0);
			showInputDialog("cube");
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
			bgCube.setVisible(false);
			bgCylinder.setVisible(true);
			horizontalLine.setVisible(true);
			verticalLine.setVisible(true);
			forceSlider.adjustValue(0.0);
			showInputDialog("cylinder");
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
		old_x = obj.getX();
		obj.proceed(elastedSecond);
		if (ls1.getX() - (obj.getX() - old_x)*40 < -BACKGROUND_WIDTH) {
			ls1.setX(0);
			ls2.setX(0);
		} else if (ls1.getX() - (obj.getX() - old_x)*40 > 0) {
			ls1.setX(-BACKGROUND_WIDTH);
			ls2.setX(-BACKGROUND_WIDTH);
		} else {
			ls1.setX(ls1.getX() - (obj.getX() - old_x)*40);
			ls2.setX(ls2.getX() - (obj.getX() - old_x)*40);
		}
		if (bg1.getX() - (obj.getX() - old_x)*10 < -BACKGROUND_WIDTH) {
			bg1.setX(0);
			bg2.setX(0);
		} else if (bg1.getX() - (obj.getX() - old_x)*10 > 0) {
			bg1.setX(-BACKGROUND_WIDTH);
			bg2.setX(-BACKGROUND_WIDTH);
		} else {
			bg1.setX(bg1.getX() - (obj.getX() - old_x)*10);
			bg2.setX(bg2.getX() - (obj.getX() - old_x)*10);
		}
		
		if (obj instanceof Cylinder) {
			//multiply by 25 because the rotation is faster than transition
			horizontalLine.setRotate(horizontalLine.getRotate() + (obj.getX() - old_x)*20);
			verticalLine.setRotate(verticalLine.getRotate() + (obj.getX() - old_x)*20);
		}
		
		if (obj.validateSpeedThreshold()) {
			forceSlider.adjustValue(0.0);
		}
	}
	
	private void showInputDialog(String type) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Input object information");
		dialog.setResizable(true);
		
		DialogPane pane = dialog.getDialogPane();
		pane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		pane.getStyleClass().add("inputDialog");
		 
		Label labelMass = new Label("Mass (kg): ");
		Label labelSidelength = new Label("Side length (cm): ");
		Label labelRadius = new Label("Radius (cm): ");
		
		labelMass.setTextFill(Paint.valueOf("#8be9fd"));
		labelSidelength.setTextFill(Paint.valueOf("#8be9fd"));
		labelRadius.setTextFill(Paint.valueOf("#8be9fd"));
		
		TextField txtMass = new TextField();
		TextField txtLength = new TextField();
		TextField txtRadius = new TextField();
		
		txtMass.setPromptText("50.0");
		txtLength.setPromptText("200");
		txtRadius.setPromptText("100");
		         
		GridPane grid = new GridPane();
		grid.add(labelMass, 1, 1);
		grid.add(txtMass, 2, 1);
		
		if (type.equals("cube")) {
			grid.add(labelSidelength, 1, 2);
			grid.add(txtLength, 2, 2);
		} else if (type.equals("cylinder")) {
			grid.add(labelRadius, 1, 2);
			grid.add(txtRadius, 2, 2);
		}
		
		
		grid.setHgap(10); 
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		pane.setContent(grid);
		         
		ButtonType btnConfirm = new ButtonType("Confirm", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(btnConfirm);
		 
		dialog.setResultConverter(new Callback<ButtonType, String>() {
		    @Override
		    public String call(ButtonType b) { 
		        if (b == btnConfirm) {
		        	if (type.equals("cube")) {
		        		obj = new Cube(Double.parseDouble(txtMass.getText()), Double.parseDouble(txtLength.getText()), force, surface);
		        	} else if (type.equals("cylinder")) {
		    			obj = new Cylinder(Double.parseDouble(txtMass.getText()), Double.parseDouble(txtRadius.getText()), force, surface);
		    		}
		        }
		        return "";
		    }
		});
		
		dialog.showAndWait();
    }
}