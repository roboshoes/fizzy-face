package com.roboshoes.fizzy.gl;

public class Shader {

    private Shader() {}

    private static final String NL = "\n";

    public static final String vertex =
            "attribute vec4 vPosition;" + NL +
            "uniform mat4 mvp;" + NL +
            "uniform vec2 screensize;" + NL +
            "varying float radius;" + NL +
            "varying vec2 screenPosition;" + NL +

            "void main() {" + NL +
            "    gl_PointSize = 6.0;" + NL +
            "    gl_Position = mvp * vPosition;" + NL +

            "    vec3 ndc = gl_Position.xyz / gl_Position.w;" + NL +

            "    screenPosition = ( ndc.xy * 0.5 + 0.5 ) * screensize;" + NL +
            "    radius = 3.0;" + NL +
            "}";

    public static final String fragment =
            "precision mediump float;" + NL +
            "uniform vec3 color;" + NL +
            "uniform vec2 screensize;" + NL +
            "varying float radius;" + NL +
            "varying vec2 screenPosition;" + NL +
            "void main() {" + NL +
            "    vec2 xy = vec2( gl_FragCoord.x, screensize.y - gl_FragCoord.y );" + NL +
            "    float length = distance( xy, screenPosition );" + NL +
            "    if ( length > radius ) discard;" + NL +
            "    else if ( length > radius * 0.25 ) { " + NL +
            "        float ease = 1.0 - smoothstep( radius * 0.25, radius, length );" + NL +
            "        ease *= ease;" + NL +
            "        gl_FragColor = vec4( color, ease );" + NL +
            "    } else gl_FragColor = vec4( color, 1.0 );" + NL +
            "}";
}
