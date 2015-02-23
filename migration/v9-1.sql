CREATE SEQUENCE faq_seq;
CREATE SEQUENCE translation_seq;
CREATE SEQUENCE translation_value_seq;

CREATE TABLE translation
(
  id bigint NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  last_update timestamp without time zone NOT NULL,
  CONSTRAINT pk_translation PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE translation
  OWNER TO florian;

CREATE TABLE translation_value
(
  id bigint NOT NULL,
  language_code character varying(255) NOT NULL,
  content text NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  last_update timestamp without time zone NOT NULL,
  translation_id bigint NOT NULL,
  CONSTRAINT pk_translation_value PRIMARY KEY (id),
   CONSTRAINT fk_translation_value_1 FOREIGN KEY (translation_id)
      REFERENCES translation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE translation_value
  OWNER TO florian;


CREATE TABLE faq
(
  id bigint NOT NULL,
  question_id bigint NOT NULL,
  answer_id bigint NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  last_update timestamp without time zone NOT NULL,
  CONSTRAINT pk_faq PRIMARY KEY (id),
   CONSTRAINT fk_faq_1 FOREIGN KEY (question_id)
      REFERENCES translation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT fk_faq_2 FOREIGN KEY (answer_id)
      REFERENCES translation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE faq
  OWNER TO florian;

  alter table roommate add column is_super_admin boolean NOT NULL DEFAULT false