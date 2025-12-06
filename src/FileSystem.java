/**
 * Manages the file system state and operations.
 * Separates business logic from user interface.
 */
public class FileSystem {
    final private Directory root;
    private Directory currentDirectory;

    public FileSystem() {
        this.root = new Directory("/", null);
        this.currentDirectory = root;
    }

    public Directory getRoot() {
        return root;
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Returns the full path of the current working directory.
     * Uses a stack to build the path from bottom to top.
     */
    public String pwd() {
        if (currentDirectory == root) {
            return "/";
        }

        LabStack<String> stack = new LabStack<>();
        Directory dir = currentDirectory;

        while (dir != null && dir != root) {
            stack.push(dir.getName());
            dir = dir. getParent();
        }

        StringBuilder path = new StringBuilder();
        while (!stack.isEmpty()) {
            path. append("/").append(stack. pop());
        }

        return path.toString();
    }

    /**
     * Creates a directory.  Supports -p flag for creating parent directories.
     */
    public void mkdir(String path, boolean createParents) throws FileSystemException {
        if (createParents) {
            mkdirWithParents(path);
        } else {
            mkdirSingle(path);
        }
    }

    private void mkdirSingle(String name) throws FileSystemException {
        if (currentDirectory.hasChild(name)) {
            throw new FileSystemException("'" + name + "' already exists.");
        }
        Directory newDir = new Directory(name, currentDirectory);
        currentDirectory. addChild(newDir);
    }

    private void mkdirWithParents(String path) throws FileSystemException {
        String[] parts = path.split("/");
        Directory dir = currentDirectory;

        for (String part : parts) {
            if (part.isEmpty()) continue;

            Node child = dir.getChild(part);
            if (child == null) {
                Directory newDir = new Directory(part, dir);
                dir.addChild(newDir);
                dir = newDir;
            } else if (child.isDirectory()) {
                dir = (Directory) child;
            } else {
                throw new FileSystemException("'" + part + "' is not a directory.");
            }
        }
    }

    /**
     * Creates a file with the given name and size.
     */
    public void touch(String name, int size) throws FileSystemException {
        if (currentDirectory.hasChild(name)) {
            throw new FileSystemException("'" + name + "' already exists.");
        }
        File newFile = new File(name, currentDirectory, size);
        currentDirectory.addChild(newFile);
    }

    /**
     * Writes content to a file, creating it if it doesn't exist. 
     * Supports paths like "Documents/plan. doc".
     */
    public void echo(String content, String path) throws FileSystemException {
        // Check if path contains directory components
        int lastSlash = path.lastIndexOf('/');

        if (lastSlash == -1) {
            // Simple filename in current directory
            echoToFile(content, path, currentDirectory);
        } else {
            // Path with directories
            String dirPath = path.substring(0, lastSlash);
            String fileName = path.substring(lastSlash + 1);

            Node targetDir = resolvePath(dirPath);
            if (targetDir == null || !targetDir.isDirectory()) {
                throw new FileSystemException("Path '" + dirPath + "' not found.");
            }
            echoToFile(content, fileName, (Directory) targetDir);
        }
    }

    private void echoToFile(String content, String name, Directory dir) {
        Node existing = dir.getChild(name);
        if (existing != null && ! existing.isDirectory()) {
            ((File) existing).setContent(content);
        } else {
            File newFile = new File(name, dir, content);
            dir.addChild(newFile);
        }
    }

    /**
     * Changes the current directory. 
     * Supports: absolute paths (/home/user), relative paths (mydir), 
     * parent (. .), current (.), and root (/). 
     */
    public void cd(String path) throws FileSystemException {
        Node target = resolvePath(path);

        if (target == null) {
            throw new FileSystemException("Path '" + path + "' not found.");
        }
        if (! target.isDirectory()) {
            throw new FileSystemException("'" + path + "' is not a directory.");
        }

        currentDirectory = (Directory) target;
    }

    /**
     * Resolves a path string to a Node.
     * Helper method used by cd, rm, and other commands.
     */
    public Node resolvePath(String path) {
        if (path. equals("/")) {
            return root;
        }

        Directory startDir = path.startsWith("/") ? root : currentDirectory;
        String[] parts = path. split("/");

        Node current = startDir;
        for (String part : parts) {
            if (part.isEmpty() || part.equals(".")) {
                continue;
            }
            if (part.equals("..")) {
                if (current. isDirectory()) {
                    Directory dir = (Directory) current;
                    current = (dir.getParent() != null) ? dir. getParent() : root;
                }
            } else {
                if (! current.isDirectory()) {
                    return null;
                }
                current = ((Directory) current).getChild(part);
                if (current == null) {
                    return null;
                }
            }
        }
        return current;
    }

    /**
     * Lists contents of current directory.
     */
    public String ls() {
        StringBuilder sb = new StringBuilder();
        for (Node child : currentDirectory.getChildren()) {
            if (child.isDirectory()) {
                sb. append(child.getName()).append("/");
            } else {
                sb. append(child.getName()). append(" ("). append(child.getSize()).append("B)");
            }
            sb.append(" ");
        }
        return sb.toString(). trim();
    }

    /**
     * Removes a file or empty directory.
     */
    public void rm(String name) throws FileSystemException {
        Node target = currentDirectory.getChild(name);

        if (target == null) {
            throw new FileSystemException("'" + name + "' not found.");
        }
        if (target. isDirectory() && !((Directory) target).isEmpty()) {
            throw new FileSystemException("Cannot remove directory '" + name + "'.  It is not empty.");
        }

        currentDirectory.removeChild(name);
    }

    /**
     * Recursively removes a directory and all its contents. 
     */
    public void rmRecursive(String name) throws FileSystemException {
        Node target = currentDirectory.getChild(name);

        if (target == null) {
            throw new FileSystemException("'" + name + "' not found.");
        }

        currentDirectory.removeChild(name);
    }

    /**
     * Displays tree structure starting from current directory.
     */
    public String tree() {
        StringBuilder sb = new StringBuilder();
        sb.append(".\n");
        printTree(currentDirectory, "", sb);
        return sb.toString(). trim();
    }

    private void printTree(Directory dir, String prefix, StringBuilder sb) {
        java.util.List<Node> children = dir.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            boolean isLast = (i == children.size() - 1);
            String connector = isLast ?  "└── " : "├── ";
            String extension = isLast ?  "    " : "│   ";

            if (child.isDirectory()) {
                sb.append(prefix). append(connector).append(child.getName()). append("/\n");
                printTree((Directory) child, prefix + extension, sb);
            } else {
                sb.append(prefix).append(connector).append(child.getName())
                        .append(" ("). append(child.getSize()).append("B)\n");
            }
        }
    }

    /**
     * Returns total size of current directory and all contents.
     */
    public int du() {
        return currentDirectory.getSize();
    }

    /**
     * Searches for pattern in file using KMP algorithm. 
     */
    public boolean grep(String pattern, String fileName) throws FileSystemException {
        Node target = currentDirectory. getChild(fileName);

        if (target == null) {
            throw new FileSystemException("'" + fileName + "' not found.");
        }
        if (target.isDirectory()) {
            throw new FileSystemException("'" + fileName + "' is a directory.");
        }

        return ((File) target).containsPattern(pattern);
    }
}