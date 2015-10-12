package com.roboshoes.fizzy;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

public class CompanionConfigActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String BACKGROUND = "com.roboshoes.color.background";
    private static final String FOREGROUND = "com.roboshoes.color.foreground";

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
