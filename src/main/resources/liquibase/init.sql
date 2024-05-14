--liquibase formatted sql
--changeset Emil Askarov:lab4_client dbms:postgresql
CREATE TABLE "client"
(
    id       uuid unique not null,
    username text primary key,
    password text        not null
);


--liquibase formatted sql
--changeset Emil Askarov:lab4_attempt dbms:postgresql
CREATE TABLE attempt
(
    id        uuid        not null primary key,
    x         numeric     not null,
    y         numeric     not null,
    r         numeric     not null,
    timestamp timestamptz not null,
    success   boolean     not null,
    user_id   uuid        not null references client (id)
);
