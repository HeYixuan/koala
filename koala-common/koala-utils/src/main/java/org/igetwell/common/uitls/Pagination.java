package org.igetwell.common.uitls;

import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> extends RowBounds {

    private List<T> rows = new ArrayList<T>(); //结果集
    private int pages = 0; //总页数
    private long total = 0; //总记录数
    private boolean firstPage = false;  //第一页
    private boolean lastPage = false;  //最后一页
    private int index = 0; //索引

    public Pagination() {
    }

    public Pagination(List<T> rows) {

        if (rows instanceof ArrayList){
            System.err.println("rows as arrayList");
        }
    }

    /**
     * 根据offset和limit计算当前页第一条记录在总结果集中的索引位置.
     */
    public int getFirstIndex() {
        if (super.getOffset() < 1 || super.getLimit() < 1)
            return 0;
        else
            return (((super.getOffset() - 1) * super.getLimit()) < total? ((super.getOffset() - 1) * super.getLimit()) : (int) total) + 1;
    }

    public Pagination(int offset, int limit, int total, List<T> rows) {
        this.setTotal(total);
        this.setOffset(offset);
        this.setLimit(limit);
        this.setRows(rows);
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
        if (super.getOffset() > pages) {
            super.setOffset(pages);
        }
        if((super.getOffset() / super.getLimit()) + 1 == pages){
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
        return (int) ((total - 1) / super.getLimit() + 1);
    }

    public boolean isFirstPage() {
        if (super.getOffset() <= 1){
            return true;
        }else {
            return false;
        }
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
