drop table if exists beer;
drop table if exists company;


create table company (
    id      identity primary key not null ,
    name    varchar(20) not null ,
    ort     varchar(20) not null ,
    plz     integer not null
);

create table beer (
    id identity primary key not null ,
    sorte varchar(20) not null ,
    stammwuerze integer not null ,
    alkoholanteil float not null ,
    braujahr integer not null ,
    preis float not null ,
    show_id BIGINt,
    constraint fk_Company foreign key (show_id) references company (id)
);

