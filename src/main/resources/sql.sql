ALTER TABLE movie MODIFY COLUMN is_deleted BOOLEAN DEFAULT FALSE;

ALTER TABLE cinema_movie.payment_detail
    CHANGE COLUMN paymentid paymentId BIGINT;

SELECT * FROM cinema_movie.post;
