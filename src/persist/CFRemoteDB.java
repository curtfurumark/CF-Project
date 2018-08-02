/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author curtr
 */
public class CFRemoteDB {
    private String url;
    public CFRemoteDB(String url) {
        System.out.println("hello: " + url);
        this.url = url;
    }
    
    public int doStuff(){
        boolean DEBUG_THIS = true;
        int id = 42;
        System.out.println("CFRemoteDB.doStuff()");
        URL myURL;
        try {
            myURL = new URL(url);
            if (DEBUG_THIS) System.out.println("\turl: " + myURL.toString());
            URLConnection urlConnection = myURL.openConnection();
            urlConnection.connect();
            if (DEBUG_THIS) System.out.println("connected ....???");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                    urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null){ 
                System.out.println("\ta line: " + inputLine);
            }
            bufferedReader.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(CFRemoteDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CFRemoteDB.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return id;
    }
}
