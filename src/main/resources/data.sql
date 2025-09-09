INSERT INTO problem(
    problem_id,
    title,
    content,
    input_format,
    output_format,
    submission_count,
    submitter_count,
    created_at,
    updated_at
) VALUES
      (1, 'ATAXX', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 32, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (2, '두 번째 게임', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 234, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (3, '세 번째 게임', '이 게임은 아주 엄청난 게임입니다.', '입력 형식', '출력 형식', 45, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users(
    user_id,
    profile_image_url,
    identifier,
    password,
    nickname,
    created_at,
    updated_at
) VALUES
      (1, 'profile1.png', 'amily9011', '1234', 'cherry', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (2, 'profile2.png', 'amily9012', '1234', 'cherry2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO submission(
    submission_id,
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
      (1, 1, 1, 'c++', true, 0, 0, 0, 'key1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (2, 2, 1, 'c++', true, 0, 0, 0, 'key2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);