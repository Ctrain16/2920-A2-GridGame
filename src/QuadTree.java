import java.awt.*;
import java.util.ArrayList;


/**
 * Represents a QuadTree with each node having four children.
 * Also performs all rotations and changes to said tree
 * for each game function.
 *
 * @author Cal Trainor
 */
public class QuadTree{

    private Node root;
    private Node selectedNode;
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


        root = new Node(randomColor(),null, size,0,0, false, 1, 0);
        root.selected = true;
        selectedNode = root;

        smash();
    }


    /**
     * Moves selected node down a level
     */
    public void moveDown(){
        if(selectedNode.tlChild != null){
            selectedNode.selected = false;
            selectedNode = selectedNode.tlChild;
            selectedNode.selected = true;
        }
    }


    /**
     * Moves selected node up a level
     */
    public void moveUp(){
        if(selectedNode.parent != null){
            selectedNode.selected = false;
            selectedNode = selectedNode.parent;
            selectedNode.selected = true;
        }
    }


    /**
     * Moves selected node right (clockwise)
     */
    public void moveRight(){
        if(selectedNode.parent != null){
            selectedNode.selected = false;
            if(selectedNode.position == 1)
                selectedNode = selectedNode.parent.trChild;
            else if(selectedNode.position == 2)
                selectedNode = selectedNode.parent.brChild;
            else if(selectedNode.position == 3)
                selectedNode = selectedNode.parent.blChild;
            else
                selectedNode = selectedNode.parent.tlChild;
            selectedNode.selected = true;
        }
    }


    /**
     * Moves selected node right (clockwise)
     */
    public void moveLeft(){
        if(selectedNode.parent != null){
            selectedNode.selected = false;
            if(selectedNode.position == 1)
                selectedNode = selectedNode.parent.blChild;
            else if(selectedNode.position == 2)
                selectedNode = selectedNode.parent.tlChild;
            else if(selectedNode.position == 3)
                selectedNode = selectedNode.parent.trChild;
            else
                selectedNode = selectedNode.parent.brChild;
            selectedNode.selected = true;
        }
    }


    /**
     * Smashes selected node into four children
     */
    public void smash(){

        Node node = selectedNode;

        if(node.level < MAX_LEVEL && node.tlChild == null){
            node.tlChild = new Node(randomColor(), node, node.size/2, node.xCord, node.yCord, true, node.level + 1, 1);
            node.trChild = new Node(randomColor(), node, node.size/2, node.xCord + node.size/2,
                    node.yCord,true, node.level + 1,2);
            node.brChild = new Node(randomColor(), node, node.size/2, node.xCord + node.size/2,
                    node.yCord + node.size/2, true, node.level + 1,3);
            node.blChild = new Node(randomColor(), node, node.size/2, node.xCord,
                    node.yCord + node.size/2, true, node.level + 1,4);

            node.visible = false;
        }
    }


    /**
     * Returns an arraylist of rectangles corresponding to
     * each block
     * @return
     */
    public ArrayList<Rectangle> toRectangles(){
        if(root == null)
            return null;

        ArrayList<Rectangle> rectangles = new ArrayList<>();
        rectangles = getRectangles(rectangles, root);

        return  rectangles;
    }
    public ArrayList<Rectangle> getRectangles(ArrayList<Rectangle> list, Node node){
        if(node == null)
            return list;
        else{
            Rectangle r = new Rectangle(node.xCord, node.yCord, node.size, node.color,
                    node.visible, node.selected);
            list.add(r);
            r.setBorderSize(getBorderSize(node));

            getRectangles(list,node.tlChild);
            getRectangles(list,node.trChild);
            getRectangles(list,node.blChild);
            getRectangles(list,node.brChild);
            return list;
        }
    }


    /**
     * Rotates selected node and its children.
     * if clockwise is true then rotates clockwise,
     * else counterclockwise
     * @param clockwise
     */
    public void rotate(boolean clockwise){
        if(clockwise)
            rotateClockwise(selectedNode);
        else
            rotateCounterClockwise(selectedNode);
    }


    /**
     * Rotates nodes children clockwise
     * @param node
     */
    public void rotateClockwise(Node node){
        if(node.tlChild != null){
            setChildPositions(node);
            node.tlChild.xCord += node.size/2;
            node.tlChild.position = 2;
            node.trChild.yCord += node.size/2;
            node.trChild.position = 3;
            node.brChild.xCord -= node.size/2;
            node.brChild.position = 4;
            node.blChild.yCord -= node.size/2;
            node.blChild.position = 1;

            Node oldTl = node.tlChild;
            Node oldTr = node.trChild;
            Node oldBl = node.blChild;
            Node oldBr = node.brChild;
            node.tlChild = oldBl;
            node.trChild = oldTl;
            node.brChild = oldTr;
            node.blChild = oldBr;

            rotateClockwise(node.tlChild);
            rotateClockwise(node.trChild);
            rotateClockwise(node.brChild);
            rotateClockwise(node.blChild);
        }
    }


    /**
     * Rotates nodes children counterclockwise
     * @param node
     */
    public void rotateCounterClockwise(Node node){
        if(node.tlChild != null){
            setChildPositions(node);
            node.tlChild.yCord += node.size/2;
            node.tlChild.position = 4;
            node.trChild.xCord -= node.size/2;
            node.trChild.position = 1;
            node.brChild.yCord -= node.size/2;
            node.brChild.position = 2;
            node.blChild.xCord += node.size/2;
            node.blChild.position = 3;

            Node oldTl = node.tlChild;
            Node oldTr = node.trChild;
            Node oldBl = node.blChild;
            Node oldBr = node.brChild;
            node.tlChild = oldTr;
            node.trChild = oldBr;
            node.brChild = oldBl;
            node.blChild = oldTl;

            rotateCounterClockwise(node.tlChild);
            rotateCounterClockwise(node.trChild);
            rotateCounterClockwise(node.brChild);
            rotateCounterClockwise(node.blChild);
        }
    }


    /**
     * Swaps selected node.
     * If horizontally is true then horizontally,
     * else vertically
     * @param horizontally
     */
     public void swap(boolean horizontally){
         if(horizontally)
             swapHorizontally(selectedNode);
         else
             swapVertically(selectedNode);
     }


    /**
     * Horizontally swaps selected node
     * @param node
     */
     public void swapHorizontally(Node node){
         if(node.tlChild != null){
             setChildPositions(node);
             node.tlChild.yCord += node.size/2;
             node.tlChild.position = 4;
             node.trChild.yCord += node.size/2;
             node.trChild.position = 3;
             node.blChild.yCord -= node.size/2;
             node.blChild.position = 1;
             node.brChild.yCord -= node.size/2;
             node.brChild.position = 2;

             Node oldBr = node.brChild;
             Node oldBl = node.blChild;
             node.brChild = node.trChild;
             node.blChild = node.tlChild;
             node.trChild = oldBr;
             node.tlChild = oldBl;

             swapHorizontally(node.tlChild);
             swapHorizontally(node.trChild);
             swapHorizontally(node.brChild);
             swapHorizontally(node.blChild);
         }
     }


    /**
     * Vertically swaps selected node
     * @param node
     */
     public void swapVertically(Node node){
         if(node.tlChild != null){
             setChildPositions(node);
             node.tlChild.xCord += node.size/2;
             node.tlChild.position = 2;
             node.trChild.xCord -= node.size/2;
             node.trChild.position = 1;
             node.blChild.xCord += node.size/2;
             node.blChild.position = 3;
             node.brChild.xCord -= node.size/2;
             node.brChild.position = 4;

             Node oldTr = node.trChild;
             Node oldBr = node.brChild;
             node.trChild = node.tlChild;
             node.brChild = node.blChild;
             node.tlChild = oldTr;
             node.blChild = oldBr;

             swapVertically(node.tlChild);
             swapVertically(node.trChild);
             swapVertically(node.brChild);
             swapVertically(node.blChild);
         }
     }


    /**
     * Sets children blocks in correct positions
     * @param node
     */
    public void setChildPositions(Node node){
        node.tlChild.xCord = node.xCord;
        node.tlChild.yCord = node.yCord;

        node.trChild.xCord = node.xCord + node.size/2;
        node.trChild.yCord = node.yCord;

        node.brChild.xCord = node.xCord + node.size/2;
        node.brChild.yCord = node.yCord + node.size/2;

        node.blChild.xCord = node.xCord;
        node.blChild.yCord = node.yCord + node.size/2;
    }


    /**
     * Returns a random color from palette
     * @return
     */
    public Color randomColor(){ return palette[(int)(Math.random() * palette.length)]; }


    /**
     * Returns the border size of a specific block
     * @param node
     * @return
     */
    public int getBorderSize(Node node){
        int borderSize = 0;

        if(node.xCord == 0 || node.xCord + node.size == root.size)
            borderSize += node.size;
        if(node.yCord == 0 || node.yCord + node.size == root.size)
            borderSize += node.size;

        return borderSize;
    }


    /**
     * Represents a Node
     */
    class Node{

        private int size, xCord, yCord, level;
        private Color color;
        private Node parent;
        private Node tlChild, trChild, brChild, blChild;
        private boolean visible, selected;

        private int position; //0 = root, 1 = tl, 2 = tr, 3 = br, 4 = bl

        /**
         * Constructor
         */
        public Node(Color color, Node parent, int size, int xCord, int yCord, boolean visible, int level, int position){
            this.color = color;
            this.parent = parent;
            this.size = size;
            this.xCord = xCord;
            this.yCord = yCord;
            this.visible = visible;
            this.level = level;
            this.position = position;

            selected = false;
        }
    }
}