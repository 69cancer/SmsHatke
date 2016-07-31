package com.buyhatke.smshatke.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ssaxena on 7/30/16.
 */
public class SentReceiver extends BroadcastReceiver {
    SentNotifier sentNotifier;

    @Override
    public void onReceive(Context context, Intent intent) {
        sentNotifier = (SentNotifier) context;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                sentNotifier.msgSentNotify("sent");
                break;
            default:
                sentNotifier.msgSentNotify("failed");
        }
    }

    public interface SentNotifier {
        void msgSentNotify(String cause);
    }
}
