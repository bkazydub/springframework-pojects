package com.practice.springframework.test.utils;

public class TestUtils {

    private static char ch = 'a';

    public static String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(ch);
        return sb.toString();
    }
}
