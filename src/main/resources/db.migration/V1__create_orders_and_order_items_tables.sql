create table order_items (
    id bigint not null,
    created_at timestamp(6) not null,
    deleted boolean not null,
    deleted_at timestamp(6),
    price float(53) not null,
    product_id integer not null,
    quantity integer not null,
    total float(53) not null,
    order_id bigint not null,
    primary key (id)
);

create table orders (
    id bigint not null,
    canceled boolean not null,
    canceled_at timestamp(6),
    created_at timestamp(6) not null,
    status_payment varchar(255) not null check (status_payment in ('NOT_PAID','IN_PROCESSING','CONFIRMED','REFUSED','CANCELED')),
    summary varchar(255) not null,
    total float(53) not null,
    user_id bigint not null,
    primary key (id)
);

alter table if exists order_items
    add constraint FK_OrderItems_Order
    foreign key (order_id)
    references orders;

create sequence order_id_seq start with 1 increment by 1;

create sequence order_items_id_seq start with 1 increment by 1;