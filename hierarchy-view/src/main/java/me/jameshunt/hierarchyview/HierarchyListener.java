package me.jameshunt.hierarchyview;

/**
 * Created by James on 7/29/2017.
 */

public interface HierarchyListener {

    interface Data {
        void hierarchyDataSelected(String data);
    }

    interface Scroll {

        HierarchyData getHierarchyData();

        void startExpand(int fullHeight);
        void stopExpand();
        void setExpandCurrentHeight(int height);

        void startCollapse(int height, int maxTicksCollapse);
        void stopCollapse();
        void setCollapseCurrentHeight(int height);

        void setClickY(int y);
        void setHeightOfClickedView(int height);

        int getState();
    }
}
