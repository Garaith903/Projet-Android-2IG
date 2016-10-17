package com.henallux.deal_it_version_1.model;

import android.content.Context;

import com.henallux.deal_it_version_1.exception.DateException;
import com.henallux.deal_it_version_1.exception.IdException;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Demoustier.Julien on 28-07-16.
 */
public class Consultation {

    private Context context;

    private String idConsultation;
    private GregorianCalendar dateConsultation;
    private String idUtilisateur;
    private int idAnnouncement;

    public Consultation (String idConsul, String dateConsult, String idViewer, int idAnno) throws IdException, DateException
    {
        idConsultation = idConsul;
        setDateConsultationString(dateConsult);
        idUtilisateur = idViewer;
        idAnnouncement = idAnno;

    }

    public void setDate(GregorianCalendar dateCons) throws DateException
    {
        GregorianCalendar dayDate = new GregorianCalendar();

        if(dateCons.after(dayDate))
            throw new DateException(context);
        else
            dateConsultation = dateCons;
    }

    public void setDateConsultationString (String date) throws DateException
    {
        String [] cuttedDate = date.split("-");
        GregorianCalendar gCal = new GregorianCalendar(Integer.valueOf(cuttedDate[0]),((Integer.valueOf(cuttedDate[1]))-1) , Integer.valueOf(cuttedDate[2]));

        setDate(gCal);
    }

    public void setIdViewer (String idIN) throws IdException
    {
        Pattern p = Pattern.compile("^etu[0-9]{3}$");
        Matcher m = p.matcher(idIN);
        if(m.matches())
            idUtilisateur = idIN;
        else
            throw new IdException(context);
        /*
        String acceptedExpression = "^etu[0-9]{3}$";

        if (idIN.matches(acceptedExpression))
        {
            idViewer = idIN;
        }
        throw new IdException(ctx);
        */
    }

    public void setDateConsultation(GregorianCalendar dateCons)
    {
        dateConsultation = dateCons;
    }


    //GETTORS

    public String getIdConsultation()
    {
        return idConsultation;
    }

    public GregorianCalendar getDateConsultation()
    {
        return dateConsultation;
    }

    public String getDateConstultationString()
    {
        String dt = "";
        dt += dateConsultation.get(GregorianCalendar.YEAR)+"-"+( (dateConsultation.get(GregorianCalendar.MONTH)) +1)+"-"+dateConsultation.get(GregorianCalendar.DAY_OF_MONTH);
        return dt;
    }

    public String getIdViewer()
    {
        return idUtilisateur;
    }

    public int getIdAnnouncement () { return idAnnouncement; }

}
