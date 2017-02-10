package com.roboshoes.shared.math;


public class MathUtils {

    public static final float TAU = (float) Math.PI * 2.0f;

    public static float[] polarToCartesian(float angle, float distance ) {
        return new float[] {
                (float) Math.sin( angle ) * distance,
                (float) Math.cos( angle ) * distance
        };
    }

    public static float[] interpolate( float[] a, float[] b, float percentage ) {
        float[] difference = new float[] { b[ 0 ] - a[ 0 ], b[ 1 ] - a[ 1 ] };

        return new float[] {
                a[ 0 ] + difference[ 0 ] * percentage,
                a[ 1 ] + difference[ 1 ] * percentage
        };
    };

    public static float random( float min, float max ) {
        return min + (float) Math.random() * ( max - min );
    }
}
