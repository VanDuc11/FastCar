package com.example.fastcar.Model.MauXe;

public class Collection {
    private String url;
    private int count;
    private int pages;
    private int total;
    private String next;
    private String prev;
    private String first;
    private String last;

    public Collection(String url, int count, int pages, int total, String next, String prev, String first, String last) {
        this.url = url;
        this.count = count;
        this.pages = pages;
        this.total = total;
        this.next = next;
        this.prev = prev;
        this.first = first;
        this.last = last;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
