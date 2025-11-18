package app.models;

import java.time.LocalDate;

public class Event {
    private int id;
    private String name;
    private LocalDate date;
    private String location;
    private String volunteer; // userid of volunteer (nullable)
    private String ngoUserid; // creator
    private int maxVolunteers;
private int bookedCount;


    public Event(int id, String name, LocalDate date, String location, String volunteer,
             String ngoUserid, int maxVolunteers, int bookedCount) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.location = location;
    this.volunteer = volunteer;
    this.ngoUserid = ngoUserid;
    this.maxVolunteers = maxVolunteers;
    this.bookedCount = bookedCount;
}

    public Event(String name, LocalDate date, String location, String ngoUserid, int maxVolunteers) {
    this(0, name, date, location, null, ngoUserid, maxVolunteers, 0);
}

private String volunteerList;

public void setVolunteerList(String volunteerList) {
    this.volunteerList = volunteerList;
}

public String getVolunteerList() {
    return volunteerList;
}



    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getDate() { return date; }
    public String getLocation() { return location; }
    public String getVolunteer() { return volunteer; }
    public String getNgoUserid() { return ngoUserid; }
    public void setVolunteer(String v) { this.volunteer = v; }
    public int getMaxVolunteers() { return maxVolunteers; }
public int getBookedCount() { return bookedCount; }
public int getRemainingSlots() { return maxVolunteers - bookedCount; }

}