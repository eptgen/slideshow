package com.sun_rogers.slideshow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by pisgood on 12/28/2016.
 */
public class SlideChanger extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.v(MainActivity.TAG, "gotem");
        String action = intent.getAction();

        if (action.equals(MainActivity.NEXT_SLIDE))
        {
            new MainActivity.NextSlide().execute();
        }
        else if (action.equals(MainActivity.PREV_SLIDE))
        {
            new MainActivity.PrevSlide().execute();
        }
    }
}
