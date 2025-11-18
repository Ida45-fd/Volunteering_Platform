package app.controllers;

import app.dao.EventDao;
import app.models.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;

public class NGOHomeController {
    @FXML
    private TextField tfMaxVolunteers;
    @FXML
    private TableColumn<Event, String> colRemaining;

    @FXML
    private TextField tfEventName, tfLocation;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TableView<Event> table;
    @FXML
    private TableColumn<Event, String> colName, colLoc, colVolunteer, colDate;
    @FXML
    private Label lblHeader, lblMsg;

    private final EventDao dao = new EventDao();
    private String ngoUserid;
    private final ObservableList<Event> data = FXCollections.observableArrayList();

    public void setContext(String ngoUserid) {
        this.ngoUserid = ngoUserid;
        lblHeader.setText("NGO Home – " + ngoUserid);
        refresh();
    }

    @FXML
    public void initialize() {
        colVolunteer.setCellValueFactory(new PropertyValueFactory<>("volunteerList"));
        colRemaining.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getRemainingSlots() + "/" + c.getValue().getMaxVolunteers()));

        dpDate.setValue(LocalDate.now());
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        colLoc.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLocation()));
        colVolunteer.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getVolunteer() == null ? "(unbooked)" : c.getValue().getVolunteer()));
        colDate.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDate().toString()));
        table.setItems(data);
    }

   @FXML
public void addEvent() {
    try {
        int max = Integer.parseInt(tfMaxVolunteers.getText().trim());
        String name = tfEventName.getText().trim();
        LocalDate date = dpDate.getValue();
        String location = tfLocation.getText().trim();

        Event event = new Event(name, date, location, ngoUserid, max);

        if (dao.insert(event)) {
            lblMsg.setText("Event added successfully!");
            clearForm();
            refresh();
        } else {
            lblMsg.setText("Failed to add event!");
        }

    } catch (Exception ex) {
        lblMsg.setText("Error: " + ex.getMessage());
        ex.printStackTrace();
    }
}

private void clearForm() {
    tfEventName.clear();
    tfLocation.clear();
    tfMaxVolunteers.clear();
    dpDate.setValue(LocalDate.now());
}

private void refresh() {
    try {
        data.setAll(dao.listAll());

        for (Event e : data) {
            List<String> vols = dao.getVolunteersForEvent(e.getId());

            if (vols.isEmpty()) {
                e.setVolunteerList("Unbooked");
            } else {
                e.setVolunteerList(String.join(", ", vols));
            }
        }

    } catch (SQLException e) {
        lblMsg.setText("Error loading events: " + e.getMessage());
        e.printStackTrace();
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


    @FXML
public void onEdit() {
    Event selected = table.getSelectionModel().getSelectedItem();
    if (selected == null) {
        lblMsg.setText("Select an event to edit.");
        return;
    }

    TextInputDialog dialog = new TextInputDialog(selected.getName());
    dialog.setTitle("Edit Event");
    dialog.setHeaderText("Edit Event Name");
    dialog.setContentText("Enter new event name:");

    Optional<String> result = dialog.showAndWait();

    if (result.isPresent()) {
        try {
            dao.updateEventName(selected.getId(), result.get());
            lblMsg.setText("Event updated successfully.");
            refresh(); 
        } catch (Exception e) {
            lblMsg.setText("Error updating event: " + e.getMessage());
        }
    }
}

}