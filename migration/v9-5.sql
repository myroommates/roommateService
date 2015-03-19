CREATE SEQUENCE comment_last_visualization_seq;

CREATE TABLE comment_last_visualization(

  id bigint NOT NULL,
  creation_date timestamp without time zone NOT NULL,
  last_update timestamp without time zone NOT NULL,

  date  timestamp without time zone NOT NULL,
  roommate_id bigint NOT NULL,
  home_id bigint,
  ticket_id bigint,
  shopping_item_id bigint,


  CONSTRAINT pk_comment_last_visualization PRIMARY KEY (id),
  CONSTRAINT fk_comment_last_visualization_1 FOREIGN KEY (roommate_id)
      REFERENCES roommate (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_comment_last_visualization_2 FOREIGN KEY (home_id)
      REFERENCES home (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_comment_last_visualization_3 FOREIGN KEY (ticket_id)
      REFERENCES ticket (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_comment_last_visualization_4 FOREIGN KEY (shopping_item_id)
      REFERENCES shopping_item (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE comment_last_visualization
  OWNER TO florian;