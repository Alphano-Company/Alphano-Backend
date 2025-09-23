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

-- 유저 등록 (user_id는 자동 생성)
INSERT INTO users(
    profile_image_key,
    identifier,
    password,
    created_at,
    updated_at,
    role
) VALUES
      ('profile1.png', 'amily9011', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'),
      ('profile2.png', 'amily9012', '$2a$10$bmZIWISzBbsKqYbYTlrB9O7oub9bPNcZmqusHmNiS26GTRYVBgHbe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER');

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
) VALUES (1500, 10, 2, 1, 5, 6, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
         (1720, 20, 5, 3, 7, 8, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
         (1630, 15, 3, 2, 4, 5, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
         (1580, 11, 4, 1, 3, 4, 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
         (1400, 8, 6, 0, 2, 3, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
         (1300, 5, 7, 1, 1, 2, 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 제출 등록 (submission_id는 자동 생성)
-- FK(user_id, problem_id)는 위에서 INSERT한 순서대로 1, 2, 3... 값이 매겨짐
INSERT INTO submission(
    user_id,
    problem_id,
    language,
    is_default,
    win,
    lose,
    draw,
    code_key,
    created_at,
    updated_at
) VALUES
      (1, 1, 'c++', true, 4, 0, 0, 'submissions/1/1/1/sample.cpp', CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP),
      (1, 1, 'python', false, 1, 4, 3, 'key2', CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP),
      (1, 1, 'c++', false, 5, 6, 5, 'key1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (2, 1, 'python', true, 0, 0, 0, 'submissions/1/2/4/sample.py', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);