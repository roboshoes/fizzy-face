package com.roboshoes.fizzy.font;


public class LetterFactory {

    public static final int BLOCK_FONT = 0;
    public static final int ROUND_FONT = 1;

    public static final int COLS = 10;
    public static final int ROWS = 14;

    public static ILetter[] factories = {
            new BlockLetter(),
            new RoundLetter()
    };

    private LetterFactory() {}

    public static int[] getBitmap( int font, char character ) {
        return factories[ font ].getBitmap( character );
    }
}
