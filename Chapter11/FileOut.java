// Victor Delgado
//p.454

import java.io.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;

public class FileOut {
    public static void main(String[] args) {
        Path chapterFolder = Path.of("Chapter11");
        Path file = Files.isDirectory(chapterFolder)
                ? chapterFolder.resolve("names.txt")
                : Path.of("names.txt");
        String s = "Victor Delgado";
        byte[] data = s.getBytes();

        try (OutputStream output = new BufferedOutputStream(
                Files.newOutputStream(file, CREATE))) {
            output.write(data);
            output.flush();
            System.out.println("names.txt was written to " + file.toAbsolutePath());
        } catch (IOException exception) {
            System.out.println("Message: " + exception);
        }
    }
}
