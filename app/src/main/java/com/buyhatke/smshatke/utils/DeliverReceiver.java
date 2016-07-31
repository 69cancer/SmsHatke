package com.buyhatke.smshatke.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ssaxena on 7/30/16.
 */
public class DeliverReceiver extends BroadcastReceiver {
    DeliverNotifier deliverNotifier;

    @Override
    public void onReceive(Context context, Intent intent) {
        deliverNotifier = (DeliverNotifier) context;
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                deliverNotifier.msgDeliverNotify(true);
                break;
            case Activity.RESULT_CANCELED:
                deliverNotifier.msgDeliverNotify(false);
                break;
        }
    }

    public interface DeliverNotifier {
        void msgDeliverNotify(boolean b);
    }
}
