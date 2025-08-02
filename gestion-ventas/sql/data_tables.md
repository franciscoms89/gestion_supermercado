INSERT INTO product (id, category, name, price) VALUES
(1, 'Lácteos', 'Leche Entera 1L', 1.25),
(2, 'Panadería', 'Pan Baguette', 0.80),
(3, 'Carnes', 'Pollo Entero', 5.60),
(4, 'Bebidas', 'Jugo de Naranja 1L', 2.10),
(5, 'Frutas y Verduras', 'Manzanas (1kg)', 1.90);

INSERT INTO shop (id, address, city, name) VALUES
(101, 'Av. Siempre Viva 123', 'Springfield', 'Supermercado Central'),
(102, 'Calle 8 No. 45', 'Shelbyville', 'Market Express'),
(103, 'Ruta Nacional 9 Km 15', 'Ogdenville', 'Super Surti'),
(104, 'Diagonal 77 No. 14-22', 'North Haverbrook', 'Mini Ahorros'),
(105, 'Av. Libertador 999', 'Capital Federal', 'HiperMarket');

INSERT INTO sale (id, sale_date, shop_id) VALUES
(1, '2025-07-20', 101),
(2, '2025-07-21', 102),
(3, '2025-07-22', 101),
(4, '2025-07-23', 103),
(5, '2025-07-23', 102);

INSERT INTO sale_products (sale_id, product_id) VALUES
(1, 1),
(1, 2),  
(2, 3),
(3, 1),
(3, 5);  