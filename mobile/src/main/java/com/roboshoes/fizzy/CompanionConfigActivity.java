package com.roboshoes.fizzy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.roboshoes.fizzy.ui.CircleView;

public class CompanionConfigActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String BACKGROUND = "com.roboshoes.color.background";
    private static final String FOREGROUND = "com.roboshoes.color.foreground";
    private static final String FONT = "com.roboshoes.font.type";
    private static final int BLOCK_FONT = 0;
    private static final int ROUND_FONT = 1;

    private GoogleApiClient googleApiClient;
    private Boolean isConnected = false;

    private LinearLayout backgroundColorHolder;
    private LinearLayout bubbleColorHolder;

    private Button blockFontButton;
    private Button roundFontButton;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        googleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApiIfAvailable( Wearable.API )
                .build();

        backgroundColorHolder = (LinearLayout) findViewById( R.id.background_color_holder );
        bubbleColorHolder = (LinearLayout) findViewById( R.id.bubble_color_holder );

        blockFontButton = (Button) findViewById( R.id.block_font_button );
        roundFontButton = (Button) findViewById( R.id.round_font_button );

        fillContainer( bubbleColorHolder, FOREGROUND );
        fillContainer( backgroundColorHolder, BACKGROUND );

        initButton( blockFontButton, BLOCK_FONT );
        initButton( roundFontButton, ROUND_FONT );
    }

    private void fillContainer( LinearLayout container, String target ) {
        int[] colors = { 0xff37474f, 0xff009688, 0xff80cbc4, 0xffff0000 };

        for ( int i = 0; i < 4; i++ ) {
            CircleView circle = new CircleView( this, colors[ i ], target );

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

    private void initButton( Button button, final int font ) {
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                setData( FONT, font );
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
