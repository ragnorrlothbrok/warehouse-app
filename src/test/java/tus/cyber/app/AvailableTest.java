package tus.cyber.app;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
public class AvailableTest {

    @Test
    public void getStatus() {
        Available available = new Available("Laptop");
        //System.out.println(available.getStatus());
        Assert.assertEquals("Product Laptop is available.", available.getStatus());
    }
}