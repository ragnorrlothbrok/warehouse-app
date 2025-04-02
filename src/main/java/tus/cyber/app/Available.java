package tus.cyber.app;

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
