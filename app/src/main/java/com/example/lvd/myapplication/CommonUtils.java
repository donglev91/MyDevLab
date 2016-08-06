package com.example.lvd.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvd on 02/07/2016.
 */
public class CommonUtils {
    public static String ReadBaseData(InputStream is) throws IOException {
        String str = "";
        StringBuffer st = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        if (is!=null){
            while ((str = reader.readLine()) != null){
                st.append(str + "\n");
            }
        }

        is.close();
        return st.toString();
    }

    public static List<String> ReadBaseDataToStringArray(InputStream is) throws IOException {
        String str = "";
        List<String> stringList = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        if (is!=null){
            while ((str = reader.readLine()) != null){
                stringList.add(str);
            }
        }

        is.close();
        return stringList;
    }

    public static String ReadSpecificBaseData(InputStream is, String startFlag, String endFlag) throws IOException {
        String str = "";
        StringBuffer st = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        boolean flag = false;
        if (is!=null){
            while ((str = reader.readLine()) != null){
                if (str.equals(startFlag)){
                    flag = true;
                    continue;
                }
                else if (str.equals(endFlag))
                    break;
                if (flag)
                    st.append(str + "\n");
            }
        }
        is.close();
        return st.toString();
    }

    public static String ReadReverseSpecificBaseData(InputStream is, String startFlag, String endFlag) throws IOException {
        String str = "";
        StringBuffer st = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        boolean flag = false;
        if (is!=null){
            while ((str = reader.readLine()) != null){
                if (str.equals(startFlag)){
                    flag = true;
                    continue;
                }
                else if (str.equals(endFlag))
                    break;
                if (flag)
                    st.append(str + "\n");
            }
        }
        is.close();
        return st.toString();
    }

    public static void OpenFileInput(String innerFile, String content){
        //Write last point to internal file
        //final String LASTPOINTFILE = "last_point_file";

        /*FileOutputStream fos = new FileOutputStream();
        fos.write(memoryItem);
        fos.close();*/
    }
}
