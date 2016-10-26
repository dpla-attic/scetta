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