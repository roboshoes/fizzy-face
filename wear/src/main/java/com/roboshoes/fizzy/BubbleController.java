package com.roboshoes.fizzy;

import com.roboshoes.fizzy.font.BlockLetter;
import com.roboshoes.fizzy.font.LetterFactory;

import java.util.ArrayList;

public class BubbleController {

    private static final int GRID_SIZE = BlockLetter.zero.length;
    private static final int BUBBLE_COUNT = 600;

    private String latestTime;
    private Bubble[] bubbles = new Bubble[ BUBBLE_COUNT ];
    private int bubblesPerField = 1;
    private int latestFont = -1;
    private int font = LetterFactory.BLOCK_FONT;
    private int[][] numbers = new int[ 4 ][ GRID_SIZE ];
    private int width = 0;
    private int height = 0;
    private boolean dirty = false;

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

    public void update() {
        dirty = true;
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

            if ( dirty ) bubbles[ i / 3 ].update( false );

            float[] xy = bubbles[ i / 3 ].getPosition();

            positions[ i ]     = zero[ 0 ] + xy[ 0 ] * useArea[ 0 ];
            positions[ i + 1 ] = zero[ 1 ] + xy[ 1 ] * useArea[ 1 ];
            positions[ i + 2 ] = 0.0f;
        }

        dirty = false;

        return positions;
    }

    public float[] getSizes() {

        float[] sizes = new float[ BUBBLE_COUNT ];

        for( int i = 0; i < BUBBLE_COUNT; i++ ) {

            if ( dirty ) bubbles[ i / 3 ].update( false );

            sizes[ i ] = bubbles[ i ].getSize();
        }

        dirty = false;

        return sizes;
    }
}
