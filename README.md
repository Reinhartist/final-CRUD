This project produces a site wherein one can manage accounts, accounts may have
resources associated with them and accounts may be associated with projects.

For the database I used neo4j, this is an extremely cool graph database, by thinking of
data in terms of a graph it provides a very intuitive feel for join and for traversing joins.
Consider say the table of people and a join table called friends consisting of ordered pairs (a, b) which 
says a is a friend of b. To find the people who are either your friends or friends of friends of id = 1
is as simple as "match (x), (x) - [:friends *..2] -> (y) return y" which easily generalises to any 
depth of friends. Neo4j also has great support in many common graph algorithms, it makes traversing 
data in a graph like way much simpler.

I did not make too much use of its power, but for example when viewing a project you can see all the
resources available for the people on that project, this is as simple as going to each person on the project
and seeing their resources but the query is especially pleasant in neo4j.

For the api I used springboot, this makes things more or less trivial. It abstracts away from the actual
process so much so that you merely need to write the thing you would like to write and it will work, subject
to some preliminary syntax.

For the frontend I used vue, this is an extremely powerful framework which creates a seamless link between
the javascript and html and makes it trivial to have reactive pages. It enables very concise code as
we can just pass around variables all around the place easily, never having to really work our way through
the DOM.

To aid in making things look reasonably pretty I used bootstrap vue, in particular bootstrap vue
provides an extremely powerful table component that does everything you could ever ask in regards to 
formatting a table.

To make calls to my api I used axios, this is very simple to use, there is seemingly little that can
go wrong.

To do authentication for the login feature I used firebase, this hands over the tricky details to someone
else and makes implementing the security simply a case of following some instructions.