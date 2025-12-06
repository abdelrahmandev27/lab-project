/**
 * Abstract base class for all file system entities. 
 * Implements the Composite Design Pattern. 
 */
public abstract class Node {
    protected String name;
    protected Directory parent;

    public Node(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    /**
     * Returns the size of this node. 
     * Files return their size, directories return sum of children sizes.
     */
    public abstract int getSize();

    /**
     * Returns true if this node is a directory.
     */
    public abstract boolean isDirectory();
}