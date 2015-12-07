package com.roboshoes.fizzy.gl;

public class Shader {

    private Shader() {}


    public static final String vertex =
            "precision mediump float;\n" +
            "\n" +
            "attribute vec4 vPosition;\n" +
            "attribute float pointSize;\n" +
            "\n" +
            "uniform mat4 mvp;\n" +
            "uniform vec2 screenSize;\n" +
            "\n" +
            "varying float radius;\n" +
            "varying vec2 vertex;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_PointSize = pointSize;\n" +
            "    gl_Position = mvp * vPosition;\n" +
            "\n" +
            "    vec3 ndc = gl_Position.xyz / gl_Position.w;\n" +
            "\n" +
            "    vertex = ( ndc.xy * 0.5 + 0.5 ) * screenSize;\n" +
            "    radius = pointSize / 2.0;\n" +
            "}";

    public static final String fragment =
            "precision mediump float;\n" +
            "\n" +
            "uniform vec3 color;\n" +
            "uniform vec2 screenSize;\n" +
            "uniform float shape;\n" +
            "\n" +
            "varying float radius;\n" +
            "varying vec2 vertex;\n" +
            "\n" +
            "const float HALF_PI = 1.570796327;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    vec2 pixel = gl_FragCoord.xy;\n" +
            "    float len = distance( pixel, vertex );\n" +
            "    float alpha = 1.0;\n" +
            "\n" +
            "    if ( shape < 0.5 ) {\n" +
            "\n" +
            "        if ( len > radius ) discard;\n" +
            "\n" +
            "        if ( len > radius * 0.25 ) {\n" +
            "\n" +
            "            alpha = 1.0 - smoothstep( radius * 0.25, radius, len );\n" +
            "            alpha *= alpha;\n" +
            "\n" +
            "            gl_FragColor = vec4( color, alpha );\n" +
            "\n" +
            "        } else {\n" +
            "\n" +
            "            gl_FragColor = vec4( color, 1.0 );\n" +
            "\n" +
            "        }\n" +
            "\n" +
            "    } else {\n" +
            "\n" +
            "        vec2 distances = vertex - pixel;\n" +
            "\n" +
            "        distances /= radius;\n" +
            "        distances *= HALF_PI;\n" +
            "        distances = cos( distances );\n" +
            "        distances = floor( distances + vec2( 0.1 ) );\n" +
            "\n" +
            "        if ( ! any( greaterThan( distances, vec2( 0.5 ) ) ) ) discard;\n" +
            "\n" +
            "        gl_FragColor = vec4( color, 1.0 );\n" +
            "\n" +
            "    }\n" +
            "}";
}
