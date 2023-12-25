package org.example.utils;
import org.example.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static Boolean validateUrl(String url){
        if(url == null ){
            logger.error("URL is null");
            return false;
        }
        if(!url.startsWith("https://") && !url.startsWith("http://")){
            logger.error("URL does not start with http:// or https://");
            return false;
        }
        if(url.contains(" ")){
            logger.error("URL contains illegal characters");
            return false;
        }
        if(url.length() > 100){
            logger.error("URL exceeds 100 characters");
            return false;
        }
        return true;
    }
}
