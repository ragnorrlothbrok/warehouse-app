package tus.cyber.app;

import org.junit.Assert;
import org.junit.Test;

public class OutOfStockTest {

    @Test
    public void testGetStatus() {
        OutOfStock outOfStock = new OutOfStock("Smartphone");
        Assert.assertEquals("Product Smartphone is out of stock.", outOfStock.getStatus());
    }
}