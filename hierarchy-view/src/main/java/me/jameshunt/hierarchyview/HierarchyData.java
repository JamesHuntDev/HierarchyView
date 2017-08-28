package me.jameshunt.hierarchyview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 7/29/2017.
 */

public class HierarchyData {

    private List<DepthLevelFormat> formats;
    private HierarchyDataHelper.Data data;

    public HierarchyData(HierarchyDataHelper.Data data) {
        formats = new ArrayList<>();
        this.data = data;
    }

    public HierarchyData(final List<? extends HierarchyDataHelper.Data> list) {

        formats = new ArrayList<>();
        data = new HierarchyDataHelper.Data() {
            @Override
            public List<? extends HierarchyDataHelper.Data> getHierarchyData() {
                return list;
            }

            @Override
            public String getText() {
                return null;
            }
        };
    }

    public void addFormatter(DepthLevelFormat format) {
        formats.add(format);
    }


    DepthLevelFormat getFormat(int depthLevel) {
        if (formats.size() > depthLevel)
            return formats.get(depthLevel);
        else
            return formats.get(formats.size() - 1);
    }

    public HierarchyDataHelper.Data getData() {
        return data;
    }
}
