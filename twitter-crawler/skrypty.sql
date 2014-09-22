select
  --*
  id, latitude, longitude, to_char(created_at, 'yyyy-MM-dd HH24:MI'), created_at, text, user_id
from mgr.tweets
where match_event = 258
;



select to_char(created_at, 'yyyy-MM-dd HH24:MI') as time,
  count(nullif(valence > 0.4786984978198536, false)) as positives,
  count(nullif(valence <= 0.4786984978198536, false)) as negatives
from mgr.paroubek_tweets a JOIN mgr.tweets b ON a.tweet_id = b.id AND b.match_event = 258
where valence != 'NaN'
group by time
order by time
  ;



select tweet_id, a.text, b.text, (valence - 0.4786984978198536) as difference
from mgr.paroubek_tweets a JOIN mgr.tweets b ON a.tweet_id = b.id AND valence != 'NaN' and match_event = 258
where
  valence > 0.4786984978198536
order by difference desc
  ;



select *
from mgr.paroubek_tweets
  where valence != 'NaN'


;


select country, count(*)
from mgr.geodata
group by country
order by country

  ;




select count(*)
from mgr.users


select * from mgr.tweets;



select id, screen_name as label  from mgr.users where id in ( 	SELECT user_id 	FROM ( 		SELECT DISTINCT user_id, match_event 		FROM mgr.tweets t 		WHERE match_event IS NOT NULL 	) as view1 	GROUP BY user_id 	HAVING count(*) >= 30 )


SELECT t.user_id as source, t.in_reply_to_user_id as target, count(*) as weight FROM mgr.tweets as t WHERE t.user_id IN (  SELECT user_id  FROM (   SELECT DISTINCT user_id, match_event   FROM mgr.tweets t   WHERE match_event IS NOT NULL  ) as view1  GROUP BY user_id  HAVING count(*) >= 30 ) AND  t.in_reply_to_user_id IN (  SELECT user_id  FROM (   SELECT DISTINCT user_id, match_event   FROM mgr.tweets t   WHERE match_event IS NOT NULL  ) as view1  GROUP BY user_id  HAVING count(*) >= 30 ) AND t.in_reply_to_user_id != -1 GROUP BY t.user_id, t.in_reply_to_user_id


-- userzy, ktorzy wystapili min. 5 razy w relacjach komentowania wpisow na twitterze
-- w roli autora lub komentujacego
-- nodes
select id, screen_name as label from mgr.users
  where id in (
    select user_id
    from mgr.tweets
    group by user_id
    having count(*) >= 5
    union ALL
    select in_reply_to_user_id
    from mgr.tweets
    group by in_reply_to_user_id
    having count(*) >= 5
  )
;

-- liczba interakcji miedzy userami w postaci komentujacy wpis <-> autor wpisu
-- edges
select user_id as source, in_reply_to_user_id as target, count(*) as weight
  from mgr.tweets where in_reply_to_user_id != -1
  group by user_id, in_reply_to_user_id
  having count(*) >= 5 ;

select count(*)
from mgr.tweets
  where in_reply_to_user_id != -1

  ;

-- club: id (match_events)
-- cfc:   408 (267, 2680, 255, 720, 709, 243, 674, 589, 445)
-- afc:   158 (266, 258, 255, 715, 714, 244, 611, 547, 404, 176)
-- mufc:  447 (265, 256, 249, 725, 699, 197, 612, 567, 486)
-- mcfc:  508 (260, 259, 254, 715, 704, 198, 694, 609, 526)

select *
from mgr.match_events
  where home_team = 508 or away_team = 508;

select *
from mgr.tweets
  where match_event in (260, 259, 254, 715, 704, 198, 694, 609, 526)



select user_id as source, in_reply_to_user_id as target, count(*) as weight
from mgr.tweets
where in_reply_to_user_id != -1 and
      match_event in (267, 2680, 255, 720, 709, 243, 674, 589, 445)
group by user_id, in_reply_to_user_id
having count(*) >= 5 ;


select id, screen_name as label from mgr.users   where id in (     select user_id      from mgr.tweets     group by user_id     having count(*) >= 5     union ALL      select in_reply_to_user_id     from mgr.tweets     group by in_reply_to_user_id     having count(*) >= 5   )
;






-- 2014-05-07
select count(*), modularity_class,
  (SELECT u.screen_name FROM mgr.users_group ug JOIN mgr.users u ON ug.user_id = u.id WHERE ug.modularity_class = u1.modularity_class order by weighted_degree desc limit 1)
from mgr.users_group u1
group by modularity_class
  order by count(*) desc;


select ug.*, u.name, u.screen_name, u.location, u.description
from mgr.users_group ug join mgr.users u on ug.user_id = u.id
  where modularity_class = 1540
  order by weighted_degree desc



  ;











-- 2014-05-12
SELECT min(created_at) startDate, max(created_at) + interval '1h' endDate, user_id as source, in_reply_to_user_id as target, count(*) weight
from mgr.tweets
  where match_event in (266, 258, 255, 715, 714, 244, 611, 547, 404, 176)
  AND in_reply_to_user_id != -1
  group by user_id, in_reply_to_user_id
having count(*) >= 3;


SELECT user_id as source, in_reply_to_user_id as target, match_event, count(*)
from mgr.tweets
  where match_event in (260, 259, 254, 715, 704, 198, 694, 609, 526)
  AND in_reply_to_user_id != -1
group by user_id, in_reply_to_user_id, match_event
  having count(*) >= 3



select me.id, start_date, tweets_number, t.name, t2.name
from mgr.match_events me JOIN mgr.teams t on me.home_team = t.id
  JOIN mgr.teams t2 on me.away_team = t2.id
  where t.name LIKE 'Manchester City%' OR t2.name LIKE 'Manchester City%'
  order by start_date asc
;








-- 2014-05-13

select u.screen_name, meg.*
from mgr.match_events_gephi meg JOIN mgr.users u ON meg.user_id = u.id
  where match_event_id = 176
  order by authority desc
;



select
  count(nullif(valence > 0.4786984978198536, false)) as positives,
  count(nullif(valence <= 0.4786984978198536, false)) as negatives
from mgr.paroubek_tweets pt JOIN mgr.tweets t on pt.tweet_id = t.id
  where t.user_id = 34613288
  and t.match_event = 176
  and valence != 'NaN';

select *
from mgr.paroubek_tweets
  where valence != 'NaN';

update mgr.paroubek_tweets
    set valence = NULL
  where valence = 'NaN'





select ums.user_id, ums.match_event_id, positives, negatives, authority, positives * 1.0 / (positives + negatives)
from mgr.users_match_sentiment ums JOIN mgr.match_events_gephi meg ON ums.user_id = meg.user_id AND ums.match_event_id = meg.match_event_id
where ums.match_event_id = 267
      and positives + negatives > 0
order by authority desc
limit 5;














-- 2014-05-15
select avg(in_degree)
from mgr.match_events_gephi
  where match_event_id = 176





-- 2104-05-19
select match_event_id, count(*)
from mgr.match_events_gephi
  where match_event_id in (699,249,612,197)
  group by match_event_id
;

SELECT
     *
   FROM mgr.match_events
   WHERE home_team = 447


select *
from mgr.users_match_sentiment
  where match_event_id = 249

