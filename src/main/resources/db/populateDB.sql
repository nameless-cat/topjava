DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, description, calories, date_time)
VALUES
  (100000, 'Завтрак пользователя', 1200, '2017-04-01 09:05:00.000000'),
  (100000, 'Обед пользователя', 600, '2017-04-01 14:15:00.000000'),
  (100000, 'Ужин пользователя', 300, '2017-04-01 20:30:00.000000'),
  (100000, 'Завтрак пользователя', 1200, '2017-03-01 09:05:00.000000'),
  (100000, 'Обед пользователя', 600, '2017-03-01 14:15:00.000000'),
  (100000, 'Ужин пользователя', 300, '2017-03-01 20:30:00.000000');

INSERT INTO meals (user_id, description, calories, date_time)
VALUES
  (100001, 'Завтрак админа', 800, '2017-03-01 09:05:00.000000'),
  (100001, 'Обед админа', 500, '2017-03-01 14:15:00.000000'),
  (100001, 'Ужин админа', 200, '2017-03-01 20:30:00.000000');
