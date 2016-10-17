package com.henallux.deal_it_version_1.model;

import android.content.Context;

import com.henallux.deal_it_version_1.exception.DateException;
import com.henallux.deal_it_version_1.exception.DescriptionException;
import com.henallux.deal_it_version_1.exception.IdException;
import com.henallux.deal_it_version_1.exception.PriceException;
import com.henallux.deal_it_version_1.exception.TitleException;
import com.henallux.deal_it_version_1.exception.TypeException;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Demoustier.Julien on 28-07-16.
 */
public class Announcement
{
    private Context context;
    private int idAnnouncement;
    private String category;
    private String typeAnnounce;
    private String titleAnnounce;
    private GregorianCalendar dateReleaseAnnounce;
    private String description;
    private Double price;
    private String idAnnonceur;

    public Announcement (int idA, String cat, String type, String title, String descript, String dateAnn, Double pr, String idPost) throws IdException, TypeException, TitleException, DescriptionException, PriceException, DateException
    {
        idAnnouncement = idA;
        category = cat;
        //setType(type);
        typeAnnounce = type;
        //setTitle(title);
        titleAnnounce = title;
        setDateString(dateAnn);
        //setdescripton(descript);
        description = descript;
        price = pr;
        //setID(idPost);
        idAnnonceur = idPost;
    }

    public void setID(String idA) throws IdException
    {
        String acceptedExpression = "^etu[0-9]{3}$";

        if (idA.matches(acceptedExpression))
        {
            idAnnonceur = idA;
        }
        else
        {
            throw new IdException(context);
        }
    }

    public void setType(String typeIn) throws TypeException
    {
        if ((typeIn.equals("Offre")) || (typeIn.equals("Demande")) )
            typeAnnounce = typeIn;
        else
            throw new TypeException(context);
    }

    public void setTitle(String titleIn) throws TitleException
    {
        //verrification chaine non vide
        if (titleIn == "")
            throw new TitleException(context);
            //verrification chaine de taille < 60
        else if (titleIn.length() > 60)
            throw new TitleException(context, titleIn.length());
        else
            titleAnnounce = titleIn;
    }

    public void setdescripton(String descript) throws DescriptionException
    {
        //verrification chaine non vide
        if (descript == "")
            throw new DescriptionException(context);
        //verrification chaine de taille <= 500
        else if (descript.length() > 500)
            throw new DescriptionException(context, descript.length());
        else
            description = descript;
    }

    public void setPrice(Double priceIn) throws PriceException
    {
        //verification prix via expression reguliere

        // expression reguliere
        String acceptedExpr = "^[0-9]{1,4}\\.[0-9]{1,2}$";

        String priceString = Double.toString(priceIn);

        //si prix non nul
        if (priceIn > 0)
        {
            //et si forme correcte.
            if(priceString.matches(acceptedExpr))
            {
                price = priceIn;
            }
        }
        else
        {
            throw new PriceException(context);
        }
    }

    public void setDate(GregorianCalendar dateAnn) throws DateException
    {
        GregorianCalendar dayDate = new GregorianCalendar();

        if(dateAnn.after(dayDate))
            throw new DateException(context);
        else
            dateReleaseAnnounce = dateAnn;
    }


    public void setDateString(String dateAnn) throws DateException
    {
        String [] cuttedDate = dateAnn.split("-");
        GregorianCalendar gCal = new GregorianCalendar(Integer.valueOf(cuttedDate[0]),((Integer.valueOf(cuttedDate[1]))-1) , Integer.valueOf(cuttedDate[2]));

        setDate(gCal);
    }

    public void setCategory (String cat)
    {
        category = cat;
    }

    public void setDateAnnounce(GregorianCalendar dateAnn)
    {
        dateReleaseAnnounce = dateAnn;
    }


    public GregorianCalendar getDateReleaseAnnounce()
    {
        return dateReleaseAnnounce;
    }


    //GETTORS
    public int getIdAnnoncement() { return idAnnouncement; }

    public String getCategory() { return category; }

    public String getTypeAnnounce() { return typeAnnounce; }

    public String getTitleAnnounce() { return titleAnnounce; }

    public String getDescription() { return description; }

    public String getDateReleaseAnnounceString() {
        String dt = "";
        dt += dateReleaseAnnounce.get(GregorianCalendar.YEAR)+"-"+((dateReleaseAnnounce.get(GregorianCalendar.MONTH))+1)+"-"+dateReleaseAnnounce.get(GregorianCalendar.DAY_OF_MONTH);
        return dt;
    }

    public Double getPrice() { return price; }

    public String getIdAnnonceur()
    {
        return idAnnonceur;
    }


}
