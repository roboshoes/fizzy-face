/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.roboshoes.fizzy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class FizzyFace extends CanvasWatchFaceService {

    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.MILLISECONDS.toMillis( 17 );
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine implements
            DataApi.DataListener,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener  {

        final Handler updateTimeHandler = new EngineHandler( this );
        final BroadcastReceiver timeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive( Context context, Intent intent ) {
                time.clear( intent.getStringExtra( "time-zone" ) );
                time.setToNow();
            }
        };

        private static final String BACKGROUND = "com.roboshoes.color.background";
        private static final String FOREGROUND = "com.roboshoes.color.foreground";
        private static final String FONT = "com.roboshoes.font.type";
        private static final String SHAPE = "com.roboshoes.shape.type";

        private Paint backgroundPaint;
        private Paint bubblePaint;
        private BubbleController bubbleController;
        private Time time;

        private GoogleApiClient googleApiClient = new GoogleApiClient.Builder( FizzyFace.this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApiIfAvailable( Wearable.API )
                .build();

        private boolean isRegisteredTimeZoneReceiver = false;
        private boolean isAmbient;
        private boolean isLowBitAmbient;
        private boolean isRound;

        @Override
        public void onCreate( SurfaceHolder holder ) {
            super.onCreate( holder );

            setWatchFaceStyle( new WatchFaceStyle.Builder( FizzyFace.this )
                    .setCardPeekMode( WatchFaceStyle.PEEK_MODE_VARIABLE )
                    .setBackgroundVisibility( WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE )
                    .setShowSystemUiTime( false )
                    .build() );

            bubblePaint = new Paint();
            bubblePaint.setColor( 0xFFFFDE00 );
            bubblePaint.setAntiAlias( !isAmbient );

            backgroundPaint = new Paint();
            backgroundPaint.setColor( 0xFF2B1330 );

            bubbleController = new BubbleController( backgroundPaint, bubblePaint );

            time = new Time();
        }

        @Override
        public void onDataChanged( DataEventBuffer dataEventBuffer ) {

            final List<DataEvent> events = FreezableUtils.freezeIterable( dataEventBuffer );
            for ( DataEvent event : events ) {

                final Uri uri = event.getDataItem().getUri();
                final String path = uri != null ? uri.getPath() : null;

                if ( "/COLORS".equals( path ) ) {
                    final DataMap map = DataMapItem.fromDataItem( event.getDataItem() ).getDataMap();

                    if ( map.containsKey( BACKGROUND ) )
                        backgroundPaint.setColor( map.getInt( BACKGROUND ) );

                    if ( map.containsKey( FOREGROUND ) )
                        bubblePaint.setColor( map.getInt( FOREGROUND ) );

                    if ( map.containsKey( FONT ) )
                        bubbleController.setFont( map.getInt( FONT ) );

                    if ( map.containsKey( SHAPE  ) )
                        bubbleController.setShape( map.getInt( SHAPE ) );
                }
            }
        }

        @Override
        public void onConnected( Bundle bundle ) {
            Wearable.DataApi.addListener( googleApiClient, Engine.this );
        }

        @Override
        public void onConnectionSuspended( int cause ) {}

        @Override
        public void onConnectionFailed( ConnectionResult connectionResult ) {}

        @Override
        public void onDestroy() {
            updateTimeHandler.removeMessages( MSG_UPDATE_TIME );
            super.onDestroy();
        }

        @Override
        public void onApplyWindowInsets( WindowInsets insets ) {
            super.onApplyWindowInsets( insets );

            isRound = insets.isRound();
        }

        @Override
        public void onVisibilityChanged( boolean visible ) {
            super.onVisibilityChanged( visible );

            if ( visible ) {
                registerReceiver();

                time.clear( TimeZone.getDefault().getID() );
                time.setToNow();

                googleApiClient.connect();
            } else {
                unregisterReceiver();
                googleApiClient.disconnect();
            }

            updateTimer();
        }

        private void registerReceiver() {
            if ( isRegisteredTimeZoneReceiver ) return;

            isRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter( Intent.ACTION_TIMEZONE_CHANGED );
            FizzyFace.this.registerReceiver( timeZoneReceiver, filter );
        }

        private void unregisterReceiver() {
            if ( !isRegisteredTimeZoneReceiver ) return;

            isRegisteredTimeZoneReceiver = false;
            FizzyFace.this.unregisterReceiver( timeZoneReceiver );
        }

        @Override
        public void onPropertiesChanged( Bundle properties ) {
            super.onPropertiesChanged( properties );
            isLowBitAmbient = properties.getBoolean( PROPERTY_LOW_BIT_AMBIENT, false );
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged( boolean inAmbientMode ) {
            super.onAmbientModeChanged( inAmbientMode );

            if ( isAmbient != inAmbientMode ) {
                isAmbient = inAmbientMode;

                if ( isLowBitAmbient ) {
                    bubblePaint.setAntiAlias( !inAmbientMode );
                }

                invalidate();
            }

            updateTimer();
        }

        @Override
        public void onDraw( Canvas canvas, Rect bounds ) {

            Calendar calendar = Calendar.getInstance();

            int hours = calendar.get( Calendar.HOUR );
            int minutes = calendar.get( Calendar.MINUTE );

            if ( hours == 0 ) hours = 12;

            String timeString = String.format( "%02d%02d", hours, minutes );

            bubbleController.setNumber( timeString );
            bubbleController.draw( canvas, bounds, isAmbient, isRound );
        }

        /**
         * Starts the {@link #updateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            updateTimeHandler.removeMessages( MSG_UPDATE_TIME );
            if ( shouldTimerBeRunning() ) {
                updateTimeHandler.sendEmptyMessage( MSG_UPDATE_TIME );
            }
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        private void handleUpdateTimeMessage() {

            invalidate();

            if ( shouldTimerBeRunning() ) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - ( timeMs % INTERACTIVE_UPDATE_RATE_MS );
                updateTimeHandler.sendEmptyMessageDelayed( MSG_UPDATE_TIME, delayMs );
            }
        }
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<FizzyFace.Engine> weakReference;

        public EngineHandler( FizzyFace.Engine reference ) {
            weakReference = new WeakReference<>( reference );
        }

        @Override
        public void handleMessage( Message msg ) {
            FizzyFace.Engine engine = weakReference.get();
            if ( engine != null ) {
                switch ( msg.what ) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }
}
