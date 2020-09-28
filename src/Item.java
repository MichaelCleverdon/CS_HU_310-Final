public class Item {
    int item_id;
    String item_code, description;
    Double price;
    int inventory_amount;

    Item(int item_id, String item_code, String description, Double price, int inventory_amount){
        this.item_id = item_id;
        this.item_code = item_code;
        this.description = description;
        this.price = price;
        this.inventory_amount = inventory_amount;
    }

    public String toString(){
        return String.format("(%s, %s, %s, %s, %s)", item_id, item_code, description, price, inventory_amount);
    }
}
