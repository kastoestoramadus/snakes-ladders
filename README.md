# snakes-ladders

sbt run

runs on localhost, port 9000

GET     /reset-game               needed to be the first step. ex. for game creation: curl "localhost:9000/reset-game?players=Joe&computersNo=2"
GET     /make-move                performs legal move of next player. No params.
GET     /players-positions        check which player is where. No params.
GET     /is-finished              check if game has finished. No params.
GET     /next-moves               check who will perfrom next move. No params.
GET     /board                    get the board model. No params.
