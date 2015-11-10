package draw.com.canvas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import draw.com.canvas.constant.Constant;
import draw.com.canvas.model.Box;
import draw.com.canvas.model.Line;
import draw.com.canvas.model.Node;

/**
 * Created by Windows 7 on 10/21/2015.
 */
public class NodesView extends View {
    private float damping = 0.85f;
    private float attraction = 0.01f;
    private float repulsion = 200f;
    private Node[] nodes = new Node[0];
    private boolean[][] edges = new boolean[1][1];
    private Box box;
    Line line;
    int idMouse = -1;

    // Constructor
    public NodesView(Context context, int[] screenSize) {
        super(context);
        box = new Box(0xff00003f);  // ARGB
        line = new Line(this.nodes);

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();

        // To enable touch mode
        this.setFocusableInTouchMode(true);
    }

    public void AddNode(Node node, Node[] arr) {
        Node[] tmp = new Node[nodes.length + arr.length];
        for (int i = 0; i < tmp.length; i++) {
            if (node != null && i >= nodes.length) {
                node.groupIndex.add(i);
            }
            if (i < nodes.length) {
                tmp[i] = nodes[i];
            } else {
                tmp[i] = arr[i - nodes.length];
            }
        }
        nodes = tmp;
        line.setNodes(nodes);
    }

    public void AddEdges(int index, int n) {
        int length = nodes.length - n;
        boolean[][] tmp = new boolean[length + n][length + n];
        for (int i = 0; i < length + n; i++) {
            for (int j = 0; j < length + n; j++) {
                if (i < length && j < length) {
                    tmp[i][j] = this.edges[i][j];
                }
            }
            if (i >= length) {
                tmp[index][i] = true;
                tmp[i][index] = true;
            }
        }
        this.edges = tmp;
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the components
        Arrange();
        box.draw(canvas);
        for (Node node : nodes) {
            node.draw(canvas);
        }
        line.draw(canvas);
        invalidate();  // Force a re-draw
    }

    private void Arrange() {
        if (nodes.length > 1) {
            for (int i = 0; i < nodes.length; i++) { // loop through vertices
                Node v = nodes[i];
                Node u;

                v.net_force[0] = v.net_force[1] = 0;
                for (int j = 0; j < nodes.length; j++) { // loop through other vertices
                    if (i == j)
                        continue;
                    float repulsion = this.repulsion;
                    u = nodes[j];
                    if (!(u.isNodeParent() && v.isNodeParent())) {
                        if (v.isNodeParent()) {
                            if (!v.CheckVertexGroup(j))
                                continue;
                        } else if (v.getIdParent() != -1) {
                            if (!nodes[v.getIdParent()].CheckVertexGroup(j))
                                continue;
                        }
                    } else {
                        if ((u.getIdParent() == v.getId() || v.getIdParent() == u.getId())) {
                            repulsion = this.repulsion * 5f;
                        } else {
                            float d = (v.getX() - u.getX()) * (v.getX() - u.getX()) + (v.getY() - u.getY()) * (v.getY() - u.getY());
                            repulsion = this.repulsion - d * this.repulsion / 1000000;
                            if (repulsion < 0) repulsion = 0;
                        }
                    }
                    // squared distance between "u" and "v" in 2D space
                    float rsq = (v.getX() - u.getX()) * (v.getX() - u.getX()) + (v.getY() - u.getY()) * (v.getY() - u.getY());
                    // counting the repulsion between two vertices
                    v.net_force[0] += repulsion * (v.getX() - u.getX()) / rsq;
                    v.net_force[1] += repulsion * (v.getY() - u.getY()) / rsq;
                }
                for (int j = 0; j < nodes.length; j++) { // loop through edges
                    if (!edges[i][j])
                        continue;
                    u = nodes[j];
                    if (!(u.isNodeParent() && v.isNodeParent())) {
                        if (v.isNodeParent()) {
                            if (!v.CheckVertexGroup(j))
                                continue;
                        } else if (v.getIdParent() != -1) {
                            if (!nodes[v.getIdParent()].CheckVertexGroup(j))
                                continue;
                        }
                    }

                    // countin the attraction
                    float attraction = (u.isNodeParent() && v.isNodeParent()) ? this.attraction / 5f : this.attraction;
                    v.net_force[0] += attraction * (u.getX() - v.getX());
                    v.net_force[1] += attraction * (u.getY() - v.getY());
                }
                // counting the velocity (with damping 0.85)
                v.velocity[0] = (v.velocity[0] + v.net_force[0]) * damping;
                v.velocity[1] = (v.velocity[1] + v.net_force[1]) * damping;
            }
        }
        for (int i = 0; i < nodes.length; i++) // set new positions
        {
            Node v = nodes[i];
            if (isMouseDrag && idMouse == i) {
            } else {
                v.setX(v.getX() + v.velocity[0]);
                v.setY(v.getY() + v.velocity[1]);
            }
        }
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ballParent
        box.set(0, 0, w, h);
    }

    // Touch-input handler
    boolean isMouseDrag;
    float touchDownX = 0, touchDownY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                touchDownY = event.getY();
                idMouse = -1;
                for (Node node : nodes) {
                    if (node.CheckNodeOnMouse(event))
                        idMouse = node.getId();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMouseDrag && idMouse != -1) {
                    Node node = nodes[idMouse];
                    if (!node.isNodeParent()) {
                        node.setIsNodeParent(true);
                        Random rand = new Random();
                        int n = rand.nextInt(Constant.RAN_NODE) + Constant.MIN_NODE;
                        Node[] arrNode = new Node[n];
                        for (int i = 0; i < n; i++) {
                            float x = node.getX() + rand.nextInt(600) - 300;
                            float y = node.getY() + rand.nextInt(600) - 300;
                            Node nodeChile = new Node(Color.RED, x, y, nodes.length + i, Constant.ROOT_ID);
                            nodeChile.setIdParent(node.getId());
                            arrNode[i] = nodeChile;
                        }

                        AddNode(node, arrNode);
                        AddEdges(node.getId(), n);

                        node.groupIndex.add(node.getId());
                        if (node.getIdParent() != -1) {
                            int indexRemove = nodes[node.getIdParent()].groupIndex.indexOf(node.getId());
                            nodes[node.getIdParent()].groupIndex.remove(indexRemove);
                        }
                    }
                    idMouse = -1;
                }
                isMouseDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (idMouse != -1 && touchDownX != event.getX() && touchDownY != event.getY()) {
                    isMouseDrag = true;
                    nodes[idMouse].setX(event.getX());
                    nodes[idMouse].setY(event.getY());
                }
                break;
        }

        return true;  // Event handled
    }
}
