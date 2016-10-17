package com.henallux.deal_it_version_1.exception;

import android.content.Context;

import com.henallux.deal_it_version_1.R;

/**
 * Created by Demoustier.Julien on 09-08-16.
 */
public class DateException extends Exception
{
    private Context ctx;

    public DateException(Context c)
    {
        ctx =c;
    }

    public String getMessage()
    {
        return ctx.getString(R.string.ErrorDateAnnouncement);
    }
}