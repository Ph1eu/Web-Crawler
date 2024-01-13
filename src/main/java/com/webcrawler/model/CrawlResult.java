package com.webcrawler.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
@Getter
@ToString
@EqualsAndHashCode(of = "url")
@RequiredArgsConstructor
public class CrawlResult implements  Comparable<CrawlResult> {
    private final String url;
    private final int depth;
    @Override
    public int compareTo(CrawlResult o) {
        return this.url.compareTo(o.url);
    }
}