package com.roboshoes.shared.gl;


import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLPoints {

    private int length;
    private Shader shader;
    private FloatBuffer vertexBuffer;
    private FloatBuffer sizeBuffer;

    public GLPoints( int length ) {
        this.length = length;

        ByteBuffer bbVertex = ByteBuffer.allocateDirect( length * 3 * 4 );
        bbVertex.order( ByteOrder.nativeOrder() );

        vertexBuffer = bbVertex.asFloatBuffer();

        ByteBuffer bbSize = ByteBuffer.allocateDirect( length * 4 );
        bbSize.order( ByteOrder.nativeOrder() );

        sizeBuffer = bbSize.asFloatBuffer();
    }

    public void setShader( Shader shader ) {
        this.shader = shader;
    }

    public void setPosition( float[] positions ) {
        vertexBuffer.clear();
        vertexBuffer.put( positions );
        vertexBuffer.position( 0 );
    }

    public void setSize( float[] sizes ) {
        sizeBuffer.clear();
        sizeBuffer.put( sizes );
        sizeBuffer.position( 0 );
    }

    public void draw() {
        int program = shader.getProgram();

        GLES20.glUseProgram( program );

        int vPosition = GLES20.glGetAttribLocation( program, "vPosition" );

        GLES20.glEnableVertexAttribArray( vPosition );
        GLES20.glVertexAttribPointer( vPosition, 3, GLES20.GL_FLOAT, false, 4 * 3, vertexBuffer );

        int vPointSize = GLES20.glGetAttribLocation( program, "pointSize" );

        GLES20.glEnableVertexAttribArray( vPointSize );
        GLES20.glVertexAttribPointer( vPointSize, 1, GLES20.GL_FLOAT, false, 4, sizeBuffer );

        GLES20.glDrawArrays( GLES20.GL_POINTS, 0, length );
    }
}
