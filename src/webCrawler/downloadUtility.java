package webCrawler;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class downloadUtility {
    
    public void download(String file, String directory, String credentials){
        try{
            String fileName = file.substring(file.lastIndexOf("/")).replaceAll("%20", " ");
            URLConnection conn = (new URL(file)).openConnection();
            conn.addRequestProperty("User-Agent",searchUtility.USER_AGENT);
            conn.setRequestProperty("Authorization", "Basic "+credentials);
            InputStream in = conn.getInputStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(directory+fileName));
            for (int b; (b = in.read()) != -1;) {
                out.write(b);
            }
            out.close();
            in.close();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
