package com.example.nitish.border_wait_times;

public class SearchResults {
    private String lastupdated = "";
    private String commercialflow = "";
    private String travellersflow = "";

    public void setLastUpdated(String name) {
        this.lastupdated = name;
    }

    public String getLastUpdated() {
        return lastupdated;
    }

    public void setCommercialFlow(String commercialflow) {
        this.commercialflow = commercialflow;
    }

    public String getCommercialFlow() {
        return commercialflow;
    }

    public void setTravellersFlow(String travellersflow) {
        this.travellersflow = travellersflow;
    }

    public String getTravellersFlow() {
        return travellersflow;
    }
}