/*
-- CREATE SCHEMA IF NOT EXISTS migration WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 3};
*/
CREATE TABLE test (
  group text,
  key text,
  val text,
  PRIMARY KEY (group, key)
) with CLUSTERING ORDER BY (key ASC);

INSERT INTO test (group, key, val) VALUES ('group1', 'key1', 'value1');