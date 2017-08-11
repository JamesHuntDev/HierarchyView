package hunt.james.hierarchyview;

/**
 * Created by James on 6/27/2017.
 */

interface HierarchyLayoutContract {
    interface View {
        void displayText(String[] text, int hierarchyDepthLevel, int color, int textSize);
        void addInnerLayout(HierarchyDataHelper.Data data, int i);
    }

    interface Presenter {
        void viewClicked(int i);
        void setScrollClickIndex(int index);
        boolean getFullSizeInitially();
        int getIndexClicked();
    }
}
