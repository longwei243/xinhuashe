package com.xinhua.xinhuashe.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 给客户端传送JSON格式的分页数据
 * 
 */
public class JsonPageModel<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<T> content; //内容

    private int totalPage; //总页数

    private int currentPage = 1; //当前页

    private long count;// 总记录数，设置为“-1”表示不查询总数

    private int first;// 首页索引

    private int last;// 尾页索引

    private int prev;// 上一页索引

    private int next;// 下一页索引

    private boolean firstPage;//是否是第一页

    private boolean lastPage;//是否是最后一页

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
}
