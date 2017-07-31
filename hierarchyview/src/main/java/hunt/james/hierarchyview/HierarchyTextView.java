package hunt.james.hierarchyview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;

/**
 * Created by James on 6/28/2017.
 */

public class HierarchyTextView extends AppCompatTextView implements PreComputedHeight {

    //Paint paint;
    private int hierarchyDepthLevel;
    private int preComputedHeight;
    private int padding = Math.round(8 * getResources().getDisplayMetrics().density);
    private Rect rect;
    private int lines = 0;

    public HierarchyTextView(Context context) {
        super(context);
        //paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setGravity(Gravity.CENTER_VERTICAL);
        rect = new Rect();
    }

    public void setHierarchyDepthLevel(int hierarchyDepthLevel) {
        this.hierarchyDepthLevel = hierarchyDepthLevel;
        setPadding((hierarchyDepthLevel + 1) * padding, 0, padding, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, getPreComputedHeight(0));
    }

    @Override
    public int getPreComputedHeight(int x) {
        return preComputedHeight;
    }

    public void setPreComputedHeight(int screenWidth) {

        getPaint().getTextBounds(getText().toString(), 0, getText().length(), rect);

        int maxTextWidth = screenWidth - getPaddingLeft() - getPaddingRight(); //padding on front of TextView, and padding on back of TextView

        String text = getText().toString();
        String removed = "";

        while (rect.width() > maxTextWidth) {

            int lastSpaceIndex = text.lastIndexOf(" ");


            removed = text.substring(lastSpaceIndex) + removed;
            text = text.substring(0, lastSpaceIndex);


            getPaint().getTextBounds(text, 0, text.length(), rect);

            if (rect.width() < maxTextWidth) {
                lines++;

                text = removed;
                removed = "";
                getPaint().getTextBounds(text, 0, text.length(), rect);
            }
        }

        lines++;


        preComputedHeight = ((rect.height() + padding) * lines) + (padding * (4 - hierarchyDepthLevel));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        super.onDraw(canvas);
        //invalidate();
    }
}
