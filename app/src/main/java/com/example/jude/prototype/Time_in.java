package com.example.jude.prototype;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jude on 12/13/2017.
 */

public class Time_in {


    public Time_in(){



    }

    public String getTime()
    {
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("kk:mm");
        String tt=sdf.format(date);
        return tt;
    }

    public String getDate()
    {
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy");
        String dd=sdf.format(date);
        return dd;
    }

    public String getDiff(String date1,String date2,String time1)
    {
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("kk:mm");
        String time2=sdf.format(date);

        return null;
    }







}
