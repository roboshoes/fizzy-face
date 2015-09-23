package com.roboshoes.motion;

import com.roboshoes.math.MathUtils;


public class Pulse {

    private float max;
    private float speed;
    private float min;
    private float middle;
    private float valley;
    private float peak;
    private float value;

    private float current = 0;

    public Pulse( float min, float max, float speed ) {
        this.min = min;
        this.max = max;
        this.speed = speed;

        middle = ( max - min ) / 2;

        setAnchor();
    }

    public void update() {

        current += speed;

        if ( current >= 1.0f ) {
            current -= 1.0f;
            setAnchor();
        }

        value = middle + (float) Math.sin( current * MathUtils.TAU ) * ( current < 0.5f ? peak : - valley );
    }

    public float get() {
        return value;
    }

    private void setAnchor() {
        valley = MathUtils.random( min, middle ) - middle;
        peak = MathUtils.random( middle, max ) - middle;
    }
}
