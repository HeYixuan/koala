package org.igetwell.common.uitls;

import java.util.List;

public class TreeNode {

    /**
     * 当前节点ID
     */
    private String id;

    /**
     * 当前节点名称
     */
    private String name;

    /**
     * 父节点ID
     */
    private String parentId;

    /**
     * 子节点列表
     */
    private List<TreeNode> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void add(TreeNode node) {
        children.add(node);
    }
}
