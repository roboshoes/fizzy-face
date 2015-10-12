package com.roboshoes.fizzy.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.roboshoes.fizzy.R;
import com.roboshoes.fizzy.utils.ColorUtils;

/**
 * Created by roboshoes on 10/12/15.
 */
public class CircleView extends ImageView {
    public CircleView( Context context ) {
        super( context );

        int margin = (int) ( context.getResources().getDisplayMetrics().density * 20 );

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins( margin, margin, margin, margin );

        GradientDrawable shape = (GradientDrawable) context.getDrawable( R.drawable.circle );
        shape.setColor( ColorUtils.createRandomColor() );

        this.setBackground( shape );
        this.setLayoutParams( params );
    }


}
