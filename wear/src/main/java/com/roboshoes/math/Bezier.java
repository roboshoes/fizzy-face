package com.roboshoes.math;

import java.util.ArrayList;


public class Bezier {

    private float mPercent = 0.0f;
    private ArrayList<float[]> mAnchors = new ArrayList<>();
    private float x = 0.0f;
    private float y = 0.0f;

    public void addAnchor( float[] point ) {
        mAnchors.add( point );
        update();
    }

    public void setTime( float time ) {
        mPercent = time;
        update();
    }

    public float[] getPosition() {
        return new float[] { x, y };
    }

    private void update() {

        int i, j;
        float[][] points = new float[ mAnchors.size() ][ 2 ];
        int length = mAnchors.size();

        for ( i = 0; i < mAnchors.size(); i++ ) {
            points[ i ][ 0 ] = mAnchors.get( i )[ 0 ];
            points[ i ][ 1 ] = mAnchors.get( i )[ 1 ];
        }

        for ( j = 1; j < length; j++ ) {
            for ( i = 0; i < length - i; i++ ) {
                points[ i ][ 0 ] = ( 1.0f - mPercent ) * points[ i ][ 0 ] + mPercent * points[ i + 1 ][ 0 ];
                points[ i ][ 1 ] = ( 1.0f - mPercent ) * points[ i ][ 1 ] + mPercent * points[ i + 1 ][ 1 ];
            }
        }

        x = points[ 0 ][ 0 ];
        y = points[ 0 ][ 1 ];
    }
}
