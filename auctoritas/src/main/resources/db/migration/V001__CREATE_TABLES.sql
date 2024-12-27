/*
DROP TABLE tb_user_role;
DROP TABLE tb_role;
DROP TABLE tb_user;
DROP TABLE tb_software;
DROP TABLE flyway_schema_history;
*/

CREATE TABLE tb_software (
    id_software int          NOT NULL IDENTITY(1, 1),
    code           varchar(3)   NULL,
    name           varchar(255) NOT NULL,
    date_created   datetime2    NOT NULL DEFAULT (GETDATE()),
    last_updated   datetime2    NOT NULL DEFAULT (GETDATE()),

    CONSTRAINT pk_software PRIMARY KEY (id_software),
    CONSTRAINT uq_software_name UNIQUE (name)
);

CREATE TABLE tb_user (
    id_user        int          NOT NULL IDENTITY(1, 1),
    id_software    int          NULL,
    username       varchar(255) NOT NULL,
    password       varchar(255) NOT NULL,
    date_created   datetime2    NOT NULL DEFAULT (GETDATE()),
    last_updated   datetime2    NOT NULL DEFAULT (GETDATE()),

    CONSTRAINT pk_user PRIMARY KEY (id_user),
    CONSTRAINT uq_user_username UNIQUE (username),
    CONSTRAINT fk_user_software FOREIGN KEY (id_software) REFERENCES tb_software (id_software)
);

CREATE TABLE tb_role (
    id_role      int          NOT NULL IDENTITY(1, 1),
    role         varchar(255) NOT NULL,
    description  varchar(MAX) NULL,
    date_created datetime2    NOT NULL DEFAULT (GETDATE()),
    last_updated datetime2    NOT NULL DEFAULT (GETDATE()),

    CONSTRAINT pk_role PRIMARY KEY (id_role),
    CONSTRAINT uq_role_role UNIQUE (role)
);

CREATE TABLE tb_user_role (
    id_user int NOT NULL,
    id_role int NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (id_user, id_role)
);