select count(*)
from mgr.tweets
  where in_reply_to_user_id != -1


select count(*)
from mgr.tweets
  where text LIKE '%@%'
    AND text NOT LIKE '%RT%'
    AND text NOT LIKE '"@%'
  and in_reply_to_user_id  = -1

;

select country, state, county, city, count(*)
from mgr.tweets t JOIN mgr.geodata g ON t.id = g.tweet_id
where match_event = 267
group by country, state, county, city
UNION
select country, state, county, NULL, count(*)
from mgr.tweets t JOIN mgr.geodata g ON t.id = g.tweet_id
where match_event = 267
group by country, state, county
UNION
select country, state, NULL, NULL, count(*)
from mgr.tweets t JOIN mgr.geodata g ON t.id = g.tweet_id
where match_event = 267
group by country, state
UNION
select country, NULL, NULL, NULL, count(*)
from mgr.tweets t JOIN mgr.geodata g ON t.id = g.tweet_id
where match_event = 267
group by country
UNION
select NULL, NULL, NULL, NULL, count(*)
from mgr.tweets t JOIN mgr.geodata g ON t.id = g.tweet_id
where match_event = 267;




SELECT country, count(*)
FROM mgr.tweets t JOIN mgr.geodata g ON t.id = g.tweet_id
  where match_event = 267
  group by country
  order by count(*) desc




select count(*)
from mgr.tweets
  where match_event = 267
  and latitude IS NOT NULL



-- 2014-05-20

SELECT a.country, count(*), sum(a.positivness) FROM (
  SELECT
    country,
    pt.valence > 0.4786984978198536 AS positivness
  FROM mgr.tweets t JOIN mgr.geodata g ON t.id = g.tweet_id
    JOIN mgr.paroubek_tweets pt ON t.id = pt.tweet_id
  WHERE match_event = 267
        AND country IS NOT NULL
) a
group by a.country
ORDER BY count(*) DESC

SELECT count(*)




SELECT id, created_at, latitude, longitude, match_event, pt.valence > 0.4786984978198536 AS positivness
FROM mgr.tweets t JOIN mgr.paroubek_tweets pt on t.id = pt.tweet_id
  WHERE latitude IS NOT NULL
    AND match_event IS NOT NULL



select *
from mgr.tweets
  where id = 416992948732317696





-- 2014-05-26
-- cfinder
SELECT
  u1.screen_name as author,
  u2.screen_name as receiver,
  count(*) as weight
FROM
  mgr.tweets t JOIN
  mgr.users u1 ON t.user_id = u1.id JOIN
  mgr.users u2 ON t.in_reply_to_user_id = u2.id
WHERE
  in_reply_to_user_id != -1
GROUP BY
  u1.screen_name,
  u2.screen_name
HAVING count(*) >= 3
ORDER BY count(*) DESC


select count(*)
from (
  select user_id, in_reply_to_user_id, count(*)
  from mgr.tweets
  where in_reply_to_user_id != -1
  group by user_id, in_reply_to_user_id
  having count(*) >= 3
  order by count(*) DESC
) a



select user_id, in_reply_to_user_id, count(*)
from mgr.tweets
where in_reply_to_user_id != -1
group by user_id, in_reply_to_user_id
having count(*) >= 5
order by count(*) DESC








SELECT
  u1.screen_name as author,
  u2.screen_name as receiver,
  count(*) as weight
FROM
  mgr.tweets t JOIN
  mgr.users u1 ON t.user_id = u1.id JOIN
  mgr.users u2 ON t.in_reply_to_user_id = u2.id
WHERE
  in_reply_to_user_id != -1
  AND match_event
GROUP BY
  u1.screen_name,
  u2.screen_name
HAVING count(*) >= 3
ORDER BY count(*) DESC  ;



-- 2014-05-27
SELECT *
from mgr.users
  where screen_name = 'JamesLake_89';



select *
from mgr.cliques_team_user

select *
from mgr.cliques_match_user

SELECT *
FROM mgr.cliques_match
WHERE match_id = '255'  ;














-- 2014-05-31

select *
FROM mgr.users_match_sentiment;



select *
from mgr.match_events_gephi
;




SELECT positives + negatives tweets, u.screen_name, 100 * positives / (positives + negatives) as positivness
FROM
  mgr.users_match_sentiment ums JOIN
  mgr.users u ON ums.user_id = u.id
WHERE
  match_event_id = 176 AND
  positives + negatives > 0
  ORDER BY positives + negatives DESC
;








-- tweets
SELECT count(*)
FROM mgr.tweets
WHERE
  text NOT LIKE 'RT @%' AND
  match_event = 255;


-- users
SELECT count(DISTINCT user_id)
FROM mgr.tweets
WHERE
  text NOT LIKE 'RT @%' AND
  match_event = 255;


-- positives
SELECT count(*)
FROM mgr.paroubek_tweets pt JOIN mgr.tweets t ON pt.tweet_id = t.id
WHERE
  t.text NOT LIKE 'RT @%' AND
  t.match_event = 255 AND
    pt.valence != 'NaN' AND
    pt.valence >= 0.4786984978198536
;

-- negatives
SELECT count(*)
FROM mgr.paroubek_tweets pt JOIN mgr.tweets t ON pt.tweet_id = t.id
WHERE
  t.text NOT LIKE 'RT @%' AND
  t.match_event = 255 AND
      pt.valence != 'NaN' AND
      pt.valence < 0.4786984978198536
;


-- neutrals
SELECT count(*)
FROM mgr.paroubek_tweets pt JOIN mgr.tweets t ON pt.tweet_id = t.id
WHERE
  t.text NOT LIKE 'RT @%' AND
  t.match_event = 255 AND
      pt.valence = 'NaN'
;

-- geolocated
SELECT count(*)
FROM mgr.tweets
WHERE latitude IS NOT NULL AND
  text NOT LIKE 'RT @%' AND
  match_event = 255;


-- replies
SELECT count(*)
FROM mgr.tweets
WHERE
  text NOT LIKE 'RT @%' AND
  in_reply_to_user_id != -1 AND
  match_event = 255;

-- retweets
SELECT count(*)
FROM mgr.tweets
WHERE
  text LIKE 'RT @%' AND
  match_event = 255;


-- cliques

SELECT count(*)
FROM mgr.cliques_match
WHERE
  match_id = 255;


















-- 2014-06-01
SELECT distinct (
  SELECT tweets
  FROM mgr.match_stats b1
  WHERE b1.match_id = a1.match_id
    AND includert = TRUE
) -
  (
    SELECT tweets
    FROM mgr.match_stats b2
    WHERE b2.match_id = a1.match_id
          AND includert = FALSE
  ) difference,
  a1.match_id
FROM mgr.match_stats a1


update mgr.match_stats a1
  set retweets = (
   SELECT tweets
   FROM mgr.match_stats b1
   WHERE b1.match_id = a1.match_id
         AND includert = TRUE
 ) -
 (
   SELECT tweets
   FROM mgr.match_stats b2
   WHERE b2.match_id = a1.match_id
         AND includert = FALSE
 );







select u.screen_name, (100 * positives /  (positives + negatives)), count(*)
from mgr.cliques_team_user ctu JOIN mgr.users u ON ctu.user_id = u.id
  WHERE clique_id IN (
    SELECT clique_id
    FROM mgr.cliques_team
    WHERE team_id = 158
  )
  group by u.screen_name, positives, negatives
  order by count(*) desc




