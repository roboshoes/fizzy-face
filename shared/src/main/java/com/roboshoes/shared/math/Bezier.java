package com.roboshoes.shared.math;

import java.util.ArrayList;


public class Bezier {

    private float percent = 0.0f;
    private ArrayList<float[]> anchors = new ArrayList<>();
    private float x = 0.0f;
    private float y = 0.0f;

    public void addAnchor( float[] point ) {
        anchors.add( point );
        update();
    }

    public void setTime( float time ) {
        percent = time;
        update();
    }

    public void clear() {
        anchors.clear();
    }

    public float[] getPosition() {
        return new float[] { x, y };
    }

    private void update() {

        int i, j;
        float[][] points = new float[ anchors.size() ][ 2 ];
        int length = anchors.size();

        for ( i = 0; i < anchors.size(); i++ ) {
            points[ i ][ 0 ] = anchors.get( i )[ 0 ];
            points[ i ][ 1 ] = anchors.get( i )[ 1 ];
        }

        for ( j = 1; j < length; j++ ) {
            for ( i = 0; i < length - i; i++ ) {
                points[ i ][ 0 ] = ( 1.0f - percent ) * points[ i ][ 0 ] + percent * points[ i + 1 ][ 0 ];
                points[ i ][ 1 ] = ( 1.0f - percent ) * points[ i ][ 1 ] + percent * points[ i + 1 ][ 1 ];
            }
        }

        x = points[ 0 ][ 0 ];
        y = points[ 0 ][ 1 ];
    }
}
