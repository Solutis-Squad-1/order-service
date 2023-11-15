-- Index for table orders
CREATE INDEX idx_orders_id ON orders (id);
CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_orders_status_payment ON orders (status_payment);
CREATE INDEX idx_orders_canceled ON orders (canceled);

-- Index for table ordem items
CREATE INDEX idx_order_item_id ON order_items (id);
CREATE INDEX idx_order_item_order_id ON order_items (order_id);
CREATE INDEX idx_order_item_product_id ON order_items (product_id);
CREATE INDEX idx_order_item_deleted ON order_items (deleted);