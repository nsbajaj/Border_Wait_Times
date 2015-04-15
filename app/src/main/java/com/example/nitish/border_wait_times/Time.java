package com.example.nitish.border_wait_times;

public class Time {

    private int _id;
    private String _customsOffice;
    private String _location;
    private String _lastUpdated;
    private String _commericialFlow;
    private String _travellersFlow;

    public Time(){
        this._id = 0;
        this._customsOffice = null;
        this._location = null;
        this._lastUpdated = null;
        this._commericialFlow = null;
        this._travellersFlow = null;
    }
    public Time(String customsOffice, String location, String lastUpdated, String commericialFlow, String travellersFlow){
        this._customsOffice = customsOffice;
        this._location = location;
        this._lastUpdated = lastUpdated;
        this._commericialFlow = commericialFlow;
        this._travellersFlow = travellersFlow;
    }
    public Time(int id, String customsOffice, String location, String lastUpdated, String commericialFlow, String travellersFlow){
        this._id = id;
        this._customsOffice = customsOffice;
        this._location = location;
        this._lastUpdated = lastUpdated;
        this._commericialFlow = commericialFlow;
        this._travellersFlow = travellersFlow;
    }
    //Setters
    public void setID(int id) {
        this._id = id;
    }
    public void setCustomsOffice(String customsOffice) {
        this._customsOffice = customsOffice;
    }
    public void setLocation(String location){
        this._location = location;
    }
    public void setLastUpdated(String lastUpdated){
        this._lastUpdated = lastUpdated;
    }
    public void setCommericialFlow(String commericialFlow){
        this._commericialFlow = commericialFlow;
    }
    public void setTravellersFlow(String travellersFlow){
        this._travellersFlow = travellersFlow;
    }
    //Getters
    public int getID() {
        return this._id;
    }
    public String getCustomsOffice(){
        return this._customsOffice;
    }
    public String getLocation(){
        return this._location;
    }
    public String getLastUpdated(){
        return this._lastUpdated;
    }
    public String getCommericialFlow(){
        return this._commericialFlow;
    }
    public String getTravellersFlow(){
        return this._travellersFlow;
    }

}
