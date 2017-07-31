package hunt.james.hierarchyview.today;

/**
 * Created by James on 7/29/2017.
 */

public interface HierarchyListener {

    interface Data {
        void hierarchyDataSelected(String data);
    }

    interface Scroll {
        void startExpand(int fullHeight);
        void stopExpand();
        void setExpandCurrentHeight(int height);

        void startCollapse(int height);
        void stopCollapse();
        void setCollapseCurrentHeight(int height);

        void setClickY(int y);
        void setHeightOfChildren(int height);
    }
}
