-- ==========================================
-- Mmove database schema — MySQL version
-- (converted from PostgreSQL: inline ENUMs,
--  AUTO_INCREMENT instead of IDENTITY,
--  native GEOMETRY type, no extension needed)
-- ==========================================

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
    CONSTRAINT chk_sender_receiver
        CHECK (sender_id <> receiver_id),
    UNIQUE KEY uk_pending_request(sender_id, receiver_id)
);

CREATE TABLE `user_stats` (
  `user_id` INT PRIMARY KEY,
  `lifetime_points` INT DEFAULT 0,
  `current_streak_days` INT DEFAULT 0,
  `global_rank_tier` ENUM('bronze','silver','gold','platinum') DEFAULT 'bronze',
  `updated_at` TIMESTAMP NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `achievements` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `description` VARCHAR(255),
  `icon` VARCHAR(255),
  `condition_type` VARCHAR(50),
  `condition_value` INT,
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

    event_type ENUM(
        'TASK_CREATED',
        'TASK_UPDATED',
        'TASK_COMPLETED',

        'CHECKLIST_ADDED',
        'CHECKLIST_COMPLETED',

        'COMMENT_ADDED',

        'GROUP_CREATED',
        'MEMBER_INVITED',
        'MEMBER_JOINED',
        'MEMBER_LEFT',

        'JOIN_REQUEST_SENT',
        'JOIN_REQUEST_APPROVED',
        'JOIN_REQUEST_REJECTED',

        'LEADER_TRANSFERRED',

        'DEADLINE_CHANGED',

        'LABEL_ADDED'
    ) NOT NULL,

    message VARCHAR(500) NOT NULL,

    reference_id BIGINT NULL,

    created_at DATETIME NOT NULL,

    CONSTRAINT fk_feed_activity
        FOREIGN KEY(activity_id)
        REFERENCES activity(id),

    CONSTRAINT fk_feed_user
        FOREIGN KEY(user_id)
        REFERENCES user(id)
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

CREATE TABLE `fitness_profile` (
  `user_id` INT PRIMARY KEY,
  `height` DECIMAL(5,2),
  `weight` DECIMAL(5,2),
  `bmi` DECIMAL(4,2),
  `goal_type` ENUM('weight_loss','muscle_gain','endurance','general_fitness'),
  `target_weight` DECIMAL(5,2),
  `target_timeline` DATE,
  `workout_type` VARCHAR(100),
  `updated_at` TIMESTAMP NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `Fitness` (
  `activity_id` VARCHAR(10) PRIMARY KEY,
  `step` INT,
  `distance` DECIMAL(10,2),
  FOREIGN KEY (`activity_id`) REFERENCES `Activity` (`id`)
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

CREATE TABLE  activity_groups (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50),
  `created_by` INT NOT NULL,
  `activity_id` VARCHAR(10) NOT NULL UNIQUE,
  `created_at` TIMESTAMP NULL,
  FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  FOREIGN KEY (`activity_id`) REFERENCES `Activity` (`id`),
  join_token VARCHAR(100) UNIQUE
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
        FOREIGN KEY (group_id)
        REFERENCES activity_groups(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_group_invite_inviter
        FOREIGN KEY (inviter_id)
        REFERENCES user(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_group_invite_invitee
        FOREIGN KEY (invitee_id)
        REFERENCES user(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_group_pending_invite
        UNIQUE (group_id, invitee_id)
);

CREATE TABLE join_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL,
    requester_id INT NOT NULL,
    status ENUM(
        'PENDING',
        'APPROVED',
        'REJECTED'
    ) NOT NULL DEFAULT 'PENDING',
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP NULL,
    CONSTRAINT fk_join_request_group
        FOREIGN KEY (group_id)
        REFERENCES activity_groups(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_join_request_user
        FOREIGN KEY (requester_id)
        REFERENCES user(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_join_request
        UNIQUE (group_id, requester_id)
);

CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id VARCHAR(10) NOT NULL,
    user_id INT NOT NULL,
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
