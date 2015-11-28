package com.roboshoes.fizzy;

import com.roboshoes.fizzy.font.BlockLetter;
import com.roboshoes.fizzy.font.LetterFactory;

import java.util.ArrayList;

public class BubbleController {

    private static final int GRID_SIZE = BlockLetter.zero.length;
    private static final int BUBBLE_COUNT = 400;

    private String latestTime;
    private Bubble[] bubbles = new Bubble[ BUBBLE_COUNT ];
    private int bubblesPerField = 1;
    private int latestFont = -1;
    private int font = LetterFactory.BLOCK_FONT;
    private int shape = Bubble.PLUS;
    private int[][] numbers = new int[ 4 ][ GRID_SIZE ];
    private int width = 0;
    private int height = 0;

    public BubbleController() {
        for ( int i = 0; i < BUBBLE_COUNT; i++ ) {
            bubbles[ i ] = new Bubble( 0.5f, 0.5f );
        }
    }

    private void updatePosition() {
        int used = 0;

        ArrayList<float[]> possibleFields = new ArrayList<>();

        for ( int j = 0; j < 4; j++ ) {
            for ( int i = 0; i < GRID_SIZE; i++ ) {
                if ( numbers[ j ][ i ] == 1 ) {

                    int row = (int) Math.floor( (float) i / LetterFactory.COLS );
                    int col = i % LetterFactory.COLS;

                    float colPercent = ( j % 2 == 0 ? 0.0f : 0.55f ) + ( col / ( LetterFactory.COLS - 1.0f ) )  * 0.45f;
                    float rowPercent = ( j < 2 ? 0.0f : 0.55f ) + ( row / ( LetterFactory.ROWS - 1.0f ) ) * 0.45f;

                    possibleFields.add( new float[] { colPercent, rowPercent } );

                    for ( int k = 0; k < bubblesPerField; k++ ) {
                        bubbles[ used++ ].setOrigin( colPercent, rowPercent );
                    }
                }
            }

        }

        while ( used < BUBBLE_COUNT ) {
            float[] position = possibleFields.get( (int) ( Math.random() * possibleFields.size() ) );
            bubbles[ used++ ].setOrigin( position[ 0 ], position[ 1 ] );
        }
    }

//    public void draw( Canvas canvas, Rect bounding, boolean fullUpdate, boolean isRound ) {
//        canvas.drawRect( 0, 0, bounding.width(), bounding.height(), backgroundPaint );
//
//        float scale = isRound ? 0.6f : 0.75f;
//
//        float[] useArea = new float[] {
//                (float) bounding.width() * scale,
//                (float) bounding.height() * scale
//        };
//
//        float[] zero = new float[] {
//                bounding.width() / 2.0f - useArea[ 0 ] / 2.0f,
//                bounding.height() / 2.0f - useArea[ 0 ] / 2.0f
//        };
//
//        for ( Bubble bubble : bubbles ) {
//            bubble.update( fullUpdate );
//
//            float[] position = bubble.getPosition();
//
//            float x = zero[ 0 ] + position[ 0 ] * useArea[ 0 ];
//            float y = zero[ 1 ] + position[ 1 ] * useArea[ 1 ];
//
//            if ( shape == Bubble.CIRCLE ) {
//
//                canvas.drawCircle(
//                        x,
//                        y,
//                        bubble.getSize(),
//                        foregroundPaint
//                );
//
//            } else if ( shape == Bubble.PLUS ) {
//
//                float size = Math.max( bubble.getSize() * 2f, 2f );
//
//                canvas.save();
//                canvas.translate( x, y );
//                canvas.rotate( bubble.getRotation() );
//
//                canvas.drawLine( - size, 0, size, 0, foregroundPaint );
//                canvas.drawLine( 0, - size, 0, size, foregroundPaint );
//
//                canvas.restore();
//            }
//        }
//    }

    public void setNumber( String time ) {
        if ( time.equals( latestTime ) && latestFont == font ) return;

        latestTime = time;
        latestFont = font;

        int totalFields = 0;

        for ( int j = 0; j < 4; j++ ) {
            numbers[ j ] = LetterFactory.getBitmap( font, time.charAt( j ) );

            for ( int i = 0; i < GRID_SIZE; i++ ) {
                if ( numbers[ j ][ i ] == 1 ) totalFields++;
            }
        }

        bubblesPerField = (int) Math.floor( BUBBLE_COUNT / totalFields );

        updatePosition();
    }

    public void setRect( int width, int height ) {
        this.width = width;
        this.height = height;
    }

    public void setFont( int value ) {
        this.font = value;
    }

    public void setShape( int value ) {
        this.shape = value;
    }

    public float[] getPositions3D( boolean isRound ) {

        float[] positions = new float[ 3 * BUBBLE_COUNT ];

        float scale = isRound ? 0.6f : 0.75f;

        float[] useArea = new float[] {
                (float) width * scale,
                (float) height * scale
        };

        float[] zero = new float[] {
                width / 2.0f - useArea[ 0 ] / 2.0f,
                height / 2.0f - useArea[ 0 ] / 2.0f
        };

        for ( int i = 0; i < bubbles.length * 3; i += 3 ) {

            bubbles[ i / 3 ].update( false );

            float[] xy = bubbles[ i / 3 ].getPosition();

            positions[ i ]     = zero[ 0 ] + xy[ 0 ] * useArea[ 0 ];
            positions[ i + 1 ] = zero[ 1 ] + xy[ 1 ] * useArea[ 1 ];
            positions[ i + 2 ] = 0.0f;
        }

        return positions;
    }

    public float[] getSizes() {
        float[] sizes = new float[ BUBBLE_COUNT ];

        for( int i = 0; i < BUBBLE_COUNT; i++ ) {
            sizes[ i ] = bubbles[ i ].getSize();
        }

        return sizes;
    }
}
