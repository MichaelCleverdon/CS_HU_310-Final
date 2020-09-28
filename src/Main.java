import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String item_code;
        String description;
        Double price;
        int inventory_amount;
        int quantity;
        switch(args[0]){
            case "CreateItem":
                item_code = args[1];
                description = args[2];
                price = Double.parseDouble(args[3]);
                inventory_amount = Integer.parseInt(args[4]);
                tryCreateItem(item_code, description, price, inventory_amount);
                break;
            case "UpdateInventory":
                item_code = args[1];
                inventory_amount = Integer.parseInt(args[2]);
                tryCreateUpdateInventory(item_code, inventory_amount);
                break;
            case "DeleteItem":
                item_code = args[1];
                tryDeleteItem(item_code);
                break;
            case "GetItems":
                item_code = args[1];
                tryGetItems(item_code);
                break;
            case "CreateOrder":
                item_code = args[1];
                quantity = Integer.parseInt(args[2]);
                tryCreateOrder(item_code, quantity);
                break;
            case "DeleteOrder":
                item_code = args[1];
                tryDeleteOrder(item_code);
                break;
            case "GetOrders":
                item_code = args[1];
                tryGetOrders(item_code);
                break;
            case "GetOrderDetails":
                String order_id = args[1];
                tryGetOrderDetails(order_id);
                break;
            default:
                System.out.println("Incorrect command, please review usage directions");
                break;
        }
    }

    private static void tryGetOrderDetails(String order_id) {
        try {
            List<OrderDetails> ODS = getOrderDetails(order_id);
            for(OrderDetails OD : ODS){
                System.out.println(OD.toString());
            }
        } catch (SQLException sqlE) {
            System.out.println("Failed to obtain order details");
            System.out.println(sqlE.getMessage());
        }
    }

    private static List<OrderDetails> getOrderDetails(String order_id) throws SQLException{
        List<OrderDetails> ODS = new ArrayList<OrderDetails>();
        Connection connection = null;

        connection = MySqlDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql;
        if(order_id.equals('*')){
            sql = "SELECT order_id, orders.item_code, description, quantity, price from orders inner join items on orders.item_code = items.item_code";
        }
        else {
            sql = String.format("SELECT order_id, orders.item_code, description, quantity, price from orders inner join items on orders.item_code = items.item_code WHERE order.order_id = %d", Integer.parseInt(order_id));
        }
        ResultSet rs = sqlStatement.executeQuery(sql);

        while(rs.next()){
            //int orderId, String itemCode, String description, int orderQuantity, Double price
            int orderId = rs.getInt(0);
            String itemCode = rs.getString(1);
            String description = rs.getString(2);
            int orderQuantity = rs.getInt(3);
            Double price = rs.getDouble(4);
            ODS.add(new OrderDetails(orderId, itemCode, description, orderQuantity, price));
        }
        rs.close();
        connection.close();
        return ODS;
    }

    private static void tryGetOrders(String item_code) {
        try{
            List<Order> orders = getOrders(item_code);
            for(Order order : orders){
                //Nasty piece of code that one, but it gets the quantity of the order and gets the price of each item in the order and multiplies it by the quantity of the order.
                //This is like an inner join, but without me having to modify the order object
                System.out.println("(" + Double.toString(order.quantity*getItems(order.item_code).get(0).price));
            }
        }
        catch(SQLException sqlException){
            System.out.println("Failed to get orders");
            System.out.println(sqlException.toString());
        }
    }

    private static List<Order> getOrders(String item_code) throws SQLException{
        List<Order> orders = new ArrayList<Order>();
        Connection connection = null;

        connection = MySqlDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql;
        if(item_code.equals('*')) {
            sql = "SELECT * from orders";
        }
        else{
            sql = String.format("SELECT * FROM orders WHERE item_code = %s", item_code);
        }
        ResultSet rs = sqlStatement.executeQuery(sql);
        while(rs.next()){
            int order_id = rs.getInt(0);
            String itemCode = rs.getString(1);
            int quantity = rs.getInt(2);
            Date order_timestamp = rs.getDate(3);
            orders.add(new Order(order_id, itemCode, quantity, order_timestamp));
        }
        rs.close();
        connection.close();
        return orders;
    }

    private static void tryDeleteOrder(String item_code) {
        try {
            deleteOrder(item_code);
            System.out.println("Order has been deleted");
        } catch (SQLException e) {
            System.out.println("Failed to delete order");
            System.out.println(e.getMessage());
        }
    }

    private static void deleteOrder(String item_code) throws SQLException{
        Connection connection = null;

        connection = MySqlDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("DELETE FROM orders WHERE item_code = %s", item_code);
        sqlStatement.executeUpdate(sql);
        connection.close();
    }

    private static void tryCreateOrder(String item_code, int quantity) {
        try{
            Order order = createOrder(item_code, quantity);
            System.out.println(order.toString());
        }
        catch(SQLException sqlException){
            System.out.println("Failed to create order");
            System.out.println(sqlException.getMessage());
        }
    }

    private static Order createOrder(String item_code, int quantity) throws SQLException{
        Connection connection = null;
        Order order = new Order(item_code, quantity);

        connection = MySqlDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("INSERT INTO orders (item_code, quantity) VALUES ('%s', '%d');",
                order.item_code,
                order.quantity);
        sqlStatement.executeUpdate(sql);
        order = getOrderFromCode(sqlStatement, item_code);
        connection.close();

        return order;
    }

    private static Order getOrderFromCode(Statement sqlStatement, String item_code) throws SQLException {
        ResultSet resultSet = sqlStatement.executeQuery(String.format("SELECT * from orders WHERE item_code = %s", item_code));

        resultSet.next();
        Order order = new Order(item_code, resultSet.getInt(2));
        resultSet.close();
        return order;
    }

    private static void tryGetItems(String item_code) {
        try{
            List<Item> items = getItems(item_code);
            for(Item item : items){
                System.out.println(item.toString());
            }
        }
        catch(SQLException sqlException){
            System.out.println("Failed to get item");
            System.out.println(sqlException.getMessage());
        }
    }

    private static List<Item> getItems(String item_code) throws SQLException {
        Connection connection = null;

        connection = MySqlDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        ResultSet resultSet;
        if(item_code.equals('*')) {
            resultSet = sqlStatement.executeQuery(String.format("SELECT * from items"));
        }
        else{
            resultSet = sqlStatement.executeQuery(String.format("SELECT  from items WHERE items.item_code = %s", item_code));
        }
        List<Item> items = new ArrayList<Item>();
        while(resultSet.next()){
            int item_id = resultSet.getInt(0);
            String brand = resultSet.getString(1);
            String description = resultSet.getString(2);
            Double price = resultSet.getDouble(3);
            int inventory_amount = resultSet.getInt(4);

            items.add(new Item(item_id, brand, description, price, inventory_amount));
        }

        resultSet.close();
        return items;
    }

    private static void tryDeleteItem(String item_code) {
        try{
            deleteItem(item_code);
            System.out.println("Item has been deleted");
        }
        catch(SQLException sqlException){
            System.out.println("Failed to delete item");
            System.out.println(sqlException.getMessage());
        }
    }

    private static void deleteItem(String item_code) throws SQLException{
        Connection connection = null;

        connection = MySqlDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("DELETE FROM items where items.item_code = %s", item_code);

        sqlStatement.executeUpdate(sql);
        connection.close();
    }

    private static void tryCreateUpdateInventory(String item_code, int inventory_amount) {
        try{
            Item item = updateItem(item_code, inventory_amount);
            System.out.println(item.toString());
        }
        catch(SQLException sqlException){
            System.out.println("Failed to update inventory");
            System.out.println(sqlException.getMessage());
        }
    }

    private static Item updateItem(String item_code, int inventory_amount) throws SQLException{
        Connection connection = null;

        connection = MySqlDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("UPDATE items SET inventory_amount = %d where items.item_code = %s",
                inventory_amount,
                item_code);

        sqlStatement.executeUpdate(sql);
        Item item = getItemFromCode(sqlStatement, item_code);
        connection.close();
        return item;
    }

    private static Item getItemFromCode(Statement sqlStatement, String item_code) throws SQLException{
        ResultSet resultSet = sqlStatement.executeQuery(String.format("SELECT * from items WHERE items.item_code = %s", item_code));

        resultSet.next();
        Item item = new Item(resultSet.getInt(0), item_code, resultSet.getString(2), resultSet.getDouble(3), resultSet.getInt(4));
        resultSet.close();
        return item;
    }

    private static void tryCreateItem(String item_code, String description, Double price, int inventory_amount) {
        try{
            Item item = createItem(item_code, description, price, inventory_amount);
            System.out.println(item.toString());
        }
        catch(SQLException sqlException){
            System.out.println("Failed to create item");
            System.out.println(sqlException.getMessage());
        }
    }

    private static Item createItem(String item_code, String description, Double price, int inventory_amount) throws SQLException {
        Connection connection = null;

        connection = MySqlDatabase.getDatabaseConnection();
        Statement sqlStatement = connection.createStatement();

        String sql = String.format("INSERT INTO items (item_code, description, price, inventory_amount) VALUES ('%s', '%s', '%f', '%d');",
                item_code,
                description,
                price,
                inventory_amount);
        sqlStatement.executeUpdate(sql);

        Item item = getItemFromCode(sqlStatement, item_code);
        connection.close();

        return item;
    }
}
