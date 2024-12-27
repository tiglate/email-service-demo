IF NOT EXISTS(SELECT 1 FROM tb_role)
BEGIN
    INSERT INTO tb_role (role, description) VALUES ('ROLE_USER_READ', 'Permission to view users');
    INSERT INTO tb_role (role, description) VALUES ('ROLE_USER_WRITE', 'Permission to create, update and delete users');
    INSERT INTO tb_role (role, description) VALUES ('ROLE_SOFTWARE_READ', 'Permission to view softwares');
    INSERT INTO tb_role (role, description) VALUES ('ROLE_SOFTWARE_WRITE', 'Permissions to create, update and delete softwares');
    INSERT INTO tb_role (role, description) VALUES ('ROLE_SEND_EMAIL', 'Permission to send email');
END

IF NOT EXISTS(SELECT 1 FROM tb_user)
BEGIN
    DECLARE @Id_User INT

    INSERT INTO tb_user (
        username,
        password
    ) VALUES (
        'admin',
        '{bcrypt}$2a$12$NYZurvH.l.vujYDufA6X6uFLBqQ1tDSDxX5VPTAcKSpNxJ3mBiWOW' -- 12345
    );

    SET @Id_User = SCOPE_IDENTITY()

    INSERT INTO tb_user_role (id_user, id_role)
    SELECT
        @Id_User,
        id_role
    FROM
        tb_role
END

IF NOT EXISTS(SELECT 1 FROM tb_software)
BEGIN
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
END