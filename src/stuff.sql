CREATE TABLE `items` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  `item_code` varchar(10) NOT NULL UNIQUE ,
  `description` varchar(50) DEFAULT NULL,
  `price` decimal(13,2) DEFAULT '0.00',
  `inventory_amount` int(11) NOT NULL DEFAULT '0'
);

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `item_code` varchar(10) NOT NULL,
  `quantity` int(11) NOT NULL,
  `order_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
);