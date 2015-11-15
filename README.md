lslock
------
lslock provides a means of viewing the process id's associated with file locks leveraging /proc/locks on Linux.

Requirements
------------
Tested with:
* Java 8
* docker 1.9.0

Standalone Usage
-----
As a result of the /proc/locks requirement, this is not portable beyond linux.

* Install docker
```
curl -sSL https://get.docker.com/ | sh
```

* Run the build
```
cd lslock && mvn clean package
```

* Run the script
```
cd lslock
bin/lslock-test target/lslock-test*jar-with-dependencies.jar &
bin/lslock target/lslock-*jar-with-dependencies.jar
```

Usage
-----
* lslock
```
# java -cp target/lslock-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.github.sakserv.lslock.cli.LockListerCli
[main] ERROR com.github.sakserv.lslock.cli.parser.LockListerCliParser - The directory option is required
usage: lslock
 -d,--directory <arg>   directory to check for locked files
 -h,--help              show help.
```

* lslock-test
```
# java -cp target/lslock-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.github.sakserv.lslock.cli.LockTakerCli
  [main] ERROR com.github.sakserv.lslock.cli.parser.LockTakerCliParser - The directory option is required
  usage: lslock-test
   -d,--directory <arg>    directory to lock files in
   -h,--help               show help.
   -l,--lockcount <arg>    number of files to lock
   -s,--sleeptimer <arg>   how long to sleep after taking the lock
```


Example using Docker
-------
The example project uses Spotify's [docker-maven-plugin](https://github.com/spotify/docker-maven-plugin) to run the example.

* Creating the docker image
```
cd lslock && mvn clean package
```

* Run the example
```
# docker run lslock
PID                        PATH
10              /tmp/lslock-test/lock-file-4/lock-file-4.lock
27              /tmp/lslock-test/lock-file-8/lock-file-8.lock
27              /tmp/lslock-test/lock-file-6/lock-file-6.lock
27              /tmp/lslock-test/lock-file-5/lock-file-5.lock
27              /tmp/lslock-test/lock-file-9/lock-file-9.lock
10              /tmp/lslock-test/lock-file-0/lock-file-0.lock
10              /tmp/lslock-test/lock-file-2/lock-file-2.lock
10              /tmp/lslock-test/lock-file-1/lock-file-1.lock
10              /tmp/lslock-test/lock-file-3/lock-file-3.lock
27              /tmp/lslock-test/lock-file-7/lock-file-7.lock
```

