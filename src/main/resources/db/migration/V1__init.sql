CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE incidents (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  title varchar(200) NOT NULL,
  description text NULL,
  severity varchar(20) NOT NULL,
  status varchar(20) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  version bigint NOT NULL DEFAULT 0
);

CREATE INDEX idx_incidents_status ON incidents(status);
CREATE INDEX idx_incidents_created_at ON incidents(created_at);
