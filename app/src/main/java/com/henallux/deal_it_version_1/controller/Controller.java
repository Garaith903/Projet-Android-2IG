package com.henallux.deal_it_version_1.controller;

import com.henallux.deal_it_version_1.business.Business;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Demoustier.Julien on 02-08-16.
 */
public class Controller {

    private Business business;

    public Controller()
    {
        business = new Business();
    }

    public String cryptPwd(String password) throws NoSuchAlgorithmException
    {
        return business.cryptPwd(password);
    }
}
