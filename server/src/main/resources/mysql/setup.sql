USE archiet;

DROP TABLE IF EXISTS 3manmv;
DROP TABLE IF EXISTS 3mangp;
DROP TABLE IF EXISTS chessbot;
DROP TABLE IF EXISTS chessuser;
DROP TABLE IF EXISTS 3manplayer;

DROP TABLE IF EXISTS 3manst;
CREATE TABLE 3manst (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  board          BINARY(144) NOT NULL,
  moats          TINYINT     NOT NULL, -- _ _ _ _ _ W G B
  movesnext      TINYINT     NOT NULL,
  castling       TINYINT     NOT NULL, -- _ _ K Q x u k q , where x,q are gray king&queen
  enpassant      BINARY(4)   NOT NULL,
  halfmoveclock  TINYINT     NOT NULL,
  fullmovenumber SMALLINT    NOT NULL,
  alive          TINYINT     NOT NULL, -- _ _ _ _ _ W G B
  CONSTRAINT everything UNIQUE (
    board, moats, movesnext, castling,
    enpassant,
    halfmoveclock, fullmovenumber,
    alive
  )
)
  ENGINE = InnoDB;

CREATE TABLE 3manplayer (
  id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  auth VARBINARY(100) NOT NULL
  -- name varchar(100) not null,
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE chessuser (
  id     BIGINT AUTO_INCREMENT PRIMARY KEY,
  login  VARCHAR(20) UNIQUE KEY,
  passwd VARCHAR(100) NOT NULL,
  name   VARCHAR(100),
  player BIGINT       NOT NULL UNIQUE KEY,
  CONSTRAINT
  FOREIGN KEY (player) REFERENCES 3manplayer (id)
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE chessbot (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  whoami   VARBINARY(20) NOT NULL, -- ai type identifier
  owner    BIGINT        NOT NULL,
  ownname  VARCHAR(50),
  player   BIGINT        NOT NULL UNIQUE KEY,
  settings VARBINARY(500),
  CONSTRAINT everything UNIQUE (whoami, owner, settings),
  CONSTRAINT
  FOREIGN KEY (owner) REFERENCES chessuser (id)
    ON UPDATE RESTRICT,
  CONSTRAINT
  FOREIGN KEY (player) REFERENCES 3manplayer (id)
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE 3mangp (
  id      BIGINT    AUTO_INCREMENT PRIMARY KEY,
  state   BIGINT,
  white   BIGINT,
  gray    BIGINT,
  black   BIGINT,
  created TIMESTAMP DEFAULT current_timestamp,
  --	constraint everything unique ( state,white,gray,black ),
  CONSTRAINT
  FOREIGN KEY (white) REFERENCES 3manplayer (id)
    ON UPDATE RESTRICT,
  CONSTRAINT
  FOREIGN KEY (gray) REFERENCES 3manplayer (id)
    ON UPDATE RESTRICT,
  CONSTRAINT
  FOREIGN KEY (black) REFERENCES 3manplayer (id)
    ON UPDATE RESTRICT,
  CONSTRAINT
  FOREIGN KEY (state) REFERENCES 3manst (id)
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB;

CREATE TABLE 3manmv (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  fromto     BINARY(4) NOT NULL,
  beforegame BIGINT    NOT NULL,
  aftergame  BIGINT,
  promotion  TINYINT   NOT NULL,
  who        BIGINT    NOT NULL,
  CONSTRAINT
  FOREIGN KEY (beforegame) REFERENCES 3mangp (id)
    ON UPDATE RESTRICT,
  CONSTRAINT
  FOREIGN KEY (who) REFERENCES 3manplayer (id)
    ON UPDATE RESTRICT,
  CONSTRAINT
  FOREIGN KEY (aftergame) REFERENCES 3mangp (id)
    ON UPDATE CASCADE,
  CONSTRAINT
  FOREIGN KEY (aftergame) REFERENCES 3mangp (id)
    ON DELETE RESTRICT,
  CONSTRAINT onemove UNIQUE (fromto, beforegame, promotion, who)
)
  ENGINE = InnoDB;

-- vi:ft=mysql
