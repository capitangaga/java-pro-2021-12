alter table client add column address_id bigint;
create table address (id bigint not null, street varchar(255), primary key (id));
create table phone (id bigint not null, number varchar(255), client_id bigint, primary key (id));
alter table client add constraint address_constraint foreign key (address_id) references address;
alter table phone add constraint phone_constraint foreign key (client_id) references client;