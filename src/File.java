/**
 * Represents a file in the file system. 
 * Stores content as a string and size as an integer.
 */
public class File extends Node {
    private String content;
    private int size;

    public File(String name, Directory parent, int size) {
        super(name, parent);
        this. size = size;
        this.content = "";
    }

    public File(String name, Directory parent, String content) {
        super(name, parent);
        this.content = content;
        this.size = content.length();
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.size = content.length();
    }

    /**
     * Searches for pattern in content using KMP algorithm.
     * Returns true if pattern is found. 
     */
    public boolean containsPattern(String pattern) {
        return KMP.search(content, pattern);
    }
}