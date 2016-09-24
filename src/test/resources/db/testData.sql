SET AUTOCOMMIT FALSE;

INSERT INTO Product(id,type,name,description,img_url,price,inventory) VALUES (100, 'LAPTOP', 'HP Pavillion', 'laptop description','laptop.jpg',456.78,9);
INSERT INTO product_technical_characteristics (Product_id,properties,technical_property) VALUES (100,'Intel Core i5','laptop.technical.cpu');
INSERT INTO product_technical_characteristics (Product_id,properties,technical_property) VALUES (100,'AMD Radeon','laptop.technical.graphics');

INSERT INTO Product(id,type,name,description,img_url,price,inventory) VALUES (101,'PHONE','Samsung Galaxy S III','description goes here','phone.jpg',523.32,0);
INSERT INTO product_technical_characteristics (Product_id,properties,technical_property) VALUES (101,'Samsung Exynos 4 Quad','mobile.technical.cpu');
INSERT INTO product_technical_characteristics (Product_id,properties,technical_property) VALUES (101,'8 MPX','mobile.technical.cam');

COMMIT;