update mgr.cliques_team_user ctu set
  occurences = (
    SELECT count(*)
    FROM mgr.cliques_team_user
    WHERE user_id = ctu.user_id
  )

select ctu.*, u.screen_name
from
  mgr.cliques_team_user ctu
  JOIN mgr.users u ON ctu.user_id = u.id

  order by occurences desc ;



select *
from
mgr.match_events me
;








-- 2014-06-02
SELECT user_id, count(distinct match_event), sum(t.)
FROM mgr.tweets t JOIN mgr.paroubek_tweets pt ON t.id = pt.tweet_id
where match_event IN (266, 258, 255, 715, 714, 244, 611, 547, 404, 176)
GROUP BY user_id
ORDER BY count(distinct match_event) DESC


SELECT user_id, sum(negatives) negatives, sum(positives) positives
FROM mgr.users_match_sentiment
where match_event_id IN
      (SELECT id FROM mgr.match_events WHERE away_team = 158 OR home_team = 158)
GROUP BY user_id
ORDER BY sum(positives) + sum(negatives) DESC ;




select *
from mgr.user_team_stats
;





-- club: id (match_events)
-- cfc:   408 (267, 2680, 255, 720, 709, 243, 674, 589, 445)
-- afc:   158 (266, 258, 255, 715, 714, 244, 611, 547, 404, 176)
-- mufc:  447 (265, 256, 249, 725, 699, 197, 612, 567, 486)
-- mcfc:  508 (260, 259, 254, 715, 704, 198, 694, 609, 526)

UPDATE mgr.user_team_stats uts SET
  positives = (SELECT COALESCE(sum(positives), 0)
               FROM mgr.users_match_sentiment
               WHERE
                 match_event_id IN (SELECT id FROM mgr.match_events WHERE home_team = 408 OR away_team = 408) AND
                 user_id = uts.user_id),
  negatives = (SELECT COALESCE(sum(negatives), 0)
               FROM mgr.users_match_sentiment
               WHERE
                 match_event_id IN (SELECT id FROM mgr.match_events WHERE home_team = 408 OR away_team = 408) AND
                 user_id = uts.user_id)
  WHERE team_id = 408


;

select *
from mgr.user_team_stats


SELECT count(*)
FROM mgr.tweets t JOIN mgr.paroubek_tweets pt ON t.id = pt.tweet_id
WHERE
  pt.valence != 'NaN' AND
  pt.valence >= 0.4786984978198536 AND
  t.match_event IN (SELECT id FROM mgr.match_events WHERE home_team = 408 OR away_team = 408) AND
  t.user_id = 296211518



SELECT *
FROM mgr.user_team_stats;




select
  count(nullif(valence > 0.4786984978198536, false)) as positives,
  count(nullif(valence <= 0.4786984978198536, false)) as negatives
from mgr.paroubek_tweets pt JOIN mgr.tweets t on pt.tweet_id = t.id
where t.user_id = 26203650
      and t.match_event IN (267, 2680, 255, 720, 709, 243, 674, 589, 445)
      and valence != 'NaN';


SELECT count(*)
FROM mgr.paroubek_tweets
  where valence = 'NaN';

SELECT * FROM mgr.paroubek_tweets_neutral;

CREATE TABLE mgr.paroubek_tweets_sentiment AS
  select pt.*, user_id, match_event
  FROM mgr.paroubek_tweets pt
    JOIN mgr.tweets t ON pt.tweet_id = t.id
  WHERE pt.sentiment = 'NEG'  OR pt.sentiment = 'POS';


SELECT count(*)
FROM mgr.paroubek_tweets_negatives
WHERE user_id = 26203650
    and match_event IN (267, 2680, 255, 720, 709, 243, 674, 589, 445);



SELECT * FROM (
  SELECT
    g.country,
    COUNT(CASE WHEN sentiment = 'POS' THEN 1
          ELSE NULL END) positives,
    COUNT(CASE WHEN sentiment = 'NEG' THEN 1
          ELSE NULL END) negatives
  FROM mgr.paroubek_tweets_sentiment pts
    JOIN mgr.geodata g ON pts.tweet_id = g.tweet_id
  WHERE pts.match_event = 445
    AND country IS NOT NULL
  GROUP BY country
) a
ORDER BY positives + negatives DESC



-- 2014-06-03
SELECT pts.match_event,  g.country, count('POS')
FROM mgr.geodata g
  JOIN mgr.paroubek_tweets_sentiment pts ON g.tweet_id = pts.tweet_id
WHERE pts.match_event IN (267, 2680, 255, 720, 709, 243, 674, 589, 445)
  AND country IS NOT NULL
GROUP BY country, pts.match_event
ORDER BY match_event, count(*) DESC
LIMIT 10
  ;,





-- 2014-06-19
select user_id, home_team, away_team
from mgr.tweets t
  JOIN mgr.match_events e ON t.match_event = e.id



select user_id, count(*)
from mgr.tweets
  where match_event IN (267, 2680, 255, 720, 709, 243, 674, 589, 445)
  GROUP BY user_id;


select count(*)
from mgr.user_team_count


select *
from mgr.users;

update mgr.users set top_team = 'Arsenal' where id = 25035250;

select top_team, count(*)
from mgr.users
group by top_team


-- 2014-06-21
SELECT t.user_id as source, t.in_reply_to_user_id as target, t.created_at, pts.sentiment
FROM mgr.tweets t JOIN mgr.paroubek_tweets_sentiment pts ON t.id = pts.tweet_id
  WHERE in_reply_to_user_id != -1;

select count(*)
from mgr.tweets t JOIN mgr.paroubek_tweets_sentiment pts ON t.id = pts.tweet_id
where in_reply_to_user_id != -1;

select count(*)
from mgr.tweets t
where in_reply_to_user_id != -1;


SELECT count(*) FROM (
  SELECT
    t.user_id             AS source,
    t.in_reply_to_user_id AS target,
    count(*)
  FROM mgr.tweets t
  WHERE in_reply_to_user_id != -1
  GROUP BY t.user_id, t.in_reply_to_user_id
) a


select id, screen_name as label, top_team
from mgr.users


-- 2014-06-24
SELECT count(*)
FROM mgr.tweets
  where match_event = 256
  and (text LIKE 'RT @%:%'
        OR in_reply_to_user_id != -1);

SELECT count(*)
FROM mgr.tweets
  where match_event = 256
  and in_reply_to_user_id != -1
  AND TEXT NOT LIKE '%RT @%:%';


SELECT *
FROM mgr.tweets
where match_event = 256
  and in_reply_to_user_id != -1
  and text LIKE '%RT @%:%'
  ;


SELECT count(*)
FROM mgr.tweets
  where match_event = 256
  and (in_reply_to_user_id = -1
       AND  text LIKE '%RT @%:%'
  );


SELECT count(*)
FROM mgr.tweets
  where match_event = 256
  and (in_reply_to_user_id = -1
  AND text LIKE '%RT @%:%')



SELECT count(*)
FROM mgr.tweets
  where text LIKE '%RT @%:%'
  AND retweet_user_id IS NULL


;


select count(*)
from mgr.tweets
where retweet_user_id is not null;

