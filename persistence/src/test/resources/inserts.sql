--DEFAULT INSERT INTO TABLES FOR TESTING PURPOSES

--USERS

INSERT INTO users (userid,email, username, password, role) VALUES (1,'cavani@test.com', 'Cavani', 'Cavanipass', 1);
INSERT INTO users (userid, email, username, password, role) VALUES (2,'figal@test.com', 'null', 'null', 0);
INSERT INTO users (userid, email, username, password, role) VALUES (3,'barco@test.com', 'Barco', 'Barcopass', 2);


--INSERT INTO media (type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, status) VALUES(false, 'Meg 2: The Trench', 'en', False, '2023-08-02',  'An exploratory dive into the deepest depths of the ocean of a daring research team spirals into chaos when a malevolent mining operation threatens their mission and forces them into a high-stakes battle for survival.', 'https://image.tmdb.org/t/p/original/8pjWz2lt29KyVGoq1mXYu6Br7dE.jpg', 'https://image.tmdb.org/t/p/original/4m1Au3YkjqsxF8iwQy0fPYSxE0h.jpg', 'https://www.youtube.com/watch?v=dG91B3hHyY4', 7, 'Released');
--INSERT INTO media (type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, status) VALUES(false, 'Barbie', 'en', False, '2023-07-19',  'Barbie and Ken are having the time of their lives in the colorful and seemingly perfect world of Barbie Land. However, when they get a chance to go to the real world, they soon discover the joys and perils of living among humans.', 'https://image.tmdb.org/t/p/original/ctMserH8g2SeOAnCw5gFjdQF8mo.jpg', 'https://image.tmdb.org/t/p/original/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg', 'https://www.youtube.com/watch?v=Y1IgAEejvqM', 7.3, 'Released');

INSERT INTO reviews(reviewid,userid, mediaid, rating, reviewcontent) VALUES (1,1, 1, 5, 'Buena Peli');
INSERT INTO reviews(reviewid,userid, mediaid, rating, reviewcontent) VALUES (2,2, 1, 2, 'No me gusto');
INSERT INTO reviews(reviewid,userid, mediaid, rating, reviewcontent) VALUES (3,1, 2, 2, 'Aguante Ken');

INSERT INTO moovielists(moovielistid,userid, name, description, type) VALUES (1,1, 'CavaniList', 'CavaniListDescription', 1);
INSERT INTO moovielists(moovielistid,userid, name, description, type) VALUES (2,1, 'CavaniListToDelete', 'CavaniListDescription', 1);

INSERT INTO moovielistscontent(id,moovielistid, mediaid, customorder) VALUES (1,1, 1, 1);

INSERT INTO moovielistslikes(userid, moovielistid) VALUES (1, 2);

INSERT INTO moovielistsfollows(userid, moovielistid) VALUES (1, 2);

INSERT INTO comments(commentid,content,reviewid,userid) VALUES (1,'Good review',3,1);
INSERT INTO comments(commentid,content,reviewid,userid) VALUES (2,'I do not agree',3,2);