import java.awt.*;
import java.util.ArrayList;

/**
 * The back-end for the Grid Game
 * Each Square in the Grid Game can have either 0 or 4
 * Children - it's a quad tree
 *
 **/
public class NestedGrid {

    /**
     *    |-----|-----|
     *    |  TL |  TR |
     *    |-----+-----|
     *    |  BL |  BR |
     *    |_____|_____|
     */
    public static final int MAX_SIZE = 512;
    private QuadTree quadTree;

    /**
     * Create a NestedGrid w/ 5 random colored squares to start
     * a root and its 4 children the root is at level 1 and children at 2
     * the selected square (denoted as yellow highlight)
     * is the root square (the owner of the 4 child squares)
     * @param mxLevels the max depth of the game board
     * @param palette the color palette to use
     */
    public NestedGrid(int mxLevels, Color[] palette) {
        quadTree = new QuadTree(MAX_SIZE, palette, mxLevels);
        rectanglesToDraw();
    }

    /**
     * The selected square moves up to be its parent (if possible)
     */
    public void moveUp() { quadTree.moveUp(); }

    /**
     * the selected square moves into the upper left child (if possible)
     * of the currently selected square
     */
    public void moveDown(){ quadTree.moveDown(); }

    /**
     * the selected square moves counter clockwise to a sibling
     */
    public void moveLeft() { quadTree.moveLeft(); }

    /**
     * Move the selected square to the next sibling clockwise
     */
    public void moveRight() { quadTree.moveRight(); }

    /**
     * Return an array of the squares (as class Rectangle) to draw on the screen
     * @return
     */
    public Rectangle[] rectanglesToDraw ( ) {
        ArrayList<Rectangle> allBlocks = quadTree.toRectangles();
        return allBlocks.toArray(new Rectangle[allBlocks.size()]);
    }

    /**
     * smash a square into 4 smaller squares (if possible)
     * a square at max depth level is not allowed to be smashed
     * leave the selected square as the square that was just
     * smashed (it's just not visible anymore)
     */
    public void smash() { quadTree.smash(); }

    /**
     * Rotate the descendants of the currently selected square
     * @param clockwise if true rotate clockwise, else counterclockwise
     */
    public void rotate(boolean clockwise) { quadTree.rotate(clockwise); }

    /**
     * flip the descendants of the currently selected square
     * the descendants will become the mirror image
     * @param horizontally if true then flip over the x-axis,
     *                     else flip over the y-axis
     */
    public void swap (boolean horizontally) { quadTree.swap(horizontally); }

}
