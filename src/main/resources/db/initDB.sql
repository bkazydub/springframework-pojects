DROP TABLE product_technical_characteristics IF EXISTS;
DROP TABLE Product IF EXISTS CASCADE;

DROP TABLE hibernate_sequence IF EXISTS;

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

CREATE TABLE hibernate_sequence(next_val BIGINT);
INSERT INTO hibernate_sequence VALUES(1);
INSERT INTO hibernate_sequence VALUES(1);
INSERT INTO hibernate_sequence VALUES(1);
INSERT INTO hibernate_sequence VALUES(1);
INSERT INTO hibernate_sequence VALUES(1);
INSERT INTO hibernate_sequence VALUES(1);