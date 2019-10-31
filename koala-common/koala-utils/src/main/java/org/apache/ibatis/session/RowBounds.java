package org.apache.ibatis.session;


/**
 * 重写mybatis RowBounds类
 * @see org.apache.ibatis.session.RowBounds
 */
public class RowBounds {

  public static final int NO_ROW_OFFSET = 1;
  public static final int NO_ROW_LIMIT = 2147483647;
  public static final RowBounds DEFAULT = new RowBounds();

  private int offset;
  private int limit;

  public RowBounds() {
    this.offset = 1;
    this.limit = 2147483647;
  }

  public RowBounds(int offset, int limit) {
    this.offset = offset;
    this.limit = limit;
  }

  public int getOffset() {
      if (offset <= 0){
          return 0;
      } else {
          return (offset - 1) * limit;
      }
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
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

}
