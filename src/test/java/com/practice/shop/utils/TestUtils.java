package com.practice.shop.utils;

public class TestUtils {
    public static String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append('a');
        return sb.toString();
    }
}
