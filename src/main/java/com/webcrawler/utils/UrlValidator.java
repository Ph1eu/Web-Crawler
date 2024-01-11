package com.webcrawler.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UrlValidator {
    private static final Logger logger = LoggerFactory.getLogger(UrlValidator.class);
    public static boolean checkIfUrlIsNullOrEmpty(String url){
        if(url == null || url.isEmpty()){
            logger.error("URL is null");
            return true;
        }
        return false;
    }
    public static boolean checkIfUrlIsTooLong(String url){
        if(url.length() > 100){
            logger.error("URL exceeds 100 characters");
            return true;
        }
        return false;
    }
    public static boolean checkIfUrlStartWithHttpOrHttps(String url){
        if(!url.startsWith("https://") && !url.startsWith("http://")){
            logger.error("URL does not start with https:// or https://");
            return true;
        }
        return false;
    }

    public static boolean validateUrl(String url){
        if(!checkIfUrlIsNullOrEmpty(url)){
            return true;
        }
        if (!checkIfUrlIsTooLong(url)){
            return true;
        }
        return checkIfUrlStartWithHttpOrHttps(url);
    }
}
