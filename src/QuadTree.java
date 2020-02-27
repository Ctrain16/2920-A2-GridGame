import java.awt.*;

public class QuadTree{

    private Node root;
    private final int MAX_LEVEL;
    private Color[] palette;

    /**
     * Constructor
     * @param size
     * @param palette
     * @param mxLevel
     */
    public QuadTree(int size, Color[] palette, int mxLevel){
        MAX_LEVEL = mxLevel;
        this.palette = palette;


        root = new Node(randomColor(),null, size,0,0, false);
        root.selected = true;

        smash(root);
    }


    /**
     * Smashes selected node into four children
     * @param node
     */
    public void smash(Node node){
        node.tlChild = new Node(randomColor(), node, node.size/2, node.xCord, node.yCord, true);
        node.trChild = new Node(randomColor(), node, node.size/2, node.xCord + node.size/2,
                node.yCord,true);
        node.brChild = new Node(randomColor(), node, node.size/2, node.xCord + node.size/2,
                node.yCord + node.size/2, true);
        node.blChild = new Node(randomColor(), node, node.size/2, node.xCord,
                node.yCord + node.size/2, true);

        node.visible = false;
    }


    /**
     * Returns a random color from palette
     * @return
     */
    public Color randomColor(){ return palette[(int)(Math.random() * palette.length)]; }


    public Node getRoot(){ return root; }


    /**
     * Represents a Node
     */
    class Node{

        private int size, xCord, yCord;
        private Color color;
        private Node parent;
        private Node tlChild, trChild, brChild, blChild;
        private boolean visible, selected;



        /**
         * Constructor
         */
        public Node(Color color, Node parent, int size, int xCord, int yCord, boolean visible){
            this.color = color;
            this.parent = parent;
            this.size = size;
            this.xCord = xCord;
            this.yCord = yCord;
            this.visible = visible;

            selected = false;
        }
    }
}