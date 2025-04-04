package tus.cyber.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class WarehouseApp {
    Logger logger;
    List<InventoryItem> items;

    public WarehouseApp() {
        logger = Logger.getLogger(WarehouseApp.class.getName());

        // Sample inventory items using Record
        items = new ArrayList<>();
        Consumer<InventoryItem> addItem = items::add;
        addItem.accept(new InventoryItem("Laptop", 1200, 10, true));
        addItem.accept(new InventoryItem("Smartphone", 800, 5, true));
        addItem.accept(new InventoryItem("Tablet", 300, 0, false));
        addItem.accept(new InventoryItem("Headphones", 150, 25, true));
        addItem.accept(new InventoryItem("Smartwatch", 200, 0, false));
        logger.info("Warehouse Application Initialized");
    }

    // Main method to run the application
    public static void main(String[] args) {
        WarehouseApp app = new WarehouseApp();
        app.execute();
    }

    public void printSortedItems() {
        // Collections and Comparator with Generics
        logger.info("Sorted items by price:");
        items.stream()
                .sorted(Comparator.comparing(InventoryItem::price))
                .forEach(System.out::println);

    }

    public void printLowStockItems() {
        // Collections and Comparator with Generics
        logger.info("\nItems with low stock:");
        items.stream()
                .filter(item -> item.stock() > 0 && item.stock() <= 10)
                .forEach(System.out::println);
    }

    public void printStockStatus() {
        // Switch Expressions and Pattern Matching
        logger.info("\nSwitch Expressions and Pattern Matching:");
        for (InventoryItem item : items) {
            String stockStatus = switch (item) {
                case InventoryItem i when (i.stock() > 0) -> "In Stock: " + i.stock(); // 'when' is used instead of '&&'
                case InventoryItem i when (i.stock() == 0) -> "Out of Stock";
                default -> "Unknown Stock Status";
            };
            logger.info(item.name() + " - " + stockStatus);
        }
    }

    // Concurrency using ExecutorService with Callable
    public void fastProcessInventory() {
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            List<Callable<String>> tasks = items.stream()
                    .map(item -> (Callable<String>) () -> {
                        Thread.sleep((long) (Math.random() * 100)); // Simulate processing time
                        return "Processed item: " + item.name();
                    })
                    .collect(Collectors.toList());

            try {
                List<Future<String>> results = executor.invokeAll(tasks);
                for (Future<String> result : results) {
                    logger.info(result.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
        }
    }

    public void persistData() {
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
            logger.info("\nReading from inventory file:");
            Files.lines(filePath, StandardCharsets.UTF_8)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Localization using ResourceBundle
    public void printLocalePrices() {
        Locale locale = Locale.FRANCE; // Change to Locale.US for English
        //Locale locale = Locale.US; // Change to Locale.FRANCE for French

        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);

        String itemName = items.getFirst().name();
        double itemPrice = items.getFirst().price();

        String localizedMessage = MessageFormat.format(
                messages.getString("item_info"), itemName, itemPrice);

        logger.info("\nLocalized message:");
        logger.info(localizedMessage);
    }

    public void printExpirationDate() {
        // Date/Time API
        logger.info("\nDate/Time API:");
        LocalDateTime now = LocalDateTime.now();
        logger.info("Current Date and Time: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LocalDate expiryDate = now.toLocalDate().plusDays(30);
        logger.info("Expiry Date for Items: " + expiryDate);
    }

    public void printDiscontinuedItems() {
        // Sealed Classes Example
        logger.info("\nSealed Classes Example:");
        ProductStatus status = new Discontinued("Tablet");
        logger.info(status.getStatus());
    }

    // findFirst() method to get the first item
    public void getFirst() {
        InventoryItem item = items.stream().findFirst().orElse(null);
        System.out.println("First item is: " + item.name());
    }

    // Stream count() method to count items
    public void countItems() {
        double count = items.stream().count();
        System.out.println("Total items in inventory: " + count);
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("""
                \nWarehouse Application Menu:
                1. Print Sorted Items
                2. Print Low Stock Items
                3. Print Stock Status
                4. Fast Process Inventory
                5. Persist Data
                6. Print Locale Prices
                7. Print Expiration Date
                8. Print Discontinued Items
                9. Print First Item
                10. Print Item Count
                20. Exit
                Enter your choice: """);

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> printSortedItems();
                case 2 -> printLowStockItems();
                case 3 -> printStockStatus();
                case 4 -> fastProcessInventory();
                case 5 -> persistData();
                case 6 -> printLocalePrices();
                case 7 -> printExpirationDate();
                case 8 -> printDiscontinuedItems();
                case 9 -> getFirst();
                case 10 -> countItems();
                case 20 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}

