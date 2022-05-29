
create sequence address_id_seq start with 1 increment by 1;

create table address (
    id bigint not null primary key default nextval('address_id_seq'),
    street varchar(255),
    client_id bigint
);

create sequence phone_id_seq start with 1 increment by 1;

create table phone (
    id bigint not null primary key default nextval('phone_id_seq'),
    number varchar(255),
    phone_n bigint,
    client_id bigint
);

create sequence client_sequence start with 1 increment by 1;

create table client
(
    id   bigint not null primary key default nextval('client_sequence'),
    name varchar(50)
);

