-- ==========================================
-- MoveM database schema — MySQL version
-- (converted from PostgreSQL: inline ENUMs,
--  AUTO_INCREMENT instead of IDENTITY,
--  native GEOMETRY type, no extension needed)
-- ==========================================

-- New Tables to create:

CREATE TABLE fitness_workout_kudos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workout_session_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT uk_workout_kudos_user
        UNIQUE (workout_session_id, user_id),
    CONSTRAINT fk_workout_kudos_session
        FOREIGN KEY (workout_session_id)
            REFERENCES fitness_workout_session(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_workout_kudos_user
        FOREIGN KEY (user_id)
            REFERENCES user(id)
            ON DELETE CASCADE,

    INDEX idx_workout_kudos_session (workout_session_id),
    INDEX idx_workout_kudos_user (user_id)
);

CREATE TABLE fitness_workout_analysis (
    id INT AUTO_INCREMENT PRIMARY KEY,
    workout_session_id INT NOT NULL UNIQUE,
    exercise VARCHAR(50) NOT NULL,
    reps INT NOT NULL DEFAULT 0,
    valid_reps INT NOT NULL DEFAULT 0,
    invalid_reps INT NOT NULL DEFAULT 0,
    form_score INT NULL,
    feedback TEXT NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    FOREIGN KEY (workout_session_id)
        REFERENCES fitness_workout_session(id)
);

CREATE TABLE user_devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    device_token VARCHAR(500) NOT NULL,
    platform VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_seen_at DATETIME(6),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT uk_user_device_token
        UNIQUE (device_token),
    CONSTRAINT fk_user_device_user
        FOREIGN KEY (user_id)
            REFERENCES user(id)
                ON DELETE CASCADE
                ON UPDATE CASCADE,

    INDEX idx_user_device_user (user_id),
    INDEX idx_user_device_token (device_token)
);

CREATE TABLE `user` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) UNIQUE NOT NULL,
  `email` VARCHAR(100) UNIQUE NOT NULL,
  `firstname` VARCHAR(50),
  `lastname` VARCHAR(50),
  `date_of_birth` DATE,
  `joined_date` TIMESTAMP NULL,
  `phone` VARCHAR(15),
  `gender` ENUM('male','female','other','prefer_not_to_say'),
  `profile_pic` VARCHAR(500),
  `password_hash` VARCHAR(255) NOT NULL,
  `city_province` VARCHAR(50),
  `theme_preference` ENUM('light','dark_mode') DEFAULT 'dark_mode',
  `language_preference` ENUM('english','khmer') DEFAULT 'english',
  `is_active` BOOLEAN DEFAULT true,
  `created_at` TIMESTAMP NULL,
  `updated_at` TIMESTAMP NULL
);

