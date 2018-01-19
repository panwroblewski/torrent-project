# School project for PJWSTK SKJ 
# TorrentProject
**https://github.com/panwroblewski/TorrentProject**

Instructions:
-
- Each instance of application has it's own default ./downloads, ./assets and ./assets/files folders, which can be changed in application.conf
- If you would like to run more then one instance just duplicate containig folder and run command inside the app you want to run:
````
java -jar TorrentProject.jar
````
- Paths and globals can be configured in conf/application.conf - Application specific
- You can configure a list of available host which applications will try to connect to in assets/hosts.txt - This file needs to be the same in every instance, so instances know about each other

**API**

- all API query parameters have to be splitted with space
- "=" sign is a key/value separator
- FEATURE! files in downloads folder cannot have spaces in names!!! :)

Possible queries:

- ping
- list
- pull
- push
- exit

parameters:

- host=ip:port
- file=_file_number_
- downloadFromByte=d+
- retransmitTo=ip:host

**API Request regex:**
````
"(list|push|pull|ping|exit)\s*(file=\d+)?\s*(host=[a-zA-Z0-9./]*:[\d]*)?\s*(downloadFromByte=\d+)?\s*(retransmitTo=[a-zA-Z0-9./]*:[\d]*)?\s*"
````
e.g.

- list
- list host=localhost:16900
- pull file=0 host=localhost:16901

If no host is specified, then first available host from known will be chosen to connect to.

**CONF**

- _conf/application.conf_ - specifies all env variables (You can turn on/off logs and transmission error mock for test)
- _assets/hosts.txt_ - All known hosts routing, when app starts it tries to open socket on each consecutive port from table, if a port is busy it tries another etc.
- _applicationLog.txt_ - when app starts for the first time the file is created. All logs are streamed to this file. Logs file can be dynamically viewed through HttpServer

**HttpServer**

- Logs can be viewed on _http://localhost:17000/log_ 



Adam Wróblewski