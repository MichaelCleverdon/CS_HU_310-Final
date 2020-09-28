public class OrderDetails {
    int orderId;
    String itemCode, description;
    int orderQuantity;
    Double price, totalAmount;
    OrderDetails(int orderId, String itemCode, String description, int orderQuantity, Double price){
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.description = description;
        this.orderQuantity = orderQuantity;
        this.price = price;
        this.totalAmount = orderQuantity * price;
    }
    public String toString(){
        return String.format("(%s, %s, %s, %s, %s, %s)", orderId, itemCode, description, orderQuantity, price, totalAmount);
    }
}
