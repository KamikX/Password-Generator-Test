package com.sarriel.pass.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;
import com.sarriel.pass.App;
import com.sarriel.pass.ClipboardHelper;
import com.sarriel.pass.service.PasswordGenException;
import com.sarriel.pass.R;
import com.sarriel.pass.Storage;

/**
 * Created by matej on 12.8.2016.
 */
public class DirectReplyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String input = remoteInput.getCharSequence(PasswordGeneratorNotification.KEY_TEXT_REPLY).toString();
            Log.d(DirectReplyReceiver.class.getSimpleName(), input);
            PasswordGeneratorNotification.showNotification(context);
            generatePassword(context, input);
        } else {
        }
    }

    private void generatePassword(Context context, String alias) {
        Toast.makeText(context, R.string.notification_toast_clipboard, Toast.LENGTH_LONG).show();
        String secret = Storage.getSecret(context);
        try {
            // generate password in bound service
            String password =  App.getInstance().getService().generatePassword(alias, secret);
            ClipboardHelper.copyToClipboard(context, password);
            Toast.makeText(context, R.string.notification_toast_clipboard, Toast.LENGTH_LONG).show();
        } catch (PasswordGenException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.toast_err_pass_generator, Toast.LENGTH_LONG).show();
        }
    }
}