select count(*)
from mgr.tweets
where retweet_user_id = -1;


select * from mgr.tweets;

select *
from mgr.users
  where screen_name = 'ManUtd'




-- 2014-07-03
-- 724644
select count(*)
from mgr.tweets
  where in_reply_to_user_id != -1;

-- 710033
select count(*)
from mgr.tweets
where retweet_user_id IS NOT NULL
      AND retweet_user_id != -1;
-- 709757
select count(*)
from mgr.tweets
where retweet_user_id IS NOT NULL
  AND retweet_user_id != -1
  AND in_reply_to_user_id = -1;

;


SELECT
  t.user_id as source,
  t.in_reply_to_user_id as target,
  me.start_date,
  t1.name || ' - ' || t2.name as teams,
  pts.sentiment,
  'REPLY' as type
FROM
  mgr.tweets t JOIN
  mgr.match_events me ON t.match_event = me.id JOIN
  mgr.teams t1 ON t1.id = me.home_team JOIN
  mgr.teams t2 ON t2.id = me.away_team JOIN
  mgr.paroubek_tweets_sentiment pts ON t.id = pts.tweet_id
WHERE
  t.in_reply_to_user_id != -1
UNION ALL
SELECT
  t.user_id as source,
  t.retweet_user_id as target,
  me.start_date,
  t1.name || ' - ' || t2.name as teams,
  pts.sentiment,
  'RETWEET' as type
FROM
  mgr.tweets t JOIN
  mgr.match_events me ON t.match_event = me.id JOIN
  mgr.teams t1 ON t1.id = me.home_team JOIN
  mgr.teams t2 ON t2.id = me.away_team
  LEFT jOIN mgr.paroubek_tweets_sentiment pts ON t.id = pts.tweet_id
WHERE
  t.retweet_user_id != -1 AND
  t.retweet_user_id IS NOT NULL
;



SELECT count(*)
FROM mgr.paroubek_tweets_sentiment



-- 2014-07-04

SELECT t.*,
  st_distance_sphere(
      st_point(c1.latitude, c1.longitude),
      st_point(c2.latitude, c2.longitude))
FROM (
  SELECT
    t.match_event,
    t.user_id,
    t.in_reply_to_user_id,
    count(*) AS relations
  FROM mgr.tweets t
  WHERE t.latitude IS NOT NULL
        AND t.in_reply_to_user_id != -1
        AND t.match_event IS NOT NULL
  GROUP BY t.match_event, t.user_id, t.in_reply_to_user_id
) t

  JOIN mgr.users_avg_coords c1 ON t.user_id = c1.user_id
  JOIN mgr.users_avg_coords c2 ON t.in_reply_to_user_id = c2.user_id




-- wyliczanie fizycznej odleglosci miedzy userami

SELECT
  t.match_event,
  t.user_id,
  t.in_reply_to_user_id,
  avg(mgr.st_distance_sphere(
          mgr.st_point(c1.latitude, c1.longitude),
          mgr.st_point(c2.latitude, c2.longitude))) as distance,
  count(*) AS relations
FROM mgr.tweets t
  JOIN mgr.users_avg_coords c1 ON t.user_id = c1.user_id
  JOIN mgr.users_avg_coords c2 ON t.in_reply_to_user_id = c2.user_id
WHERE t.latitude IS NOT NULL
      AND t.in_reply_to_user_id != -1
      AND t.match_event IS NOT NULL
      AND t.user_id != t.in_reply_to_user_id
GROUP BY t.match_event, t.user_id, t.in_reply_to_user_id
ORDER BY relations desc, distance asc






select *, (latitude - longitude) ^ 2
from mgr.users_avg_coords


  ;


select version();



-- 2014-07-06
SELECT distance / 1000 as distance, relations
FROM mgr.users_distance
--where match_event = 255
order by distance;


select *
from mgr.match_events
  order by tweets_number desc
;



-- club: id (match_events)
-- afc:   158 (266, 258, 255, 715, 714, 244, 611, 547, 404, 176)
-- cfc:   408 (267, 2680, 255, 720, 709, 243, 674, 589, 445)
-- mufc:  447 (265, 256, 249, 725, 699, 197, 612, 567, 486)
-- mcfc:  508 (260, 259, 254, 715, 704, 198, 694, 609, 526)

158, 408, 447, 508


select  distance / 1000 as distance, relations
from mgr.users_distance
where match_event in (260, 259, 254, 715, 704, 198, 694, 609, 526)
order by distance desc;



select count(*)
from mgr.users
  where top_team = 'United';


select *
from mgr.users_match_sentiment

  ;

select *
from mgr.user_team_stats;

SELECT *-
FROM mgr.paroubek_tweets_sentiment
  where user_id = 920810623
  and sentiment = 'NEG'




select * from teams ;

select *
from mgr.match_events
  where home_team = 447


-- arsenal at home lat, long (51.555, -0.108611)

SELECT
  st_distance_sphere(
      st_point(a.lat, a.long),
      st_point(51.555, -0.108611)
  ) / 1000 as distance_km
FROM (
  SELECT
    t.user_id,
    avg(t.latitude)  lat,
    avg(t.longitude) long
  FROM
    mgr.tweets t
    JOIN mgr.users u ON t.user_id = u.id
  WHERE
    t.match_event IN (255, 404, 176, 244, 611)
    AND latitude IS NOT NULL
    AND u.arsenal IS false
  GROUP BY t.user_id
) a
ORDER BY distance_km asc
;


SELECT
  st_distance_sphere(
    st_point(t.latitude, t.longitude),
    st_point(me.latitude, me.longitude)
 ) / 1000 as distance_km
FROM mgr.tweets t
  JOIN mgr.users u ON t.user_id = u.id
  JOIN mgr.match_events me ON t.match_event = me.id
WHERE t.match_event IN (255, 404, 176, 244, 611)
  AND t.latitude IS NOT NULL
  AND u.arsenal IS false
ORDER BY distance_km asc



SELECT
  t.user_id,
  avg(t.latitude)  lat,
  avg(t.longitude) long
FROM
  mgr.tweets t
  JOIN mgr.users u ON t.user_id = u.id
WHERE
  t.match_event IN (255, 404, 176, 244, 611)
  AND latitude IS NOT NULL
  AND u.arsenal IS TRUE
GROUP BY t.user_id




-- arsenal as guest (away)

SELECT
  st_distance_sphere(
      st_point(a.lat, a.long),
      st_point(melat, melong)
  ) / 1000 as distance_km
FROM (
       SELECT
         t.user_id,
         me.latitude melat,
         me.longitude melong,
         avg(t.latitude)  lat,
         avg(t.longitude) long
       FROM
         mgr.tweets t
         JOIN mgr.users u ON t.user_id = u.id
         JOIN mgr.match_events me ON me.id = t.match_event
       WHERE
         t.match_event IN (715, 714, 258, 266, 547)
         AND t.latitude IS NOT NULL
         AND u.arsenal IS false
       GROUP BY t.user_id, me.latitude, me.longitude
     ) a
ORDER BY distance_km asc
;


SELECT
  t.user_id,
  me.latitude melat,
  me.longitude melong,
  avg(t.latitude)  lat,
  avg(t.longitude) long
