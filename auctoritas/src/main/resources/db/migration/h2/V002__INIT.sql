INSERT INTO tb_role (role, description) VALUES ('ROLE_USER_READ', 'Permission to view users');
INSERT INTO tb_role (role, description) VALUES ('ROLE_USER_WRITE', 'Permission to create, update and delete users');
INSERT INTO tb_role (role, description) VALUES ('ROLE_SOFTWARE_READ', 'Permission to view softwares');
INSERT INTO tb_role (role, description) VALUES ('ROLE_SOFTWARE_WRITE', 'Permissions to create, update and delete softwares');
INSERT INTO tb_role (role, description) VALUES ('ROLE_SEND_EMAIL', 'Permission to send email');

INSERT INTO tb_user (
    username,
    password
) VALUES (
    'admin',
    '{bcrypt}$2a$12$NYZurvH.l.vujYDufA6X6uFLBqQ1tDSDxX5VPTAcKSpNxJ3mBiWOW' -- 12345
);

INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 1);
INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 2);
INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 3);
INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 4);
INSERT INTO tb_user_role (id_user, id_role) VALUES (1, 5);

INSERT INTO tb_software (code, name) VALUES ('ABR', 'CONTROLE PATRIMONIAL');
INSERT INTO tb_software (code, name) VALUES ('BAS', 'CONTROLE FINANCEIRO INTEGRADOR');
INSERT INTO tb_software (code, name) VALUES ('CDZ', 'CADASTRO DE CLIENTES DO SISTEMA FINANCEIRO');
INSERT INTO tb_software (code, name) VALUES ('CHG', 'CHANGE CAMBIO');
INSERT INTO tb_software (code, name) VALUES ('CKB', 'CRK BUSINESS PLATFORM');
INSERT INTO tb_software (code, name) VALUES ('EGU', 'INFOTREASURY');
INSERT INTO tb_software (code, name) VALUES ('EIP', 'EASY IRPJ');
INSERT INTO tb_software (code, name) VALUES ('JDC', 'JD CABINE SPB COMPULSORIOS');
INSERT INTO tb_software (code, name) VALUES ('MEN', 'MENSAGENS CAMBIO');
INSERT INTO tb_software (code, name) VALUES ('ORJ', 'ORDENS JUDICIAIS');
INSERT INTO tb_software (code, name) VALUES ('UNE', 'MD COMUNE');
INSERT INTO tb_software (code, name) VALUES ('ZCO', 'ZAP CONTABIL');