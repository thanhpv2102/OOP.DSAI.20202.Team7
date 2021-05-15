package main;

import force.ActorForce;
import objects.*;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
 
public class MyController implements Initializable {
	private ActorForce force = new ActorForce(0.0, 0.0);
	private Surface surface = new Surface(0.25, 0.25);
	
 
   @FXML
   private Button myButton;
  
   @FXML
   private TextField myTextField;
   
   @FXML
   private Slider staticSlider;
   
   @FXML
   private Slider kineticSlider;
  
   @Override
   public void initialize(URL location, ResourceBundle resources) {
 
	   
       //Alter static friction coef with staticSlider
	   staticSlider.valueProperty().addListener(new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			if (staticSlider.getValue() >= kineticSlider.getValue()) {
				staticSlider.adjustValue(kineticSlider.getValue());
			}
			surface.setStaticFrictionCoef((double)staticSlider.getValue());
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
			surface.setKineticFrictionCoef((double)kineticSlider.getValue());
//			System.out.println("kinetic:" + surface.getKineticFrictionCoef());
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
  
}