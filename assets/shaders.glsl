// This file is simply to write the shaders outside
// of android studio. It won't get loaded. The contents
// are manually copied into the file

// == vertex == //

precision mediump float;

attribute vec4 vPosition;
attribute float pointSize;

uniform mat4 mvp;
uniform vec2 screenSize;

varying float radius;
varying vec2 vertex;

void main() {
    gl_PointSize = pointSize;
    gl_Position = mvp * vPosition;

    vec3 ndc = gl_Position.xyz / gl_Position.w;

    vertex = ( ndc.xy * 0.5 + 0.5 ) * screenSize;
    radius = pointSize / 2.0;
}


// == fragment == //

precision mediump float;

uniform vec3 color;
uniform vec2 screenSize;
uniform float shape;

varying float radius;
varying vec2 vertex;

const float HALF_PI = 1.570796327;

void main() {

    vec2 pixel = gl_FragColor.xy;
    float len = distance( pixel, vertex );

    if ( shape < 0.5 ) {

        if ( len > radius ) discard;

        if ( len > radius * 0.25 ) {

            float alpha = 1.0 - smoothstep( radius * 0.25, radius, len );
            alpha *= alpha;

            gl_FragColor = vec4( color, alpha );

        } else {

            gl_FragColor = vec4( color, 1.0 );

        }

    } else {

        vec2 distances = vertex - pixel;

        distances /= radius;
        distances *= HALF_PI;
        distances = cos( distances );
        distances = floor( distances + vec2( 0.1 ) );

        if ( ! any( greaterThan( distances, vec2( 0.5 ) ) ) ) discard;

        gl_FragColor = vec4( color, 1.0 );

    }
}