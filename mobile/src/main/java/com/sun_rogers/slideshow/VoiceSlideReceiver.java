package com.sun_rogers.slideshow;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;

/**
 * Created by pisgood on 12/29/2016.
 */
public class VoiceSlideReceiver extends BroadcastReceiver
{
    private static final String NEXT_SLIDE_PLEASE = "next slide please";
    private static final String PREV_SLIDE_PLEASE = "previous slide please";

    protected CharSequence getVoiceString(Intent voiceIntent)
    {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(voiceIntent);

        if (remoteInput != null)
        {
            CharSequence result = remoteInput.getCharSequence(MainActivity.EXTRA_VOICE_SLIDE);
            Log.d(MainActivity.TAG, result.toString());
            return result;
        }
        return null;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String whatHeSaid = getVoiceString(intent).toString();

        if (whatHeSaid.equals(NEXT_SLIDE_PLEASE))
        {
            new MainActivity.NextSlide().execute();
        }
        else if (whatHeSaid.equals(PREV_SLIDE_PLEASE))
        {
            new MainActivity.PrevSlide().execute();
        }
        Log.d(MainActivity.TAG, "VoiceSlideReceiver");
    }
}
