package com.buyhatke.smshatke;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.buyhatke.smshatke.adapters.AdapterThread;
import com.buyhatke.smshatke.models.Message;
import com.buyhatke.smshatke.utils.DeliverReceiver;
import com.buyhatke.smshatke.utils.SentReceiver;
import com.melnykov.fab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ssaxena on 7/30/16.
 */
public class ThreadActivity extends AppCompatActivity implements View.OnClickListener, com.buyhatke.smshatke.utils.SentReceiver.SentNotifier, com.buyhatke.smshatke.utils.DeliverReceiver.DeliverNotifier {
    private LinearLayoutManager lmThread;
    private AdapterThread adapterThread;
    List<Message> listMessage = new ArrayList<>();
    private String threadID, address;
    public View mLayout;
    private RecyclerView rvThread;
    BroadcastReceiver sendBroadcastReceiver;
    BroadcastReceiver deliveryBroadcastReciever;
    EditText msgEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        mLayout = findViewById(R.id.rv_thread);
        Intent intent = getIntent();
        threadID = intent.getStringExtra("threadID");
        address = intent.getStringExtra("address");
        getMessages();
        initTb();
        initRv();
        FloatingActionButton btnSendMsg = (FloatingActionButton) findViewById(R.id.msgSendButton);
        btnSendMsg.setOnClickListener(this);
        msgEditText = (EditText) findViewById(R.id.messageEdit);
        sendBroadcastReceiver = new SentReceiver();
        deliveryBroadcastReciever = new DeliverReceiver();

    }

    private void getMessages() {
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), new String[]{"body", "type", "date"}, "thread_id" + " = ?", new String[]{threadID}, "Date DESC");
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                Message message = new Message();
                message.setBody(cursor.getString(cursor.getColumnIndex("body")));
                Date date = new Date(Long.valueOf(cursor.getString(cursor.getColumnIndex("date"))));
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(date);
                message.setDate(formattedDate);
                message.setType(cursor.getString(cursor.getColumnIndex("type")));
                listMessage.add(message);
            }
        } finally {
            cursor.close();
        }
    }

    private void initTb() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(address);
    }

    private void initRv() {
        rvThread = (RecyclerView) findViewById(R.id.rv_thread);
        rvThread.setHasFixedSize(true);
        lmThread = new LinearLayoutManager(this);
        lmThread.setStackFromEnd(true);
        lmThread.setReverseLayout(true);
        adapterThread = new AdapterThread(this, listMessage);
        rvThread.setLayoutManager(lmThread);
        rvThread.setAdapter(adapterThread);
        rvThread.scrollToPosition(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msgSendButton: {
                sendMessage();
                break;
            }

        }
    }

    private void sendMessage() {
        String msg = msgEditText.getText().toString();
        if (msg.trim().equalsIgnoreCase(""))
            Snackbar.make(mLayout,
                    "Empty message. Type message to send",
                    Snackbar.LENGTH_SHORT).show();
        else {
            deliverSms();
        }
    }

    private void deliverSms() {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));

        registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(address, null, msgEditText.getText().toString(), sentPI, deliveredPI);
    }

    @Override
    protected void onPause() {
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
    protected void onDestroy() {
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
            msgEditText.setText("");
            Snackbar.make(mLayout,
                    "Message sent",
                    Snackbar.LENGTH_LONG).show();
            Message message = new Message();
            message.setType("2");
            message.setBody(msgEditText.getText().toString());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            String date = df.format(Calendar.getInstance().getTime());
            message.setDate(date);
            listMessage.add(0, message);
            rvThread.scrollToPosition(0);
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
}
