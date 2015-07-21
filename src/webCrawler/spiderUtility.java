package webCrawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class spiderUtility {
    
    public spiderUtility() {
        this.urls = new LinkedList<>();
    }
    
    private List<String> urls;
    private Document htmlPage;
    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    
    public void crawl(String url){
        try{
            Connection conn = Jsoup.connect(url).userAgent(USER_AGENT);
            this.htmlPage = conn.get();
            
            Elements pageLinks = this.htmlPage.select("a[href]");
            for (Element e : pageLinks) {
               this.urls.add(e.absUrl("href")); 
            }
        }catch(IOException e){
            System.out.println("Error while crawling");
            e.printStackTrace();
        }
    }
    
  //overloaded function with credential logging 
    public void crawl(String url, String credentials){
        try{
            Connection conn = Jsoup.connect(url).header("Authorization", "Basic "+credentials).userAgent(USER_AGENT);
            this.htmlPage = conn.get();
            
            Elements pageLinks = this.htmlPage.select("a[href]");
            for (Element e : pageLinks) {
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
}
