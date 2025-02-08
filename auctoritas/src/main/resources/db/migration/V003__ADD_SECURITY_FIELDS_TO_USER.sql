ALTER TABLE tb_user ADD enabled BIT NOT NULL DEFAULT 1;
ALTER TABLE tb_user ADD account_expiration_date DATETIME2;
ALTER TABLE tb_user ADD failed_login_attempts INT NOT NULL DEFAULT 0;
ALTER TABLE tb_user ADD last_failed_login_attempt DATETIME2;
ALTER TABLE tb_user ADD account_locked BIT NOT NULL DEFAULT 0;