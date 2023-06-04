package com.striczkof.bruh_wiki.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {
    @Test
    @DisplayName("PS enums' names should match")
    public void psEnumMatchTest() {
        String[] psNames = new String[PS.values().length];
        String[] sqlStatementNames = new String[SQLStatement.values().length];
        for (int i = 0; i < PS.values().length; i++) {
            psNames[i] = PS.values()[i].name();
            sqlStatementNames[i] = SQLStatement.values()[i].name();
        }
        assertArrayEquals(psNames, sqlStatementNames);
        assertEquals(PS.values().length, SQLStatement.values().length);
    }
}
