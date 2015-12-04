package com.roboshoes.utils;


public class Colors {

    private Colors() {}

    private static float clamp( float value ) {
        return Math.min( 1.0f, Math.max( value, 0.0f ) );
    }

    public static float[] intToFloats( int argb ) {

        float alpha = ( ( argb >> 32 ) & 0xFF ) / 255.0f;
        float red = ( ( argb >> 16 ) & 0xFF ) / 255.0f;
        float green = ( ( argb >> 8 ) & 0xFF ) / 255.0f;
        float blue = ( argb & 0xFF ) / 255.0f;

        return new float[] { alpha, red, green, blue };
    }

    public static void lighten( float[] color, float amount ) {
        color[ 1 ] = clamp( color[ 1 ] * ( 1 - amount ) + amount );
        color[ 2 ] = clamp( color[ 2 ] * ( 1 - amount ) + amount );
        color[ 3 ] = clamp( color[ 3 ] * ( 1 - amount ) + amount );
    }
}
