package main;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import force.ActorForce;
import force.GravitationalForce;
import force.NormalForce;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import objects.ActedObject;
import objects.Cube;
import objects.Surface;
 
public class MyController implements Initializable {
 
   @FXML
   private Button myButton;
  
   @FXML
   private TextField myTextField;
   
   @FXML 
   private  Slider staticSlider;
   
   @FXML
   private Slider kineticSlider;
   
   @FXML
   private Slider forceSlider;
   
   @FXML
   private CheckBox forcesBox;
   
   @FXML
   private CheckBox sumForcesBox;
   
   @FXML
   private CheckBox valuesBox;
   
   @FXML
   private CheckBox massBox;
   
   @FXML
   private CheckBox speedBox;
   
   @FXML
   private CheckBox accelerationBox;
   
   @FXML
   private TextField massField;
   
   @FXML
   private Label massOutput;
   
   @FXML 
   private TextField sideLengthField;
   
   @FXML
   private Label sideLengthOutput;
   
   @FXML
   private Text gravitationalForceBar;
   
   @FXML
   private Text normalForceBar;
   
   @FXML
   private Text frictionalForceBar;
   
   @FXML
   private Text actorForceBar;
   
   @FXML
   private Text sumForceBar;
   
   @FXML
   private Text massBar ;
   
   @FXML
   private Text speedBar;
   
   @FXML
   private Text accelerationBar;
   
   
   double x = 0;
   double y = 0;
   double staticFrictionCoef;
   double kineticFrictionCoef;
   
	
   Surface surface = new Surface(staticFrictionCoef, kineticFrictionCoef);
   ActorForce actorForce = new ActorForce(x, y);
   
   double mass;
   boolean massInput = false;
   
   double sideLength;
   boolean sideInput = false;
   double sideThreshold = 100;
   

   
   Cube cube = new Cube(mass,sideLength, actorForce, surface);


   public void massInput(ActionEvent event) {
	   if (massInput == false) {
		   try {
			   mass = Double.parseDouble(massField.getText());
			   massOutput.setText("Mass: " + massField.getText());
			   massInput = true;
			   cube.setMass(mass);
		   } catch(Exception e) {
			   massOutput.setText("Please enter a number only");
		   }
	   }
	   
   }
   
   public void sideLengthInput(ActionEvent event) {
	   if (sideInput == false) {
		   try {
			   sideLength = Double.parseDouble(sideLengthField.getText());
			   if (sideLength <= sideThreshold) {
				   sideLengthOutput.setText("Side length: " + sideLengthField.getText());
				   sideInput = true;	
				   cube.setSideLength(sideLength);
			   } else {
				   sideLengthOutput.setText("Please enter a number smaller than the threshold");

			   }
			   
		   } catch(Exception e) {
			   sideLengthOutput.setText("Please enter a number only");
		   }
	   }
	   
   }
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {
 
       // TODO (don't really need to do anything here).
	   staticSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				staticFrictionCoef = staticSlider.getValue();
				surface.setStaticFrictionCoef(staticFrictionCoef);
				cube.setSurface(surface);
				//System.out.println("Static: " + cube.surface.getStaticFrictionCoef());
				move(cube);
			}  
		   }); 
	   
	   kineticSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				kineticFrictionCoef = kineticSlider.getValue();
				surface.setKineticFrictionCoef(kineticFrictionCoef);
				cube.setSurface(surface);
				//System.out.println("Kinetic: " + cube.surface.getKineticFrictionCoef())
				move(cube);

			}
			   
		   });
	   
	   forceSlider.valueProperty().addListener(new ChangeListener<Number>() {	   
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				// TODO Auto-generated method stub
				double force = forceSlider.getValue();
				actorForce.setMagnitude(force);
				move(cube);
			}  
		   });
	   
   }
 
   // When user click on myButton
   // this method will be called.
   public void showDateTime(ActionEvent event) {
       System.out.println("Button Clicked!");
      
       Date now= new Date();
      
       DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
      
        // Model Data
        String dateTimeString = df.format(now);
        
        // Show in VIEW
        myTextField.setText(dateTimeString);
      
   }
   
   public void move(ActedObject object) {
	   double t = 0;
		while (true) {
			object.proceed(0.01);
			if (t % 10 == 0) {
				System.out.println("Time (s): " + t);
				System.out.println("Position (x, y): " + "(" + object.getX() + ", " + object.getY() + ")\n");
				System.out.println("Velocity: " + object.getVelocity() + "\n");
				System.out.println("Total force magnitude: " + object.getSumForce());
				System.out.println("--------------------------------------");
				System.out.println("Gravitational Force: " + object.getGravitationalForceMagnitude());
				System.out.println("Frictional Force: " + object.getFrictionalForceMagnitude());
				
				if (forcesBox.isSelected() == true) {
					gravitationalForceBar.setVisible(true);
					normalForceBar.setVisible(true);
					frictionalForceBar.setVisible(true);
					actorForceBar.setVisible(true);
					
					gravitationalForceBar.setText("Gravitational Force: " + object.getGravitationalForceMagnitude());
					normalForceBar.setText("Normal Force: " + object.getNormalForceMagnitude());
					frictionalForceBar.setText("Frictional Force: " + object.getFrictionalForceMagnitude());
					actorForceBar.setText("Actor Force: " + object.getActorForceMagnitude());
				} else {
					gravitationalForceBar.setVisible(false);
					normalForceBar.setVisible(false);
					frictionalForceBar.setVisible(false);
					actorForceBar.setVisible(false);
				}
				
				if (sumForcesBox.isSelected() == true) {
					sumForceBar.setVisible(true);
					sumForceBar.setText("Sum of forces: " +  object.getSumForce());
				} else {
					sumForceBar.setVisible(false);
				}
				
				if (massBox.isSelected() == true) {
					massBar.setVisible(true);
					massBar.setText("The mass: " + object.getMass());
				} else {
					massBar.setVisible(false);

				}
				if (speedBox.isSelected() == true) {
					speedBar.setVisible(true);
					speedBar.setText("The speed: " + object.getVelocity());
				} else {
					speedBar.setVisible(false);

				}
				if (accelerationBox.isSelected() == true) {
					accelerationBar.setVisible(true);
					accelerationBar.setText("The acceleration: " + object.getAcceleration()); 

				} else {
					accelerationBar.setVisible(false);

				}
			}
			
			t += 0.01;
		}
   }
   
  
   
   
   
   

	
   
  
}