package com.buyhatke.smshatke.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;

import com.buyhatke.smshatke.MainActivity;
import com.buyhatke.smshatke.R;

/**
 * Created by ssaxena on 7/31/16.
 */
public class MessagingReceiver extends BroadcastReceiver {
    private final String TAG = "MessagingReceiver";
    private Context mContext;
    private String mAddress;
    private String mBody;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        abortBroadcast();
        if (intent.getExtras() != null) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            SmsMessage sms = messages[0];
            if (messages.length == 1 || sms.isReplace()) {
                mBody = sms.getDisplayMessageBody();
            } else {
                StringBuilder bodyText = new StringBuilder();
                for (SmsMessage message : messages) {
                    bodyText.append(message.getMessageBody());
                }
                mBody = bodyText.toString();
            }

            mAddress = sms.getDisplayOriginatingAddress();

        }
        insertMessageAndNotify();
    }

    private void insertMessageAndNotify() {
        int mId = 0;
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(mAddress)
                        .setContentText(mBody);
        mBuilder.setAutoCancel(true);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        mBuilder.setVibrate(pattern);
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());

    }
}
