create table tvehicle
(
    registrationNumber varchar(50) not null,
    brand              varchar(50) not null,
    model              varchar(50) not null,
    year               int         not null,
    price              int         not null,
    isRented           boolean     not null,
        rentedBy           int,
    category           varchar(50) not null
);

create table tuser
(
    login varchar(200) not null,
    password varchar(200) not null,
    role     varchar(200) not null
);
ALTER TABLE tvehicle ALTER COLUMN rentedBy TYPE VARCHAR;