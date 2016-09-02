package com.restful_client.restfulclient.model;

/**
 * Created by JasonMckay on 29-Aug-16.
 */

public class Vehicle {


    private Long id;
    private String engine;
    private String doors;

    public Long getId() {
        return id;
    }

    public String getEngine() {
        return engine;
    }

    public String getDoors() {
        return doors;
    }

    public Vehicle(String fuel, String door){
        this.engine = fuel;
        this.doors = door;
    }

    public Vehicle(){

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }
}
