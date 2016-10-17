package com.henallux.deal_it_version_1.exception;

import android.content.Context;

import com.henallux.deal_it_version_1.R;

/**
 * Created by Demoustier.Julien on 28-07-16.
 */
public class DescriptionException extends Exception {
    private Context ctx;
    private Integer lengthError = null;


    public DescriptionException (Context c)
    {
        ctx = c;
    }

    public DescriptionException (Context c, int l)
    {
        this (c);
        lengthError = Integer.valueOf(l);

    }

    public String getMessage()
    {
        if (lengthError != null)
            return ctx.getString(R.string.ErrorDescriptionLenght);
        else
            return ctx.getString(R.string.ErrorDescriptionEmpty);
    }
}
