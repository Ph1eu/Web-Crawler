package org.example.crawler;


public class CrawlResult implements  Comparable<CrawlResult> {
    private String url;
    private Integer depth;

    public CrawlResult(String url, Integer depth) {
        this.url = url;
        this.depth = depth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "CrawlResult{" +
                "url='" + url + '\'' +
                ", depth=" + depth +
                '}';
    }

    @Override
    public int compareTo(CrawlResult o) {
        return this.url.compareTo(o.url);
    }
}