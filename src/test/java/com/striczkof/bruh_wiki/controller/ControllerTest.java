package com.striczkof.bruh_wiki.controller;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerTest {
    // User tests
    @RepeatedTest(2)
    @DisplayName("Test if the hashing algorithm works and is consistent, does nothing for now")
    public void passHashingTest() {
        SecureRandom random = new SecureRandom();
        byte[] pass1 = new byte[32];
        byte[] pass2 = new byte[32];
        byte[] salt1 = new byte[16];
        byte[] salt2 = new byte[16];
        random.nextBytes(pass1);
        random.nextBytes(pass2);
        random.nextBytes(salt1);
        random.nextBytes(salt2);
        // String hash1 = User.hashPassword(pass1, salt1);
    }
}
