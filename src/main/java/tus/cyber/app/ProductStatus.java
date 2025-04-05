package tus.cyber.app;

// Sealed Classes Example
sealed interface ProductStatus permits Available, OutOfStock, Discontinued {
    String getStatus();
}
