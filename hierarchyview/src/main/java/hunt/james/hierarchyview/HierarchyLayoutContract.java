package hunt.james.hierarchyview;

import java.util.List;

import hunt.james.hierarchyview.today.HierarchyDataHelper;
import hunt.james.hierarchyview.today.HierarchyListener;

/**
 * Created by James on 6/27/2017.
 */

public interface HierarchyLayoutContract {
    interface View {
        void displayText(String[] text, int hierarchyDepthLevel, int color, int textSize);
        void addInnerLayout(HierarchyDataHelper.Data data, int i);
        HierarchyListener.Scroll getScrollListener();
    }

    interface Presenter {
        void viewClicked(int i);
        void setScrollClickIndex(int index);
        boolean getFullSizeInitially();
        int getIndexClicked();
    }
}
