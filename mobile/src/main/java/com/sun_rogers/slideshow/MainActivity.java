package com.sun_rogers.slideshow;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    public static String NEXT_SLIDE = "com.sun_rogers.slideshow.next";
    public static String PREV_SLIDE = "com.sun_rogers.slideshow.previous";
    public static String TAG = "Slideshow";
    public static final String EXTRA_VOICE_SLIDE = "extra_voice_slide";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static String readConnection(HttpURLConnection connection)
    {
        try {
            InputStream site = new DataInputStream(connection.getInputStream());
            Scanner scanner = new Scanner(site);
            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {

        }
        return null;
    }

    public static class NextSlide extends AsyncTask<Void, Void, String>
    {
        protected String doInBackground(Void... params)
        {
            try {
                String url = "http://sun-rogers.com/slideshow/nextslide/";
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                String res = readConnection(connection);
            } catch (MalformedURLException e) {

            } catch (IOException e) {

            }

            return "success";
        }
    }

    public static class PrevSlide extends AsyncTask<Void, Void, String>
    {
        protected String doInBackground(Void... params)
        {
            try {
                String url = "http://sun-rogers.com/slideshow/prevslide/";
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                String res = readConnection(connection);
            } catch (MalformedURLException e) {

            } catch (IOException e) {

            }

            return "success";
        }
    }

    public class ChangeShow extends AsyncTask<Integer, Void, String>
    {
        protected String doInBackground(Integer... params)
        {
            int id = params[0];

            try {
                String url = "http://sun-rogers.com/slideshow/change/show/?show=" + id;
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                String res = readConnection(connection);
            } catch (MalformedURLException e) {

            } catch (IOException e) {

            }

            return "success";
        }
    }

    public void nextSlide(View view) {
        new NextSlide().execute();
    }

    public void prevSlide(View view) {
        new PrevSlide().execute();
    }

    public void changeShow(View view) {
        EditText editText = (EditText) findViewById(R.id.changeShow);
        int id = Integer.parseInt(editText.getText().toString());
        new ChangeShow().execute(id);
    }

    public void notification(View view) {

        Intent show = new Intent(this, MainActivity.class);
        PendingIntent showPending = PendingIntent.getActivity(this, 0, show, 0);
        Log.v(TAG, "Showing notification");

        Intent nextSlide = new Intent(this, SlideChanger.class);
        nextSlide.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        nextSlide.setAction(NEXT_SLIDE);
        PendingIntent nextSlidePending = PendingIntent.getBroadcast(this, 0, nextSlide, 0);

        Intent prevSlide = new Intent(this, SlideChanger.class);
        prevSlide.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        prevSlide.setAction(PREV_SLIDE);
        PendingIntent prevSlidePending = PendingIntent.getBroadcast(this, 0, prevSlide, 0);

        String speakLabel = getString(R.string.speakSlideLabel);

        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_SLIDE)
                .setLabel(speakLabel)
                .build();

        Intent speakIntent = new Intent(this, VoiceSlideReceiver.class);
        PendingIntent speakIntentPending = PendingIntent.getBroadcast(this, 0, speakIntent, 0);

        NotificationCompat.Action speakAction =
                new NotificationCompat.Action.Builder(R.drawable.speak,
                        getString(R.string.speakLabel), speakIntentPending)
                .addRemoteInput(remoteInput)
                .build();

        NotificationCompat.Builder notBuilder =
                new NotificationCompat.Builder(this)
                .setContentText(getString(R.string.notificationDesc))
                .setSmallIcon(R.drawable.next_slide)
                .setContentTitle(getString(R.string.notificationTitle))
                .setContentIntent(showPending)
                .extend(new NotificationCompat.WearableExtender().addAction(speakAction))
                .addAction(R.drawable.prev_slide, getString(R.string.actionPrevSlide), prevSlidePending)
                .addAction(R.drawable.next_slide, getString(R.string.actionNextSlide), nextSlidePending);

        NotificationManagerCompat notManager = NotificationManagerCompat.from(this);

        notManager.notify(1, notBuilder.build());

    }

}




































