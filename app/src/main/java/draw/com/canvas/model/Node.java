package draw.com.canvas.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows 7 on 10/21/2015.
 */
public class Node {
    private float radius = 40;      // Ball's radius
    private float x = radius + 20;  // Ball's center (x,y)
    private float y = radius + 40;
    private float speedX = 5;       // Ball's speed (x,y)
    private float speedY = 3;
    private RectF bounds;   // Needed for Canvas.drawOval
    private Paint paint;    // The paint style, color used for drawing
    private int id;
    private int idParent;

    private boolean isNodeParent;
    public float[] velocity = new float[2];
    public float[] net_force = new float[2];
    public List groupIndex = new ArrayList();

    public boolean isNodeParent() {
        return isNodeParent;
    }

    public void setIsNodeParent(boolean isNodeParent) {
        this.isNodeParent = isNodeParent;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    // Constructor
    public Node(int color, int id, int parentId) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);
        this.id = id;
        this.idParent = parentId;
    }

    public Node(int color, float x, float y, int id, int parentId) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);
        this.x = x;
        this.y = y;
        this.id = id;
        this.idParent = parentId;
    }

    public void moveWithCollisionDetection(Box box) {
        // Get new (x,y) position
        x += speedX;
        y += speedY;
        // Detect collision and react
        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax - radius;
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin + radius;
        }
        if (y + radius > box.yMax) {
            speedY = -speedY;
            y = box.yMax - radius;
        } else if (y - radius < box.yMin) {
            speedY = -speedY;
            y = box.yMin + radius;
        }
    }

    public boolean CheckNodeOnMouse(MotionEvent event) {
        if (Math.abs(event.getX() - x) <= radius && Math.abs(event.getY() - y) <= radius)
            return true;
        return false;
    }

    public void draw(Canvas canvas) {
        bounds.set(x - radius, y - radius, x + radius, y + radius);
        canvas.drawRect(bounds, paint);
    }

    public boolean CheckVertexGroup(int id) {
        if (groupIndex == null)
            return false;
        return groupIndex.indexOf(id) != -1;
    }
}
