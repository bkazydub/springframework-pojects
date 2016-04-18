package com.shop.springframework.domain.products;

import java.util.HashMap;
import java.util.Map;

public class ProductUtil {

    public static final String LAPTOP_PROPERTIES_PREFIX = "laptop.technical.";
    public static final String MOBILE_PROPERTIES_PREFIX = "mobile.technical.";
    public static final String ACCESSORIES_PROPERTIES_PREFIX = "accessories.technical.";

    public static final String[] LAPTOP_REQUIRED_PROPERTIES = modify(
            LAPTOP_PROPERTIES_PREFIX, new String[] {
                    "screen", "cpu", "ram", "hdd", "graphics", "audio"
            }
    );

    public static final String[] MOBILE_REQUIRED_PROPERTIES = modify(
            MOBILE_PROPERTIES_PREFIX, new String[] {
                    "screen", "cpu", "ram", "size", "cam"
            }
    );

    public static final String[] ACCESSORIES_REQUIRED_PROPERTIES = modify(
            ACCESSORIES_PROPERTIES_PREFIX, new String[] {
                    "max_screen_size", "colour", "dimensions"
            }
    );

    public static String[] modify(String prefix, String[] properties) {
        String[] modified = new String[properties.length];
        for (int i = 0; i < properties.length; i++) {
            modified[i] = prefix.concat(properties[i]);
        }
        return modified;
    }

    public static Map<String,String> createCharacteristicsMap(String[] characteristics) {
        Map<String,String> characteristicsMap = new HashMap<>();
        for (String characteristic : characteristics) {
            characteristicsMap.put(characteristic, "");
        }
        return characteristicsMap;
    }
}
