package tus.cyber.app;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class WarehouseApp {
    public static void main(String[] args) {
        // Sample inventory items using Record
        List<InventoryItem> items = Arrays.asList(
                new InventoryItem("Laptop", 1200, 10, true),
                new InventoryItem("Smartphone", 800, 5, true),
                new InventoryItem("Tablet", 300, 0, false),
                new InventoryItem("Headphones", 150, 25, true),
                new InventoryItem("Smartwatch", 200, 0, false)
        );

        // Collections and Comparator with Generics
        System.out.println("Sorted items by price:");
        items.stream()
                .sorted(Comparator.comparing(InventoryItem::price))
                .forEach(System.out::println);

        System.out.println("\nItems with low stock:");
        items.stream()
                .filter(item -> item.stock() > 0 && item.stock() <= 10)
                .forEach(System.out::println);

        // Switch Expressions and Pattern Matching
        System.out.println("\nSwitch Expressions and Pattern Matching:");
//        for (InventoryItem item : items) {
//            String stockStatus = switch (item) {
//                case InventoryItem i && i.stock() > 0 -> "In Stock: " + i.stock();
//                case InventoryItem i && i.stock() == 0 -> "Out of Stock";
//                    default -> "Unknown Stock Status";
//            };
//            System.out.println(item.name() + " - " + stockStatus);
//        }
        for (InventoryItem item : items) {
            String stockStatus = switch (item) {
                case InventoryItem i when (i.stock() > 0) -> "In Stock: " + i.stock(); // 'when' is used instead of '&&'
                case InventoryItem i when (i.stock() == 0) -> "Out of Stock";
                default -> "Unknown Stock Status";
            };
            System.out.println(item.name() + " - " + stockStatus);
        }


        // Concurrency using ExecutorService with Callable
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            List<Callable<String>> tasks = items.stream()
                    .map(item -> (Callable<String>) () -> {
                        Thread.sleep(100); // Simulate processing time
                        return "Processed item: " + item.name();
                    })
                    .collect(Collectors.toList());

            try {
                List<Future<String>> results = executor.invokeAll(tasks);
                for (Future<String> result : results) {
                    System.out.println(result.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
        }

        // NIO2 for File Handling
        Path filePath = Paths.get("inventory.txt");
        try {
            // Write items to file
            Files.write(filePath,
                    items.stream()
                            .map(InventoryItem::toString)
                            .collect(Collectors.toList()),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            // Read items from file
            System.out.println("\nReading from inventory file:");
            Files.lines(filePath, StandardCharsets.UTF_8)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Localization using ResourceBundle
        //Locale locale = Locale.FRANCE; // Change to Locale.US for English
        Locale locale = Locale.US; // Change to Locale.US for English

        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);

        String itemName = items.getFirst().name();
        double itemPrice = items.getFirst().price();

        String localizedMessage = MessageFormat.format(
                messages.getString("item_info"), itemName, itemPrice);

        System.out.println("\nLocalized message:");
        System.out.println(localizedMessage);

        // Date/Time API
        System.out.println("\nDate/Time API:");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current Date and Time: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LocalDate expiryDate = now.toLocalDate().plusDays(30);
        System.out.println("Expiry Date for Items: " + expiryDate);

        // Sealed Classes Example
        System.out.println("\nSealed Classes Example:");
        ProductStatus status = new Discontinued("Tablet");
        System.out.println(status.getStatus());
    }
}

// Record for InventoryItem
record InventoryItem(String name, double price, int stock, boolean inStock) {}

// Sealed Classes Example
sealed interface ProductStatus permits Available, OutOfStock, Discontinued {
    String getStatus();
}

final class Available implements ProductStatus {
    private final String productName;

    public Available(String productName) {
        this.productName = productName;
    }

    @Override
    public String getStatus() {
        return "Product " + productName + " is available.";
    }
}

final class OutOfStock implements ProductStatus {
    private final String productName;

    public OutOfStock(String productName) {
        this.productName = productName;
    }

    @Override
    public String getStatus() {
        return "Product " + productName + " is out of stock.";
    }
}

final class Discontinued implements ProductStatus {
    private final String productName;

    public Discontinued(String productName) {
        this.productName = productName;
    }

    @Override
    public String getStatus() {
        return "Product " + productName + " has been discontinued.";
    }
}
