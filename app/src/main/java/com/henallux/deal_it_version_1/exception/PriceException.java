package com.henallux.deal_it_version_1.exception;

import android.content.Context;

import com.henallux.deal_it_version_1.R;

/**
 * Created by Demoustier.Julien on 28-07-16.
 */
public class PriceException extends Exception {
    private Context ctx;

    public PriceException (Context c)
    {
        ctx = c;
    }

    public String getMessage()
    {
        return ctx.getString(R.string.ErrorType);
    }
}
