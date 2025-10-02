INSERT INTO problem_image (
    image_key,
    created_at,
    updated_at
) VALUES
      ('problems/1/icons/problem_1_icon.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      ('problems/2/icons/problem_2_icon.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      ('problems/3/icons/problem_3_icon.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 문제 등록
INSERT INTO problem(
    title,
    content,
    input_format,
    output_format,
    submission_count,
    submitter_count,
    icon_image_id,
    created_at,
    updated_at
) VALUES
      ('ATAXX', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 32, 2, 1, CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP),
      ('두 번째 게임', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 234, 12, 2, CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP),
      ('세 번째 게임', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 45, 4, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (
    profile_image_key,
    identifier,
    password,
    created_at,
    updated_at,
    role
) VALUES
    ('profile1.png',  'tester01', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 1
    ('profile2.png',  'tester02', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 2
    ('profile3.png',  'tester03', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 3
    ('profile4.png',  'tester04', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 4
    ('profile5.png',  'tester05', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 5
    ('profile6.png',  'tester06', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 6
    ('profile7.png',  'tester07', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 7
    ('profile8.png',  'tester08', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 8
    ('profile9.png',  'tester09', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'), -- id 9
    ('profile10.png', 'tester10', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'); -- id 10

-- user_rating: 문제 1에서의 현재 레이팅 (유저별 1행)
INSERT INTO user_rating (
    rating,
    win,
    lose,
    draw,
    win_streak,
    best_win_streak,
    problem_id,
    user_id,
    created_at,
    updated_at
) VALUES
      (1500, 10, 2, 1,  5,  6, 1, 1,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 1
      (1300, 12, 3, 1,  4,  5, 1, 2,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 2
      (1350,  8, 4, 2,  3,  4, 1, 3,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 3
      (1400,  9, 6, 1,  2,  3, 1, 4,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 4
      (1450, 15, 5, 0,  6,  7, 1, 5,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 5
      (1500, 20, 8, 2,  7,  8, 1, 6,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 6
      (1550, 11, 4, 2,  3,  4, 1, 7,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 7
      (1600,  9, 7, 1,  2,  3, 1, 8,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 8
      (1650, 16, 6, 0,  5,  6, 1, 9,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),  -- uid 9
      (1700, 10, 5, 2,  4,  5, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);  -- uid 10

-- submission: 각 유저가 문제 1에 대해 READY 상태의 대표 제출 1건
INSERT INTO submission (
    user_id,
    problem_id,
    status,
    language,
    is_default,
    win,
    lose,
    draw,
    code_key,
    created_at,
    updated_at
) VALUES
      (1,  1, 'READY', 'c++20',   true, 10, 2, 1, 'submissions/1/1/main.cpp',     CURRENT_TIMESTAMP - 4, CURRENT_TIMESTAMP),
      (2,  1, 'READY', 'pypy3',   true,  9, 3, 2, 'submissions/1/2/main.py',      CURRENT_TIMESTAMP - 3, CURRENT_TIMESTAMP),
      (3,  1, 'READY', 'java',    true,  8, 4, 2, 'submissions/1/3/Main.java',    CURRENT_TIMESTAMP - 3, CURRENT_TIMESTAMP),
      (4,  1, 'READY', 'python3', true,  7, 6, 1, 'submissions/1/4/main.py',      CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP),
      (5,  1, 'READY', 'c++20',   true,  6, 5, 1, 'submissions/1/5/main.cpp',     CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP),
      (6,  1, 'READY', 'c20',     true,  5, 7, 1, 'submissions/1/6/main.c',       CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP),
      (7,  1, 'READY', 'c++20',   true,  7, 4, 1, 'submissions/1/7/main.cpp',     CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP),
      (8,  1, 'READY', 'python3', true,  6, 6, 1, 'submissions/1/8/main.py',      CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP),
      (9,  1, 'READY', 'java',    true,  9, 5, 0, 'submissions/1/9/Main.java',    CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP),
      (10, 1, 'READY', 'pypy3',   true,  8, 5, 2, 'submissions/1/10/main.py',     CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP);