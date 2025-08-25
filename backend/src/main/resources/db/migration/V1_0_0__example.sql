create sequence if not exists sequence_example;
create table if not exists example
(
    id        bigint       not null
        primary key,
    text     varchar(250) not null
);
