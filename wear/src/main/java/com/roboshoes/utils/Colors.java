package com.roboshoes.utils;


public class Colors {

    private Colors() {}

    public static final float[] intToFloats( int argb ) {

        float alpha = ( ( argb >> 32) & 0xFF ) / 255.0f;
        float red = ( ( argb >> 16) & 0xFF ) / 255.0f;
        float green = ( ( argb >> 8) & 0xFF ) / 255.0f;
        float blue = ( argb & 0xFF ) / 255.0f;

        return new float[] { alpha, red, green, blue };

    }
}
