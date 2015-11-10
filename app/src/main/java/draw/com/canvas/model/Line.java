package draw.com.canvas.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Windows 7 on 10/21/2015.
 */
public class Line {
    Node[] nodes;
    Paint paint;

    public Line(Node[] points) {
        this.nodes = points;
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }

    public void draw(Canvas canvas) {
        for (Node node : nodes) {
            if (node.getIdParent() != -1) {
                Node parentNode = nodes[node.getIdParent()];
                canvas.drawLine(node.getX(), node.getY(), parentNode.getX(), parentNode.getY(), paint);
            }
        }
    }
}
