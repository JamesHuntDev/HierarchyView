package hunt.james.hierarchyview.today;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 7/29/2017.
 */

public class HierarchyData {

    List<DepthLevelFormat> formats;
    HierarchyDataHelper.Data data;

    public HierarchyData(HierarchyDataHelper.Data data)
    {
        formats = new ArrayList<>();
        this.data = data;
    }

    public void addFormatter(DepthLevelFormat format) {
        formats.add(format);
    }


    public DepthLevelFormat getFormat(int depthLevel) {
        if(formats.size()>depthLevel)
            return formats.get(depthLevel);
        else
            return formats.get(formats.size()-1);
    }

    public HierarchyDataHelper.Data getData() {
        return data;
    }
}
