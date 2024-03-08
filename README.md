# Datomic Getting Started <img src="https://docs.datomic.com/pro/impl/logo-160x128.png" align="right"/>
> _As of April 27, 2023, [Datomic Pro is free](https://blog.datomic.com/2023/04/datomic-is-free.html)!_

This repository contains the source code generated from [Datomic's Getting Started](https://docs.datomic.com/pro/getting-started/brief-overview.html), ready to be used alongside your REPL.

## Installing Datomic

You can download the zip running:

```
curl https://datomic-pro-downloads.s3.amazonaws.com/1.0.7075/datomic-pro-1.0.7075.zip -O
```

After that, don't forget to unzip this zip inside your directory of preference. I'm using `~/datomic`, so:

```
mkdir ~/datomic && unzip datomic-pro-1.0.7075.zip && mv ./datomic-pro-1.0.7075 ~/datomic/
```

Well, now we need to configure our transactor! In this repository you will find the `resources` directory. First of all, configure your SQL database. I'm using PostgreSQL:

```
...
# See https://docs.datomic.com/on-prem/storage.html

# The database name can be anything. I'm using "datomic" here.
sql-url=jdbc:postgresql://localhost:5432/datomic
sql-user=yourusername
sql-password=yourpassword
...
```

Copy the `resources/datomic_properties` into `~/datomic/datomic-pro-1.0.7075`:

```
cp -r ./resources/datomic_properties ~/datomic/datomic-pro-1.0.7075/
```

Then you can start your transactor with:

```
~/datomic/datomic-pro-1.0.7075/bin/transactor ~/datomic/datomic-pro-1.0.7075/datomic_properties/sql-transactor-template.properties
```

### If you got an error about a table which doesn't exists, just create it

To create the specific table, connect to your database and run:

```sql
CREATE TABLE datomic_kvs
(
 id text NOT NULL,
 rev integer,
 map text,
 val bytea,
 CONSTRAINT pk_id PRIMARY KEY (id )
)
WITH (
 OIDS=FALSE
);
```

Great! Now you can start evaluating!

Don't forget to see the [Datomic Documentation](https://docs.datomic.com/pro/getting-started/brief-overview.html)!
