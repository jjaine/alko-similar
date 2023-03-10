-- Drop tables
drop table if exists product;

-- Create schema
create table product (
    id text not null primary key,
    look_count int not null default 1,
    look_date timestamp not null default current_timestamp
);