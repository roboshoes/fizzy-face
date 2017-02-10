package com.roboshoes.fizzy.ui;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.roboshoes.shared.BubbleController;
import com.roboshoes.shared.font.LetterFactory;
import com.roboshoes.shared.gl.GLPoints;
import com.roboshoes.shared.gl.Shader;
import com.roboshoes.shared.gl.ShaderAssets;
import com.roboshoes.shared.utils.Colors;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ClockGLView extends GLSurfaceView {

    public ClockGLView( Context context ) {
        super( context  );

        setEGLContextClientVersion( 2 );

        setRenderer( new ClockRenderer() );
    }

    @Override
    public void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        int width = MeasureSpec.getSize( widthMeasureSpec );
        int height = MeasureSpec.getSize( heightMeasureSpec );
        int size = Math.min( height, width );

        setMeasuredDimension( size, size );
    }

    public class ClockRenderer implements GLSurfaceView.Renderer {

        private float[] bubbleColor;
        private float[] backgroundColor;
        private Shader shader;
        private GLPoints points;
        private BubbleController bubbleController;
        private int width;
        private int height;

        public void onSurfaceCreated( GL10 unused, EGLConfig config ) {

            shader = Shader.create( ShaderAssets.vertex, ShaderAssets.fragment );

            backgroundColor = Colors.intToFloats( 0xFF2B1330 );
            bubbleColor = Colors.intToFloats( 0xFFFFDE00 );

            bubbleController = new BubbleController();
            bubbleController.setFont( LetterFactory.BLOCK_FONT );
            bubbleController.setNumber( "1020" );

            points = new GLPoints( bubbleController.getAmount() );
            points.setShader( shader );
            points.setPosition( bubbleController.getPositions3D( false ) );
            points.setSize( bubbleController.getSizes() );
        }

        public void onDrawFrame( GL10 unused ) {

            GLES20.glDisable( GLES20.GL_CULL_FACE );
            GLES20.glDisable( GLES20.GL_DEPTH_TEST );

            GLES20.glClearColor( backgroundColor[ 1 ], backgroundColor[ 2 ], backgroundColor[ 3 ], 1.0f );
            GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );
            GLES20.glEnable( GLES20.GL_BLEND );
            GLES20.glBlendFunc( GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA );

            bubbleController.update( false );

            shader.uniform( "shape", 0.0f );
            shader.uniform( "screenSize", width, height );
            shader.uniform( "color", bubbleColor[ 1 ], bubbleColor[ 2 ], bubbleColor[ 3 ] );

            points.setPosition( bubbleController.getPositions3D( false ) );
            points.setSize( bubbleController.getSizes() );
            points.draw();
        }

        public void onSurfaceChanged( GL10 unused, int width, int height ) {
            this.width = width;
            this.height = height;

            bubbleController.setRect( width, height );

            GLES20.glViewport( 0, 0, width, height );
        }
    }
}
