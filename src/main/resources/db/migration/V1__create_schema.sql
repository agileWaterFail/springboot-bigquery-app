CREATE TABLE "dataset" (
  "id" uuid NOT NULL,
  "search_request_id" uuid NOT NULL,
  "data" VARCHAR
);

CREATE UNIQUE INDEX dataset_pkey
  ON dataset USING BTREE (id);