FROM
  mgr.tweets t
  JOIN mgr.users u ON t.user_id = u.id
  JOIN mgr.match_events me ON me.id = t.match_event
WHERE
  t.match_event IN (715, 714, 258, 266, 547)
  AND t.latitude IS NOT NULL
  AND u.arsenal IS TRUE
GROUP BY t.user_id, me.latitude, me.longitude





select *
from mgr.match_events
where away_team = 447

select *
from mgr.match_events where id = 249

-- united at home lat, long (53.463056,  -2.291389)
SELECT
  st_distance_sphere(
      st_point(a.lat, a.long),
      st_point(53.463056, -2.291389)
  ) / 1000 as distance_km
FROM (
       SELECT
         t.user_id,
         avg(t.latitude)  lat,
         avg(t.longitude) long
       FROM
         mgr.tweets t
         JOIN mgr.users u ON t.user_id = u.id
       WHERE
         t.match_event IN (249, 699, 197, 612)
         AND latitude IS NOT NULL
         AND u.united IS false
       GROUP BY t.user_id
     ) a
ORDER BY distance_km asc
;

-- untied as guest (away)
,
SELECT
  st_distance_sphere(
      st_point(a.lat, a.long),
      st_point(melat, melong)
  ) / 1000 as distance_km
FROM (
       SELECT
         t.user_id,
         me.latitude melat,
         me.longitude melong,
         avg(t.latitude)  lat,
         avg(t.longitude) long
       FROM
         mgr.tweets t
         JOIN mgr.users u ON t.user_id = u.id
         JOIN mgr.match_events me ON me.id = t.match_event
       WHERE
         t.match_event IN (256, 265, 725, 567, 486)
         AND t.latitude IS NOT NULL
         AND u.united IS false
       GROUP BY t.user_id, me.latitude, me.longitude
     ) a
ORDER BY distance_km asc
;


select *
from mgr.match_events_gephi;




-- dario 123


select distinct count_strategy
from mgr.word_frequency;





select *
from mgr.paroubek_tweets




-- 2014-07-28

select *
from mgr.match_events

-- ARSvsEVE
select user_id source, in_reply_to_user_id target, count(*) weight
from mgr.tweets
where match_event = 244
  and in_reply_to_user_id != -1
group by user_id, in_reply_to_user_id
having count(*) >= 5

select user_id source, in_reply_to_user_id target, count(*)
from mgr.tweets
  where match_event = 255
  and in_reply_to_user_id > 0
GROUP BY user_id, in_reply_to_user_id
  having count(*) >= 5


select *
from mgr.teams
158, afc
408 cfc
508 mufc
447 mufc


select id
from mgr.match_events
  where home_team IN  (158, 408, 508, 447)
  OR away_team IN  (158, 408, 508, 447)

SELECT * FROM (
  SELECT
    match_id,
    modularity_class,
    count(*)
  FROM mgr.modularities
  GROUP BY match_id, modularity_class
  UNION ALL
  SELECT
    match_id,
    NULL,
    count(*)
  FROM mgr.modularities
  GROUP BY match_id
) a
ORDER BY match_id, modularity_class


SELECT match_id, group_size, count(*) groups_number
FROM (
  SELECT
    match_id,
    modularity_class,
    count(*) AS group_size
  FROM mgr.modularities
  GROUP BY match_id, modularity_class
) a1
group by match_id, group_size
ORDER BY group_size desc , groups_number ;













SELECT *
FROM mgr.match_events
WHERE home_team = 408 or away_team = 408
  order by start_date ;


-- club: id (match_events)
-- afc:   158 (176, 404, 547, 611, 244, 714, 715, 255, 258, 266)
-- cfc:   408 (445, 589, 674, 243, 709, 720, 255, 2680, 267),
-- mufc:  447 (486, 567, 612, 197, 699, 725, 249, 256, 265)
-- mcfc:  508 (526, 609, 694, 198, 704, 715, 254, 259, 260)


select me.id, count_modularity(me.id::integer) as licznosc, t1.name as home, t2.name as away, c.name, me.start_date
FROM mgr.match_events me
  JOIN mgr.teams t1 ON me.home_team = t1.id
  JOIN mgr.teams t2 ON me.away_team = t2.id
  JOIN mgr.competitions c ON me.competition = c.id
  WHERE home_team = 408 OR away_team = 408
  order by start_date, licznosc ;


select count_modularity(674);

drop type key_value cascade ;
create type key_value as (key text, value bigint);
create or replace function count_modularity(matchVariable integer)
  returns setof key_value AS $$
BEGIN
  return query SELECT 'od 3', count(*)
  FROM (
    SELECT
      match_id,
      modularity_class,
      count(*) licznosc_grupy
    FROM mgr.modularities
    WHERE match_id = matchVariable
    GROUP BY match_id, modularity_class
    HAVING count(*) >2
  ) a
  WHERE licznosc_grupy < 5
  UNION ALL
  SELECT 'od 5', count(*)
  FROM (
         SELECT
           match_id,
           modularity_class,
           count(*) licznosc_grupy
         FROM mgr.modularities
         WHERE match_id = matchVariable
         GROUP BY match_id, modularity_class
         HAVING count(*) > 2
       ) a
  WHERE licznosc_grupy >= 5
    AND licznosc_grupy < 10
  UNION ALL
  SELECT 'od 9', count(*)
  FROM (
         SELECT
           match_id,
           modularity_class,
           count(*) licznosc_grupy
         FROM mgr.modularities
         WHERE match_id = matchVariable
         GROUP BY match_id, modularity_class
         HAVING count(*) > 2
       ) a
  WHERE licznosc_grupy >= 10;
  --AND licznosc_grupy < 10
END;
$$ language plpgsql;


-- club: id (match_events)
-- afc:   158 (176, 404, 547, 611, 244, 714, 715, 255, 258, 266)
-- cfc:   408 (445, 589, 674, 243, 709, 720, 255, 2680, 267),
-- mufc:  447 (486, 567, 612, 197, 699, 725, 249, 256, 265)
-- mcfc:  508 (526, 609, 694, 198, 704, 715, 254, 259, 260)
select id, tweets_number, start_date
from mgr.match_events
where id in  (176, 404, 547, 611, 244, 714, 715, 255, 258, 266)
order by start_date


select user_id source, in_reply_to_user_id target, count(*) from mgr.tweets
 where match_event = 256 and in_reply_to_user_id > 0 GROUP BY user_id, in_reply_to_user_id
 having count(*) >= 5


select match_id, modularity_class, count(*)
from mgr.modularities
  where match_id = 256
GROUP BY match_id, modularity_class
  having count(*) > 2;








-- 2014-07-29
-- 244 vs Everton
-- 714 vs Napoli
SELECT  count(*)
FROM mgr.modularities
WHERE match_id = 244;

-- count = 1081


-- club: id (match_events)
-- afc:   158 (176, 404, 547, 611, 244, 714, 715, 255, 258, 266)
-- cfc:   408 (445, 589, 674, 243, 709, 720, 255, 2680, 267),
-- mufc:  447 (486, 567, 612, 197, 699, 725, 249, 256, 265)
-- mcfc:  508 (526, 609, 694, 198, 704, 715, 254, 259, 260)
SELECT me1.start_date,m1.match_id, m2.match_id, me2.start_date,  count(*)
FROM mgr.modularities m1
  JOIN mgr.modularities m2 ON m1.user_id = m2.user_id
  JOIN mgr.match_events me1 ON m1.match_id = me1.id
  JOIN mgr.match_events me2 ON m2.match_id = me2.id
