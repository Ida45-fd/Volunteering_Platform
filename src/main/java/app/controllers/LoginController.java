package app.controllers;

import app.controllers.NGOHomeController;
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

public class LoginController {
    @FXML private TextField tfUserid;
    @FXML private PasswordField pfPassword;
    @FXML private ChoiceBox<String> cbRole;
    @FXML private Label lblMsg;

    private final AuthService auth = new AuthService();

    @FXML
    public void initialize() {
        cbRole.getItems().addAll("NGO", "Volunteer");
        cbRole.setValue("Volunteer");
    }

    @FXML
    public void onLogin(ActionEvent e) {
        try {
            UserRole role = cbRole.getValue().equalsIgnoreCase("NGO") ? UserRole.NGO : UserRole.VOLUNTEER;
            boolean ok = auth.login(tfUserid.getText().trim(), pfPassword.getText(), role);
            if (ok) {
                switchToHome(((Node)e.getSource()).getScene().getWindow(), role, tfUserid.getText().trim());
            } else {
                lblMsg.setText("Invalid credentials");
            }
        } catch (Exception ex) {
            lblMsg.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void goToRegister(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/registration.fxml"));
        Stage s = (Stage)((Node)e.getSource()).getScene().getWindow();
        s.setScene(new Scene(root));
        s.show();
    }

    private void switchToHome(javafx.stage.Window w, UserRole role, String userid) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        if (role == UserRole.NGO) {
            loader.setLocation(getClass().getResource("/fxml/ngo_home.fxml"));
            Parent root = loader.load();
            NGOHomeController controller = loader.getController();
            controller.setContext(userid);
            Stage s = (Stage) w;
            s.setScene(new Scene(root));
            s.show();
        } else {
            loader.setLocation(getClass().getResource("/fxml/volunteer_home.fxml"));
            Parent root = loader.load();
            VolunteerHomeController controller = loader.getController();
            controller.setContext(userid);
            Stage s = (Stage) w;
            s.setScene(new Scene(root));
            s.show();
        }
    }
}