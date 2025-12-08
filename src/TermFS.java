import java.util.Scanner;

/**
 * Main class - Terminal interface for the file system.
 * Handles command parsing and user interaction.
 */
public class TermFS {
    private final FileSystem fs;
    private final Scanner scanner;
    private boolean running;

    public TermFS() {
        this.fs = new FileSystem();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public static void main(String[] args) {
        TermFS termFS = new TermFS();
        termFS.run();
    }

    public void run() {
        while (running) {
            System.out.print(fs.pwd() + "$ ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            processCommand(input);
        }
        scanner.close();
    }

    private void processCommand(String input) {
        String[] parts = input.split("\\s+");
        String command = parts[0];

        try {
            switch (command) {
                case "mkdir" -> handleMkdir(parts);
                case "touch" -> handleTouch(parts);
                case "ls" -> System.out.println(fs.ls());
                case "cd" -> handleCd(parts);
                case "pwd" -> System.out.println(fs.pwd());
                case "rm" -> handleRm(parts);
                case "tree" -> System.out.println(fs.tree());
                case "du" -> System.out.println("Total size: " + fs.du() + "B");
                case "grep" -> handleGrep(input);
                case "echo" -> handleEcho(input);
                case "exit" -> running = false;
                default -> System.out.println("Unknown command: " + command);
            }
        } catch (FileSystemException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleMkdir(String[] parts) throws FileSystemException {
        if (parts.length < 2) {
            System.out.println("Usage: mkdir [-p] <dir_name>");
            return;
        }

        boolean createParents = parts[1].equals("-p");

        if (createParents) {
            if (parts.length < 3) {
                System.out.println("Usage: mkdir -p <path>");
                return;
            }
            fs.mkdir(parts[2], true);
        } else {
            // Support multiple directories: mkdir bin etc home var
            for (int i = 1; i < parts.length; i++) {
                fs.mkdir(parts[i], false);
            }
        }
    }

    private void handleTouch(String[] parts) throws FileSystemException {
        if (parts.length < 3) {
            System.out.println("Usage: touch <file_name> <size>");
            return;
        }

        int size;
        try {
            size = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("touch: invalid size '" + parts[2] + "'");
            return;
        }

        fs.touch(parts[1], size);
    }

    private void handleCd(String[] parts) throws FileSystemException {
        if (parts.length < 2) {
            System.out.println("Usage: cd <path>");
            return;
        }

        fs.cd(parts[1]); // FileSystemException will be caught in processCommand
    }

    private void handleRm(String[] parts) throws FileSystemException {
        if (parts.length < 2) {
            System.out.println("Usage: rm [-r] <name>");
            return;
        }

        if (parts[1].equals("-r")) {
            if (parts.length < 3) {
                System.out.println("Usage: rm -r <dir_name>");
                return;
            }
            fs.rmRecursive(parts[2]);
        } else {
            fs.rm(parts[1]);
        }
    }

    private void handleEcho(String input) throws FileSystemException {
        // Parse: echo "text" > filename
        int firstQuote = input.indexOf('"');
        int lastQuote = input.lastIndexOf('"');

        if (firstQuote == -1 || lastQuote == firstQuote) {
            System.out.println("Usage: echo \"<text>\" > <file_name>");
            return;
        }

        String content = input.substring(firstQuote + 1, lastQuote);
        String rest = input.substring(lastQuote + 1).trim();

        if (!rest.startsWith(">")) {
            System.out.println("Usage: echo \"<text>\" > <file_name>");
            return;
        }

        String fileName = rest.substring(1).trim();
        if (fileName.isEmpty()) {
            System.out.println("Usage: echo \"<text>\" > <file_name>");
            return;
        }

        fs.echo(content, fileName); // Any FileSystemException bubbles up to processCommand
    }

    private void handleGrep(String input) throws FileSystemException {
        // Parse: grep "pattern" filename
        int firstQuote = input.indexOf('"');
        int lastQuote = input.lastIndexOf('"');

        if (firstQuote == -1 || lastQuote == firstQuote) {
            System.out.println("Usage: grep \"<pattern>\" <file_name>");
            return;
        }

        String pattern = input.substring(firstQuote + 1, lastQuote);
        String fileName = input.substring(lastQuote + 1).trim();

        boolean found = fs.grep(pattern, fileName);
        if (found) {
            System.out.println("Pattern \"" + pattern + "\" found in " + fileName + ".");
        } else {
            System.out.println("Pattern \"" + pattern + "\" not found in " + fileName + ".");
        }
    }
}
