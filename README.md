# HALA - Httpd Access Log Analyzer

### Overview

A CLI that imports httpd access logs in default format, converts them to json or csv, lookup ip addresses and give statistics on usage (see goaccess.io).  Stores converted data to MongoDB (possibly by day).

### MVP Goals
1. Menu will have these choices:
 - [ ] help    
 - [ ] import (as CSV) 
 - [ ] analyze (basic stats)
 - [ ] export/save data to database   
 - [ ] load data that is in database 
 - [ ] exit
2. **Help** and **exit** functions work
3. CSV pre-formatted seed data (httpd access log) can be loaded
4. Data can be analyzed
5. Data can be displayed as a table or list
6. Data can be exported as objects to the database
7. Data can be loaded as a collection of objects from the database

## Stretch Goals

1. Import JSON
2. Convert from standard log format
3. Add more stats/analysis
4. Switch to import and export to db automatically
5. Improve help files

## User Stories

1. As a user I can use the command to import my log file that is already in JSON/CSV format and analyze it.
2. As a user I can export my data to the database
3. As a user I can reload data I have saved to the database

## Database 

1. MVP database storage will store entries as objects that place the data in members/values of the object.
2. MVP database will retrieve all entries from the database and add to a collection
3. Stretch - import objects using filtering (dates, country, etc)

---

## **Milestones**

#### I. Basic CLI functionality
1. Methods are written for each command option.
2. Basic file I/O is working.
3. Help and Exit is working.
4. Sample data is created in CSV format
5. CSV Data can be read to a collection

#### II. Manipulate data
1. Data objects analyzed and stats calculated 
2. App outputs stats table 

#### III. Export and Import to/from DB
1. Export objects to DB
2. Import objects from DB

#### IV. MVP Achieved
#### V. Stretch Goals
#### VI. Fin.

---

> Written with [StackEdit](https://stackedit.io/).
