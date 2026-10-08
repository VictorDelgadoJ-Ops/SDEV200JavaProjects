// Victor Delgado
// p.476

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadStateFile {
    private static final class CustomerRecord {
        private final String id;
        private final String name;
        private final String state;
        private final double balance;
        private final String balanceText;

        private CustomerRecord(String text, int lineNumber) {
            String[] fields = text.split(",", -1);
            if (fields.length != 4) {
                throw new IllegalArgumentException(
                        "Invalid customer record on line " + lineNumber + ": " + text);
            }

            id = fields[0].trim();
            name = fields[1].trim();
            state = fields[2].trim();
            balanceText = fields[3].trim();
            try {
                balance = Double.parseDouble(balanceText);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException(
                        "Invalid balance on line " + lineNumber + ": " + fields[3], exception);
            }
        }

        private String asText() {
            return id + "," + name + "," + state + "," + balanceText;
        }

        private boolean isNonDefault() {
            return !id.equals("000") && balance != 0.0;
        }
    }

    public static void main(String[] args) throws IOException {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter name of file to use >> ");
            Path file = findFile(input.nextLine().trim());
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);

            System.out.println();
            System.out.println("Attributes of the file:");
            System.out.println("Creation time " + attributes.creationTime());
            System.out.println("Size " + attributes.size());

            List<CustomerRecord> records = readRecords(file);
            System.out.println();
            System.out.println("All non-default records:");
            double total = 0.0;
            for (CustomerRecord record : records) {
                if (record.isNonDefault()) {
                    System.out.printf("ID #%s  %s  %s $%07.2f%n",
                            record.id, record.name, record.state, record.balance);
                    total += record.balance;
                }
            }
            System.out.printf("Total of all balances is $%.1f%n", total);

            System.out.println();
            System.out.print("Enter account to seek >> ");
            String accountToFind = input.nextLine().trim();
            CustomerRecord desiredRecord = null;
            for (CustomerRecord record : records) {
                if (record.id.equals(accountToFind)) {
                    desiredRecord = record;
                    break;
                }
            }

            if (desiredRecord == null) {
                System.out.println("No record found for account " + accountToFind);
            } else {
                System.out.println("Desired record: " + desiredRecord.asText());
            }
        }
    }

    private static Path findFile(String fileName) throws IOException {
        Path requested;
        try {
            requested = Path.of(fileName);
        } catch (InvalidPathException exception) {
            throw new IOException("Invalid file name: " + fileName, exception);
        }

        if (Files.isRegularFile(requested)) {
            return requested;
        }

        if (requested.getNameCount() == 1) {
            Path directory = Path.of("").toAbsolutePath();
            while (directory != null) {
                Path chapterFile = directory.resolve("Chapter11").resolve(requested);
                if (Files.isRegularFile(chapterFile)) {
                    return chapterFile;
                }
                directory = directory.getParent();
            }
        }

        throw new IOException("Could not find " + fileName
                + ". Enter OutOfStateCusts.txt from the Chapter11 folder, "
                + "or Chapter11/OutOfStateCusts.txt from the repository root.");
    }

    private static List<CustomerRecord> readRecords(Path file) throws IOException {
        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(Math.toIntExact(channel.size()));
            while (buffer.hasRemaining() && channel.read(buffer) != -1) {
                // Read until the buffer is full or the file reaches EOF.
            }
            buffer.flip();
            String contents = StandardCharsets.UTF_8.decode(buffer).toString();

            List<CustomerRecord> records = new ArrayList<>();
            String[] lines = contents.split("\\R");
            for (int index = 0; index < lines.length; index++) {
                String line = lines[index].trim();
                if (!line.isEmpty()) {
                    records.add(new CustomerRecord(line, index + 1));
                }
            }
            return records;
        }
    }
}
