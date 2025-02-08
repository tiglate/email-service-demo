INSERT INTO tb_role (role, description, date_created, last_updated) VALUES ('ROLE_USER_READ', 'Permission to view users', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_role (role, description, date_created, last_updated) VALUES ('ROLE_USER_WRITE', 'Permission to create, update and delete users', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_role (role, description, date_created, last_updated) VALUES ('ROLE_SOFTWARE_READ', 'Permission to view softwares', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_role (role, description, date_created, last_updated) VALUES ('ROLE_SOFTWARE_WRITE', 'Permissions to create, update and delete softwares', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_role (role, description, date_created, last_updated) VALUES ('ROLE_SEND_EMAIL', 'Permission to send email', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO tb_user (
    username,
    password,
    enabled,
    failed_login_attempts,
    account_locked,
    date_created,
    last_updated
) VALUES (
    'admin',
    '{bcrypt}$2a$12$NYZurvH.l.vujYDufA6X6uFLBqQ1tDSDxX5VPTAcKSpNxJ3mBiWOW', -- 12345
    1,
    0,
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 1);
INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 2);
INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 3);
INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 4);
INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 5);

INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('ABR', 'CONTROLE PATRIMONIAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('BAS', 'CONTROLE FINANCEIRO INTEGRADOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('CDZ', 'CADASTRO DE CLIENTES DO SISTEMA FINANCEIRO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('CHG', 'CHANGE CAMBIO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('CKB', 'CRK BUSINESS PLATFORM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('EGU', 'INFOTREASURY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('EIP', 'EASY IRPJ', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('JDC', 'JD CABINE SPB COMPULSORIOS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('MEN', 'MENSAGENS CAMBIO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('ORJ', 'ORDENS JUDICIAIS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('UNE', 'MD COMUNE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tb_software (code, name, date_created, last_updated) VALUES ('ZCO', 'ZAP CONTABIL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);