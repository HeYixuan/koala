package org.igetwell.common.uitls;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TreeUtils {

    public List<TreeNode> treeNodes(List<TreeNode> nodes) {
        List<TreeNode> nodeList = new ArrayList<TreeNode>();
        for(TreeNode node1 : nodes){
            boolean mark = false;
            for(TreeNode node2 : nodes){
                if(node1.getParentId()!=null && node1.getParentId().equals(node2.getId())){
                    mark = true;
                    if(node2.getChildren() == null)
                        node2.setChildren(new ArrayList<TreeNode>());
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if(!mark){
                nodeList.add(node1);
            }
        }
        return nodeList;
    }

    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {

        List<T> nodeList = new ArrayList<>();

        for (T node : treeNodes) {
            if (root.equals(node.getParentId())) {
                nodeList.add(node);
            }

            for (T subNode : treeNodes) {
                if (subNode.getParentId() == node.getId()) {
                    if (node.getChildren() == null) {
                        node.setChildren(new ArrayList<>());
                    }
                    node.add(subNode);
                }
            }
        }
        return nodeList;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(getChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> T getChildren(T treeNode, List<T> treeNodes) {
        for (T node : treeNodes) {
            if (treeNode.getId() == node.getParentId()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(getChildren(node, treeNodes));
            }
        }
        return treeNode;
    }
}