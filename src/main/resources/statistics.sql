CREATE TABLE requirements_statistics (
  requirement_id CHAR(36),
  count INT
);

CREATE TABLE visit_statistics (
  requirement_id CHAR(36),
  user_id VARCHAR(36),
  visited DATETIME,
  count INT
);

COMMIT TRANSACTION;