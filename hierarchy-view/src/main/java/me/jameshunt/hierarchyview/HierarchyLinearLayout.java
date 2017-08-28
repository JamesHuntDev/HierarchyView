package me.jameshunt.hierarchyview;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 6/28/2017.
 */

class HierarchyLinearLayout extends LinearLayout implements HierarchyLayoutContract.View, View.OnClickListener, PreComputedHeight {

    private int tick = 0;
    private int maxTicks;
    private List<PreComputedHeight> views;
    private int closingIndex = -1;
    private int openingIndex = -1;
    private boolean shrinking = false;
    private static int screenWidth;

    int currentHeight;
    int depthLevel = 0;


    private HierarchyLayoutContract.Presenter presenter;
    private HierarchyListener.Scroll scrollListener;

    HierarchyLinearLayout(Context context, HierarchyListener.Scroll scrollListener) {
        super(context);
        setOrientation(VERTICAL);
        views = new ArrayList<>();
        this.scrollListener = scrollListener;

        setData(scrollListener.getHierarchyData().getData(), 0);
    }

    void setData(HierarchyDataHelper.Data data, int depthLevel) {

        this.depthLevel = depthLevel;

        if (screenWidth == 0) {
            Display display = ((AppCompatActivity) getContext()).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        presenter = new Presenter(data, scrollListener.getHierarchyData().getFormat(depthLevel), depthLevel, this);

    }

    public HierarchyListener.Data getExternalListener() {
        return scrollListener.getExternalListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int fullHeight = getPreComputedHeight(widthMeasureSpec);
        currentHeight = (fullHeight * tick) / maxTicks;

        if (presenter.getFullSizeInitially()) {
            setMeasuredDimension(widthMeasureSpec, fullHeight);
        } else if (currentHeight <= fullHeight && !shrinking) {

            if (tick == 0)
                scrollListener.startExpand(fullHeight);

            setMeasuredDimension(widthMeasureSpec, currentHeight);
            scrollListener.setExpandCurrentHeight(currentHeight);

            if (tick != maxTicks)
                handlerRequestLayout();
            else
                scrollListener.stopExpand();

            tick++;
        } else if (currentHeight >= 0 && shrinking) {

            if (tick == maxTicks)
                scrollListener.startCollapse(currentHeight, maxTicks);

            setMeasuredDimension(widthMeasureSpec, currentHeight);
            scrollListener.setCollapseCurrentHeight(currentHeight);

            if (tick > 0)
                handlerRequestLayout();
            else {
                scrollListener.stopCollapse();
                removeAllChildViews();
            }

            tick--;
        } else {
            setMeasuredDimension(widthMeasureSpec, fullHeight);
        }
    }

    @Override
    public int getPreComputedHeight(int widthMeasureSpec) {
        int tempHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            //try {
                getChildAt(i).measure(widthMeasureSpec, views.get(i).getPreComputedHeight(widthMeasureSpec));

                if (getChildAt(i) instanceof HierarchyTextView)
                    tempHeight += views.get(i).getPreComputedHeight(widthMeasureSpec);
                else if (getChildAt(i) instanceof HierarchyLinearLayout) {
                    HierarchyLinearLayout smoothLinearLayout = (HierarchyLinearLayout) views.get(i);
                    tempHeight += smoothLinearLayout.getCurrentHeight();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return tempHeight;
    }

    private int getCurrentHeight() {
        return currentHeight;
    }

    @Override
    public void displayText(String[] text, int hierarchyDepthLevel, int color, int textSize) {
        removeAllViews();
        views.clear();

        maxTicks = 3 * text.length;

        for (String s : text) {
            HierarchyTextView textView = new HierarchyTextView(getContext());
            textView.setText(s);
            textView.setHierarchyDepthLevel(hierarchyDepthLevel);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            textView.setTextColor(ContextCompat.getColor(getContext(), color));
            textView.setPreComputedHeight(screenWidth);
            textView.setOnClickListener(this);
            views.add(textView);
            addView(textView);
        }

    }

    @Override
    public void addInnerLayout(HierarchyDataHelper.Data data, int i) {

        int numSmooth = 0;

        for (int x = 0; x <= i; x++) {
            if (views.get(x) instanceof HierarchyLinearLayout)
                numSmooth++;
        }

        HierarchyLinearLayout subLayout = new HierarchyLinearLayout(getContext(), scrollListener);
        subLayout.setData(data, depthLevel + 1);

        int index = i + 1 + numSmooth;

        addView(subLayout, index);
        views.add(index, subLayout);
        openingIndex = index;

        removeOtherSubLayouts(index);
    }

    @Override
    public void onClick(View view) {

        if(scrollListener.getState() != 0) //0 == HierarchyView.NONE
            return;

        if (openingIndex == views.size())
            openingIndex--;

        if (closingIndex == -1)
            closingIndex = openingIndex;

        int numSmooth = 0;
        for (int i = 0; i < views.size(); i++) {
            if (views.get(i) instanceof HierarchyLinearLayout)
                numSmooth++;

            if (views.get(i) == view) {

                boolean same = false;
                if (openingIndex != -1 && openingIndex == closingIndex) {

                    if (openingIndex == i + 1)
                        same = true;

                    removeOtherSubLayouts(-1);
                    openingIndex = -1;

                }

                if (openingIndex != i && !same) {
                    closingIndex = openingIndex;

                    int clickIndex = i - numSmooth;

                    scrollListener.setHeightOfClickedView(view.getHeight());
                    presenter.setScrollClickIndex(clickIndex);
                    scrollListener.setClickY(getHeightOfClick());
                    presenter.viewClicked(clickIndex);
                }

                return;
            }
        }
    }

    private int getHeightOfClick() {

        int height = 0;

        for (int i = 0; i <= presenter.getIndexClicked(); i++) {
            height += views.get(i).getPreComputedHeight(0);
        }

        if (getParent() instanceof HierarchyLinearLayout) {
            height += ((HierarchyLinearLayout) getParent()).getHeightOfClick();
        }

        Log.d("click y", "cliiicky: " + height);

        return height;
    }


    private void removeOtherSubLayouts(int keepIndex) {

        for (int i = 0; i < views.size(); i++) {
            if (views.get(i) instanceof HierarchyLinearLayout) {
                if (i != keepIndex) {

                    if (i < keepIndex)
                        openingIndex--;

                    ((HierarchyLinearLayout) views.get(i)).setShrinking(i);
                    return;
                }
            }
        }
    }

    private void setShrinking(int index) {
        closingIndex = index;
        shrinking = true;
        tick = maxTicks;
        handlerRequestLayout();
    }

    private void removeAllChildViews() {
        removeAllViews();
        views.clear();

        HierarchyLinearLayout parent = ((HierarchyLinearLayout) getParent());

        int index = 0;
        for (int i = 0; i < parent.views.size(); i++) {
            if (this == parent.views.get(i)) {
                index = i;
                i = parent.views.size();
            }
        }

        ((HierarchyLinearLayout) getParent()).views.remove(index);
        ((HierarchyLinearLayout) getParent()).removeView(this);
        closingIndex = -1;

    }

    private void handlerRequestLayout() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        }, 0);
    }


}
