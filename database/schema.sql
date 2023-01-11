-- create a database called eshop
drop database if exists eshop;

create database eshop;

-- Point to use the database
use eshop;

DROP TABLE IF EXISTS customers;
-- create a table called customers in the eshop database 
create table customers (
    name varchar(32) not null,
    address varchar(128) not null, 
    email varchar(128) not null,
    primary key (name)
);

-- Write sql statement in the schema.sql file to populate the customers table with customers's data from data.csv
INSERT INTO customers (name, address, email) values ('fred', '201 Cobblestone Lane', 'fredflintstone@bedrock.com');
INSERT INTO customers (name, address, email) values ('sherlock', '221B Baker Street, London', 'sherlock@consultingdetective.org');
INSERT INTO customers (name, address, email) values ('spongebob', '124 Conch Street, Bikini Bottom', 'spongebob@yahoo.com');
INSERT INTO customers (name, address, email) values ('jessica', '698 Candlewood Land, Cabot Cove', 'fletcher@gmail.com');
INSERT INTO customers (name, address, email) values ('dursley', '4 Privet Drive, Little Whinging, Surrey', 'dursley@gmail.com');


DROP TABLE IF EXISTS orders;
-- create a table order
create table orders (
    order_id varchar(32) not null,
    name varchar(32) not null,
    primary key (order_id),
    foreign key (name) references customers(name)
);

DROP TABLE IF EXISTS line_items;

create table line_items (
    order_id varchar(32) not null,
    item varchar(128) not null,
    quantity int not null,
    primary key (order_id, item),
    foreign key (order_id) references orders (order_id)
);
commit;

-- Insertion of orders, must contain an existing customer record
-- Insert into orders
INSERT INTO orders (order_id, name) values ('123456X', 'fred');

-- Insert into line_items
INSERT INTO line_items (order_id, item, quantity) values ('123456X', 'iphone', 10);

DROP TABLE IF EXISTS order_status;

create table order_status (
    order_id varchar(32) not null,
    delivery_id varchar(32) not null,
    status enum('pending', 'dispatched'),
    status_update datetime default current_timestamp ON UPDATE current_timestamp,
    primary key (order_id)
);
