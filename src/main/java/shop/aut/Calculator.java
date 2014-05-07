package shop.aut;

/**
 * Dummy-Applikation für das JBehave-Beispiel.
 */
public final class Calculator {
    
    private Integer orderQuantity;
    
    private Integer availableStock;
    
    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
    
    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
    
    public void reset() {
        this.orderQuantity = null;
        this.availableStock = null;
    }
    
    public boolean isInStock() {
        return this.availableStock >= this.orderQuantity;
    }
    
    public double getDiscount() {
        double discount = 0;
        if (this.orderQuantity >= 50 && this.orderQuantity < 100) {
            discount = 10;
        } else if (this.orderQuantity >= 100) {
            discount = 20;
        }
        return discount;
    }
    
    public double getPricePerItem() {
        return 10 - (10 * getDiscount() / 100);
    }
    
    public double getShippingCosts() {
        return 7.5;
    }
    
    public double getOrderValue() {
        return (this.orderQuantity * getPricePerItem()) + getShippingCosts();
    }
    
}
