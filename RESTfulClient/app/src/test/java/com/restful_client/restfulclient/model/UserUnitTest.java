package com.restful_client.restfulclient.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserUnitTest {
    @Test
    public void createUser() throws Exception {
        User u = new User("Person", "PassWord");
        assertEquals("PassWord", u.getPassword());
    }
}