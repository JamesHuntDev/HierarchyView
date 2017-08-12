package me.jameshunt.hierarchyview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.HashSet;

/**
 * Created by James on 7/29/2017.
 */

public class HierarchyView extends ScrollView implements HierarchyListener.Scroll {

    private HierarchyData hierarchyData;

    //active
    private final int IS_EXPANDING = 1;
    private final int IS_COLLAPSING = 2;


    //states
    private final int NONE = 0;
    private final int EXPAND_NORMAL = 1;
    private final int EXPAND_UP_THEN_DOWN = 2;
    private final int COLLAPSE_NORMAL = 3;

    private int state = NONE;


    private int collapseStartHeight;
    private int expandStopHeight; //height it will be when its done expanding
    private int collapseCurrentHeight, expandCurrentHeight;
    private int initialScrollY;


    private int previousFrameExpandHeight;  //expand up then down


    private boolean scrollWithoutExpand = false; //collapse normal

    private int clickY; //the y value of the element that was clicked
    private int previousClickY;


    private int maxTicksCollapse; //number of total ticks collapse has to go through


    private int clickedViewHeight; //height of view that was clicked


    private HashSet<Integer> active;

    public HierarchyView(Context context) {
        super(context);
    }

    public HierarchyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HierarchyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHierarchyData(HierarchyData hierarchyData) {
        this.hierarchyData = hierarchyData;
        init();
    }

    private void init() {
        active = new HashSet<>();

        HierarchyLinearLayout hierarchyLinearLayout = new HierarchyLinearLayout(getContext(), this);
        hierarchyLinearLayout.setHierarchyData(hierarchyData);
        addView(hierarchyLinearLayout);
    }

    private void scrollAndStayInPlace() {

        if (initialScrollY == 0) {
            initialScrollY = getScrollY();
        }

        if (state == NONE) {


            if (active.contains(IS_COLLAPSING)) {
                state = COLLAPSE_NORMAL;

            } else if (active.contains(IS_EXPANDING) && (clickY < previousClickY || previousClickY == 0)) {
                expandSetState();
            }
        }

        animateScroll(state);

    }

    private void expandSetState() {
        if (clickY + clickedViewHeight + expandStopHeight > initialScrollY + getHeight()) {
            state = EXPAND_UP_THEN_DOWN;
        }

        if (clickY + clickedViewHeight + expandStopHeight <= getScrollY() + getHeight()) {
            state = EXPAND_NORMAL;
        }
    }


    private void animateScroll(int scrollMode) {

        switch (scrollMode) {
            case EXPAND_UP_THEN_DOWN:
                upThenDown();
                break;

            case COLLAPSE_NORMAL:
                collapseNormal();
                break;
        }
    }

    private void collapseNormal() {

        if(clickY - collapseStartHeight < previousClickY) {
            expandSetState();
            animateScroll(state);
            return;
        }

        int expandClickPosition = clickY - collapseStartHeight + collapseCurrentHeight;

        if (active.contains(IS_EXPANDING) && clickY - collapseStartHeight > previousClickY) {


            if (expandClickPosition <= getScrollY() && collapseStartHeight != 0) {
                scrollWithoutExpand = true;
                scrollTo(0, expandClickPosition);

            } else if (!active.contains(IS_COLLAPSING)) {
                scrollWithoutExpand = false;

                int scrollToY = clickY - collapseStartHeight;
                int scrollDiff = clickY - scrollToY;
                int pxPerCollapseTick = scrollDiff / (maxTicksCollapse); //fine because even though collapse isnt active, the px per frame should be same

                scrollBy(0, pxPerCollapseTick);
            }

        } else if (!active.contains(IS_EXPANDING)) {
            if (scrollWithoutExpand) {

                if(expandClickPosition < getScrollY()) {
                    scrollTo(0, expandClickPosition);
                }
            }
        }

    }

    private void upThenDown() {

        if (active.contains(IS_COLLAPSING) && (clickY - collapseStartHeight > previousClickY)) {

            state = COLLAPSE_NORMAL;
            animateScroll(state);
            return;
        }

        // original
        /*boolean goUp = false;
        if (clickY + expandCurrentHeight >= getScrollY() + expandStopHeight + clickedViewHeight) {
            goUp = true;
        }*/

        boolean goUp = true;

        if (clickY + expandCurrentHeight < getScrollY() + expandStopHeight + clickedViewHeight) {
            goUp = false;

            int minHeightOfExpandOrScreen = Math.min(expandStopHeight, getHeight());

            if (clickY + expandCurrentHeight >= getScrollY() + minHeightOfExpandOrScreen && clickY > getScrollY() + clickedViewHeight) {
                goUp = true;
            }
        }


        if (goUp) {
            int diff = expandCurrentHeight - previousFrameExpandHeight;
            scrollBy(0, diff);
        }

        previousFrameExpandHeight = expandCurrentHeight;
    }

    @Override
    public void setClickY(int y) {
        clickY = y;
    }


    @Override
    public void startExpand(int fullHeight) {
        expandStopHeight = fullHeight;
        active.add(IS_EXPANDING);
    }

    @Override
    public void stopExpand() {
        active.remove(IS_EXPANDING);
        scrollWithoutExpand = true;
        reset();
    }

    @Override
    public void setExpandCurrentHeight(int height) {
        expandCurrentHeight = height;

        if (expandStopHeight != 0)
            scrollAndStayInPlace();
    }

    @Override
    public void startCollapse(int height, int maxTicksCollapse) {
        collapseStartHeight = height;
        this.maxTicksCollapse = maxTicksCollapse;
        active.add(IS_COLLAPSING);
    }

    @Override
    public void stopCollapse() {
        active.remove(IS_COLLAPSING);
        reset();
    }

    @Override
    public void setCollapseCurrentHeight(int height) {
        collapseCurrentHeight = height;
        scrollAndStayInPlace();
    }

    @Override
    public void setHeightOfClickedView(int height) {
        clickedViewHeight = height;
    }

    private void reset() {

        if (active.size() == 0) {

            previousClickY = Math.max(clickY - collapseStartHeight,0);

            collapseStartHeight = 0;
            initialScrollY = 0;
            collapseCurrentHeight = 0;
            expandCurrentHeight = 0;
            expandStopHeight = 0;
            clickY = 0;
            clickedViewHeight = 0;

            previousFrameExpandHeight = 0;
            scrollWithoutExpand = false;

            maxTicksCollapse = 0;

            state = NONE;
        }
    }
}
