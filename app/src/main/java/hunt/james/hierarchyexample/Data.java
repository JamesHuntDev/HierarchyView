package hunt.james.hierarchyexample;

import java.util.ArrayList;
import java.util.List;

import hunt.james.hierarchyview.today.HierarchyDataHelper;

/**
 * Created by James on 7/29/2017.
 */

public class Data implements HierarchyDataHelper.Data {

    List<HierarchyDataHelper.Data> categories;


    public Data() {
        categories = new ArrayList<>();

        for(int i = 0; i < 15; i++)
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
