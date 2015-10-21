package draw.com.canvas.util;

import android.app.Activity;
import android.util.DisplayMetrics;

import draw.com.canvas.model.Node;

/**
 * Created by Windows 7 on 10/21/2015.
 */
public class Util {
    /**
     * Function: get screen width, screen height of device
     */
    public static int[] getScreenWidthHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return new int[]{width, height};
    }

    public static Node getParentNode(Node[] nodes, int idParent) {
        for (Node node : nodes) {
            if (node.getId() == idParent) {
                return node;
            }
        }
        return null;
    }
}
