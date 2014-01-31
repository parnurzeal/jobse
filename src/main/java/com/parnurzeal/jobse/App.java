package com.parnurzeal.jobse;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.commons.io.IOUtils;
import java.util.Scanner;
/**
 * Hello world!
 *
 */
public class App 
{

    public static String convertStreamToString(java.io.InputStream is){
      java.util.Scanner s = new Scanner(is).useDelimiter("\\A");
      return s.hasNext()?s.next():"";
    }
    public static void main( String[] args )
    {
        InputStream is = App.class.getResourceAsStream("/rakuten1.job");
        String text = convertStreamToString(is);
        System.out.println(text);

      System.out.println( "Hello World!" );
    }
}
