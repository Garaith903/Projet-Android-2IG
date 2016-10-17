package com.henallux.deal_it_version_1.model;

import android.content.Context;

import com.henallux.deal_it_version_1.exception.EmailException;
import com.henallux.deal_it_version_1.exception.PhoneException;

/**
 * Created by Demoustier.Julien on 28-07-16.
 */
public class Email {
    private Context ctx;
    private String emailPerson;

    public Email (String emai) throws EmailException
    {
        setEmail(emai);
    }

    public void setEmail(String emailIn) throws EmailException
    {
        String acceptedExpression = "^[a-z0-9._-]+@([a-z0-9._-]+)\\.[a-z]{2,6}$";

        if(emailIn.equals(""))
            throw new EmailException(ctx);
        else if (emailIn.matches(acceptedExpression) == false)
            throw new EmailException(ctx);
        else
            emailPerson = emailIn;
    }

    public String getEmailPerson()
    {
        return emailPerson;
    }
}
