CREATE SCHEMA autodesk;
USE autodesk;

create table tblSubscriptionInfo
(
  subscription_id int UNIQUE,
  product_id int,
  product_name varchar(255),
  subscription_start_date datetime,
  subscription_end_date datetime,
  customer_id int,
  customer_contact_phone varchar(255),
  customer_name varchar(255),
  customer_address varchar(255)
);

INSERT INTO tblSubscriptionInfo (subscription_id, product_id, product_name, subscription_start_date, subscription_end_date, customer_id, customer_contact_phone, customer_name, customer_address)
VALUES
    (1, 101, 'Product A', '2023-08-01', '2024-07-31', 1001, '555-1234', 'John Doe', '123 Main St'),
    (2, 102, 'Product A', '2023-07-15', '2023-12-31', 1002, '555-5678', 'Jane Smith', '456 Elm Ave'),
    (3, 103, 'Product C', '2023-09-01', '2024-08-31', 1003, '555-9876', 'Michael Johnson', '789 Oak Rd'),
    (4, 104, 'Product D', '2023-08-20', '2024-08-19', 1004, '555-4321', 'Sarah Williams', '567 Pine St'),
    (5, 103, 'Product A', '2022-08-20', '2022-09-29', 1004, '555-8888', 'Bill Gates', '333 Somerset'),
    (6, 103, 'Product A', '2023-08-20', '2023-09-29', 1004, '555-8888', 'Bill Gates', '333 Somerset'),
    (7, 105, 'Autocad 2022', '2023-01-01', '2023-04-30', 1003, '555-9876', 'Michael Johnson', '789 Oak Rd'),
    (8, 106, 'Autocad 2023', '2023-09-01', '2024-08-31', 1003, '555-9876', 'Michael Johnson', '789 Oak Rd');

UPDATE tblSubscriptionInfo
SET customer_contact_phone = '555-9876'
WHERE customer_id = 1001;


SELECT COUNT(1)
FROM tblSubscriptionInfo
WHERE YEAR(subscription_end_date) = 2023;


SELECT COUNT(*) AS num_subscribers
FROM tblSubscriptionInfo
WHERE YEAR(subscription_start_date) = 2023
  AND DATEDIFF(subscription_end_date, subscription_start_date) > 90;


SELECT customer_id
FROM tblSubscriptionInfo
GROUP BY customer_id
HAVING COUNT(DISTINCT product_id) > 2;

SELECT product_id
FROM tblSubscriptionInfo
WHERE YEAR(subscription_start_date) = 2022
GROUP BY product_id
ORDER BY COUNT(DISTINCT customer_id) DESC
LIMIT 3;


SELECT p.product_id, COALESCE(COUNT(r.customer_id), 0) AS re_subscribe_count
FROM (
    SELECT DISTINCT product_id
    FROM tblSubscriptionInfo
) AS p
LEFT JOIN (
    SELECT product_id, customer_id
    FROM tblSubscriptionInfo
    GROUP BY product_id, customer_id
    HAVING COUNT(*) > 1
) AS r ON p.product_id = r.product_id
GROUP BY p.product_id;

SELECT DISTINCT s1.customer_id
FROM tblSubscriptionInfo s1
JOIN tblSubscriptionInfo s2 ON s1.customer_id = s2.customer_id
WHERE YEAR(s2.subscription_start_date) = 2023
  AND s1.product_name LIKE 'Autocad%'
  AND s2.product_name LIKE 'Autocad%'
  AND CAST(SUBSTRING_INDEX(s1.product_name, ' ', -1) AS UNSIGNED) < CAST(SUBSTRING_INDEX(s2.product_name, ' ', -1) AS UNSIGNED);
