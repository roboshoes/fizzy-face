package com.roboshoes.fizzy.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by roboshoes on 10/12/15.
 */
public class ColorUtils {

    private static Random random = new Random();

    private ColorUtils() {
        throw new InstantiationError( "Can't instantiate static class ColorUtils" );
    }

    public static int createRandomColor() {
        return Color.argb(
                255,
                random.nextInt( 256 ),
                random.nextInt( 256 ),
                random.nextInt( 256 )
        );
    }
}
