package me.jameshunt.hierarchyexample;

import java.util.ArrayList;
import java.util.List;

import me.jameshunt.hierarchyview.HierarchyDataHelper;

/**
 * Created by James on 7/29/2017.
 */

public class Data implements HierarchyDataHelper.Data {

    List<HierarchyDataHelper.Data> categories;


    public Data() {
        categories = new ArrayList<>();

        int rand = (int)(Math.random() * 5 + 12);

        for(int i = 0; i < rand; i++)
            categories.add(new Category("hehe: " + i));
    }


    @Override
    public List<HierarchyDataHelper.Data> getHierarchyData() {
        return categories;
    }

    @Override
    public String getText() {
        return null;
    }
}
