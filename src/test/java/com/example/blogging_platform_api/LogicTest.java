package com.example.blogging_platform_api;

import org.junit.jupiter.api.Test;

public class LogicTest {

    @Test
    void testingArrayLengthAndForLoop() {
        String[] stringArray = new String[4];
        stringArray[1] = "yes";
        stringArray[3] = "no";
        for(int i = 0; i < stringArray.length; i++) {
            System.out.println(stringArray[i]);
        }

        int[] numArray = {};
        System.out.println(numArray.length);
    }

    @Test
    void testingStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder("Hello");
        stringBuilder.append(" world!");
        System.out.println(stringBuilder);
        int nullCounter = 0;

        StringBuilder sqlUpdateAll = new StringBuilder("UPDATE blogs SET ");
        String[] columns = new String[4];

        if(true) {
            columns[0] = "title";
            nullCounter++;
        }
        if(false) {
            columns[1] = "content";
            nullCounter++;
        }
        if(true) {
            columns[2] = "category";
            nullCounter++;
        }
        if(true) {
            columns[3] = "tags";
            nullCounter++;
        }
        for(int i = 0; i < columns.length; i++) {
            if(columns[i] != null) {
                if(nullCounter == 1) {
                    sqlUpdateAll.append(columns[i] + "=?");
                    System.out.println(true);
                } else {
                    sqlUpdateAll.append(columns[i] + "=?,");
                    nullCounter--;
                    System.out.println(false);
                }
            }

        }
        System.out.println(sqlUpdateAll.append(" WHERE id = blog.getId()"));
    }

}
