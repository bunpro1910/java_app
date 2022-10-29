package vn.edu.greenwich.cw1.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Backup implements Serializable {
    protected Date _date;
    protected String _deviceName;
    protected ArrayList<Trip> _trips;
    protected ArrayList<TripItem> _tripItems;

    public Backup(Date date, String deviceName, ArrayList<Trip> trips, ArrayList<TripItem> tripItems) {
        _date = date;
        _deviceName = deviceName;
        _trips = trips;
        _tripItems = tripItems;
    }

    public void setDate(Date date) {
        _date = date;
    }

    public Date getDate() {
        return _date;
    }

    public void setDeviceName(String deviceName) {
        _deviceName = deviceName;
    }

    public String getDeviceName() {
        return _deviceName;
    }

    public void setResidents(ArrayList<Trip> trips) {
        _trips = trips;
    }

    public ArrayList<Trip> getResidents() {
        return _trips;
    }

    public void setRequests(ArrayList<TripItem> tripItems) {
        _tripItems = tripItems;
    }

    public ArrayList<TripItem> getRequests() {
        return _tripItems;
    }
}