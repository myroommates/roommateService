CREATE SEQUENCE survey_seq;
CREATE SEQUENCE survey_answer_seq;
CREATE SEQUENCE survey_value_seq;

CREATE TABLE survey
(
  id bigint NOT NULL,
  key character varying(255) NOT NULL,
  question_id bigint NOT NULL,
  is_multiple_answer boolean NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  last_update timestamp without time zone NOT NULL,
  CONSTRAINT pk_survey PRIMARY KEY (id),
   CONSTRAINT fk_survey_1 FOREIGN KEY (question_id)
      REFERENCES translation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uq_survey_key UNIQUE (key)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE survey
  OWNER TO florian;


CREATE TABLE survey_answer
(
  id bigint NOT NULL,
  answer_id bigint NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  survey_id  bigint NOT NULL,
  last_update timestamp without time zone NOT NULL,
  CONSTRAINT pk_survey_answer PRIMARY KEY (id),
   CONSTRAINT fk_survey_answer_1 FOREIGN KEY (answer_id)
      REFERENCES translation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_survey_answer_2 FOREIGN KEY (survey_id)
      REFERENCES survey (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE TABLE survey_value
(
  id bigint NOT NULL,
  roommate_id bigint NOT NULL,
  survey_id bigint NOT NULL,
  survey_answer_id bigint,
  was_submit boolean not null default false,
  creation_date timestamp without time zone NOT NULL,
  last_update timestamp without time zone NOT NULL,
  CONSTRAINT pk_survey_value PRIMARY KEY (id),
   CONSTRAINT fk_survey_value_1 FOREIGN KEY (roommate_id)
      REFERENCES roommate (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_survey_value_2 FOREIGN KEY (survey_id)
      REFERENCES survey (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_survey_value_3 FOREIGN KEY (survey_answer_id)
      REFERENCES survey_answer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
