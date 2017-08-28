package me.jameshunt.hierarchyexample.common;

import java.util.List;

import me.jameshunt.hierarchyview.HierarchyDataHelper;

/**
 * Created by James on 7/29/2017.
 */

public class SubCategory implements HierarchyDataHelper.Data {

    private String text;

    public SubCategory(String text) {
        this.text = text;
    }

    @Override
    public List<HierarchyDataHelper.Data> getHierarchyData() {
        return null;
    }

    @Override
    public String getText() {
        return text;
    }
}
