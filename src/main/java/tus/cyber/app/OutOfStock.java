package tus.cyber.app;

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
