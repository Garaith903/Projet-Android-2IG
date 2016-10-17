package com.henallux.deal_it_version_1.exception;

import android.content.Context;

import com.henallux.deal_it_version_1.R;

/**
 * Created by Demoustier.Julien on 28-07-16.
 */
public class TitleException extends Exception
{
    private Context ctx;
    private Integer lengthError = null;


    public TitleException (Context c)
    {
        ctx = c;
    }

    public TitleException (Context c, int l)
    {
        this (c);
        lengthError = Integer.valueOf(l);

    }

    public String getMessage()
    {
        if (lengthError != null)
            return ctx.getString(R.string.ErrorTitleLenght);
        else
            return ctx.getString(R.string.ErrorTitleEmpty);
    }
}
