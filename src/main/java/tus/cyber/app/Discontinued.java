package tus.cyber.app;

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
