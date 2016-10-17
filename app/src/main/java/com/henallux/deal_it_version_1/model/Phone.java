package com.henallux.deal_it_version_1.model;

import android.content.Context;

import com.henallux.deal_it_version_1.exception.PhoneException;

/**
 * Created by Demoustier.Julien on 28-07-16.
 */
public class Phone {

    private Context ctx;
    private String phoneNumber;

    public Phone (String phNmbr) throws PhoneException
    {
        setPhoneNumber(phNmbr);
    }

    public void setPhoneNumber(String phNmbr) throws PhoneException
    {
        if(phoneNumber.equals(""))
            throw new PhoneException(ctx);
        else if(phNmbr.length() != 10)
            throw new PhoneException(ctx);
        else
            phoneNumber = phNmbr;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

}
