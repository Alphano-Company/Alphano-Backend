-- 문제 등록 (problem_id는 자동 생성)
INSERT INTO problem(
    title,
    content,
    input_format,
    output_format,
    submission_count,
    submitter_count,
    created_at,
    updated_at
) VALUES
      ('ATAXX', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 32, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      ('두 번째 게임', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 234, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      ('세 번째 게임', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 45, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 유저 등록 (user_id는 자동 생성)
INSERT INTO users(
    profile_image_url,
    identifier,
    password,
    nickname,
    created_at,
    updated_at,
    role
) VALUES
      ('profile1.png', 'amily9011', '1234', 'cherry', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER'),
      ('profile2.png', 'amily9012', '1234', 'cherry2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'USER');

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
      (1, 1, 'c++', true, 0, 0, 0, 'key1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (2, 1, 'c++', true, 0, 0, 0, 'key2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);