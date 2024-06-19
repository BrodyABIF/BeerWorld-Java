drop table if exists motorcycle;
drop table if exists company;


create table company (
    id      identity primary key,
    name    varchar(100)
);

create table motorcycle (
    id identity primary key,
    bezeichnung varchar(100),
--     baujhar date,
    hubraum integer,
    leistung integer,
    drehmoment integer,
    preis integer,
    showId BIGINt,
    constraint fk_showRoom foreign key (showId) references company (id)
);