CREATE TABLE `email_verifications` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `code_hash` VARCHAR(255) NOT NULL,
  `expires_at` TIMESTAMP NOT NULL,
  `used_at` TIMESTAMP NULL,
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `user_stats` (
                              `user_id` INT PRIMARY KEY,
                              `lifetime_points` INT DEFAULT 0,
                              `current_streak_days` INT DEFAULT 0,
                              `global_rank_tier` ENUM('bronze','silver','gold','platinum') DEFAULT 'bronze',
                              `updated_at` TIMESTAMP NULL,
                              FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `friendships` (
  `user_id` INT NOT NULL,
  `friend_id` INT NOT NULL,
  `status` ENUM('pending','accepted') DEFAULT 'pending',
  `created_at` TIMESTAMP NULL,
  PRIMARY KEY (`user_id`, `friend_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`friend_id`) REFERENCES `user` (`id`)
);

CREATE TABLE friend_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    status ENUM('PENDING','ACCEPTED','REJECTED','CANCELLED')
        DEFAULT 'PENDING',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at DATETIME NULL,
    CONSTRAINT fk_friend_request_sender
        FOREIGN KEY (sender_id)
            REFERENCES user(id),
    CONSTRAINT fk_friend_request_receiver
        FOREIGN KEY (receiver_id)
            REFERENCES user(id),
    CONSTRAINT "chk_sender_receiver"
        CHECK (sender_id <> receiver_id),
    UNIQUE KEY uk_pending_request(sender_id, receiver_id)
);

CREATE TABLE friend (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_one_id INT NOT NULL,
    user_two_id INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_friend_user_one
        FOREIGN KEY (user_one_id)
            REFERENCES user(id),
    CONSTRAINT fk_friend_user_two
        FOREIGN KEY (user_two_id)
            REFERENCES user(id),
    CONSTRAINT chk_friend_users
        CHECK (user_one_id <> user_two_id),
    UNIQUE KEY uk_friend_pair(user_one_id, user_two_id)
);

CREATE TABLE `achievements` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(255),
  `icon` VARCHAR(255),
  `condition_type` VARCHAR(50),
  `condition_value` DECIMAL(12,2),
  `created_at` TIMESTAMP NULL
);

CREATE TABLE `user_achievements` (
  `user_id` INT NOT NULL,
  `achievement_id` INT NOT NULL,
  `earned_at` TIMESTAMP NULL,
  PRIMARY KEY (`user_id`, `achievement_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`achievement_id`) REFERENCES `achievements` (`id`)
);

CREATE TABLE `newsfeed_posts` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `content_type` ENUM('achievement','leaderboard','challenge','activity') NOT NULL,
  `reference_id` INT,
  `caption` VARCHAR(500),
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `post_reactions` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `post_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `reaction_type` ENUM('like','fire','trophy') DEFAULT 'like',
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`post_id`) REFERENCES `newsfeed_posts` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `Activity` (
  `id` VARCHAR(10) PRIMARY KEY,
  `activity_name` VARCHAR(50) NOT NULL,
  `activity_type` ENUM('task','fitness','trip') NOT NULL,
  `user_id` INT NOT NULL,
  `status` varchar(30) NOT NULL,
  `start_activity` TIMESTAMP NULL,
  `deadline` TIMESTAMP NULL,
  `description` TEXT,
  `location_name` VARCHAR(100),
  `location_address` VARCHAR(255),
  `lat` DECIMAL(10,8),
  `lng` DECIMAL(11,8),
  `google_place_id` VARCHAR(255),
  `coordinates` GEOMETRY,
  `parent_activity` VARCHAR(10),
  `created_at` TIMESTAMP NULL,
  `updated_at` TIMESTAMP NULL,
  `deleted_at` DATETIME,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`parent_activity`) REFERENCES `Activity` (`id`)
);

CREATE TABLE `activity_leaderboards` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `activity_id` VARCHAR(10) NOT NULL,
  `user_id` INT NOT NULL,
  `total_score` INT DEFAULT 0,
  `final_rank` INT,
  `awarded_date` TIMESTAMP NULL,
  FOREIGN KEY (`activity_id`) REFERENCES `Activity` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE activity_feed (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id VARCHAR(10) NOT NULL,
    user_id INT NOT NULL,
    event_type VARCHAR(100) NOT NULL,

    message VARCHAR(500) NOT NULL,

    reference_id BIGINT NULL,

    created_at DATETIME NOT NULL,

    CONSTRAINT fk_feed_activity
        FOREIGN KEY (activity_id)
        REFERENCES activity(id),

    CONSTRAINT fk_feed_user
        FOREIGN KEY (user_id)
        REFERENCES user(id)
);

CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id VARCHAR(255) NOT NULL,
    activityCode VARCHAR(10),
    activity VARCHAR,
    user_id INT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    old_value TEXT,
    new_value TEXT,
    created_at TIMESTAMP,
    FOREIGN KEY (activity_id) REFERENCES activity(activity_id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE `task_labels` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `color` VARCHAR(20) NOT NULL,
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `activity_labels` (
  `activity_id` VARCHAR(10) NOT NULL,
  `label_id` INT NOT NULL,
  PRIMARY KEY (`activity_id`, `label_id`),
  FOREIGN KEY (`activity_id`) REFERENCES `Activity` (`id`),
  FOREIGN KEY (`label_id`) REFERENCES `task_labels` (`id`)
);

CREATE TABLE `Task` (
  `activity_id` VARCHAR(10) PRIMARY KEY,
  `priority` ENUM('urgent','high','medium','low') DEFAULT 'medium',
  `is_recurring` BOOLEAN DEFAULT false,
  `recurring_type` enum('DAILY','WEEKLY','MONTHLY','YEARLY') DEFAULT NULL,
  `recurring_interval` int DEFAULT '1',
  `recurring_end_date` date DEFAULT NULL,
  FOREIGN KEY (`activity_id`) REFERENCES `Activity` (`id`)
);

CREATE TABLE `task_checklists` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `task_activity_id` VARCHAR(10) NOT NULL,
  `item_name` VARCHAR(255) NOT NULL,
  `is_completed` BOOLEAN DEFAULT false,
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`task_activity_id`) REFERENCES `Task` (`activity_id`)
);

CREATE TABLE `task_reminders` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `task_activity_id` VARCHAR(10) NOT NULL,
  `remind_at` TIMESTAMP NOT NULL,
  `type` ENUM('due_date','start_date','custom') DEFAULT 'custom',
  `is_sent` BOOLEAN DEFAULT false,
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`task_activity_id`) REFERENCES `Task` (`activity_id`)
);

CREATE TABLE fitness_profile (
    user_id INT PRIMARY KEY,
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    bmi DECIMAL(4,2),
    updated_at TIMESTAMP NULL,

    CONSTRAINT fk_fitness_profile_user
        FOREIGN KEY (user_id)
            REFERENCES user(id)
            ON DELETE CASCADE
);

CREATE TABLE `fitness_logs` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `fitness_activity_id` VARCHAR(10) NOT NULL,
  `user_id` INT NOT NULL,
  `distance_covered` DECIMAL(10,2),
  `calories_burned` INT,
  `average_pace` VARCHAR(20),
  `finish_time` VARCHAR(20),
  `log_date` TIMESTAMP NULL,
  FOREIGN KEY (`fitness_activity_id`) REFERENCES `Fitness` (`activity_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE fitness_goal (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    goal_type VARCHAR(50) NOT NULL,
    target_weight DECIMAL(5,2),
    target_timeline DATE,
    workout_level VARCHAR(30),
    estimated_weight_change DECIMAL(5,2),
    estimated_daily_deficit DECIMAL(8,2),
    status VARCHAR(30),
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,

    CONSTRAINT fk_fitness_goal_user
        FOREIGN KEY (user_id)
            REFERENCES user(id)
            ON DELETE CASCADE
);

CREATE TABLE solo_challenge_catalog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    workout_type VARCHAR(50) NOT NULL,
    workout_level VARCHAR(50) NOT NULL,
    target_value DECIMAL(10,2) NOT NULL,
    target_unit VARCHAR(30) NOT NULL,
    description TEXT,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    INDEX idx_solo_catalog_type (workout_type),
    INDEX idx_solo_catalog_level (workout_level),
    INDEX idx_solo_catalog_unit (target_unit)
);

