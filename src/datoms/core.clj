(ns datoms.core
  (:require [datomic.api :as d]))

;; Define the `db-uri` to access the database
(def db-uri "datomic:sql://?jdbc:postgresql://localhost:5432/datomic?user=yourusername&password=yourpassword")

;; Create the database
(d/create-database db-uri)

;; Connect to the database
(def conn (d/connect db-uri))

;; Run the first transaction
@(d/transact conn [{:db/doc "Hello world"}])

;; We're following the example from the Datomic documentation
;; https://docs.datomic.com/pro/getting-started

;; This is the movie example
;; Feel free to change the schema and the data,
;; and run from your REPL

;; Let's create a movie schema
(def movie-schema [{:db/ident :movie/title
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The title of the movie"}

                   {:db/ident :movie/genre
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc "The genre of the movie"}

                   {:db/ident :movie/release-year
                    :db/valueType :db.type/long
                    :db/cardinality :db.cardinality/one
                    :db/doc "The year the movie was released in theaters"}])

;; Run the transaction
@(d/transact conn movie-schema)

;; Let's add some movies
(def first-movies [{:movie/title "The Goonies"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Commando"
                    :movie/genre "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title "Repo Man"
                    :movie/genre "punk dystopia"
                    :movie/release-year 1984}])

;; Run the transaction with these movies
@(d/transact conn first-movies)

;; Let's query the database 
;; NOTE: The Datomic database works similar
;; to a snapshot, so with `db` we're getting 
;; the current state of the database
(def db (d/db conn))

;; Let's query all the movies
(def all-movies-q '[:find ?e 
                    :where [?e :movie/title]])

(d/q all-movies-q db)

;; Let's query all the titles
(def all-titles-q '[:find ?movie-title 
                    :where [_ :movie/title ?movie-title]])

(d/q all-titles-q db)

;; Let's query all the titles from 1985
(def titles-from-1985 '[:find ?title 
                        :where [?e :movie/title ?title] 
                               [?e :movie/release-year 1985]])

(d/q titles-from-1985 db)

;; Let's query the movie with the title "Commando"
(d/q '[:find ?e 
              :where [?e :movie/title "Commando"]] 
            db)

;; Let's update the genre of the movie "Commando"
(def commando-id 
  (ffirst (d/q '[:find ?e 
                 :where [?e :movie/title "Commando"]] 
                db)))

@(d/transact conn [{:db/id commando-id :movie/genre "future governor"}])

;; Well, let's see the update...
(def all-data-from-1985
  '[:find ?e ?title ?genre ?year
    :where [?e :movie/title ?title]
           [?e :movie/genre ?genre]
           [?e :movie/release-year ?year]
           [(= ?year 1985)]])

(d/q all-data-from-1985 db)

;; "A database value is the state of the database at a given point in time.
;; You can issue as many queries against that database value as you want, 
;; they will always return the same results." 

;; Rerun this in your REPL:
(comment 
  (def db (d/db conn))
  )

(d/q all-data-from-1985 db) ;; Now the value is updated!
                            ;; It's similar to a snapshot

;; See the old database value
(def old-db (d/as-of db 1004))

(d/q all-data-from-1985 old-db) ;; It's a time travel to past

;; Let's see the history
(def hdb (d/history db))

(d/q '[:find ?genre 
       :where [?e :movie/title "Commando"]
              [?e :movie/genre ?genre]] 
      hdb)

;; To see more about historic data, check the Datomic documentation:
;; https://docs.datomic.com/pro/getting-started/see-historic-data.html

;; I hope you enjoyed this example! :)
