package tus.cyber.app;

import org.junit.Assert;
import org.junit.Test;

public class DiscontinuedTest {

    @Test
    public void testGetStatus() {
        Discontinued discontinued = new Discontinued("Tablet");
        Assert.assertEquals("Product Tablet has been discontinued.", discontinued.getStatus());
    }
}