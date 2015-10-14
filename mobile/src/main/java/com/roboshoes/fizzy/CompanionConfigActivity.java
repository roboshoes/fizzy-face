package com.roboshoes.fizzy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.roboshoes.fizzy.ui.CircleView;
import com.roboshoes.fizzy.ui.Colors;

public class CompanionConfigActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String BACKGROUND = "com.roboshoes.color.background";
    private static final String FOREGROUND = "com.roboshoes.color.foreground";
    private static final String FONT = "com.roboshoes.font.type";
    private static final String SHAPE = "com.roboshoes.shape.type";

    private static final int BLOCK_FONT = 0;
    private static final int ROUND_FONT = 1;

    private static final int CIRCLE = 0;
    private static final int PLUS = 1;

    private GoogleApiClient googleApiClient;
    private Boolean isConnected = false;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        googleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApiIfAvailable( Wearable.API )
                .build();

        LinearLayout backgroundColorHolder = (LinearLayout) findViewById( R.id.background_color_holder );
        LinearLayout bubbleColorHolder = (LinearLayout) findViewById( R.id.bubble_color_holder );

        fillContainer( bubbleColorHolder, FOREGROUND );
        fillContainer( backgroundColorHolder, BACKGROUND );

        initFontButton( (Button) findViewById( R.id.block_font_button ), BLOCK_FONT );
        initFontButton( (Button) findViewById( R.id.round_font_button ), ROUND_FONT );

        initShapeButton( (Button) findViewById( R.id.circle_shape_button ), CIRCLE );
        initShapeButton( (Button) findViewById( R.id.plus_shape_button ), PLUS );
    }

    private void fillContainer( LinearLayout container, String target ) {
        int[] colors = target.equals( BACKGROUND ) ? Colors.BACKGROUND : Colors.FOREGROUND;

        for ( int i = 0; i < colors.length; i++ ) {
            CircleView circle = new CircleView( this, colors[ i ], target, i == colors.length - 1 );

            circle.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    CircleView circle = (CircleView) view;

                    int color = circle.getColor();
                    String target = circle.getTarget();

                    setData( target, color );
                }
            } );

            container.addView( circle );
        }
    }

    private void initFontButton( Button button, final int font ) {
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                setData( FONT, font );
            }
        } );
    }

    private void initShapeButton( Button button, final int shape ) {
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                setData( SHAPE, shape );
            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void setData( String id, int color ) {

        if ( ! isConnected ) return;

        PutDataMapRequest putRequest = PutDataMapRequest.create( "/COLORS" );
        DataMap map = putRequest.getDataMap();

        map.putInt( id, color );

        Wearable.DataApi.putDataItem( googleApiClient, putRequest.asPutDataRequest() );
    }


    @Override
    public void onConnected( Bundle bundle ) {
        isConnected = true;
    }

    @Override
    public void onConnectionSuspended( int i ) {
        isConnected = false;
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult ) {
        isConnected = false;
    }
}
