USE mailer
GO

/*
drop table tb_attachment
drop table tb_recipient
drop table tb_message_error
drop table tb_message_log
drop table tb_message
drop table tb_user
*/

CREATE TABLE tb_message (
    id_message      int          NOT NULL IDENTITY(1, 1),
    from_address    varchar(255) NOT NULL,
    message_subject varchar(255) NOT NULL,
    body            varchar(MAX)     NULL,
    body_type       varchar(10)  NOT NULL DEFAULT('HTML'),

    CONSTRAINT pk_message PRIMARY KEY CLUSTERED (id_message),
    CONSTRAINT ck_message_body_type CHECK (body_type in ('HTML', 'TEXT'))
);

CREATE TABLE tb_message_log (
    id_message     int          NOT NULL,
    success        bit          NOT NULL DEFAULT(1),
    sender_ip      varchar(100) NOT NULL,
    created_at     datetime2    NOT NULL DEFAULT(GETDATE()),

    CONSTRAINT pk_message_log PRIMARY KEY (id_message),
    CONSTRAINT fk_message_log_message FOREIGN KEY (id_message) REFERENCES tb_message (id_message)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE tb_message_error (
    id_message  int           NOT NULL,
    content     varchar(1000) NOT NULL,
    stack_trace varchar(4000)     NULL,

    CONSTRAINT pk_message_error PRIMARY KEY (id_message),
    CONSTRAINT pk_message_error_message FOREIGN KEY (id_message) REFERENCES tb_message (id_message)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE tb_recipient (
    id_recipient int          NOT NULL IDENTITY(1, 1),
    id_message   int          NOT NULL,
    email        varchar(255) NOT NULL,
    type         varchar(3)   NOT NULL,

    CONSTRAINT pk_recipient PRIMARY KEY (id_recipient),
    CONSTRAINT pk_recipient_message FOREIGN KEY (id_message) REFERENCES tb_message (id_message)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT uq_recipient_type CHECK (type in ('TO', 'CC', 'BCC'))
);

CREATE TABLE tb_attachment (
    id_attachment int            NOT NULL IDENTITY(1, 1),
    id_message    int            NOT NULL,
    attachment    varbinary(MAX) NOT NULL,
    file_name     varchar(255)   NOT NULL,
    file_type     varchar(255)   NOT NULL,

    CONSTRAINT pk_attachment PRIMARY KEY (id_attachment),
    CONSTRAINT pk_attachment_message FOREIGN KEY (id_message) REFERENCES tb_message (id_message)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE tb_user (
    id_user   int           NOT NULL IDENTITY(1, 1),
    username  varchar(255)  NOT NULL,
    password  varchar(4000) NOT NULL,
    email     varchar(255)  NOT NULL,
    reset_uid varchar(1000) NULL,

    CONSTRAINT pk_user PRIMARY KEY CLUSTERED (id_user),
    CONSTRAINT uq_user_username UNIQUE (username),
    CONSTRAINT uq_user_email UNIQUE (email)
);