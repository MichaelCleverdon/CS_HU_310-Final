import java.util.Date;

public class Order {
    int order_id;
    String item_code;
    int quantity;
    Date order_timestamp;

    Order(String item_code, int quantity){
        this.item_code = item_code;
        this.quantity = quantity;
    }
    Order(int order_id, String item_code, int quantity, Date order_timestamp){
        this.order_id = order_id;
        this.item_code = item_code;
        this.quantity = quantity;
        this.order_timestamp = order_timestamp;
    }

    public String toString(){
        return String.format("(%s, %s, %s, %s)", order_id, item_code, quantity, order_timestamp);
    }
}