where m1.match_id IN (526, 609, 694, 198, 704, 715, 254, 259, 260)
      and m2.match_id In (526, 609, 694, 198, 704, 715, 254, 259, 260)
      and me1.start_date < me2.start_date
group by m1.match_id, m2.match_id, me1.start_date, me2.start_date
ORDER BY me1.start_date, me2.start_date

-- count = 826
































--  PODOBIENSTWO GRAFOW, 254, 704
-- wspolne krawedzie (podwojona ilosc)
SELECT count(*) FROM (
  SELECT
    m1.user_id user1,
    m2.user_id user2
  FROM mgr.modularities m1
    JOIN mgr.modularities m2 ON m1.modularity_class = m2.modularity_class
  WHERE m1.match_id = 254 AND m2.match_id = 254
        AND m1.user_id != m2.user_id
  INTERSECT
  SELECT
    m1.user_id user1,
    m2.user_id user2
  FROM mgr.modularities m1
    JOIN mgr.modularities m2 ON m1.modularity_class = m2.modularity_class
  WHERE m1.match_id = 704 AND m2.match_id = 704
        AND m1.user_id != m2.user_id
) b


SELECT
  user_id
FROM mgr.modularities
WHERE match_id = 254
INTERSECT
SELECT
  user_id
FROM mgr.modularities
WHERE match_id = 704

-- wspolne wierzcholki
select count(*) wspolne_wierzcholki FROM (
  SELECT
    user_id
  FROM mgr.modularities
  WHERE match_id = 254
  INTERSECT
  SELECT
    user_id
  FROM mgr.modularities
  WHERE match_id = 704
) b



-- wpsolne wierzcholki
SELECT
  user_id
FROM mgr.modularities
WHERE match_id = 254
INTERSECT
SELECT
  user_id
FROM mgr.modularities
WHERE match_id = 704

-- rozne wierzcholki
SELECT count(*) wszystkie_wierzcholki
FROM (
  SELECT
    user_id
  FROM mgr.modularities
  WHERE match_id = 254
  UNION
  SELECT
    user_id
  FROM mgr.modularities
  WHERE match_id = 704
) b


-- wszystkie krawedzie miedzy wspolnymi wierzcholkami

-- wszystkie krawedzie miedzy wspolnymi wierzcholkami (podwojona ilosc)
SELECT count(*) /2 as suma_krawedzi_miedzy_wspolnymi_wierzcholkami FROM (
   SELECT
     m1.user_id user1,
     m2.user_id user2
   FROM mgr.modularities m1
     JOIN mgr.modularities m2 ON m1.modularity_class = m2.modularity_class
   WHERE m1.match_id = 254 AND m2.match_id = 254
         AND m1.user_id != m2.user_id
   UNION
   SELECT
     m1.user_id user1,
     m2.user_id user2
   FROM mgr.modularities m1
     JOIN mgr.modularities m2 ON m1.modularity_class = m2.modularity_class
   WHERE m1.match_id = 704 AND m2.match_id = 704
         AND m1.user_id != m2.user_id
 ) krawedzie
  WHERE krawedzie.user1 IN (
    SELECT user_id
    FROM mgr.modularities
    WHERE match_id = 254
    INTERSECT
    SELECT
      user_id
    FROM mgr.modularities
    WHERE match_id = 704
  ) AND krawedzie.user2 IN (
    SELECT user_id
    FROM mgr.modularities
    WHERE match_id = 254
    INTERSECT
    SELECT
      user_id
    FROM mgr.modularities
    WHERE match_id = 704
  ) ;

--WHERE user1 IN ('419648079', '14573900', '47861430', '2155751556', '979062403')
  --    AND user2 in ('419648079', '14573900', '47861430', '2155751556', '979062403');


SELECT user_id
FROM mgr.modularities
WHERE match_id = 254
INTERSECT
SELECT
  user_id
FROM mgr.modularities
WHERE match_id = 704;



create function mgr.podobienstwo_grafow(match1 integer, match2 integer) returns double precision as $$
declare
  wspolne_krawedzie integer;
  suma_krawedzi_na_wspolnych_wierzcholkach integer;
  wspolne_wierzcholki integer;
  wszystkie_wierzcholki integer;
begin
  -- wspolne krawedzie
  SELECT count(*) / 2 INTO wspolne_krawedzie FROM (
    SELECT
      m1.user_id,
      m2.user_id
    FROM mgr.modularities m1
      JOIN mgr.modularities m2 ON m1.modularity_class = m2.modularity_class
    WHERE m1.match_id = match1 AND m2.match_id = match1
          AND m1.user_id != m2.user_id
    INTERSECT
    SELECT
      m1.user_id,
      m2.user_id
    FROM mgr.modularities m1
      JOIN mgr.modularities m2 ON m1.modularity_class = m2.modularity_class
    WHERE m1.match_id = match2 AND m2.match_id = match2
          AND m1.user_id != m2.user_id
  ) b;

  -- liczba wspolnych wierzcholkow
  select count(*) INTO wspolne_wierzcholki FROM (
   SELECT
    user_id
    FROM mgr.modularities
    WHERE match_id = match1
    INTERSECT
    SELECT
      user_id
    FROM mgr.modularities
    WHERE match_id = match2
    ) b;

-- suma krawedzi miedzy wspolnymi wierzcholkami
  SELECT count(*) /2 INTO suma_krawedzi_na_wspolnych_wierzcholkach FROM (
    SELECT
      m1.user_id user1,
      m2.user_id user2
    FROM mgr.modularities m1
      JOIN mgr.modularities m2 ON m1.modularity_class = m2.modularity_class
    WHERE m1.match_id = match1 AND m2.match_id = match1
          AND m1.user_id != m2.user_id
    UNION
    SELECT
      m1.user_id user1,
      m2.user_id user2
    FROM mgr.modularities m1
      JOIN mgr.modularities m2 ON m1.modularity_class = m2.modularity_class
    WHERE m1.match_id = match2 AND m2.match_id = match2
          AND m1.user_id != m2.user_id
  ) krawedzie
  WHERE krawedzie.user1 IN (
    SELECT user_id
    FROM mgr.modularities
    WHERE match_id = match1
    INTERSECT
    SELECT
      user_id
    FROM mgr.modularities
    WHERE match_id = match2
  ) AND krawedzie.user2 IN (
    SELECT user_id
    FROM mgr.modularities
    WHERE match_id = match1
    INTERSECT
    SELECT
      user_id
    FROM mgr.modularities
    WHERE match_id = match2
  ) ;

-- wszystkie wierzcholki
  SELECT count(*) INTO wszystkie_wierzcholki
  FROM (
         SELECT
           user_id
         FROM mgr.modularities
         WHERE match_id = match1
         UNION
         SELECT
           user_id
         FROM mgr.modularities
         WHERE match_id = match2
       ) b;
  return (wspolne_krawedzie * suma_krawedzi_na_wspolnych_wierzcholkach) * 100.0 /
         (wspolne_wierzcholki * wszystkie_wierzcholki);


end
$$ language plpgsql;


