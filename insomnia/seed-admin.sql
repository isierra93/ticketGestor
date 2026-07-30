-- ============================================================
-- seed-admin.sql
-- Promueve usuarios CLIENT (creados via /auth/register) a roles
-- ADMIN y AGENT.
--
-- La API registra todo usuario nuevo como CLIENT. Para obtener
-- tokens con roles superiores (necesarios para tests de DELETE,
-- PATCH con restricción de agente), hay que promoverlos en DB.
--
-- Uso (después de levantar MySQL con docker compose up -d):
--   docker compose exec -T mysql mysql -uroot -p1234 db-ticket-gestor < insomnia/seed-admin.sql
-- ============================================================

-- Primero registrar los usuarios via POST /auth/register:
--   { "email": "admin@demo.com", "password": "Demo1234" }
--   { "email": "agente1@demo.com", "password": "Demo1234" }

-- Luego ejecutar estos UPDATEs:

UPDATE usuarios SET user_role = 'ADMIN' WHERE email = 'admin@demo.com';
UPDATE usuarios SET user_role = 'AGENT' WHERE email = 'agente1@demo.com';
