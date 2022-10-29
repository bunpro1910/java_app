package vn.edu.greenwich.cw1.models;

import java.io.Serializable;

public class Trip implements Serializable {
    protected long _id;
    protected String _name;
    protected String _startDate;
    protected int _owner;
    protected  String _destination;
    protected  String _description;
    protected  long _current_amount ;
    public Trip() {
        _id = -1;
        _name = null;
        _startDate = null;
        _owner = -1;
        _destination = null;
        _description = null;
        _current_amount = 0;
    }

    public Trip(long id, String name, String startDate, int owner, String destination, String description) {
        _id = id;
        _name = name;
        _startDate = startDate;
        _owner = owner;
        _destination = destination;
        _description = description;
    }
    public void countAmount (long amount){
        _current_amount =_current_amount+amount;
    }

    public long getId() { return _id; }
    public void setId(long id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }
    public long getCurrentAmout() {
        return _current_amount;
    }
    public void setCurrentAmout(long cr_am) {
        _current_amount = cr_am;
    }

    public String getStartDate() {
        return _startDate;
    }
    public void setStartDate(String startDate) {
        _startDate = startDate;
    }

    public int getOwner() {
        return _owner;
    }
    public void setOwner(int owner) {
        _owner = owner;
    }

    public String getDestination() {
        return _destination;
    }
    public void setDestination(String destination) {
        _destination = destination;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }
    public boolean isEmpty() {
        if (-1 == _id && null == _name && null == _startDate && -1 == _owner&& null==_destination&&null==_description)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _startDate + "] " + _name+_destination+" "+_description;
    }
}