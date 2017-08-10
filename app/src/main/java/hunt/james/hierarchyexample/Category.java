package hunt.james.hierarchyexample;

import java.util.ArrayList;
import java.util.List;

import hunt.james.hierarchyview.today.HierarchyDataHelper;

/**
 * Created by James on 7/29/2017.
 */

public class Category implements HierarchyDataHelper.Data {

    List<HierarchyDataHelper.Data> subCategories;
    String text;

    public Category(String text) {
        subCategories = new ArrayList<>();
        this.text = text;

        int rand = (int)(Math.random() * 6 + 8);

        for(int i = 0; i < rand; i++)
            subCategories.add(new SubCategory("blah: " + i));
    }

    @Override
    public List<HierarchyDataHelper.Data> getHierarchyData() {
        return subCategories;
    }

    @Override
    public String getText() {
        return text;
    }
}
