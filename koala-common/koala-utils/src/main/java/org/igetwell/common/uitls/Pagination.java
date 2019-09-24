package org.igetwell.common.uitls;

import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> extends RowBounds {

    private int offset = 1; //当前页
    private int limit = 20; //每页显示数
    private List<T> rows = new ArrayList<T>(); //结果集
    private int pages = 0; //总页数
    private long total = 0; //总记录数
    private boolean firstPage = false;  //第一页
    private boolean lastPage = false;  //最后一页
    private int index = 0; //索引

    public Pagination() {
    }

    public Pagination(List<T> rows) {
//        if (rows instanceof Page){
//            Page<T> page = (Page<T>) rows;
//            this.setOffset(page.getPageNum());
//            this.setLimit(page.getlimit());
//            this.setTotal(page.getTotal());
//            this.setPages(page.getPages());
//            this.index = getFirstIndex();
//            this.setRows(page);
//        }

        if (rows instanceof ArrayList){
            System.err.println("rows as arrayList");
        }
    }

    /**
     * 根据offset和limit计算当前页第一条记录在总结果集中的索引位置.
     */
    public int getFirstIndex() {
        if (offset < 1 || limit < 1)
            return 0;
        else
            return (((offset - 1) * limit) < total? ((offset - 1) * limit) : (int) total) + 1;
    }

    public Pagination(int offset, int limit, int total, List<T> rows) {
        this.setTotal(total);
        this.setOffset(offset);
        this.setLimit(limit);
        this.setRows(rows);
    }



    public int getOffset() {
        return offset;
    }

    /**
     * 设置当前页
     * @param offset
     */
    public void setOffset(int offset) {
        if (offset < 1) {
            this.offset = 1;
        } else {
            this.offset = offset;
        }
        if(offset == 1){//第一页
            setFirstPage(true);
        }else{
            setFirstPage(false);
        }
    }


    public int getLimit() {
        return limit;
    }

    /**
     * 设置每页记录数
     * @param limit
     */
    public void setLimit(int limit) {
        if (limit < 1) {
            this.limit = 15;
        } else {
            this.limit = limit;
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
        if (offset > pages) {
            offset = pages;
        }
        if(offset == pages){
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
        return (int) ((total - 1) / limit + 1);
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
