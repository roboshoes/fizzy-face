package com.roboshoes.shared.gl;

import android.opengl.GLES20;

public class Shader {

    private final int program;

    private Shader( int program ) {
        this.program = program;
    }

    public int getProgram() {
        return this.program;
    }

    private static int loadShader( int type, String code ) {
        int shader = GLES20.glCreateShader( type );

        GLES20.glShaderSource( shader, code );
        GLES20.glCompileShader( shader );

        return shader;
    }

    public static Shader create(String vertex, String fragment ) {

        int vertexShader = loadShader( GLES20.GL_VERTEX_SHADER, vertex );
        int fragmentShader = loadShader( GLES20.GL_FRAGMENT_SHADER, fragment );

        int program = GLES20.glCreateProgram();

        GLES20.glAttachShader( program, vertexShader );
        GLES20.glAttachShader( program, fragmentShader );
        GLES20.glLinkProgram( program );

        return new Shader( program );
    }

    public void uniform( String name, float value ) {
        int location = GLES20.glGetUniformLocation( program, name );
        GLES20.glUniform1f( location, value );
    }

    public void uniform( String name, float x, float y ) {
        int location = GLES20.glGetUniformLocation( program, name );
        GLES20.glUniform2f( location, x, y );
    }

    public void uniform( String name, float x, float y, float z ) {
        int location = GLES20.glGetUniformLocation( program, name );
        GLES20.glUniform3f( location, x, y, z );
    }
}
