package webCrawler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class spider {
    
    private static final int PAGE_LIMIT = 100;
    private Set<String> pagesVisited;
    private List<String> pagesToVisit;
    private Map<String, List<String>> searchResult;

    public Map<String, List<String>> getSearchResult() {
        return this.searchResult;
    }

    public spider() {
        this.pagesToVisit = new LinkedList<>();
        this.pagesVisited = new HashSet<>();
        this.searchResult = new HashMap<>();
    }
    
    private String nextUrl(){
        String nextURL;
        do{
            nextURL = pagesToVisit.remove(0);
        }while(pagesVisited.contains(nextURL));
        pagesVisited.add(nextURL);
        return nextURL;
    }
    
    public void search(String url){
        search(url,"");
    }
    
    public void search(String url, String credentials, String... type){
        Set<String> fileTypes = new HashSet<>(Arrays.asList(type));
        do{
            spiderUtility spideyKit = new spiderUtility();
            String CurrentUrl;
            if(this.pagesToVisit.isEmpty()){
                CurrentUrl = url;
                this.pagesVisited.add(CurrentUrl);
            }else{
                CurrentUrl = nextUrl();
            }
            spideyKit.crawl(CurrentUrl, credentials, fileTypes);
            this.pagesToVisit.addAll(spideyKit.getLinks());
            spideyKit.getFiles().forEach((k,v) -> searchResult.merge(k, v, (v1,v2) -> {v1.addAll(v2); return v1;}));
        }while(this.pagesVisited.size() < PAGE_LIMIT && !this.pagesToVisit.isEmpty());
        
    }

}