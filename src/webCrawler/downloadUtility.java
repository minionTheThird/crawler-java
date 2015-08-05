package webCrawler;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utility class for downloading files
 */
public class downloadUtility {
    
    /**
     * Download a given file to directory provided.
     * @param file          : File link which needs to be downloaded
     * @param directory     : Directory where file should be downloaded
     * @param credentials   : base64 encoded login credentails (username : password)
     */
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
