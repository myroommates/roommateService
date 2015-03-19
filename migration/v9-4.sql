CREATE SEQUENCE comment_seq;

CREATE TABLE comment
(
  id bigint NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  last_update timestamp without time zone NOT NULL,

  comment text NOT NULL,
  creator_id bigint NOT NULL,
  date_creation timestamp without time zone NOT NULL,
  date_edit timestamp without time zone,
  parent_id bigint,
  home_id bigint,
  ticket_id bigint,
  shopping_item_id bigint,


  CONSTRAINT pk_comment PRIMARY KEY (id),
  CONSTRAINT fk_comment_1 FOREIGN KEY (creator_id)
      REFERENCES roommate (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_comment_2 FOREIGN KEY (parent_id)
      REFERENCES comment (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_comment_3 FOREIGN KEY (home_id)
      REFERENCES home (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_comment_4 FOREIGN KEY (ticket_id)
      REFERENCES ticket (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_comment_5 FOREIGN KEY (shopping_item_id)
      REFERENCES shopping_item (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE comment
  OWNER TO florian;