CREATE TABLE group_challenge_catalog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    workout_type VARCHAR(50) NOT NULL,
    target_value DECIMAL(10,2) NOT NULL,
    target_unit VARCHAR(30) NOT NULL,
    description TEXT,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    INDEX idx_group_catalog_type (workout_type),
    INDEX idx_group_catalog_unit (target_unit)
);

CREATE TABLE group_fitness_challenge (
    id INT AUTO_INCREMENT NOT NULL,
    activity_id VARCHAR(10) NOT NULL,
    club_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    workout_type VARCHAR(50) NOT NULL,
    target_value DECIMAL(10,2) NOT NULL,
    target_unit VARCHAR(30) NOT NULL,
    description TEXT,
    challenge_source VARCHAR(30) NOT NULL,
    created_by INT NOT NULL,
    start_at DATETIME NOT NULL,
    end_at DATETIME NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_group_challenge_activity (activity_id),
    INDEX idx_group_challenge_group (club_id),
    INDEX idx_group_challenge_creator (created_by),
    INDEX idx_group_challenge_source (challenge_source),
    INDEX idx_group_challenge_status (status),
    CONSTRAINT fk_group_challenge_activity
        FOREIGN KEY (activity_id)
            REFERENCES activity(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_group_challenge_group
        FOREIGN KEY (club_id)
            REFERENCES fitness_clubs(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_group_challenge_creator
        FOREIGN KEY (created_by)
            REFERENCES user(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE fitness_challenge_participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    challenge_id INT NOT NULL,
    user_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    joined_at DATETIME NULL,
    completed_at DATETIME NULL,
    CONSTRAINT uk_challenge_participant
        UNIQUE (challenge_id, user_id),
    INDEX idx_participant_challenge (challenge_id),
    INDEX idx_participant_user (user_id),
    INDEX idx_participant_status (status),
    CONSTRAINT fk_participant_challenge
        FOREIGN KEY (challenge_id)
            REFERENCES group_fitness_challenge(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_participant_user
        FOREIGN KEY (user_id)
            REFERENCES user(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE fitness_workout_session (
    id INT AUTO_INCREMENT PRIMARY KEY,

    activity_id VARCHAR(10) NOT NULL,
    user_id INT NOT NULL,

    solo_challenge_id INT NULL,
    group_challenge_participant_id INT NULL,

    workout_type VARCHAR(50) NOT NULL,
    tracking_mode VARCHAR(20) NOT NULL DEFAULT 'STEPS',

    status VARCHAR(30) NOT NULL,

    is_shared BOOLEAN NOT NULL DEFAULT FALSE,
    share_description TEXT NULL,

    started_at DATETIME(6) NULL,
    paused_at DATETIME(6) NULL,

    total_paused_seconds INT NOT NULL DEFAULT 0,
    finished_at DATETIME(6) NULL,

    duration_seconds INT NOT NULL DEFAULT 0,
    steps INT NOT NULL DEFAULT 0,

    distance DECIMAL(10,2) NOT NULL DEFAULT 0,
    calories_burned DECIMAL(10,2) NOT NULL DEFAULT 0,

    average_pace DECIMAL(10,2) NULL,
    average_speed DECIMAL(10,2) NULL,

    created_at DATETIME(6) NULL,
    updated_at DATETIME(6) NULL,

    UNIQUE KEY uk_fitness_session_activity (activity_id),

    INDEX idx_fitness_session_user (user_id),
    INDEX idx_fitness_session_solo (solo_challenge_id),
    INDEX idx_fitness_session_participant (
        group_challenge_participant_id
    ),
    INDEX idx_fitness_session_status (status),
    CONSTRAINT fk_fitness_session_activity
        FOREIGN KEY (activity_id)
            REFERENCES activity(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,

    CONSTRAINT fk_fitness_session_user
        FOREIGN KEY (user_id)
            REFERENCES user(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,

    CONSTRAINT fk_fitness_session_solo
        FOREIGN KEY (solo_challenge_id)
            REFERENCES solo_challenge_catalog(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE,

    CONSTRAINT fk_fitness_session_participant
        FOREIGN KEY (group_challenge_participant_id)
            REFERENCES fitness_challenge_participant(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE
);

CREATE TABLE fitness_clubs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    created_by INT NOT NULL,
    privacy VARCHAR(20) NOT NULL,
    join_token VARCHAR(100) UNIQUE,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    INDEX idx_fitness_club_creator (created_by),
    INDEX idx_fitness_club_privacy (privacy),
    INDEX idx_fitness_club_token (join_token),
    CONSTRAINT fk_fitness_club_creator
        FOREIGN KEY (created_by)
            REFERENCES user(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE fitness_club_invites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    club_id INT NOT NULL,
    inviter_id INT NOT NULL,
    invitee_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    invited_at DATETIME NULL,
    responded_at DATETIME NULL,

    INDEX idx_fitness_club_invite_club (club_id),
    INDEX idx_fitness_club_invite_invitee (invitee_id),
    INDEX idx_fitness_club_invite_inviter (inviter_id),
    INDEX idx_fitness_club_invite_status (status),

    CONSTRAINT fk_fitness_club_invite_club
        FOREIGN KEY (club_id)
            REFERENCES fitness_clubs(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_fitness_club_invite_inviter
        FOREIGN KEY (inviter_id)
            REFERENCES user(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_fitness_club_invite_invitee
        FOREIGN KEY (invitee_id)
            REFERENCES user(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE fitness_club_members (
    club_id INT NOT NULL,
    user_id INT NOT NULL,
    role VARCHAR(20) NOT NULL,
    joined_at DATETIME NULL,
    PRIMARY KEY (club_id, user_id),
    INDEX idx_fitness_club_member_club (club_id),
    INDEX idx_fitness_club_member_user (user_id),
    INDEX idx_fitness_club_member_role (role),
    CONSTRAINT fk_fitness_club_member_club
        FOREIGN KEY (club_id)
            REFERENCES fitness_clubs(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_fitness_club_member_user
        FOREIGN KEY (user_id)
            REFERENCES user(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE fitness_club_join_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    club_id INT NOT NULL,
    requester_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    requested_at DATETIME NOT NULL,
    responded_at DATETIME NULL,
    INDEX idx_fitness_club_request_club (club_id),
    INDEX idx_fitness_club_request_user (requester_id),
    INDEX idx_fitness_club_request_status (status),

    CONSTRAINT fk_fitness_club_request_club
        FOREIGN KEY (club_id)
            REFERENCES fitness_clubs(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,

    CONSTRAINT fk_fitness_club_request_user
        FOREIGN KEY (requester_id)
            REFERENCES user(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE `groups` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `created_by` INT NOT NULL,
  `activity_id` VARCHAR(10) NOT NULL UNIQUE,
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  FOREIGN KEY (`activity_id`) REFERENCES `Activity` (`id`)
);

CREATE TABLE `group_members` (
  `group_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `role` ENUM('leader','member') DEFAULT 'member',
  `joined_at` TIMESTAMP NULL,
  PRIMARY KEY (`group_id`, `user_id`),
  FOREIGN KEY (`group_id`) REFERENCES `activity_groups` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE group_invites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL,
    inviter_id INT NOT NULL,
    invitee_id INT NOT NULL,
    status ENUM(
        'PENDING',
        'ACCEPTED',
        'REJECTED'
    ) NOT NULL DEFAULT 'PENDING',
    invited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP NULL,
    CONSTRAINT fk_group_invite_group
    FOREIGN KEY (group_id) REFERENCES activity_groups(id) ON DELETE CASCADE,
    CONSTRAINT fk_group_invite_inviter FOREIGN KEY (inviter_id)
    REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_group_invite_invitee
    FOREIGN KEY (invitee_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT uq_group_pending_invite UNIQUE (group_id, invitee_id)
);

CREATE TABLE join_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL,
    requester_id INT NOT NULL,status ENUM(
            'PENDING',
            'APPROVED',
            'REJECTED'
        ) NOT NULL DEFAULT 'PENDING',
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP NULL,
    CONSTRAINT fk_join_request_group
        FOREIGN KEY (group_id) REFERENCES activity_groups(id) ON DELETE CASCADE,
    CONSTRAINT fk_join_request_user
        FOREIGN KEY (requester_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT uq_join_request UNIQUE (group_id, requester_id)
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id VARCHAR(10) NOT NULL,
    user_id INT NOT NULL,
    sender_id INT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    CONSTRAINT fk_comment_activity
        FOREIGN KEY(activity_id)
            REFERENCES Activity(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_comment_user
        FOREIGN KEY(user_id)
            REFERENCES user(id)
            ON DELETE CASCADE
);

CREATE TABLE Notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    reference_id VARCHAR(20),
    reference_type VARCHAR(30),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at DATETIME NULL,
    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
            REFERENCES User(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_notification_sender
        FOREIGN KEY (sender_id)
            REFERENCES User(id)
            ON DELETE SET NULL
);
CREATE INDEX idx_notification_user ON Notification(user_id);
CREATE INDEX idx_notification_read ON Notification(user_id, is_read);
CREATE INDEX idx_notification_created ON Notification(created_at DESC);
CREATE INDEX idx_notification_reference ON Notification(reference_id);

CREATE TABLE `Trip` (
  `activity_id` VARCHAR(10) PRIMARY KEY,
  `destination` VARCHAR(100),
  `flight_number` VARCHAR(50),
  `hotel_name` VARCHAR(100),
  FOREIGN KEY (`activity_id`) REFERENCES `Activity` (`id`)
);

CREATE TABLE `trip_stops` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `trip_activity_id` VARCHAR(10) NOT NULL,
  `location_name` VARCHAR(100),
  `sequence_order` INT NOT NULL,
  `arrival_time` TIMESTAMP NULL,
  `departure_time` TIMESTAMP NULL,
  `location_address` VARCHAR(255),
  `lat` DECIMAL(10,8),
  `lng` DECIMAL(11,8),
  `google_place_id` VARCHAR(255),
  `coordinates` GEOMETRY,
  `is_completed` BOOLEAN DEFAULT false,
  FOREIGN KEY (`trip_activity_id`) REFERENCES `Trip` (`activity_id`)
);

CREATE TABLE `trip_bookmarks` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `google_place_id` VARCHAR(255),
  `location_name` VARCHAR(100),
  `location_address` VARCHAR(255),
  `lat` DECIMAL(10,8),
  `lng` DECIMAL(11,8),
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `trip_packing_items` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `trip_activity_id` VARCHAR(10) NOT NULL,
  `item_name` VARCHAR(255) NOT NULL,
  `is_packed` BOOLEAN DEFAULT false,
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`trip_activity_id`) REFERENCES `Trip` (`activity_id`)
);

CREATE TABLE `trip_budgets` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `trip_activity_id` VARCHAR(10) NOT NULL,
  `category` VARCHAR(50) NOT NULL,
  `allocated_amount` DECIMAL(12,2) NOT NULL,
  `spent_amount` DECIMAL(12,2) DEFAULT 0,
  FOREIGN KEY (`trip_activity_id`) REFERENCES `Trip` (`activity_id`)
);

CREATE TABLE `trip_expenses` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `budget_id` INT NOT NULL,
  `payer_id` INT NOT NULL,
  `amount` DECIMAL(12,2) NOT NULL,
  `description` VARCHAR(255),
  `expense_date` TIMESTAMP NULL,
  FOREIGN KEY (`budget_id`) REFERENCES `trip_budgets` (`id`),
  FOREIGN KEY (`payer_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `trip_expense_splits` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `expense_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `amount_owed` DECIMAL(12,2) NOT NULL,
  `is_settled` BOOLEAN DEFAULT false,
  `settled_at` TIMESTAMP NULL,
  FOREIGN KEY (`expense_id`) REFERENCES `trip_expenses` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `cloud_sync_tokens` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `user_id` INT NOT NULL,
  `provider` VARCHAR(50) NOT NULL,
  `access_token` TEXT NOT NULL,
  `refresh_token` TEXT,
  `last_synced_at` TIMESTAMP NULL,
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);
