CREATE SEQUENCE session_seq;

CREATE TABLE session
(
  id bigint NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  last_update timestamp without time zone NOT NULL,

  roommate_id bigint NOT NULL,
  connection_date timestamp without time zone NOT NULL,
  from_android boolean not null,
  
  CONSTRAINT pk_session PRIMARY KEY (id),
  CONSTRAINT fk_session_roommate_1 FOREIGN KEY (roommate_id)
      REFERENCES roommate (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE session
  OWNER TO florian;