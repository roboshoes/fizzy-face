package com.roboshoes.fizzy.utils;

public class ArrayUtils {

    public static int[] concat( int[] a, int[] b ) {
        int length = a.length + b.length;

        int[] array = new int[ length ];

        System.arraycopy( a, 0, array, 0, a.length );
        System.arraycopy( b, 0, array, a.length, b.length );

        return array;
    }
}