drop function  mgr.podobienstwo_grafow(integer, integer);
select mgr.podobienstwo_grafow(176, 404);







-- club: id (match_events)
-- afc:   158 (176, 404, 547, 611, 244, 714, 715, 255, 258, 266)
-- cfc:   408 (445, 589, 674, 243, 709, 720, 255, 2680, 267),
-- mufc:  447 (486, 567, 612, 197, 699, 725, 249, 256, 265)
-- mcfc:  508 (526, 609, 694, 198, 704, 715, 254, 259, 260)

select mgr.common_users(526,	609);
select mgr.common_users(609,	694);
select mgr.common_users(694,	198);
select mgr.common_users(198,	704);
select mgr.common_users(704,	715);
select mgr.common_users(715,	254);
select mgr.common_users(254,	259);
select mgr.common_users(259,	260);






-- wspolni uzytkownicy miedzy meczami
select mgr.common_users(176, 404);
drop function mgr.common_users(integer, integer);
create function mgr.common_users(match1 integer, match2 integer) returns varchar as $$
declare
  suma integer;
  przeciecie integer;
begin
  SELECT count(*) into przeciecie FROM (
     SELECT
       user_id
     FROM mgr.tweets
     WHERE match_event = match1
     INTERSECT
     SELECT
       user_id
     FROM mgr.tweets
     WHERE match_event = match2
   ) b;
  raise notice 'Przeciecie == %', przeciecie;
  select count(*) into suma FROM (

    SELECT
      user_id
    FROM mgr.tweets
    WHERE match_event = match1
    UNION
    SELECT
      user_id
    FROM mgr.tweets
    WHERE match_event = match2
  ) b;
  raise notice 'Suma == %', suma;
  return przeciecie ||'         '|| suma;
end
$$ language plpgsql;



select mgr.common_users(486,	567);
select mgr.common_users(567,	612);
select mgr.common_users(612,	197);
select mgr.common_users(197,	699);
select mgr.common_users(699,	725);
select mgr.common_users(725,	249);
select mgr.common_users(249,	256);
select mgr.common_users(256,	265);


-- 2014-08-27

select count(*)
from mgr.match_events me
;

select * from mgr.match_stats
  where includert is true
  ;


select count(*)
from mgr.tweets
  where latitude is not null



-- replies
SELECT count(*)
FROM mgr.tweets
WHERE
  text NOT LIKE 'RT @%' AND
  in_reply_to_user_id != -1;

-- retweets
SELECT count(*)
FROM mgr.tweets
WHERE
  text LIKE 'RT @%';


select count(*)
from mgr.users;

select count(distinct user_id)
from mgr.tweets;

select count(*), 'mek' from mgr.matchevent_keywords UNION
select count(*), 'm' from mgr.managers UNION
select count(*), 'mn' from mgr.managers_nicknames UNION
select count(*), 'p' from mgr.players UNION
select count(*), 'pn' from mgr.players_nicknames UNION
select count(*), 't' from mgr.teams UNION
select count(*), 'tn' from mgr.teams_nicknames;


select count(*)
from mgr.paroubek_tweets
  where sentiment = 'NEU';

select *
from mgr.match_stats
  where includert is false



-- grupy -- podobienstwo

select count(*)
FROM mgr.modularities m1
WHERE m1.match_id = 255 AND
      m1.modularity_class IN (
        SELECT
          m2.modularity_class
        FROM mgr.modularities m2
        WHERE m2.match_id = m1.match_id
        GROUP BY m2.modularity_class
        HAVING count(*) > 2
      );

SELECT count(*) FROM (
  SELECT
    m1.user_id
  FROM mgr.modularities m1
  WHERE m1.match_id = 176 AND
        m1.modularity_class IN (
          SELECT
            m2.modularity_class
          FROM mgr.modularities m2
          WHERE m2.match_id = m1.match_id
          GROUP BY m2.modularity_class
          HAVING count(*) > 2
        )
) prev JOIN
  (
    SELECT
      m1.user_id
    FROM mgr.modularities m1
    WHERE m1.match_id = 255 AND
          m1.modularity_class IN (
            SELECT
              m2.modularity_class
            FROM mgr.modularities m2
            WHERE m2.match_id = m1.match_id
            GROUP BY m2.modularity_class
            HAVING count(*) > 2
          )
    ) next ON prev.user_id = next.user_id;


SELECT modularity_class, count(*), string_agg(user_id, ',')
FROM mgr.modularities m2
WHERE m2.match_id = 255
GROUP BY modularity_class HAVING count(*) > 2
order by modularity_class;

select *
from mgr.modularities
  where match_id = 255
;

SELECT
  array_length(string_to_array('491751292,68765159,315755181,468718670,1106800291', ','), 1);

SELECT count(*) FROM (
  SELECT
    unnest(string_to_array('491751292,453515977,315755181,468718670,1106800291', ',')) as wezel
) a
JOIN (
    SELECT
      unnest(string_to_array('491751292,68765159,315755181,468718670,1106800291', ',')) as wezel
) b ON a.wezel = b.wezel;


create or replace function powtarzalnosc_wezlow(lista1 varchar, lista2 varchar) returns double precision as $$
DECLARE
  rozmiarListy1 integer;
  wspolneWezly integer;
begin
  -- rozmiar listy 1
  SELECT INTO rozmiarListy1 array_length(string_to_array(lista1, ','), 1);

  -- liczba wspolnych wezlych
  SELECT INTO wspolneWezly count(*) FROM
    (SELECT unnest(string_to_array(lista1, ',')) as wezel) a
  JOIN
    (SELECT unnest(string_to_array(lista2, ',')) as wezel) b
  ON a.wezel = b.wezel;

  return wspolneWezly * 100.0 / rozmiarListy1;
end;
$$ language plpgsql;


select powtarzalnosc_wezlow('491751292,453515977,315755181,468718670,1106800291,1106800292','491751292,68765159,315755181,468718670,1106800291')






-- club: id (match_events)
-- afc:   158 (176, 404, 547, 611, 244, 714, 715, 255, 258, 266)
-- cfc:   408 (445, 589, 674, 243, 709, 720, 255, 2680, 267),
-- mufc:  447 (486, 567, 612, 197, 699, 725, 249, 256, 265)
-- mcfc:  508 (526, 609, 694, 198, 704, 715, 254, 259, 260)


select me.id, count_modularity(me.id::integer) as licznosc, t1.name as home, t2.name as away, c.name, me.start_date
FROM mgr.match_events me
  JOIN mgr.teams t1 ON me.home_team = t1.id
  JOIN mgr.teams t2 ON me.away_team = t2.id
  JOIN mgr.competitions c ON me.competition = c.id
WHERE home_team = 408 OR away_team = 408
order by start_date, licznosc ;


select count_modularity(176);

drop type key_value cascade ;
create type key_value as (key text, value bigint);
create or replace function count_modularity(matchVariable integer)
  returns setof key_value AS $$
