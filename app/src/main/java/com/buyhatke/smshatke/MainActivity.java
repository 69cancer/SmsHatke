package com.buyhatke.smshatke;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.buyhatke.smshatke.utils.DeliverReceiver;
import com.buyhatke.smshatke.utils.SentReceiver;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, ConversationsFragment.ChangeAcivitiy,
        ConversationsFragment.ChangeFragment, com.buyhatke.smshatke.utils.SentReceiver.SentNotifier, com.buyhatke.smshatke.utils.DeliverReceiver.DeliverNotifier,
        ComposeFragment.Deliver {

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
    private View mLayout;
    BroadcastReceiver sendBroadcastReceiver;
    BroadcastReceiver deliveryBroadcastReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sendBroadcastReceiver = new SentReceiver();
        deliveryBroadcastReciever = new DeliverReceiver();
        showConversations();
        //setBoolsValue();

    }

//    private void setBoolsValue() {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getResources().getBoolean(R.bool.hasKitKat).
//
//
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            SearchFragment sf = new SearchFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment, sf);
            ft.addToBackStack("addSearchFrag");
            ft.commitAllowingStateLoss();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_SMS) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Sms permission was granted. Starting preview.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                startConversationFrag();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Sms permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void showConversations() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            startConversationFrag();
        } else {
            // Permission is missing and must be requested.
            requestSmsPermission();
        }

    }

    private void requestSmsPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Sms access is required to read Inbox.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_REQUEST_READ_SMS);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting sms permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);
        }
    }

    private void startConversationFrag() {
        ConversationsFragment cf = new ConversationsFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, cf, "conv");
        ft.commitAllowingStateLoss();
    }


    @Override
    public void changeAcitivity(String threadID, String address) {
        Intent intent = new Intent(this, ThreadActivity.class);
        intent.putExtra("threadID", threadID);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    public void changeFrag() {
        ComposeFragment cf = new ComposeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, cf, "compose");
        ft.addToBackStack("addComposeFrag");
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        try {
            unregisterReceiver(sendBroadcastReceiver);
            unregisterReceiver(deliveryBroadcastReciever);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            unregisterReceiver(sendBroadcastReceiver);
            unregisterReceiver(deliveryBroadcastReciever);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void msgSentNotify(String cause) {
        if (cause.equalsIgnoreCase("sent")) {
            // TODO update adapter
            updateAdapter();
        } else {
            Snackbar.make(mLayout,
                    "Message sending failed",
                    Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void msgDeliverNotify(boolean b) {
        if (b) {
            Snackbar.make(mLayout,
                    "Message delivered",
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(mLayout,
                    "Message not delivered",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    public void updateAdapter() {
        startConversationFrag();
    }


    @Override
    public void deliverMsg(String[] addresses, String msg) {
        SmsManager sms = SmsManager.getDefault();
        for (int i = 0; i < addresses.length; i++) {
            String SENT = "SMS_SENT" + addresses[i];
            String DELIVERED = "SMS_DELIVERED" + addresses[i];
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                    SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(DELIVERED), 0);
            registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));
            registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));
            sms.sendTextMessage(addresses[i], null, msg, sentPI, deliveredPI);
        }
    }
}
