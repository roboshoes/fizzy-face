package com.roboshoes.fizzy;

import com.roboshoes.math.MathUtils;
import com.roboshoes.motion.Motion;
import com.roboshoes.motion.Pulse;

public class Bubble {

    public static final int CIRCLE = 0;
    public static final int PLUS = 1;

    private Motion motion;
    private Pulse pulse;
    private Pulse rotation;

    public Bubble( float x, float y ) {
        motion = new Motion( 0.03f, MathUtils.random( 0.05f, 0.1f ), new float[] { x, y } );
        pulse = new Pulse( 3, 8, MathUtils.random( 0.01f, 0.05f ) );
        rotation = new Pulse( -180, 180, MathUtils.random( 0.01f, 0.05f ) );
    }

    public void setOrigin( float x, float y ) {
        motion.setOrigin( x, y );
    }

    public void update( boolean fullUpdate ) {
        motion.update( fullUpdate );
        pulse.update();
        rotation.update();
    }

    public float[] getPosition() {
        return motion.getPosition();
    }

    public float getSize() {
        return pulse.get();
    }

    public float getRotation() {
        return rotation.get();
    }
}
