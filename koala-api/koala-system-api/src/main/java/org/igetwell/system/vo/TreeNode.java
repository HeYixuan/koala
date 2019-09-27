package org.igetwell.system.vo;

import java.util.List;

public class TreeNode<T> {

    /**
     * 当前节点ID
     */
    private Long id;

    /**
     * 当前节点名称
     */
    private String name;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 是否展开叶子
     */
    private boolean hidden = false;

    /**
     * 子节点列表
     */
    private List<T> children;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
