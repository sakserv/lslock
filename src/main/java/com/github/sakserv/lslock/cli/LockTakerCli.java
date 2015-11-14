/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.sakserv.lslock.cli;

import com.github.sakserv.lslock.cli.parser.LockTakerCliParser;
import com.github.sakserv.lslock.threads.LockTakerRunnableThread;
import com.github.sakserv.lslock.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LockTakerCli {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(LockListerCli.class);

    public static void main(String[] args) {
        LOG.debug("Starting the LockTaker");

        // Parse the command line args
        LockTakerCliParser lockTakerCliParser = new LockTakerCliParser(args);
        lockTakerCliParser.parse();

        // Set up variables
        File lockDirectory = lockTakerCliParser.getLockDirectory();
        int lockCount = lockTakerCliParser.getLockCount();
        int sleepTimer = lockTakerCliParser.getSleepTimer();

        // Create the thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(lockCount);
        int i;
        for (i = 0; i < lockCount; i++) {

            File lockFile = getLockFilePath(lockDirectory, i);
            if(lockFile.isFile()) {
                i += lockCount;
                lockCount += lockCount;
            }
            lockFile = getLockFilePath(lockDirectory, i);

            // Create the parentDirectories if necessary
            if(!lockFile.getParentFile().isDirectory()) {
                lockFile.getParentFile().mkdirs();
            }

            // Create an empty lock file, exit if it fails
            try {
                FileUtils.createEmptyFile(lockFile);
            } catch (IOException e) {
                LOG.error("Could not create {}", lockFile);
                System.exit(1);
            }

            // Start the thread that will acquire the lock
            executorService.execute(new LockTakerRunnableThread(lockFile, sleepTimer));
        }

        LOG.debug("All LockTaker Threads Started");
        // Shutdown the thread pool
        executorService.shutdown();
    }

    public static File getLockFilePath(File lockDirectory, int i) {
        // Build up file to lock
        StringBuffer sb = new StringBuffer();
        sb.append(lockDirectory.getAbsolutePath());
        sb.append("/lock-file-");
        sb.append(i);
        sb.append("/lock-file-");
        sb.append(i);
        sb.append(".lock");
        return new File(sb.toString());
    }


    }
