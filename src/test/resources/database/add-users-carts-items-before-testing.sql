INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'ivan111@gmail.com', '111', 'Ivan', 'Ivanov', 'Ivanovo, 34/5', false);
INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (2, 'petr222@gmail.com', '222', 'Pert', 'Pertov', 'Pertovo, 45/6', false);
INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (3, 'sidor333@gmail.com', '333', 'Sidor', 'Sidorov', 'Sidorovo, 56/7', false);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Sample Book 1', 'Author A', '9781234567897', 19.99, 'This is a sample book description.',
        'https://example.com/cover1.jpg', false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (2, 'Sample Book 2', 'Author B', '9789876543210', 24.99, 'This is a sample book description.',
        'https://example.com/cover2.jpg', false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (3, 'Sample Book 3', 'Author C', '9781122334455', 29.99, 'This is a sample book description.',
        'https://example.com/cover3.jpg', false);

INSERT INTO shopping_carts (user_id)
VALUES (1);
INSERT INTO shopping_carts (user_id)
VALUES (2);
INSERT INTO shopping_carts (user_id)
VALUES (3);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity, is_deleted)
VALUES (2, 3, 1, 1, false);
INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity, is_deleted)
VALUES (3, 3, 3, 2, false);
