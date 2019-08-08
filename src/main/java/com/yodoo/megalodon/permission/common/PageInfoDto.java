package com.yodoo.megalodon.permission.common;

import java.util.List;

/**
 * @Date 2019/7/11 10:56
 * @author  by houzhen
 */
public class PageInfoDto<T> extends BaseDto {

    /**
     * 总条数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 数据
     */
    private List<T> list;

    public PageInfoDto() {

    }

    public PageInfoDto(int pageNum, int pageSize, long total, int pages) {
        super(pageNum, pageSize);
        this.total = total;
        this.pages = pages;
    }

    public PageInfoDto(int pageNum, int pageSize, long total, int pages, List<T> t) {
        super(pageNum, pageSize);
        this.total = total;
        this.pages = pages;
        this.list = t;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
