create table todos
(
    id           varchar(160)    not null,
    title        varchar(200) not null,
    completed    boolean default false,
    order_number int,
    link         varchar(255),
    primary key (id)
)