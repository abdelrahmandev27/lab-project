/**
 * Custom exception for file system errors.
 * Provides clean error handling without crashing. 
 */
public class FileSystemException extends Exception {
    public FileSystemException(String message) {
        super(message);
    }
}