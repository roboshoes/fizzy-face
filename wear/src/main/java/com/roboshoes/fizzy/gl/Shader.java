package com.roboshoes.fizzy.gl;

public class Shader {

    private Shader() {}


    public static final String vertex =
            "attribute vec4 vPosition;\n" +
            "attribute float pointSize;\n" +
            "\n" +
            "uniform mat4 mvp;\n" +
            "uniform vec2 screenSize;\n" +
            "\n" +
            "varying float radius;\n" +
            "varying vec2 screenPosition;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_PointSize = pointSize;\n" +
            "    gl_Position = mvp * vPosition;\n" +
            "\n" +
            "    vec3 ndc = gl_Position.xyz / gl_Position.w;\n" +
            "\n" +
            "    screenPosition = ( ndc.xy * 0.5 + 0.5 ) * screenSize;\n" +
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
            "varying vec2 screenPosition;\n" +
            "\n" +
            "const float HALF_PI = 1.570796327f;\n" +
            "\n" +
            "void main() {\n" +
            "\n" +
            "    vec2 particlePosition = vec2( gl_FragCoord.x, screenSize.y - gl_FragCoord.y );\n" +
            "    float length = distance( particlePosition, screenPosition );\n" +
            "\n" +
            "    if ( shape < 0.5 ) {\n" +
            "\n" +
            "        if ( length > radius ) discard;\n" +
            "\n" +
            "        if ( length > radius * 0.25 ) {\n" +
            "\n" +
            "            float alpha = 1.0 - smoothstep( radius * 0.25, radius, length );\n" +
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
            "        vec2 distances = screenPosition - particlePosition;\n" +
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
