package com.example.pacemarketplace;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.google.common.truth.Truth.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class AddProductTest {

    private Context context;

    public AddProductTest(Context context) {
        this.context = context;
    }

    public String getHelloWorldString(){
        return context.getString(R.string.hello_world);
    }

    private static final String FAKE_STRING = "HELLO WORLD";
    @Mock
    Context mockContext;

    @Test
    public void addProductTest(){
        when(mockContext.getString(R.string.addProduct)).thenReturn(FAKE_STRING);
        AddProductTest myobjectUnderTest = new AddProductTest(mockContext);

        String result = myobjectUnderTest.getHelloWorldString();



    }
}