package com.example.yena.losspreventionsystem;

/**
 * Created by LGPC on 2016-06-02.
 */
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;


/**
 * Created by LGPC on 2015-12-10.
 */
public class NetworkManager {
    public static String SERVER_URL = "http://52.78.6.54:3000/";
    ItemInfo itemInfo;

    public static void setupPOSTConnection(String postParam, HttpURLConnection urlConnection) throws IOException {
        urlConnection.setDoOutput(true);

        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(postParam.getBytes().length));
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
        dataOutputStream.writeBytes(postParam);
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    public static void setupGETConnection(HttpURLConnection urlConnection) {
        try {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
    }

    public static void setupDELETEConnection(String postParam, HttpURLConnection urlConnection) throws IOException {
        urlConnection.setDoOutput(true);

        try {
            urlConnection.setRequestMethod("DELETE");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(postParam.getBytes().length));
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
        dataOutputStream.writeBytes(postParam);
        dataOutputStream.flush();
        dataOutputStream.close();


    }

    public static void setupDELETEConnection(HttpURLConnection urlConnection) throws IOException {
        urlConnection.setDoOutput(true);

        try {
            urlConnection.setRequestMethod("DELETE");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    /*public static boolean checkedID(final Context context){
    }*/

    /* public static int Join(final Context context){
         final InfoDBManager manager = new InfoDBManager(context, "Info.db", null, 1);
         SQLiteDatabase db = manager.getReadableDatabase();
         Cursor cursor = db.rawQuery("select * from infoTABLE", null);

         cursor.moveToFirst();
         try {
             URL loginUrl = new URL("http://54.65.85.60:3333/user");
             HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
             JSONObject jsonObject = new JSONObject();
             jsonObject.put("id", cursor.getString(1));
             jsonObject.put("name", URLEncoder.encode(cursor.getString(2), "UTF-8"));
             if(cursor.getInt(3)==1){
                 jsonObject.put("gender",URLEncoder.encode("m", "UTF-8"));
             }
             else{
                 jsonObject.put("gender",URLEncoder.encode("f", "UTF-8"));
             }
             jsonObject.put("age",cursor.getInt(4));
             jsonObject.put("height", (int)cursor.getFloat(5));
             jsonObject.put("weight", (int)cursor.getFloat(6));
             String strJson = jsonObject.toString();
             Log.d("Login Str", strJson);
             setupPOSTConnection(strJson,urlConnection);
             InputStream response = urlConnection.getInputStream();
             String strResult="";
             if(response != null) strResult = convertInputStreamToString(response);
             Log.d("join result", strResult);
             if(new JSONObject(strResult).getInt("code")==200){
                 return 1;
             }else{
                 if(new JSONObject(strResult).getString("reason").equals("duplicate-id")) {
                     return 2;
                 }
                 return 0;
             }
         } catch (Exception e){
             e.printStackTrace();
             return 0;
         }
     }*/


    public static int Join(final Context context, JSONObject joinObject) {
        try {
            URL joinUrl = new URL("http://52.78.6.54:3000/join");
            HttpURLConnection urlConnection = (HttpURLConnection) joinUrl.openConnection();
            String strJson = joinObject.toString();
            Log.d("Login Str", strJson);
            setupPOSTConnection(strJson, urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult = "";
            if (response != null) strResult = convertInputStreamToString(response);
            Log.d("join result", strResult);
            if (new JSONObject(strResult).getInt("code") == 200) {
                return 1;
            } else {
                if (new JSONObject(strResult).getString("data").equals("duplicate-id")) {
                    return 2;
                }
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int Login(final Context context, String id, String password) {
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/login");
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            jsonObject.put("password", password);
            //Log.d("Login Str", strJson);
            String strJson = jsonObject.toString();
            setupPOSTConnection(strJson, urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult = "";
            if (response != null) strResult = convertInputStreamToString(response);
            Log.d("join result", strResult);
            if (new JSONObject(strResult).getString("data").equals("login success")) {
                return 1;
            } else {
                if (new JSONObject(strResult).getString("data").equals("login fail")) {
                    return 2;
                }
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static int deleteUser(final Context context, String id){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/deleteUser/"+id);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",id);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupDELETEConnection(urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            //Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int addItem(final Context context, String USER_ID, String BEACON_ID, String ITEM_NAME, String ITEM_LOSS_TIME, int ITEM_ALARM_STATUS, int ITEM_LOCK){

        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/addItem");
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("BEACON_ID",BEACON_ID);
            jsonObject.put("ITEM_NAME",URLEncoder.encode(ITEM_NAME, "UTF-8"));
            jsonObject.put("ITEM_ALARM_STATUS",ITEM_ALARM_STATUS);
            jsonObject.put("ITEM_LOCK",ITEM_LOCK);
            jsonObject.put("ITEM_LOSS_TIME",ITEM_LOSS_TIME);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupPOSTConnection(strJson, urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int deleteItem(final Context context, String id, String BEACON_ID){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/deleteItem/"+id+"/"+BEACON_ID);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",id);
            jsonObject.put("BEACON_ID",BEACON_ID);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupDELETEConnection(urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            //Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int editItem(final Context context, String USER_ID, String BEACON_ID, String ITEM_NAME, String ITEM_LOSS_TIME, int ITEM_ALARM_STATUS, int ITEM_LOCK){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/editItem");
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("BEACON_ID",BEACON_ID);
            jsonObject.put("ITEM_ALARM_STATUS",ITEM_ALARM_STATUS);
            jsonObject.put("ITEM_LOCK",ITEM_LOCK);
            jsonObject.put("ITEM_NAME",URLEncoder.encode(ITEM_NAME, "UTF-8"));
            jsonObject.put("ITEM_LOSS_TIME",ITEM_LOSS_TIME);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupPOSTConnection(strJson, urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    public static int updateLossTime(final Context context, String USER_ID, String BEACON_ID, String ITEM_LOSS_TIME){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/lossTime");
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("BEACON_ID",BEACON_ID);
            jsonObject.put("ITEM_LOSS_TIME",ITEM_LOSS_TIME);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupPOSTConnection(strJson, urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    public static int addGroup(final Context context, String USER_ID, String GROUP_NAME, int GROUP_ID){

        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/addGroup");
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("GROUP_NAME",URLEncoder.encode(GROUP_NAME, "UTF-8"));
            jsonObject.put("GROUP_ID",GROUP_ID);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupPOSTConnection(strJson, urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int deleteGroup(final Context context, String id, int GROUP_ID){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/deleteGroup/"+id+"/"+GROUP_ID);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",id);
            jsonObject.put("GROUP_ID",GROUP_ID);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupDELETEConnection(urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            //Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int editGroup(final Context context, String USER_ID, String GROUP_NAME, int GROUP_ID){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/editGroup");
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("GROUP_NAME",URLEncoder.encode(GROUP_NAME, "UTF-8"));
            jsonObject.put("GROUP_ID",GROUP_ID);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupPOSTConnection(strJson, urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int addItemGroup(final Context context, String USER_ID, int ITEM_GROUP_ID, int GROUP_ID, String BEACON_ID){

        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/addItemGroup");
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("GROUP_ID",GROUP_ID);
            jsonObject.put("ITEM_GROUP_ID",ITEM_GROUP_ID);
            jsonObject.put("BEACON_ID",BEACON_ID);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupPOSTConnection(strJson, urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int deleteItemGroup(final Context context, String USER_ID, int ITEM_GROUP_ID){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/deleteItemGroup/"+USER_ID+"/"+ITEM_GROUP_ID);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("ITEM_GROUP_ID",ITEM_GROUP_ID);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupDELETEConnection(urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            //Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int allItemdeleteItemGroup(final Context context, String USER_ID, String BEACON_ID){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/allItemdeleteItemGroup/"+USER_ID+"/"+BEACON_ID);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("BEACON_ID",BEACON_ID);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupDELETEConnection(urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            //Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static int allGroupDeleteItemGroup(final Context context, String USER_ID, int GROUP_ID){
        try {
            URL loginUrl = new URL("http://52.78.6.54:3000/allGroupDeleteItemGroup/"+USER_ID+"/"+GROUP_ID);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_ID",USER_ID);
            jsonObject.put("ITEM_GROUP_ID",GROUP_ID);
            String strJson = jsonObject.toString();
            Log.d("Login Str", strJson);
            setupDELETEConnection(urlConnection);
            InputStream response = urlConnection.getInputStream();
            String strResult="";
            if(response != null) strResult = convertInputStreamToString(response);
            //Log.d("join result", strResult);
            if(new JSONObject(strResult).getInt("code")==200){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static ArrayList<ItemInfo> getItem(final Context context, String USER_ID){

        ItemInfo itemInfo = new ItemInfo();
        //cursor.moveToFirst();
        try {

            URL loginUrl = new URL("http://52.78.6.54:3000/getItem/"+USER_ID);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            setupGETConnection(urlConnection);
            Log.d("loginstate", "1");
            InputStream response = urlConnection.getInputStream();

            Log.d("loginstate", "2");
            String strResult="";

            Log.d("loginstate", "3");
            if(response != null) strResult = convertInputStreamToString(response);

            Log.d("loginstate", "4");
            JSONObject jsonObject =new JSONObject(strResult);

            Log.d("loginstate", "5s");
            JSONArray array = jsonObject.getJSONArray("data");
            ArrayList<ItemInfo> data = new ArrayList<ItemInfo>();
            for(int i=0; i<array.length(); i++){
                JSONObject temp =(JSONObject)array.get(i);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(temp.getString("ITEM_LOSS_TIME")));
                Log.d("비콘아이디",""+temp.getString("BEACON_ID"));
                ItemInfo tempData = new ItemInfo(temp.getString("BEACON_ID"), URLDecoder.decode(temp.getString("ITEM_NAME"), "UTF-8"), temp.getInt("ITEM_ALARM_STATUS"), calendar);

                data.add(tempData);
            }
            return data;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<GroupInfo> getGroup(final Context context, String USER_ID){

        //cursor.moveToFirst();
        try {

            URL loginUrl = new URL("http://52.78.6.54:3000/getGroup/"+USER_ID);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            setupGETConnection(urlConnection);
            Log.d("loginstate", "1");
            InputStream response = urlConnection.getInputStream();

            Log.d("loginstate", "2");
            String strResult="";

            Log.d("loginstate", "3");
            if(response != null) strResult = convertInputStreamToString(response);

            Log.d("loginstate", "4");
            JSONObject jsonObject =new JSONObject(strResult);

            Log.d("loginstate", "5s");
            JSONArray array = jsonObject.getJSONArray("data");
            ArrayList<GroupInfo> data = new ArrayList<GroupInfo>();
            for(int i=0; i<array.length(); i++){
                JSONObject temp =(JSONObject)array.get(i);
                //Log.d("비콘아이디",""+temp.getString("BEACON_ID"));
                GroupInfo tempData = new GroupInfo(temp.getInt("GROUP_ID"),URLDecoder.decode(temp.getString("GROUP_NAME"),"UTF-8"));

                data.add(tempData);
            }
            return data;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ItemGroup> getItemGroup(final Context context, String USER_ID){

        //cursor.moveToFirst();
        try {

            URL loginUrl = new URL("http://52.78.6.54:3000/getItemGroup/"+USER_ID);
            HttpURLConnection urlConnection = (HttpURLConnection) loginUrl.openConnection();
            setupGETConnection(urlConnection);
            Log.d("loginstate", "1");
            InputStream response = urlConnection.getInputStream();

            Log.d("loginstate", "2");
            String strResult="";

            Log.d("loginstate", "3");
            if(response != null) strResult = convertInputStreamToString(response);

            Log.d("loginstate", "4");
            JSONObject jsonObject =new JSONObject(strResult);

            Log.d("loginstate", "5s");
            JSONArray array = jsonObject.getJSONArray("data");
            ArrayList<ItemGroup> data = new ArrayList<ItemGroup>();
            for(int i=0; i<array.length(); i++){
                JSONObject temp =(JSONObject)array.get(i);
                //Log.d("비콘아이디",""+temp.getString("BEACON_ID"));
                ItemGroup tempData = new ItemGroup(temp.getInt("ITEM_GROUP_ID"),temp.getString("BEACON_ID"),temp.getInt("GROUP_ID"));

                data.add(tempData);
            }
            return data;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}