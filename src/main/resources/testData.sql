INSERT INTO Product(id,type,name,description,img_url,price,inventory) VALUES (100,'LAPTOP','laptopDefault','laptop description','laptop.jpg',456.78,9);
INSERT INTO Product(id,type,name,description,img_url,price,inventory) VALUES (101,'PHONE','phoneDefault','phone description','phone.jpg',523.32,0);

INSERT INTO product_technical_characteristics (Product_id,properties,technical_property) VALUES (100,'Intel Core i5','laptop.technical.cpu');
INSERT INTO product_technical_characteristics (Product_id,properties,technical_property) VALUES (100,'AMD Radeon','laptop.technical.graphics');

INSERT INTO product_technical_characteristics (Product_id,properties,technical_property) VALUES (101,'atom','mobile.technical.cpu');
INSERT INTO product_technical_characteristics (Product_id,properties,technical_property) VALUES (101,'5 MPX','mobile.technical.cam');