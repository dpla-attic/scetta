# README

## Data
Original records for California Digital Library are available [here](https://s3.amazonaws.com/dpla-heidrun-orignal-records/cdl/cdl_original_record.tar.gz)

## Prerequisites 

* You need Java 8 of some variety installed.
* `brew install sbt`


## Building

`sbt clean package`

## Running

`sbt "run-main la.dp.scetta.StandaloneMain /Users/michael/Downloads/cdl /Users/michael/Downloads/cdl-output"`

The first time you do this, if you haven't run any sort of build ahead of time, `sbt` will download a scala install and
a bunch of dependencies. That's not part of the normal operation of the code, so don't bother timing that if it happens.

Please note that I did nothing to make this user-friendly. Both of these directories must exist and IN_DIRECTORY should 
be full of CDL JSON files. It does filter for files that end in `.json`, however.

There are some warnings about exhaustiveness checks. Those are actually cool because it's Scala's typesystem warning us 
that we haven't handled all the potential cases for matches against the JSON path expressions. Leaving them in because 
they're illustrative of Scala's type checking.

The output you should see on the CLI is the total runtime, the number of files processed, and a list of files with 
problems and their stacktraces, if any.

## Spark Version

The Sequencefile version of our data is in S3 here: s3://dpla-mdpdb/cdl.seq

Basically, the only step required to generate that file was to point the `tar-to-seq` program available 
[here](https://stuartsierra.com/2008/04/24/a-million-little-files) at it. The process took 46 seconds on my laptop.

A Sequencefile is a record-based, splittable file format that comes from the Hadoop world. Each "line" in that file 
is a key that's the original filename, and a value that contains the bytes of each file. The whole file is split into 
blocks and compressed internally, which means that software like Hadoop and Spark can split it into chunks that 
represent batches of documents and operate over them with separate workers, while never having to unpack the file on a 
local filesystem. This makes loading it into memory quicker as well, since the IO can be pipelined.

Spark can be run in a local mode that only uses the software embedded in the project, so no install should be necessary.
Just do:

`sbt "run-main la.dp.scetta.SparkMain local[4] <infile.seq> <outfile.seq>"`

The parameter `local[4]` is the identifier for the Spark cluster you're trying to connect to. In this case, we're
specifying one embedded in the launching process by saying `local`, and we're telling it to use 4 workers with `[4]`. 
In practice, we could be running this on a cluster of multiple machines that we get Amazon EMR to launch purely by 
specifying the master node in place of this parameter.

If you catch it while it's running, there's a Spark dashboard available at 
[http://localhost:4040/jobs/](http://localhost:4040/jobs/).