package draw.com.canvas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import draw.com.canvas.L.L;
import draw.com.canvas.constant.Constant;
import draw.com.canvas.model.Node;
import draw.com.canvas.util.Util;
import draw.com.canvas.view.NodesView;

public class ActivityMain extends AppCompatActivity {

    // store
    int[] screenSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get screen size
        screenSize = Util.getScreenWidthHeight(this);

        // init sample node
        Node nodeRoot = new Node(Color.GREEN, 0, Constant.ROOT_ID);

        // init list current node

        // set position for all node
        //setPositionForAllNode(nodes);

        NodesView nodesView = new NodesView(this, screenSize);
        nodesView.AddNode(null, new Node[]{nodeRoot});
        setContentView(nodesView);
        nodesView.setBackgroundColor(Color.BLACK);
    }

    private void setPositionForAllNode(Node[] nodes) {
        for (Node node : nodes) {
            if (node.getIdParent() == Constant.ROOT_ID) {
                L.m("node parent = " + node.getId());
                node.setX(screenSize[0] / 2);
                node.setY(screenSize[1] / 2);
            } else {
                L.m("node child = " + node.getId());
                // check how many same level node
                int countSampleLevel = 1;
                for (Node node1 : nodes) {
                    if (node1.getId() != node.getId()   // it isn't checking same node
                            && node1.getIdParent() == node.getIdParent()) { // same parent id
                        countSampleLevel++;
                    }
                }
                L.m("countSampleLevel = " + countSampleLevel);

                // calculate the position according the number same level node
                Node parentNode = Util.getParentNode(nodes, node.getIdParent());
                if (parentNode != null) {
                    int[] positions = generalChildXY(parentNode.getX(), parentNode.getY(), countSampleLevel);
                    node.setX(positions[0]);
                    node.setY(positions[1]);
                }
            }
        }
    }

    /**
     * general x, y for child node ~> no collision with any other node
     */
    private int[] generalChildXY(float x, float y, int countSampleLevel) {
        return new int[]{100, 100};
    }
}
