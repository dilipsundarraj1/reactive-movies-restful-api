create table IF NOT EXISTS MOVIE_INFO
(
    movie_info_id  bigint auto_increment,
    name         varchar(255),
    year         INT,
    cast ARRAY,
    release_date DATE
);


create table IF NOT EXISTS REVIEW
(
    review_Id  bigint auto_increment,
    movie_info_id         INT references MOVIE_INFO (movie_info_id),
    rating         DOUBLE,
    comment  varchar(255)
    );