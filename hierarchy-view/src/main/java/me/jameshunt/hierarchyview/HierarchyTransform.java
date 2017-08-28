package me.jameshunt.hierarchyview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 8/20/2017.
 */

public class HierarchyTransform {

    public static List<HierarchyDataHelper.Data> fromList(List list) throws HierarchyViewIncompatibleTypeException{

        final List<HierarchyDataHelper.Data> convertData = new ArrayList<>();

        for (Object o : list) {
            if (o instanceof HierarchyDataHelper.Data) {
                convertData.add((HierarchyDataHelper.Data) o);
            } else {
                throw new HierarchyViewIncompatibleTypeException("Please make sure all appropriate classes implement HierarchyDataHelper.Data");
            }
        }

        return convertData;

    }
}
