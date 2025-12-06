import java.util.HashMap;
import java. util.Map;
import java.util. ArrayList;
import java.util.List;
import java.util. Collections;

/**
 * Represents a directory in the file system.
 * Uses HashMap for O(1) child lookup as required.
 */
public class Directory extends Node {
    private Map<String, Node> children;

    public Directory(String name, Directory parent) {
        super(name, parent);
        this.children = new HashMap<>();
    }

    @Override
    public int getSize() {
        int total = 0;
        for (Node child : children.values()) {
            total += child. getSize();
        }
        return total;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    /**
     * Adds a child node to this directory.
     */
    public void addChild(Node node) {
        children.put(node. getName(), node);
        node.setParent(this);
    }

    /**
     * Removes a child by name.
     */
    public Node removeChild(String name) {
        return children.remove(name);
    }

    /**
     * Gets a child by name.  Returns null if not found.
     */
    public Node getChild(String name) {
        return children. get(name);
    }

    /**
     * Checks if a child with the given name exists. 
     */
    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    /**
     * Returns true if directory has no children.
     */
    public boolean isEmpty() {
        return children.isEmpty();
    }

    /**
     * Returns all children as a sorted list for consistent output.
     */
    public List<Node> getChildren() {
        List<Node> list = new ArrayList<>(children.values());
        Collections.sort(list, (a, b) -> a.getName(). compareTo(b. getName()));
        return list;
    }

    /**
     * Returns the map of children for iteration.
     */
    public Map<String, Node> getChildrenMap() {
        return children;
    }
}