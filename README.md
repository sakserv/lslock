lslock
------
lslock provides a means of viewing the processes associated with file locks leveraging /proc/locks on Linux.

Requirements
------------
Tested with:
* Java 8
* docker 1.9.0

Using on Linux
-----
As a result of the /proc/locks requirement, this is not portable.

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
cd lslock && bin/lslock-test target/lslock-test*jar-with-dependencies.jar &
cd lslock && bin/lslock-test target/lslock-*jar-with-dependencies.jar
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

