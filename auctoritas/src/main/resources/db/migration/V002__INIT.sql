-- Seed roles (idempotent)
INSERT INTO tb_role (role, description) VALUES
    ('ROLE_USER_READ', 'Permission to view users'),
    ('ROLE_USER_WRITE', 'Permission to create, update and delete users'),
    ('ROLE_SOFTWARE_READ', 'Permission to view softwares'),
    ('ROLE_SOFTWARE_WRITE', 'Permissions to create, update and delete softwares'),
    ('ROLE_SEND_EMAIL', 'Permission to send email')
ON CONFLICT (role) DO NOTHING;

-- Seed admin user and grant all roles if no users exist
DO $$
DECLARE
    v_user_count integer;
    v_user_id integer;
BEGIN
    SELECT COUNT(*) INTO v_user_count FROM tb_user;
    IF v_user_count = 0 THEN
        INSERT INTO tb_user (username, password)
        VALUES ('admin', '{bcrypt}$2a$12$NYZurvH.l.vujYDufA6X6uFLBqQ1tDSDxX5VPTAcKSpNxJ3mBiWOW') -- 12345
        RETURNING id_user INTO v_user_id;

        INSERT INTO tb_user_role (id_user, id_role)
        SELECT v_user_id, id_role FROM tb_role;
    END IF;
END $$;