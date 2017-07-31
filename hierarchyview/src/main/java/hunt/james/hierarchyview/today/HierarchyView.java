package hunt.james.hierarchyview.today;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

import hunt.james.hierarchyview.HierarchyLinearLayout;

/**
 * Created by James on 7/29/2017.
 */

public class HierarchyView extends ScrollView implements HierarchyListener.Scroll{

    private HierarchyData hierarchyData;

    private int collapseStartHeight;

    private int initialScrollY;

    private int collapseCurrentHeight, expandCurrentHeight;

    private int clickY; //the y value of the element that was clicked

    private int expandStopHeight; //height it will be when its done expanding

    private int heightOfChildren;

    int numStarted = 0;
    int numStopped = 0;

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
        HierarchyLinearLayout hierarchyLinearLayout = new HierarchyLinearLayout(getContext(), this);
        hierarchyLinearLayout.setHierarchyData(hierarchyData);
        addView(hierarchyLinearLayout);
    }

    public void scrollAndStayInPlace() {

        //Log.d("same place","startCollapseHeight: "+startCollapseHeight+"       currentCollapseHeight: "+collapseCurrentHeight+"        expandCurrentHeight: "+expandCurrentHeight+"     initialScrollY: "+initialScrollY);

    }

    @Override
    public void setClickY(int y) {
        clickY = y;
    }

    @Override
    public void setHeightOfChildren(int height) {  //for the one expanding
        heightOfChildren = height;
    }

    @Override
    public void startExpand(int fullHeight) {
        Log.d("test","start expand, scrollY:  "+getScrollY());
        numStarted++;
        expandStopHeight = fullHeight;
        expandCurrentHeight = 0;
        initialScrollY = getScrollY();
    }

    @Override
    public void stopExpand() {
        Log.d("test","stopped expand");

        numStopped++;
        if(numStarted == numStopped)
            reset();
    }

    @Override
    public void setExpandCurrentHeight(int height) {
        //Log.d("test","current height: "+height);
        expandCurrentHeight = height;

        if(heightOfChildren != 0)
            scrollAndStayInPlace();
    }

    @Override
    public void startCollapse(int height) {
        numStarted++;
        collapseStartHeight = height;
        expandCurrentHeight = 0;
    }

    @Override
    public void stopCollapse() {
        Log.d("test","stopped collapse");

        numStopped++;
        if(numStarted == numStopped)
            reset();
    }

    @Override
    public void setCollapseCurrentHeight(int height) {
        collapseCurrentHeight = height;

        if(heightOfChildren != 0)
            scrollAndStayInPlace();
    }

    private void reset() {
        Log.d("test","reset");
        collapseStartHeight = 0;
        initialScrollY = 0;
        collapseCurrentHeight = 0;
        expandCurrentHeight = 0;
        expandStopHeight = 0;
        heightOfChildren = 0;
        clickY = 0;
        numStarted = 0;
        numStopped = 0;
    }
}
