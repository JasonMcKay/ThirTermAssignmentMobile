package com.restful_client.restfulclient.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class VehicleUnitTest {
    @Test
    public void createVehicle() throws Exception {
        Vehicle v = new Vehicle("Unleaded", "Two door");

        assertEquals("Unleaded", v.getEngine());
    }
    @Test
    public void modifyVehicle() throws Exception {
        Vehicle v = new Vehicle("Unleaded", "Two door");
        v.setEngine("Diesel");
        assertEquals("Diesel", v.getEngine());
    }

}