/*
DROP TABLE tb_user_role;
DROP TABLE tb_role;
DROP TABLE tb_user;
DROP TABLE tb_software;
DROP TABLE flyway_schema_history;
*/

CREATE TABLE tb_software (
    id_software  SERIAL       NOT NULL,
    code         VARCHAR(3)   NULL,
    name         VARCHAR(255) NOT NULL,
    date_created TIMESTAMP    NOT NULL DEFAULT now(),
    last_updated TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT pk_software PRIMARY KEY (id_software),
    CONSTRAINT uq_software_name UNIQUE (name)
);

CREATE TABLE tb_user (
    id_user                   SERIAL       NOT NULL,
    id_software               INTEGER      NULL,
    username                  VARCHAR(255) NOT NULL,
    password                  VARCHAR(255) NOT NULL,
    enabled                   BOOLEAN      NOT NULL DEFAULT TRUE,
    account_locked            BOOLEAN      NOT NULL DEFAULT FALSE,
    failed_login_attempts     INT          NOT NULL DEFAULT 0,
    account_expiration_date   TIMESTAMP    NULL,
    last_failed_login_attempt TIMESTAMP    NULL,

    date_created TIMESTAMP    NOT NULL DEFAULT now(),
    last_updated TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT pk_user PRIMARY KEY (id_user),
    CONSTRAINT uq_user_username UNIQUE (username),
    CONSTRAINT fk_user_software FOREIGN KEY (id_software) REFERENCES tb_software (id_software)
);

CREATE TABLE tb_role (
    id_role      SERIAL       NOT NULL,
    role         VARCHAR(255) NOT NULL,
    description  TEXT         NULL,
    date_created TIMESTAMP    NOT NULL DEFAULT now(),
    last_updated TIMESTAMP    NOT NULL DEFAULT now(),

    CONSTRAINT pk_role PRIMARY KEY (id_role),
    CONSTRAINT uq_role_role UNIQUE (role)
);

CREATE TABLE tb_user_role (
    id_user INTEGER NOT NULL,
    id_role INTEGER NOT NULL,

    CONSTRAINT pk_user_role PRIMARY KEY (id_user, id_role),
    CONSTRAINT fk_user_role_user FOREIGN KEY (id_user) REFERENCES tb_user (id_user),
    CONSTRAINT fk_user_role_role FOREIGN KEY (id_role) REFERENCES tb_role (id_role)
);