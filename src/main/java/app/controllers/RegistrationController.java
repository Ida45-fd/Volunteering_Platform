package app.controllers;

import app.api.LocationAPI;
import app.models.UserRole;
import app.services.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistrationController {
    @FXML private TextField tfUserid, tfName, tfEmail, tfLocation;
    @FXML private PasswordField pfPassword;
    @FXML private RadioButton rbNGO, rbVolunteer;
    @FXML private ToggleGroup roleGroup;
    @FXML private Label lblMsg;

    private final AuthService auth = new AuthService();

    @FXML
    public void onRegister(ActionEvent e) {
        try {
            UserRole role = rbNGO.isSelected() ? UserRole.NGO : UserRole.VOLUNTEER;
            boolean ok = auth.register(
                    tfUserid.getText().trim(),
                    tfName.getText().trim(),
                    tfEmail.getText().trim(),
                    pfPassword.getText(),
                    role);
            if (ok) {
                lblMsg.setText("Registration successful! Go to login.");
            } else {
                lblMsg.setText("Registration failed.");
            }
        } catch (Exception ex) {
            lblMsg.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void goToLogin(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Stage s = (Stage)((Node)e.getSource()).getScene().getWindow();
        s.setScene(new Scene(root));
        s.show();
    }

    @FXML
    public void initialize() {
    String city = LocationAPI.getCity();
    tfLocation.setText(city);
}

}