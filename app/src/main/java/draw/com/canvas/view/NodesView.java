package draw.com.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import draw.com.canvas.model.Box;
import draw.com.canvas.model.Line;
import draw.com.canvas.model.Node;

/**
 * Created by Windows 7 on 10/21/2015.
 */
public class NodesView extends View {
    private Node[] nodes;
    private Box box;
    Line line;

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;

    // Constructor
    public NodesView(Context context, Node[] nodes, int[] screenSize) {
        super(context);

        this.nodes = nodes;
        box = new Box(0xff00003f);  // ARGB
        line = new Line(this.nodes);

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();

        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the components
        box.draw(canvas);
        for (Node node : nodes) {
            node.draw(canvas);
        }
        line.draw(canvas);

        // Delay
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
        }

        invalidate();  // Force a re-draw
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ballParent
        box.set(0, 0, w, h);
    }

    // Key-up event handler
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_DPAD_RIGHT: // Increase rightward speed
//                ballParent.speedX++;
//                break;
//            case KeyEvent.KEYCODE_DPAD_LEFT:  // Increase leftward speed
//                ballParent.speedX--;
//                break;
//            case KeyEvent.KEYCODE_DPAD_UP:    // Increase upward speed
//                ballParent.speedY--;
//                break;
//            case KeyEvent.KEYCODE_DPAD_DOWN:  // Increase downward speed
//                ballParent.speedY++;
//                break;
//            case KeyEvent.KEYCODE_DPAD_CENTER: // Stop
//                ballParent.speedX = 0;
//                ballParent.speedY = 0;
//                break;
//            case KeyEvent.KEYCODE_A:    // Zoom in
//                // Max radius is about 90% of half of the smaller dimension
//                float maxRadius = (box.xMax > box.yMax) ? box.yMax / 2 * 0.9f : box.xMax / 2 * 0.9f;
//                if (ballParent.radius < maxRadius) {
//                    ballParent.radius *= 1.05;   // Increase radius by 5%
//                }
//                break;
//            case KeyEvent.KEYCODE_Z:    // Zoom out
//                if (ballParent.radius > 20) {  // Minimum radius
//                    ballParent.radius *= 0.95;  // Decrease radius by 5%
//                }
//                break;
//        }
//        return true;  // Event handled
//    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
//        float deltaX, deltaY;
//        float scalingFactor = 5.0f / ((box.xMax > box.yMax) ? box.yMax : box.xMax);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
//                deltaX = currentX - previousX;
//                deltaY = currentY - previousY;
//                ballParent.speedX += deltaX * scalingFactor;
//                ballParent.speedY += deltaY * scalingFactor;

                nodes[0].setX(event.getX());
                nodes[0].setY(event.getY());
        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;
        return true;  // Event handled
    }
}
