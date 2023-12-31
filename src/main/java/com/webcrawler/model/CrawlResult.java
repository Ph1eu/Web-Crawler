package com.webcrawler.model;




import java.util.Objects;
public class CrawlResult implements  Comparable<CrawlResult> {
    private final String url;
    private final int depth;

    public CrawlResult(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }

    public String getUrl() {
        return url;
    }


    public Integer getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return "CrawlResult{" +
                "url='" + url + '\'' +
                ", depth=" + depth +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrawlResult that = (CrawlResult) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public int compareTo(CrawlResult o) {
        return this.url.compareTo(o.url);
    }
}