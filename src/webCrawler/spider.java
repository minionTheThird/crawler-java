package webCrawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class spider {
    
    public spider() {
        this.PAGE_LIMIT = 10;
        this.pagesToVisit = new LinkedList<>();
        this.pagesVisited = new HashSet<>();
    }
    
    int PAGE_LIMIT;
    private Set<String> pagesVisited;
    private List<String> pagesToVisit;
    
    String nextUrl(){
        String nextURL;
        do{
            nextURL = pagesToVisit.remove(0);
        }while(pagesVisited.contains(nextURL));
        pagesVisited.add(nextURL);
        return nextURL;
    }
    
    public void search(String url){
        do{
            spiderUtility spideyKit = new spiderUtility();
            String CurrentUrl;
            if(this.pagesToVisit.isEmpty()){
                CurrentUrl = url;
                this.pagesVisited.add(CurrentUrl);
            }else{
                CurrentUrl = nextUrl();
            }
            spideyKit.crawl(CurrentUrl);
            this.pagesToVisit.addAll(spideyKit.getLinks());
            System.out.println("Visited page : "+CurrentUrl);
        }while(this.pagesVisited.size() < PAGE_LIMIT && !this.pagesToVisit.isEmpty());
        
    }
}
