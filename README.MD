This is an experiment.
Would it make sense to make an image manipulation language?
How about sql? What if you stored an image in a SQL database and manipulated it using standard SQL?
Would this work?

This repo loads an image into a simple database.

>CREATE TABLE `imagedata` (
>   `imageid` int(11) NOT NULL,
>   `x` int(11) NOT NULL,
>   `y` int(11) NOT NULL,
>   `r` int(11) NOT NULL,
>   `g` int(11) NOT NULL,
>   `b` int(11) NOT NULL,
>   `a` int(11) NOT NULL,
>   PRIMARY KEY (`imageid`,`x`,`y`)
> ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

You can store an image into the database by running
> Usage: thelaboflieven.LoadIntoDatabase <filename> <destinationImageId>

Using this command you can store the image into the database.

> Usage: thelaboflieven.StoreFromDatabase <filename> <sourceImageId>

Using this command you can retrieve the image from the database.

You can manipulate the image by using SQL queries:

> -- greyscale of the image by taking the least value.
>  INSERT INTO imagedata(imageid, x, y, r, g, b, a)
>  SELECT 4 as imageid, x, y, least(r,g,b) as r, least(r,g,b) as g, least(r,g,b) as b , least(r,g,b) as a from imagedata where imageid=3;


>  -- greyscale of the image by taking the greatest value.
>  INSERT INTO imagedata(imageid, x, y, r, g, b, a)
>  SELECT 4 as imageid, x, y, greatest(r,g,b) as r, greatest(r,g,b) as g, greatest(r,g,b) as b , greatest(r,g,b) as a from imagedata where imageid=3;


>  -- increase brightness
>  INSERT INTO imagedata(imageid, x, y, r, g, b, a)
>  SELECT 4 as imageid, x, y, least(r+50,255) as r, least(g+50,255) as g, least(b+50,255) as b , least(a+50,255) as a from imagedata where imageid=3;

>  -- increase brightness
>  INSERT INTO imagedata(imageid, x, y, r, g, b, a)
>  SELECT 4 as imageid, x, y, least(r+50,255) as r, least(g+50,255) as g, least(b+50,255) as b , least(a+50,255) as a from imagedata where imageid=3;

