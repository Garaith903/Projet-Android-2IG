package com.henallux.deal_it_version_1.model;

import android.content.Context;

import com.henallux.deal_it_version_1.exception.GenderException;
import com.henallux.deal_it_version_1.exception.IdException;

import java.util.GregorianCalendar;


/**
 * Created by Demoustier.Julien on 28-07-16.
 */
public class Person {

    private Context context ;
    private String id;
    private String password;
    private String firstName;
    private String lastName;
    private String adress;
    private String nationality;
    private GregorianCalendar birthDate;
    private char gender;

    public Person(String id, String password, String firstName, String lastName, String adress, String nationality, GregorianCalendar birthDate, char gender) throws IdException, GenderException {
        setId(id);
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adress = adress;
        this.nationality = nationality;
        this.birthDate = birthDate;
        setGender(gender);
    }

    public void setId (String idIN) throws IdException {
        String acceptedExpression = "^etu[0-9]{3}$";

        if (idIN.matches(acceptedExpression)) {
            this.id = idIN;
        } else {
            throw new IdException(context);
        }
    }

    public void setGender (char genderIn) throws GenderException
    {
        if (genderIn == 'F' || genderIn == 'M')
            this.gender = genderIn;
        else
            throw new GenderException(context);
    }

    public String getPersonId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getAdress()
    {
        return adress;
    }

    public String getNationality()
    {
        return nationality;
    }

    public GregorianCalendar getBirthDate()
    {
        return birthDate;
    }

    public char getGender()
    {
        return gender;
    }

}
