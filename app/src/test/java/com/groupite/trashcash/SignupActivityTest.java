package com.groupite.trashcash;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignupActivityTest {
    SignupActivity s = new SignupActivity();
    @Test
    public void TestIsEmailValidWhenTrue(){
        boolean actual = s.isEmailValid("janani@gmail.com");
        boolean expected = true;

        assertEquals("Email is valid",expected,actual);
    }
    @Test
    public void TestIsEmailValidWhenFalse(){
        boolean actual = s.isEmailValid("@#$%^&*@gmail.com");
        boolean expected = false;

        assertEquals("Email is invalid",expected,actual);
    }
}
