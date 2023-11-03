CREATE TABLE orders (
    id BIGINT generated always as identity,
    moment timestamp(6) not null,
    user_id BIGINT not null,
    status_payment BIGINT not null,
    summary VARCHAR(300) not null,
    canceled BOOLEAN not null,
    created_at timestamp(6) not null,
    canceled_at timestamp(6),

    primary key (id)
);

CREATE TABLE images (
    id bigint,
    archive_name varchar(255) not null,
    content_type varchar(255) not null,
    original_name varchar(255) not null,
    size bigint not null,
    url varchar(255) not null,

    primary key (id)
);

CREATE TABLE products (
    id BIGINT,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    seller_id BIGINT NOT NULL,
    image_id BIGINT NOT NULL
);

CREATE TABLE categories (
    id BIGINT,
    name VARCHAR NOT NULL,
);

CREATE TABLE product_category (
    product_id BIGINT,
    category_id BIGINT,

    FOREIGN KEY (product_id) REFERENCES products (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE order_product (
    order_id BIGINT,
    product_id BIGINT,
    price NUMERIC(10, 2) NOT NULL,
    quantity BIGINT NOT NULL,

    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);