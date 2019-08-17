package org.igetwell.common.uitls;

import java.util.ArrayList;
import java.util.Collection;

public class Page<T> {

    private int pageNo = 1; //当前页
    private int pageSize = 20; //每页显示数
    private Collection<T> rows = new ArrayList<T>(); //结果集
    private int pages = 0; //总页数
    private int total = 0; //总记录数
    private boolean firstPage = false;  //第一页
    private boolean lastPage = false;  //最后一页

    // 构造函数
    public Page() {
    }

    public Page(final int pageNo) {
        setPageNo(pageNo);
    }

    /**
     * (pageNo-1) * pageSize
     * @param pageNo
     * @param pageSize
     */
    public Page(final int pageNo, final int pageSize) {
        setPageNo(pageNo);
        setPageSize(pageSize);
    }

    // 查询参数相关函数
    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(final int pageNo) {
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

    public void setPageSize(final int pageSize) {
        if (pageSize < 1) {
            this.pageSize = 15;
        } else {
            this.pageSize = pageSize;
        }
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的索引位置.
     */
    public int getFirstIndex() {
        if (pageNo < 1 || pageSize < 1)
            return 0;
        else
            return ((pageNo - 1) * pageSize) < total? ((pageNo - 1) * pageSize) : total;
    }

    /**
     * 取得页内的记录列数.
     */
    public Collection<T> getRows() {
        return rows;
    }

    public void setRows(Collection<T> rows) {
        this.rows = rows;
    }

    /**
     * 取得总记录数.
     */
    public int getTotal() {
        return total;
    }

    public void setPages(final int total) {
        this.total = total;
        pages = getPages();
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
    public int getPages() {
        if (total < 1) {
            return 0;
        }
        return (total - 1) / pageSize + 1;
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
