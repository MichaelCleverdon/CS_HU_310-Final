This is my final!

1. javac *.java
2. java Main [Option]

Here are your options!
CreateItem <item_code> <ItemDescription> <Price> < inventory_amount>
This will create a new item in the items table and prints all of the values for each column of the created record

UpdateInventory <item_code>< inventory_amount>
This will update the inventory amount of the specified item in the items table and prints all of the values for each column of the updated record

DeleteItem<item_code>
This will delete the item from the items table with the matching itemCode

GetItems <item_code or * for all>
prints all of the values for the columns of each item or the specified item

CreateOrder <item_code> <quantity>
Creates an order with the given item_code and quantity
prints all of the values for each column of the order

DeleteOrder<item_code>
Deletes all the orders with the matching item_code

GetOrders <item_code or * for all>
prints all of the values for each column of each order as well as the order total amount (Number of items * the price of the item)

GetOrderDetails <order_id or * for all>
Prints the following for each order or specified order:Order Id, Item Code, Item Description, Order Quantity, Item Price, Total Order Amount