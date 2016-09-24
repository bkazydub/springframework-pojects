DROP TABLE product_technical_characteristics IF EXISTS;
DROP TABLE Product IF EXISTS CASCADE;
DROP TABLE Account IF EXISTS;
DROP TABLE Customer IF EXISTS;
DROP TABLE Address IF EXISTS;

CREATE TABLE Product (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2500) NOT NULL,
    img_url VARCHAR(255) DEFAULT NULL,
    inventory INTEGER DEFAULT NULL,
    price DECIMAL(7,2) NOT NULL,
    type VARCHAR(255) NOT NULL
);
CREATE INDEX Product_name ON Product (name);

CREATE TABLE product_technical_characteristics (
  Product_id BIGINT NOT NULL,
  properties VARCHAR(255) DEFAULT NULL,
  technical_property VARCHAR(255) NOT NULL,
  PRIMARY KEY (Product_id,technical_property)
);
ALTER TABLE product_technical_characteristics ADD CONSTRAINT fk_characteristics_product FOREIGN KEY (Product_id) REFERENCES Product (id);

CREATE TABLE Customer(
  id BIGINT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  phone_number VARCHAR(10) NOT NULL
);

CREATE TABLE Account(
  id BIGINT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(30) NOT NULL,
  customer_id BIGINT NOT NULL
);
ALTER TABLE Account ADD CONSTRAINT fk_account_customer FOREIGN KEY (customer_id) REFERENCES Customer (id);

CREATE TABLE Address(
  id BIGINT PRIMARY KEY,
  street VARCHAR(255) NOT NULL,
  city VARCHAR(255) NOT NULL,
  country VARCHAR(255) NOT NULL,
  customer_id BIGINT NOT NULL
);
ALTER TABLE Address ADD CONSTRAINT fk_address_customer FOREIGN KEY (customer_id) REFERENCES Customer (id);

CREATE TABLE Orders(
  id BIGINT PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  address_id BIGINT NOT NULL,
  placed TIMESTAMP NOT NULL,
  received TIMESTAMP DEFAULT NULL,
  status VARCHAR(255) NOT NULL
);

ALTER TABLE Orders ADD CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES Customer (id);
ALTER TABLE Orders ADD CONSTRAINT fk_order_address FOREIGN KEY (address_id) REFERENCES Address (id);

CREATE TABLE LineItem (
  id BIGINT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  price DECIMAL NOT NULL,
  quantity INT NOT NULL,
  order_id BIGINT NOT NULL
);

ALTER TABLE LineItem ADD CONSTRAINT fk_line_item_product FOREIGN KEY (product_id) REFERENCES Product (id);
ALTER TABLE LineItem ADD CONSTRAINT fk_line_item_order FOREIGN KEY (order_id) REFERENCES Orders (id);

CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1
