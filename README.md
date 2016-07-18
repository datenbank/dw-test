# Introduction
The Data Warehouse Test Toolkit enables you to test your data and ETL rutines in a structured manner. By compareing two queries (source and target) a test case is executed and differences are reported. The build in scripting functionality helps you to automate the task of writing trivial test cases. Also common test case scenarios are provided to get started, and autogenerate test cases for your setup.

Requirements:
* Java
* JDBC drivers of choice (jtds and oracle included in build)

The Toolkit is written Groovy, hence is able to run on any Java platform and connect to databases supported by JDBC.

Build with Gradle:   

     >gradlew fatJar

Run the application, and display the usage options:
  
    >test.bat -h

Or equalivant:

    >java -jar ./build/lib/dw-test.jar -h


# Configure

The conf.txt file let you setup connections, and driver to source and target databases. Also the model name can be setup. The file holds information about tables, columns and mapping between source and target in your environment. This is used to generate test cases through the scripts placed in the Script folder. Some common test cases are all ready provided for you, but you can ude this facility to implement your own tests. The path is a reference to the install directory.

    path=C:/Users/kha/Desktop/dw-test/
    model=model.csv
    source=jdbc:jtds:sqlserver://localhost:1433/databaseName;instance=MSSQL2014;user=test;password=testtest
    target=jdbc:jtds:sqlserver://localhost:1433/databaseName;instance=MSSQL2014;user=test;password=testtest
    sourceDriver=net.sourceforge.jtds.jdbc.Driver
    targetDriver=net.sourceforge.jtds.jdbc.Driver


# Test Cases
The following will describe how a test case is made.
## Queries
There is Source folder where queries for the source connection is put. The Target folder holds queries for the target connection. A test case is a set of queries with the same name for example test1.sql, put in both the Target and the Source folder holding a query to create a specific output. Each query is executed upon request and the result is put in a file (.csv) in the subdirectory Result for both the Source and Target folder.

Then at the compare phase the result files of a test case is compared, and any differences are reported.

## Hooks
Before and after a target query is made a commandline script can be executed by naming the .bat file [query file name]_Before.bat or [query file name]_After.bat

This can be used to setup and cleanup after a test. ETL rutines can be started to establish a certain condition etc.
## Static Test Data
If no source database exists or the test depends on static data, just put the data in the /Source/Result folder with the name [query file name].csv then the file can be used in the compare/test phase of the toolkit.

# Scripts
TODO...    
