package me.jameshunt.hierarchyview;

/**
 * Created by James on 7/29/2017.
 */

class Presenter implements HierarchyLayoutContract.Presenter {

    private DepthLevelFormat format;
    private HierarchyDataHelper.Data data;
    private int depthLevel;
    private HierarchyLayoutContract.View view;
    private int indexClicked;

    private HierarchyListener.Data dataListener;

    Presenter(HierarchyDataHelper.Data data, DepthLevelFormat format, int depthLevel, HierarchyLayoutContract.View view, HierarchyListener.Data dataListener) {
        this.format = format;
        this.data = data;
        this.depthLevel = depthLevel;
        this.view = view;

        this.dataListener = dataListener;

        displayText();
    }

    private void displayText() {
        String[] text = new String[data.getHierarchyData().size()];

        for (int i = 0; i < data.getHierarchyData().size(); i++) {
            text[i] = data.getHierarchyData().get(i).getText();
        }

        view.displayText(text, depthLevel, format.getResColor(), format.getTextSizeDP());
    }

    @Override
    public void viewClicked(int x) {

        HierarchyDataHelper.Data subData = data.getHierarchyData().get(x);
        if (subData.getHierarchyData() != null) {
            view.addInnerLayout(subData, x);
        } else {
            dataListener.hierarchyDataSelected(data.getHierarchyData().get(x).getText());
            //((HierarchyListener.Data) ((HierarchyLinearLayout) view).getContext()).hierarchyDataSelected(data.getHierarchyData().get(x).getText());
        }
    }

    @Override
    public boolean getFullSizeInitially() {
        return depthLevel == 0;
    }

    @Override
    public void setScrollClickIndex(int index) {
        indexClicked = index;
    }

    @Override
    public int getIndexClicked() {
        return indexClicked;
    }
}
