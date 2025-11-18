package app.controllers;

import app.api.LocationAPI;
import app.dao.EventDao;
import app.models.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.Label;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;


public class VolunteerHomeController {
    @FXML
    private TableView<Event> table;
    @FXML
    private TableColumn<Event, String> colName, colLoc, colDate, colVolunteer;
    @FXML
    private Label lblHeader, lblMsg;
    @FXML private TableColumn<Event, String> colRemaining;


    private final EventDao dao = new EventDao();
    private final ObservableList<Event> data = FXCollections.observableArrayList();
    private String volunteerUserid;

    public void setContext(String volunteerUserid) {
        this.volunteerUserid = volunteerUserid;
        lblHeader.setText("Volunteer Home – " + volunteerUserid);
        String userCity = LocationAPI.getCity();
        lblMsg.setText("Your location: " + userCity);

        refresh();
    }

    @FXML
    public void initialize() {
        colRemaining.setCellValueFactory(c ->
    new SimpleStringProperty(c.getValue().getRemainingSlots() + "/" + c.getValue().getMaxVolunteers())
);

        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        colLoc.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLocation()));
        colDate.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDate().toString()));
        colVolunteer.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getVolunteer() == null ? "(unbooked)" : c.getValue().getVolunteer()));
        table.setItems(data);
    }

    @FXML
    public void onBookSelected() {
        Event selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMsg.setText("Please select an event first.");
            return;
        }
        try {
            boolean ok = dao.bookEvent(selected.getId(), volunteerUserid);
            lblMsg.setText(ok ? "Event booked successfully!" : "Event already booked.");
            refresh();
        } catch (SQLException e) {
            lblMsg.setText("Error: " + e.getMessage());
        }
    }

    private void refresh() {
    try {
        String city = LocationAPI.getCity();

        ObservableList<Event> allEvents =
                FXCollections.observableArrayList(dao.listAll());

        ObservableList<Event> nearbyEvents = allEvents.filtered(
                event -> event.getLocation().equalsIgnoreCase(city)
        );

        ObservableList<Event> otherEvents = allEvents.filtered(
                event -> !event.getLocation().equalsIgnoreCase(city)
        );

        data.clear();
        data.addAll(nearbyEvents);
        data.addAll(otherEvents);

        if (!nearbyEvents.isEmpty()) {
            lblMsg.setText("Showing nearby events first (Your city: " + city + ")");
        } else {
            lblMsg.setText("No nearby events in " + city + ". Showing all events.");
        }

    } catch (SQLException e) {
        lblMsg.setText("Error loading events: " + e.getMessage());
    }
}


    @FXML
    public void onLogout() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) table.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}