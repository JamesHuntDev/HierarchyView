package me.jameshunt.hierarchyexample;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.jameshunt.hierarchyview.HierarchyDataHelper;

/**
 * Created by James on 8/20/2017.
 */

public class Category implements HierarchyDataHelper.Data{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("children")
    @Expose
    private List<Category> children = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }


    @Override
    public List<? extends HierarchyDataHelper.Data> getHierarchyData() {
        return children;
    }

    @Override
    public String getText() {
        return name;
    }
}
