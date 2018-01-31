package com.example.jude.prototype;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntityHC4;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


import static android.content.ContentValues.TAG;

/**
 * Created by Jude on 10/22/2017.
 */

public class Connector {


    public Connector(){

    }

    JSONObject shift_data;
    String check;
    String sql_command;
    public String testLogin()
    {
        URL url;
        HttpURLConnection con;
        try
        {
            url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/myconnection.php");
            con=(HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader=new BufferedReader((new InputStreamReader(con.getInputStream())));

            String json="";
            String s;
            while((s=bufferedReader.readLine())!=null)
            {

                json+=s+"\n";
            }

            JSONArray jsonArray=new JSONArray(json);
            String user="empty";
            String pass="empty";
                JSONObject obj =jsonArray.getJSONObject(0);
                user=obj.getString("username");
                pass=obj.getString("password");


            return user + pass;
        }
        catch(Exception t)
        {
            System.out.print(t.toString());
            return "Exception here :"+t.toString();
        }
    }

    public String requestLogin(String user,String pass)
    {

        try
        {
            URL url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/verifyLogin.php");
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            String sqlcommand="SELECT * FROM Employee_data WHERE emp_no="+user+" AND PASSWORD='"+pass+"'";
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.connect();
            StringBuffer packedData=new StringBuffer();
            packedData.append(URLEncoder.encode("sqlcommand","UTF-8"));
            packedData.append("=");
            packedData.append(URLEncoder.encode(sqlcommand,"UTF-8"));
            String item=packedData.toString();
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            wr.write(item);
            wr.flush();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb=new StringBuilder();

                String line;
                line=reader.readLine();
                if(line.length()==0)
                {
                    return "HTTP NOT OKAY";
                }
                else
                {
                    if(line.length()==1)
                    {
                        return line;
                    }
                    else
                    {
                        String v;
                        while((v=reader.readLine())!=null)
                        {
                            line+=" "+v;
                        }
                        return line;
                    }
                }
        }
        catch(Exception e)
        {
            return e.toString();
        }

    }

    public int addLocation(String user, double x,double y)
    {
        try
        {
            URL url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/verifyLogin.php");
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            String googlemapLink="https://www.google.com/maps/?q="+x+","+y;
            String sqlcommand="INSERT INTO `locations` (`user`, `location`) VALUES ('"+user+"','"+googlemapLink+"')";
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.connect();
            StringBuffer packedData=new StringBuffer();
            packedData.append(URLEncoder.encode("sqlcommand","UTF-8"));
            packedData.append("=");
            packedData.append(URLEncoder.encode(sqlcommand,"UTF-8"));
            String item=packedData.toString();
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            wr.write(item);
            wr.flush();
            return 1;
        }
        catch(Exception e)
        {
            return -1;
        }

    }

    public String CheckTimeIn(String user)
    {

        try
        {
            URL url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/time_in.php");
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            String sqlcommand="SELECT * FROM Time WHERE USERNAME='"+user+"' AND status=1";
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.connect();
            StringBuffer packedData=new StringBuffer();
            packedData.append(URLEncoder.encode("sqlcommand","UTF-8"));
            packedData.append("=");
            packedData.append(URLEncoder.encode(sqlcommand,"UTF-8"));
            String item=packedData.toString();
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            wr.write(item);
            wr.flush();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            line=reader.readLine();
            if(line.length()==0)
            {
                return "HTTP NOT OKAY";
            }
            else
            {
                if(line.length()==1)
                {
                    if(line.equals("1"))
                    return "1";
                    else if(line.equals("0"))
                    {

                        return"0";

                    }
                    return "May problem here";
                }
                else
                {
                    String v;
                    while((v=reader.readLine())!=null)
                    {
                        line+=" "+v;
                    }
                    return line;
                }
            }

        }
        catch(Exception e)
        {
            return e.toString();
        }

    }

    public String PerformTimeIn(String command)
    {
        try
        {

            URL url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/registerTimeIn.php");
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            StringBuffer packedData=new StringBuffer();
            packedData.append(URLEncoder.encode("sqlcommand","UTF-8"));
            packedData.append("=");
            packedData.append(URLEncoder.encode(command,"UTF-8"));
            String item=packedData.toString();
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            wr.write(item);
            wr.flush();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            line=reader.readLine();
            if(line.length()==0)
            {
                return "HTTP NOT OKAY";
            }
            else
            {
                if(line.length()==1)
                {
                    return "okay";
                }
                else
                {
                    String v;
                    while((v=reader.readLine())!=null)
                    {
                        line+=" "+v;
                    }
                    return line;
                }
            }

        }
        catch(Exception e)
        {
            return e.toString();
        }


    }
// // TODO: 12/20/2017  
    public String RequestLeave(String user,String date1,String date2,String type)
    {
        String command="INSERT INTO LEAVE (user,start_date,end_date,type) VALUES('"+user+"','"+date1+"','"+date2+"','"+type+"')";

        try
        {
            URL url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/leavePerformer.php");
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.connect();
            StringBuffer packedData=new StringBuffer();
            packedData.append(URLEncoder.encode("sqlcommand","UTF-8"));
            packedData.append("=");
            packedData.append(URLEncoder.encode(command,"UTF-8"));
            String item=packedData.toString();
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            wr.write(item);
            wr.flush();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            line=reader.readLine();
            if(line.length()==0)
            {
                return "HTTP NOT OKAY";
            }
            else
            {
                if(line.length()==1)
                {
                    return "okay";
                }
                else
                {
                    String v;
                    while((v=reader.readLine())!=null)
                    {
                        line+=" "+v;
                    }
                    return line;
                }
            }




        }
        catch(Exception e)
        {
            return e.toString();
        }
    }
    public String performTimeOut(String user)
    {
        String timeout=new Time_in().getTime();
        long hour,minute,diff;
        String timeDiff;
        try {
            String timein = selectTime("SELECT * FROM Time WHERE status=1 AND username='"+user+"';");
            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
            java.util.Date time1 = sdf.parse(timein);
            java.util.Date time2=sdf.parse(timeout);

            JSONObject jsonObject=getShift_data(user);
            java.util.Date time_Out=sdf.parse(jsonObject.getString("R_out"));

            if(time2.before(time_Out)) {
                diff=time1.getTime()-time2.getTime();
                if (diff < 0) {
                    diff = diff * -1;
                }

                minute = diff / (60 * 1000) % 60;
                hour = diff / (60 * 60 * 1000) % 24;
                String min=minute+"";
                String hr=hour+"";

                if(min.length()==1)
                {
                    min="0"+min;
                }
                if(hr.length()==1)
                {
                    hr="0"+hr;
                }
                timeDiff=hr+":"+min;

            }

            else
            {
                diff=time1.getTime()-time_Out.getTime();
                if (diff < 0) {
                    diff = diff * -1;
                }

                minute = diff / (60 * 1000) % 60;
                hour = diff / (60 * 60 * 1000) % 24;
                String min=minute+"";
                String hr=hour+"";

                if(min.length()==1)
                {
                    min="0"+min;
                }
                if(hr.length()==1)
                {
                    hr="0"+hr;
                }
                timeDiff=hr+":"+min;
            }
        }

        catch(Exception e)
        {
            return e.toString();
        }
        try
        {

            URL url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/registerTimeOut.php");
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.connect();
            String command="UPDATE Time SET status=0,timeout='"+timeout+"',totalhour='"+timeDiff+"' WHERE status=1 AND username='"+user+"'";
            StringBuffer packedData=new StringBuffer();
            packedData.append(URLEncoder.encode("sqlcommand","UTF-8"));
            packedData.append("=");
            packedData.append(URLEncoder.encode(command,"UTF-8"));
            String item=packedData.toString();
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            wr.write(item);
            wr.flush();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            line=reader.readLine();
            if(line.length()==0)
            {
                return "HTTP NOT OKAY";
            }
            else
            {
                if(line.length()==1)
                {
                    return "okay";
                }
                else
                {
                    String v;
                    while((v=reader.readLine())!=null)
                    {
                        line+=" "+v;
                    }
                    return line;
                }
            }
        }
        catch(Exception e)
        {
            return e.toString();
        }


    }

    public String selectTime(String command)
    {
        try
        {

            URL url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/selectTimeIn.php");
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            StringBuffer packedData=new StringBuffer();
            packedData.append(URLEncoder.encode("sqlcommand","UTF-8"));
            packedData.append("=");
            packedData.append(URLEncoder.encode(command,"UTF-8"));
            String item=packedData.toString();
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            wr.write(item);
            wr.flush();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            if(reader==null)
            {
                return "HTTP NOT OKAY";
            }
            else
            {
                String json="";
                String s;
                while((s=reader.readLine())!=null)
                {

                    json+=s+"\n";
                }


               JSONArray jsonArray=new JSONArray(json);
                String timein="empty";
                JSONObject obj =jsonArray.getJSONObject(0);
                timein=obj.getString("timein");


               return timein;

            }


        }
        catch(Exception e)
        {
            return e.toString();
        }

    }

    public JSONObject getLate(String id)
    {

        String command="Select * from Employee_data where emp_no="+id;
        BufferedReader reader=JSONqueryPerformer(command);
        try{
            if(reader==null)
                return null;
            else
            {
                String json="";
                String s;
                while((s=reader.readLine())!=null)
                {
                    json+=s+"\n";
                }
                JSONArray jsonArray=new JSONArray(json);
                String shift_id;
                JSONObject obj=jsonArray.getJSONObject(0);
                shift_id=obj.getString("shift_id");
                JSONObject shiftdata=getGrace(shift_id);

                return shiftdata;
            }
        }

        catch(Exception e)
        {
            return null;
        }
    }

    public JSONObject getGrace(String shift)
    {
        String command="Select * from employeeshift where shift_ID='"+shift+"'";


        BufferedReader reader=JSONqueryPerformer(command);
        try{
            if(reader==null)
            {
                return null;
            }
            else
            {
                String json="";
                String s;
                while((s=reader.readLine())!=null)
                {

                    json+=s+"\n";
                }

                JSONArray jsonArray=new JSONArray(json);
                JSONObject obj =jsonArray.getJSONObject(0);


                return obj;

            }




        }
        catch(Exception e)
        {
            return null;
        }


    }

    public BufferedReader JSONqueryPerformer(String command)
    {
        try
        {
            URL url=new URL("https://eyed-bulk.000webhostapp.com/connectionPrototype/selectTimeIn.php");
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            StringBuffer packedData=new StringBuffer();
            packedData.append(URLEncoder.encode("sqlcommand","UTF-8"));
            packedData.append("=");
            packedData.append(URLEncoder.encode(command,"UTF-8"));
            String item=packedData.toString();
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            wr.write(item);
            wr.flush();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
           return reader;




        }
        catch(Exception e)
        {
            return null;
        }


    }
public String checkLate(String user_in,String id)
{
    JSONObject shift_data=getLate(id);
    try {
        String timein = shift_data.getString("R_In");
        String grace=shift_data.getString("gracePeriod");
        SimpleDateFormat sdf=new SimpleDateFormat("kk:mm");
        java.util.Date time_R= sdf.parse(timein);
        java.util.Date user_time=sdf.parse(user_in);
        java.util.Date time_grace=sdf.parse(grace);
        if(user_time.before(time_grace)&&(user_time.after(time_R)))
        {
            return "0:00";
        }
        else
        {
            long hour,minute,diff;
            String timeDiff;
            diff=user_time.getTime()-time_R.getTime();
            minute=diff/(60*1000)%60;
            hour=diff/(60*60*1000)%24;
            String min=minute+"";
            String hr=hour+"";
            if(min.length()==1)
            {
                min="0"+min;
            }
            if(hr.length()==1)
            {
                hr="0"+hr;
            }
            timeDiff=hr+":"+min;
            return timeDiff;
        }
    }
    catch(Exception e)
    {
        return e.toString();
    }
}

public JSONObject getShift_data(String id)
{
    BufferedReader reader=JSONqueryPerformer("SELECT employeeshift.R_out, employeeshift.R_In FROM employeeshift WHERE employeeshift.shift_ID=(SELECT Employee_data.shift_id from Employee_data where Employee_data.emp_no="+id+")");
    try{
        if(reader==null)
            return null;
        else
        {
            String json="";
            String s;
            while((s=reader.readLine())!=null)
            {
                json+=s+"\n";
            }
            JSONArray jsonArray=new JSONArray(json);
            JSONObject obj=jsonArray.getJSONObject(0);


            return obj;
        }
    }

    catch(Exception e)
    {
        return null;
    }

}

public void checkNoOfEntry(String id)
{
    String date=new Time_in().getDate();
    String command="Select * from Time where ";

}

}
