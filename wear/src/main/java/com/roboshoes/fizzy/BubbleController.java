package com.roboshoes.fizzy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.roboshoes.fizzy.font.BlockLetter;
import com.roboshoes.fizzy.font.LetterFactory;

import java.util.ArrayList;

public class BubbleController {

    private static final int GRID_SIZE = BlockLetter.zero.length;
    private static final int BUBBLE_COUNT = 400;

    private Paint foregroundPaint;
    private Paint backgroundPaint;
    private String latestTime;
    private Bubble[] bubbles = new Bubble[ BUBBLE_COUNT ];
    private int bubblesPerField = 1;
    private int latestFont = -1;
    private int font = LetterFactory.ROUND_FONT;
    private int[][] numbers = new int[ 4 ][ GRID_SIZE ];

    public BubbleController( Paint backgroundPaint, Paint foregroundPaint ) {
        this.backgroundPaint = backgroundPaint;
        this.foregroundPaint = foregroundPaint;

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

    public void draw( Canvas canvas, Rect bounding, boolean fullUpdate, boolean isRound ) {
        canvas.drawRect( 0, 0, bounding.width(), bounding.height(), backgroundPaint );

        float scale = isRound ? 0.6f : 0.75f;

        float[] useArea = new float[] {
                (float) bounding.width() * scale,
                (float) bounding.height() * scale
        };

        float[] zero = new float[] {
                bounding.width() / 2.0f - useArea[ 0 ] / 2.0f,
                bounding.height() / 2.0f - useArea[ 0 ] / 2.0f
        };

        for ( Bubble bubble : bubbles ) {
            bubble.update( fullUpdate );

            float[] position = bubble.getPosition();

            canvas.drawCircle(
                    zero[ 0 ] + position[ 0 ] * useArea[ 0 ],
                    zero[ 1 ] + position[ 1 ] * useArea[ 1 ],
                    bubble.getSize(),
                    foregroundPaint
            );
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

    public void setFont( int value ) {
        this.font = value;
    }
}
