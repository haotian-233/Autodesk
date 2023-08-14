## SQL questions and answers

### When a customer updates their contact phone number, what query should we run in order to save that update to the database?

```
UPDATE tblSubscriptionInfo
SET customer_contact_phone = 'new_phone_number'
WHERE customer_id = customer_id_value;
```

### We've noted that the phone number update feature in the web application is too slow, and have identified that the update query is the primary bottleneck. What could we do to speed up this query? 

Assuming the update of phone number (also address) is not very frequent (as in real life), the main reason why the query is slow as the database grows is because it has to update all the rows of that distinct user.

The most effective optimization method in my opinion would be normalized the table to 3rd normal form. There should be a separate table contain unique customer_id, customer phone number and customer address. So the update query will only need to update 1 row in the new table. Whereas in the subscription table, we should only record unique customer_id in each row, but not other customer info.

Second, we could also introduce indexes to the new table to speed up the update query performance. For example, we could set customer_id as primary key in the new table.

This optimization could also applies to product_id, production_name and other potential product info.

Assuming the aforementioned optimization has been done, and some write operation is still slow. There are some ways to optimized the write speed from system design perspective too. Such as sharding, caching, asynchronous/batch write etc.


### Come up with the queries to find:

1. number of subscribers whose subscriptions will be ending in 2023;

```
SELECT COUNT(1)
FROM tblSubscriptionInfo
WHERE YEAR(subscription_end_date) = 2023
```

2. number of subscribers who have subscribed for more than 3 months in 2022;

```
SELECT COUNT(*) AS num_subscribers
FROM tblSubscriptionInfo
WHERE YEAR(subscription_start_date) = 2022
  AND DATEDIFF(subscription_end_date, subscription_start_date) > 90;
```

3. subscribers who have subscribed for more than two products;

```
SELECT customer_id
FROM tblSubscriptionInfo
GROUP BY customer_id
HAVING COUNT(DISTINCT product_id) > 2;
```


4. product with the most/2ndmost/3rdmost number of subscribers in 2022;

```
SELECT product_id
FROM tblSubscriptionInfo
WHERE YEAR(subscription_start_date) = 2023
GROUP BY product_id
ORDER BY COUNT(DISTINCT customer_id) DESC
LIMIT 3;
```

5. number of subscribers who have re-subscribed more than once for each product;

```
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
```

6. subscribers who have re-subscribed a higher version of the product in 2023 - for example Autocad 2022 to Autocad 2023.

```
SELECT DISTINCT s1.customer_id
FROM tblSubscriptionInfo s1
JOIN tblSubscriptionInfo s2 ON s1.customer_id = s2.customer_id
WHERE YEAR(s2.subscription_start_date) = 2023
  AND s1.product_name LIKE 'Autodesk%'
  AND s2.product_name LIKE 'Autodesk%'
  AND CAST(SUBSTRING_INDEX(s1.product_name, ' ', -1) AS UNSIGNED) < CAST(SUBSTRING_INDEX(s2.product_name, ' ', -1) AS UNSIGNED);
```
