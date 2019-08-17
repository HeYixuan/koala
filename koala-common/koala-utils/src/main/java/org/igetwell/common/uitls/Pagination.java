package org.igetwell.common.uitls;

import java.util.ArrayList;
import java.util.List;
import com.github.pagehelper.Page;

public class Pagination<T> {

    private int pageNo = 1; //当前页
    private int pageSize = 20; //每页显示数
    private List<T> rows = new ArrayList<T>(); //结果集
    private int pages = 0; //总页数
    private long total = 0; //总记录数
    private boolean firstPage = false;  //第一页
    private boolean lastPage = false;  //最后一页
    private int index = 0; //索引

    public Pagination() {
    }

    public Pagination(List<T> rows) {
        if (rows instanceof Page){
            Page<T> page = (Page<T>) rows;
            this.setPageNo(page.getPageNum());
            this.setPageSize(page.getPageSize());
            this.setTotal(page.getTotal());
            this.setPages(page.getPages());
            this.index = getFirstIndex();
            this.setRows(page);
        }

        if (rows instanceof ArrayList){
            System.err.println("rows as arrayList");
        }
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的索引位置.
     */
    public int getFirstIndex() {
        if (pageNo < 1 || pageSize < 1)
            return 0;
        else
            return (((pageNo - 1) * pageSize) < total? ((pageNo - 1) * pageSize) : (int) total) + 1;
    }

    public Pagination(int pageNo, int pageSize, int total, List<T> rows) {
        this.setTotal(total);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
        this.setRows(rows);
    }



    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置当前页
     * @param pageNo
     */
    public void setPageNo(int pageNo) {
        if (pageNo < 1) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
        if(pageNo == 1){//第一页
            setFirstPage(true);
        }else{
            setFirstPage(false);
        }
    }


    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页记录数
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            this.pageSize = 15;
        } else {
            this.pageSize = pageSize;
        }
    }

    public List<T> getRows() {
        return rows;
    }

    /**
     * 设置结果集
     * @param rows
     */
    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPages() {
        return pages;
    }

    /**
     * 设置总页数
     * @param pages
     */
    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    /**
     * 设置总记录数
     * @param total
     */
    public void setTotal(long total) {
        this.total = total;
        pages = getTotalPage();
        if (pageNo > pages) {
            pageNo = pages;
        }
        if(pageNo == pages){
            setLastPage(true);  //最后一页
        }else{
            setLastPage(false);
        }
    }

    /**
     * 计算总页数.
     */
    public int getTotalPage() {
        if (total < 1) {
            return 0;
        }
        return (int) ((total - 1) / pageSize + 1);
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
