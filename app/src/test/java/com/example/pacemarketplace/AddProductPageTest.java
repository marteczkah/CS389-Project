package com.example.pacemarketplace;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddProductPageTest {
    private AddProductPage addProductPage;

    @Before
    public void setUp(){
        addProductPage = new AddProductPage();
        System.out.println("Ready for testing");
    }

    @After
    public void tearDown(){
        System.out.println("Done with testing");
    }

    @Test
    public void testProductName(){
        String test = addProductPage.productNameTest;
        boolean bool = true;
        if(test.length() > 64)
            bool = false;
        assertEquals("Product Name Test is too long",true,bool);
    }


}
