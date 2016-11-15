package com.sarriel.pass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by matej on 27.3.2016.
 */
public class ClipboardHelper {
    private static final String CLIPBOARD_LABEL = "Pass";

    /**
     * Copy parameter text into clipboard
     * @param text
     */
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(CLIPBOARD_LABEL, text);
        clipboard.setPrimaryClip(clip);
    }

}
