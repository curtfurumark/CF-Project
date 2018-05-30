package persist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author curtr
 */
public class CFDB {
    public void executeURL(String url){
        URL myURL;
        try {
            myURL = new URL(url);
            URLConnection urlConnection = myURL.openConnection();
            urlConnection.connect();
            //myURLConnection.
            
            System.out.println("connected ....???");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                    urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null){ 
                System.out.println("\ta line: " + inputLine);
            }
            bufferedReader.close();
        } catch (MalformedURLException ex) {
            //Logger.getLogger(JavaURL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CFDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
