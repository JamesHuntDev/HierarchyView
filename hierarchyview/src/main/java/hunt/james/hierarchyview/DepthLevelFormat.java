package hunt.james.hierarchyview;

/**
 * Created by James on 7/29/2017.
 */

public class DepthLevelFormat {

    private int textSizeDP;
    private int resColor;

    public DepthLevelFormat(int textSizeDP, int resColor) {
        this.textSizeDP = textSizeDP;
        this.resColor = resColor;
    }

    public int getTextSizeDP() {
        return textSizeDP;
    }

    public int getResColor() {
        return resColor;
    }
}
