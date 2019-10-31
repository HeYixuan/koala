package org.igetwell.system.vo;


import java.util.ArrayList;
import java.util.List;

public class TreeUtils {

    public static List<MenuTree> buildMenu(List<MenuTree> menuList){
        List<MenuTree> nodeList = new ArrayList<MenuTree>();
        for(MenuTree node1 : menuList){
            boolean mark = false;
            for(MenuTree node2 : menuList){
                if(node1.getParentId()!=null && node1.getParentId().equals(node2.getId())){
                    mark = true;
                    if(node2.getChildren() == null)
                        node2.setChildren(new ArrayList<MenuTree>());
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
}