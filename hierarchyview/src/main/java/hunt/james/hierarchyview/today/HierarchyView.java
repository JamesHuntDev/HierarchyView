package hunt.james.hierarchyview.today;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

import java.util.HashSet;

import hunt.james.hierarchyview.HierarchyLinearLayout;

/**
 * Created by James on 7/29/2017.
 */

public class HierarchyView extends ScrollView implements HierarchyListener.Scroll {

    private HierarchyData hierarchyData;

    private final int IS_EXPANDING = 1;
    private final int IS_COLLAPSING = 2;


    private final int NONE = 0;
    private final int EXPAND_NORMAL = 1;
    private final int EXPAND_UP_THEN_DOWN = 2;


    private final int COLLAPSE_NORMAL = 3;
    private final int BOTH_NORMAL = 4;

    private int state = NONE;


    private int collapseStartHeight;
    private int maxTicksCollapse;

    private int initialScrollY;

    private int collapseCurrentHeight, expandCurrentHeight;
    private int previousFrameExpandHeight;
    private int scrollStopCollapseHeight;
    private int stopCollapseScrollHeight;
    private boolean scrollWithoutExpand = false;

    private int clickY; //the y value of the element that was clicked
    private int previousClickY;

    private int expandStopHeight; //height it will be when its done expanding


    private int clickedViewHeight;


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

    public void scrollAndStayInPlace() {

        if (initialScrollY == 0) {
            initialScrollY = getScrollY();
        }

        if (state == NONE) {


            int viewHeight = getHeight();
            int viewScroll = getScrollY();

            if (active.contains(IS_COLLAPSING)) {
                Log.d("active", "collapse");
                state = COLLAPSE_NORMAL;

            } else if (active.contains(IS_EXPANDING) && (clickY < previousClickY || previousClickY == 0)) {
                Log.d("active", "expand");

                if (clickY + clickedViewHeight + expandStopHeight > initialScrollY + getHeight()) {
                    state = EXPAND_UP_THEN_DOWN;
                }

                if (clickY + clickedViewHeight + expandStopHeight <= getScrollY() + getHeight()) {
                    state = EXPAND_NORMAL;
                }

            }
        }

        animateScroll(state);

    }


    private void animateScroll(int scrollMode) {

        switch (scrollMode) {
            case EXPAND_NORMAL:
                expandNormal();

                break;
            case EXPAND_UP_THEN_DOWN:
                upThenDown();
                break;

            case COLLAPSE_NORMAL:
                collapseNormal();
                break;
        }
    }

    private void collapseNormal() {


        int max = Math.max(expandStopHeight, getHeight());
        if (active.contains(IS_EXPANDING) && clickY > previousClickY) {

            scrollWithoutExpand = true;

            /*int scrollToY = clickY - collapseStartHeight + clickedViewHeight;
            //int scrollCurrent = clickY - collapseCurrentHeight + clickedViewHeight;
            int scrollCurrent = clickY + clickedViewHeight;
            int scrollDiff = scrollCurrent - scrollToY;
            int pxPerCollapseTick = scrollDiff / (maxTicksCollapse);

            int distanceFromTop = clickY - initialScrollY;

            int beginningDiff = expandStopHeight - collapseStartHeight;
            int currentDiff = collapseCurrentHeight - expandCurrentHeight;
            int endClickY = clickY + beginningDiff;*/



            int left = clickY - collapseStartHeight + collapseCurrentHeight + clickedViewHeight;
            int right = getScrollY();

            int position = stopCollapseScrollHeight - (scrollStopCollapseHeight - collapseCurrentHeight);

            if (left < right && left + Math.min(expandStopHeight, getHeight()) <= getScrollY() + getHeight() + clickedViewHeight) {

                if(scrollStopCollapseHeight == 0 && stopCollapseScrollHeight == 0) {
                    scrollStopCollapseHeight = collapseCurrentHeight;
                    stopCollapseScrollHeight = getScrollY();
                    position = stopCollapseScrollHeight - (scrollStopCollapseHeight - collapseCurrentHeight);
                }

                scrollTo(0, position);
                Log.d("test", "test");
            } else {
                scrollStopCollapseHeight = collapseCurrentHeight;
                stopCollapseScrollHeight = getScrollY();
            }

        } else if (!active.contains(IS_EXPANDING)) {
            Log.d("test", "collapse without expand");
            if (scrollWithoutExpand) {
                int position = stopCollapseScrollHeight - (scrollStopCollapseHeight - collapseCurrentHeight);
                scrollTo(0, position);
            }
        }

        previousFrameExpandHeight = expandCurrentHeight;
        //previousFrameDiff = clickY + currentDiff;
    }

    private void expandNormal() {


    }

    private void upThenDown() {

        if (active.contains(IS_COLLAPSING) && (clickY > previousClickY)) {

            state = COLLAPSE_NORMAL;
            animateScroll(state);
            return;
        }

        int right = getScrollY() + expandStopHeight;

        int left = clickY + expandCurrentHeight;

        boolean goUp = true;
        if (clickY + expandCurrentHeight < getScrollY() + expandStopHeight) {
            goUp = false;
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
        Log.d("test", "start expand, scrollY:  " + getScrollY());
        expandStopHeight = fullHeight;

        active.add(IS_EXPANDING);

    }

    @Override
    public void stopExpand() {
        active.remove(IS_EXPANDING);

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
        Log.d("test", "reset");

        if(active.size() == 0) {

            previousClickY = clickY;

            collapseStartHeight = 0;
            initialScrollY = 0;
            collapseCurrentHeight = 0;
            expandCurrentHeight = 0;
            expandStopHeight = 0;
            clickY = 0;
            clickedViewHeight = 0;

            previousFrameExpandHeight = 0;
            scrollStopCollapseHeight = 0;
            scrollWithoutExpand = false;
            stopCollapseScrollHeight = 0;

            maxTicksCollapse = 0;

            state = NONE;
        }
    }
}
