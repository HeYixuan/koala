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

    public static List<RoleTree> buildRole(List<RoleTree> roleList){
        List<RoleTree> nodeList = new ArrayList<RoleTree>();
        for(RoleTree node1 : roleList){
            boolean mark = false;
            for(RoleTree node2 : roleList){
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

    public static List<DeptTree> buildDept(List<DeptTree> deptList){
        List<DeptTree> nodeList = new ArrayList<DeptTree>();
        for(DeptTree node1 : deptList){
            boolean mark = false;
            for(DeptTree node2 : deptList){
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