BEGIN
  return query SELECT 'od 3', count(*)
  FROM (
    SELECT
      match_id,
      modularity_class,
      count(*) licznosc_grupy,
      string_agg(user_id, ',') users
    FROM mgr.modularities
    WHERE match_id = matchVariable
    GROUP BY match_id, modularity_class
    HAVING count(*) >2
  ) a
  WHERE licznosc_grupy < 5
  UNION ALL
  SELECT 'od 5', count(*)
  FROM (
         SELECT
           match_id,
           modularity_class,
           count(*) licznosc_grupy
         FROM mgr.modularities
         WHERE match_id = matchVariable
         GROUP BY match_id, modularity_class
         HAVING count(*) > 2
       ) a
  WHERE licznosc_grupy >= 5
    AND licznosc_grupy < 10
  UNION ALL
  SELECT 'od 9', count(*)
  FROM (
         SELECT
           match_id,
           modularity_class,
           count(*) licznosc_grupy
         FROM mgr.modularities
         WHERE match_id = matchVariable
         GROUP BY match_id, modularity_class
         HAVING count(*) > 2
       ) a
  WHERE licznosc_grupy >= 10;
  --AND licznosc_grupy < 10
END;
$$ language plpgsql;



drop function count_powtarzalnosc(int);
create or replace function count_powtarzalnosc(_teamId int) RETURNS INTEGER
AS $$
DECLARE
  r RECORD;
  prevId INTEGER;
  homeTeam VARCHAR; awayTeam VARCHAR;
  od3 INTEGER;
  od5 INTEGER;
  od10 INTEGER;
  powtarzalnosc DOUBLE PRECISION;
BEGIN
  FOR r IN SELECT * FROM mgr.match_events me WHERE me.home_team = _teamId OR me.away_team = _teamId ORDER BY start_date LOOP
    SELECT INTO od3 count_modularity_for(r.id::integer, 0, 5);
    SELECT INTO od5 count_modularity_for(r.id::integer, 5, 10);
    SELECT INTO od10 count_modularity_for(r.id::integer, 10, 9999);
    SELECT INTO homeTeam name FROM mgr.teams WHERE id = r.home_team;
    SELECT INTO awayTeam name FROM mgr.teams WHERE id = r.away_team;

    IF prevId IS NOT NULL THEN
      powtarzalnosc := podobienstwo_meczow(prevId::integer, r.id::integer);
    END IF;

    raise notice '%, %, %, %, %, %, %', homeTeam, awayTeam, round(powtarzalnosc::numeric, 4), od3, od5, od10, r.start_date;
    prevId := r.id;
  END LOOP;
  --return 1;
END;
$$ language plpgsql;

select count_powtarzalnosc(508);

508

create or replace function count_modularity_for(matchId integer, bottomRange integer, topRange integer) RETURNS integer AS $$
DECLARE
  wielkosc_grupy integer;
BEGIN
  SELECT count(*) INTO wielkosc_grupy
  FROM (
         SELECT
           match_id,
           modularity_class,
           count(*) licznosc_grupy
         FROM mgr.modularities
         WHERE match_id = matchId
         GROUP BY match_id, modularity_class
         HAVING count(*) > 2
       ) a
  WHERE licznosc_grupy >= bottomRange AND licznosc_grupy < topRange;
  return wielkosc_grupy;
END;
$$ language plpgsql;

select count_modularity_for(176, 0, 5);
select count_modularity_for(176, 5, 10);
select count_modularity_for(176, 10, 999999);

-- club: id (match_events)
-- afc:   158 (176, 404, 547, 611, 244, 714, 715, 255, 258, 266)
-- cfc:   408 (445, 589, 674, 243, 709, 720, 255, 2680, 267),
-- mufc:  447 (486, 567, 612, 197, 699, 725, 249, 256, 265)
-- mcfc:  508 (526, 609, 694, 198, 704, 715, 254, 259, 260)

create or replace function
  podobienstwo_meczow(prevMatchId integer, nextMatchId integer)
  RETURNS double precision AS $$
DECLARE
  wspolneWezly integer;
  rozmiarPierwszegoMeczu integer;
BEGIN
	SELECT INTO wspolneWezly count(*) FROM (
	  SELECT
	    m1.user_id
	  FROM mgr.modularities m1
	  WHERE m1.match_id = prevMatchId AND
	        m1.modularity_class IN (
	          SELECT
	            m2.modularity_class
	          FROM mgr.modularities m2
	          WHERE m2.match_id = m1.match_id
	          GROUP BY m2.modularity_class
	          HAVING count(*) > 2
	        )
	) prev JOIN (
	    SELECT
	      m1.user_id
	    FROM mgr.modularities m1
	    WHERE m1.match_id = nextMatchId AND
          m1.modularity_class IN (
            SELECT
              m2.modularity_class
            FROM mgr.modularities m2
            WHERE m2.match_id = m1.match_id
            GROUP BY m2.modularity_class
            HAVING count(*) > 2
          )
    ) next ON prev.user_id = next.user_id;

	SELECT INTO rozmiarPierwszegoMeczu count(*)
	FROM mgr.modularities m1
	WHERE m1.match_id = prevMatchId AND
      m1.modularity_class IN (
        SELECT
          m2.modularity_class
        FROM mgr.modularities m2
        WHERE m2.match_id = m1.match_id
        GROUP BY m2.modularity_class
        HAVING count(*) > 2
      );

	return wspolneWezly * 1.0 / rozmiarPierwszegoMeczu;
END;
$$ language plpgsql;

select podobienstwo_meczow(176, 255);




-- 2014-09-02

select count(*) from mgr.users
where arsenal is true -- 274676
;
select count(*) from mgr.users
where chelsea is true -- 215162
;
select count(*) from mgr.users
where city is true -- 141475
;
select count(*) from mgr.users
where united is true --192981
;















-- 2014-09-16
-- chelsea southampton 17:10
SELECT
  to_char(created_at, 'HH24:MI') minuta,
  count(*)
FROM mgr.tweets t JOIN mgr.paroubek_tweets_sentiment pts ON t.id = pts.tweet_id
WHERE t.match_event = 589
      AND pts.sentiment = 'NEG'
GROUP BY minuta
ORDER BY minuta;


SELECT
  *
FROM mgr.match_events
WHERE id = 267;


SELECT
  to_char(created_at, 'HH24:MI') minuta,
  count(*)
FROM mgr.tweets
WHERE match_event = 589
  --and latitude is not null
GROUP BY minuta
ORDER BY minuta;



SELECT
  substring(to_char(t.created_at, 'HH24:MI') from 1 for 4)  minuta,
  --pts.sentiment,
  count(*) tweetow
FROM mgr.paroubek_tweets_sentiment pts JOIN mgr.geodata g ON pts.tweet_id = g.tweet_id JOIN mgr.tweets t ON t.id = pts.tweet_id
WHERE
    pts.sentiment = 'NEG' AND
      pts.match_event = 267
  and county = 'London'
group by minuta
ORDER BY minuta;







-- 2014-09-20

SELECT
  pts.text, t.text, pts.sentiment, g.country, pts.valence
FROM mgr.paroubek_tweets_sentiment pts JOIN mgr.geodata g ON pts.tweet_id = g.tweet_id JOIN mgr.tweets t ON t.id = pts.tweet_id
WHERE
  pts.match_event = 267
  and pts.sentiment = 'POS'
  AND city = 'Indonesia';



select created_at, text
from mgr.tweets
  where text not like 'RT @%'


select *
from mgr.teams

select *
from mgr.match_events
  where home_team = 447
  or away_team = 447
  order by start_date asc