package app.dao;

import app.Database;
import app.models.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; 
import java.util.ArrayList;
import java.util.List;


public class EventDao {
    public boolean insert(Event e) throws SQLException {
        String sql = "INSERT INTO events (event_name, date, location, ngo_userid, max_volunteers, booked_count) VALUES (?,?,?,?,?,0)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, e.getName());
ps.setDate(2, java.sql.Date.valueOf(e.getDate()));
ps.setString(3, e.getLocation());
ps.setString(4, e.getNgoUserid());
ps.setInt(5, e.getMaxVolunteers());

            return ps.executeUpdate() == 1;
        }
    }

    public List<Event> listAll() throws SQLException {
        List<Event> list = new ArrayList<>();
String sql = "SELECT id, event_name, date, location, volunteer, ngo_userid, max_volunteers, booked_count FROM events";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Event(
    rs.getInt("id"),
    rs.getString("event_name"),
    rs.getDate("date").toLocalDate(),
    rs.getString("location"),
    rs.getString("volunteer"),
    rs.getString("ngo_userid"),
    rs.getInt("max_volunteers"),
    rs.getInt("booked_count")
));

            }
        }
        return list;
    }

    public boolean bookEvent(int eventId, String volunteerUserid) throws SQLException {
    String sql = """
        UPDATE events 
        SET booked_count = booked_count + 1 
        WHERE id = ? AND booked_count < max_volunteers
    """;

    try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
        ps.setInt(1, eventId);
        int rows = ps.executeUpdate();

        if (rows == 1) {
            recordVolunteerBooking(eventId, volunteerUserid);
            return true;
        } else {
            return false; 
        }
    }
}

private void recordVolunteerBooking(int eventId, String volunteerUserid) throws SQLException {
    String sql = "INSERT INTO event_volunteers (event_id, volunteer_userid) VALUES (?, ?)";
    try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
        ps.setInt(1, eventId);
        ps.setString(2, volunteerUserid);
        ps.executeUpdate();
    }
}

public boolean updateEventName(int eventId, String newName) throws SQLException {
    String sql = "UPDATE events SET event_name = ? WHERE id = ?";
    Connection conn = Database.getConnection();
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, newName);
    ps.setInt(2, eventId);
    return ps.executeUpdate() > 0;
}

public List<String> getVolunteersForEvent(int eventId) throws SQLException {
    List<String> list = new ArrayList<>();

    String sql = "SELECT volunteer_userid FROM event_volunteers WHERE event_id=?";
    try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
        ps.setInt(1, eventId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(rs.getString("volunteer_userid"));
        }
    }
    return list;
}

}