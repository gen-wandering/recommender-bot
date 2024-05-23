SELECT m.id, count(*) amount_of_ratings
FROM movies m
         JOIN ratings r on m.id = r.movie_id
GROUP BY m.id
ORDER BY amount_of_ratings DESC;


SELECT *
FROM viewers v
         JOIN ratings r ON v.id = r.viewer_id
WHERE v.id = 2345;

SELECT *
FROM viewers
ORDER BY id desc ;

-- user_avg == 10 sec
-- 860 ratings == 750 ms
-- 375 ratings == 350 ms
-- 860 ratings == 600 ms (cached data)