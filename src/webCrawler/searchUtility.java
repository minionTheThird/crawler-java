package webCrawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Utility class for providing search functionality.
 * Creates connection and fetches content on page and gets search result
 */
public class searchUtility {
    
    /**
     * USER_AGENT : To mimic a browser request so that server doesnot identify it as robot. 
     */
    static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> urls;
    private Map<String, List<String>> files;
    private Document htmlPage;
    
    public searchUtility() {
        this.urls = new LinkedList<>();
        this.files = new HashMap<>();
    }

    public void crawl(String url){
        crawl(url,""); 
    }
    
    public void crawl(String url, String credentials){
        crawl(url,credentials, new HashSet<String>() ); 
    }
    
    /**
     * For given URL, creates connection, fetches content and search for provided file types
     * @param url           : URL to be searched
     * @param credentials   : base64 encoded login credentails (username : password)
     * @param type          : file types to be searched
     */
    public void crawl(String url, String credentials, Set<String> type){
        try{
            Connection conn = Jsoup.connect(url).header("Authorization", "Basic " + credentials).userAgent(USER_AGENT);
            this.htmlPage = conn.ignoreContentType(true).get();
            Elements pageLinks = this.htmlPage.select("a[href]");
            for (Element e : pageLinks) {
                String fileType = e.absUrl("href").trim()
                        .substring(e.absUrl("href").lastIndexOf('.') + 1)
                        .toLowerCase();
                if (type.contains(fileType)) {
                    if (files.containsKey(fileType))
                        files.get(fileType).add(e.absUrl("href"));
                    else
                        files.put(fileType, new LinkedList<String>() {{ add(e.absUrl("href")); }});
                } else
                    this.urls.add(e.absUrl("href"));
            }
        }catch(IOException e){
            System.out.println("Error while crawling");
            e.printStackTrace();
        }
    }
    
    /**
     * @return : all the links found on the page 
     */
    public List<String> getLinks(){
        return this.urls;
    }
    
    /**
     * @return : all the files found on the page
     */
    public Map<String, List<String>> getFiles(){
        return this.files;
    }
}
