package com.roboshoes.motion;

import com.roboshoes.math.Bezier;
import com.roboshoes.math.MathUtils;


public class Motion {

    private float radius;
    private float speed;
    private float[] center = new float[ 2 ];
    private float currentTime = 0.0f;
    private float[][] anchors = new float[][]{
            getRandomPoint(),
            getRandomPoint(),
            getRandomPoint()
    };
    private Bezier curve;

    public Motion( float radius, float speed, float[] center ) {
        this.radius = radius;
        this.speed = speed;
        this.center = center;

        curve = new Bezier();

        initAnchors();
        initCurve();
    }

    public float[] getPosition() {
        return curve.getPosition();
    }

    public void setOrigin( float x, float y ) {
        center[ 0 ] = x;
        center[ 1 ] = y;
    }

    private float[] getRandomPoint() {
        float[] point = MathUtils.polarToCartesion( (float) Math.random() * MathUtils.TAU, radius );

        point[ 0 ] += center[ 0 ];
        point[ 1 ] += center[ 1 ];

        return point;
    }

    private void initCurve() {
        curve.clear();
        curve.addAnchor( MathUtils.interpolate( anchors[ 0 ], anchors[ 1 ], 0.5f ) );
        curve.addAnchor( anchors[ 1 ] );
        curve.addAnchor( MathUtils.interpolate( anchors[ 1 ], anchors[ 2 ], 0.5f ) );
    }

    private void initAnchors() {
        anchors[ 0 ] = getRandomPoint();
        anchors[ 1 ] = getRandomPoint();
        anchors[ 2 ] = getRandomPoint();
    }

    private void shiftAnchors() {
        anchors[ 0 ] = anchors[ 1 ];
        anchors[ 1 ] = anchors[ 2 ];
        anchors[ 2 ] = getRandomPoint();
    }

    public void update( boolean fullUpdate ) {

        currentTime += speed;

        if ( fullUpdate ) {

            shiftAnchors();
            shiftAnchors();
            initCurve();
            currentTime = 1.0f;

        } else if ( currentTime >= 1.0f ) {

            shiftAnchors();
            initCurve();
            currentTime -= 1.0f;
        }

        curve.setTime( currentTime );
    }
}
