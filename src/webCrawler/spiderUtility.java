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

public class spiderUtility {
    
    private List<String> urls;
    private Map<String, List<String>> files;
    private Document htmlPage;
    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    
    public spiderUtility() {
        this.urls = new LinkedList<>();
        this.files = new HashMap<>();
    }
    
    public void crawl(String url){
        crawl(url,""); 
    }
    
    public void crawl(String url, String credentials){
        crawl(url,credentials, new HashSet<String>() ); 
    }
    
    public void crawl(String url, String credentials, Set<String> type){
        try{
            Connection conn = Jsoup.connect(url).header("Authorization", "Basic "+credentials).userAgent(USER_AGENT);
            this.htmlPage = conn.get();
            
            Elements pageLinks = this.htmlPage.select("a[href]");
            for (Element e : pageLinks) {
                String fileType = e.absUrl("href")
                                    .trim()
                                    .substring(e.absUrl("href").lastIndexOf('.') + 1)
                                    .toLowerCase();
                if (type.contains(fileType)){
                    if(files.containsKey(fileType)){
                        files.get(fileType).add(e.absUrl("href"));
                    }
                    else files.put(fileType, new LinkedList<String>(){{
                      add(e.absUrl("href"));  
                    }});
                }
                else
                    this.urls.add(e.absUrl("href")); 
            }
        }catch(IOException e){
            System.out.println("Error while crawling");
            e.printStackTrace();
        }
    }
    
    public List<String> getLinks(){
        return this.urls;
    }
    
    public Map<String, List<String>> getFiles(){
        return this.files;
    }
}
