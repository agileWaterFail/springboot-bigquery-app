CREATE TABLE "dataset" (
  "id" uuid NOT NULL,
  "searchRequestId" uuid NOT NULL,
  "data" CHARACTER VARYING(255)
);

CREATE UNIQUE INDEX dataset_pkey
  ON dataset USING BTREE (id);