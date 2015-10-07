package com.roboshoes.fizzy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Random;

public class CompanionConfigActivity extends Activity {

    private static final String TAG = "CompanionConfigActivity";

    private static final String BACKGROUND = "com.roboshoes.color.background";
    private static final String FOREGROUND = "com.roboshoes.color.foreground";

    private GoogleApiClient googleApiClient;
    private Random random = new Random();

    private Boolean isConnected = false;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        googleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected( Bundle bundle ) {
                        Log.d( TAG, "connected" );
                        isConnected = true;
                    }

                    @Override
                    public void onConnectionSuspended( int i ) {
                        Log.d( TAG, "suspended" );
                        isConnected = false;
                    }
                } )
                .addOnConnectionFailedListener( new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed( ConnectionResult connectionResult ) {
                        Log.d( TAG, "couldn't connect" );
                        isConnected = false;
                    }
                } )
                .addApiIfAvailable( Wearable.API )
                .build();

        Button backgroundButton = (Button) findViewById( R.id.background );
        Button foregroundButton = (Button) findViewById( R.id.foreground );

        backgroundButton.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick( View view ) {
                if ( ! isConnected ) return;

                setData( BACKGROUND, createRandomColor() );
            }
        } );

        foregroundButton.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick( View view ) {
                if ( !isConnected ) return;

                setData( FOREGROUND, createRandomColor() );
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
        PutDataMapRequest putRequest = PutDataMapRequest.create( "/COLORS" );
        DataMap map = putRequest.getDataMap();

        map.putInt( id, color );

        Wearable.DataApi.putDataItem( googleApiClient, putRequest.asPutDataRequest() );

        Log.d( TAG, "data put todally and stuff" );
    }

    private int createRandomColor() {
        return Color.argb( 255, random.nextInt( 256 ), random.nextInt( 256 ), random.nextInt( 256 ) );
    }

}
