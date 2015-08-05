package webCrawler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements a utility which searches a given address for files of provided extension.
 * It also provides functionality for downloading those searched files. 
 */
public class spider {
    
   /**
    * PAGE_LIMIT defines number of pages this utility can visit before terminating the search
    */
    private static final int PAGE_LIMIT = 100;
    private Set<String> pagesVisited;
    private List<String> pagesToVisit;
    private Map<String, List<String>> searchResult;

    public spider() {
        this.pagesToVisit = new LinkedList<>();
        this.pagesVisited = new HashSet<>();
        this.searchResult = new HashMap<>();
    }
    
    public Map<String, List<String>> getSearchResult() {
        return this.searchResult;
    }
    
    /**
     * This method fetches unvisited URL from the list.
     * @return URL to be visited by crawler
     */
    private String nextUrl(){
        String nextURL;
        do{
            nextURL = pagesToVisit.remove(0);
        }while(pagesVisited.contains(nextURL));
        pagesVisited.add(nextURL);
        return nextURL;
    }
    
    /**
     * Entry point for the search
     * @param url : Starting URL where search should begin
     */
    public void search(String url, String... type){
        searchWithCredentials(url, "", type);
    }
    
    /**
     * Entry point for search.
     * Overloaded for searching pages with authentication
     * @param url           : Statrting URL where searching should begin
     * @param credentials   : base64 encoded login credentails (username : password)
     * @param type          : variable list of file types to be searched
     */
    public void searchWithCredentials(String url, String credentials, String... type){
        Set<String> fileTypes = new HashSet<>(Arrays.asList(type));
        do{
            searchUtility spideyKit = new searchUtility();
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
    
    /**
     * Downloads all the files found in search
     * @param directory     : Directory where files should be downloaded
     */
    public void downloadFiles(String directory){
        downloadFiles(directory,"");
    }

    /**
     * Download all files found in search.
     * Overloaded to downloaded files which needs authentication
     * @param directory     : Directory where files should be downloaded
     * @param credentials   : base64 encoded login credentails (username : password)
     */
    public void downloadFiles(String directory, String credentials) {
        downloadUtility downloadManager = new downloadUtility();
        this.searchResult.forEach((k,v) -> v.forEach(s -> downloadManager.download(s, directory, credentials)));
    }
}