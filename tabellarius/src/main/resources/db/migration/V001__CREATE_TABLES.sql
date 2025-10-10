/*
drop table tb_attachment
drop table tb_recipient
drop table tb_message_error
drop table tb_message_log
drop table tb_message
drop table tb_user
*/

CREATE TABLE tb_message (
    id_message      SERIAL       NOT NULL,
    from_address    VARCHAR(255) NOT NULL,
    message_subject VARCHAR(255) NOT NULL,
    body            TEXT         NULL,
    body_type       VARCHAR(10)  NOT NULL DEFAULT('HTML'),

    CONSTRAINT pk_message PRIMARY KEY (id_message),
    CONSTRAINT ck_message_body_type CHECK (body_type in ('HTML', 'TEXT'))
);

CREATE TABLE tb_message_log (
    id_message     SERIAL       NOT NULL,
    success        BOOLEAN      NOT NULL DEFAULT TRUE,
    sender_ip      VARCHAR(100) NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_message_log PRIMARY KEY (id_message),
    CONSTRAINT fk_message_log_message FOREIGN KEY (id_message) REFERENCES tb_message (id_message)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE tb_message_error (
    id_message  INT           NOT NULL,
    content     VARCHAR(1000) NOT NULL,
    stack_trace VARCHAR(4000)     NULL,

    CONSTRAINT pk_message_error PRIMARY KEY (id_message),
    CONSTRAINT pk_message_error_message FOREIGN KEY (id_message) REFERENCES tb_message (id_message)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE tb_recipient (
    id_recipient SERIAL       NOT NULL,
    id_message   INT          NOT NULL,
    email        VARCHAR(255) NOT NULL,
    type         VARCHAR(3)   NOT NULL,

    CONSTRAINT pk_recipient PRIMARY KEY (id_recipient),
    CONSTRAINT pk_recipient_message FOREIGN KEY (id_message) REFERENCES tb_message (id_message)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_recipient_type CHECK (type in ('TO', 'CC', 'BCC'))
);

CREATE TABLE tb_attachment (
    id_attachment SERIAL       NOT NULL,
    id_message    INT          NOT NULL,
    attachment    BYTEA        NOT NULL,
    file_name     VARCHAR(255) NOT NULL,
    file_type     VARCHAR(255) NOT NULL,

    CONSTRAINT pk_attachment PRIMARY KEY (id_attachment),
    CONSTRAINT pk_attachment_message FOREIGN KEY (id_message) REFERENCES tb_message (id_message)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE tb_user (
    id_user   SERIAL        NOT NULL,
    username  VARCHAR(255)  NOT NULL,
    password  VARCHAR(4000) NOT NULL,
    email     VARCHAR(255)  NOT NULL,
    reset_uid VARCHAR(1000) NULL,

    CONSTRAINT pk_user PRIMARY KEY (id_user),
    CONSTRAINT uq_user_username UNIQUE (username),
    CONSTRAINT uq_user_email UNIQUE (email)
);