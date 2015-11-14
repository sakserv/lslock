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
package com.github.sakserv.lslock.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class LockTakerRunnableThread implements Runnable {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(LockTakerRunnableThread.class);

    private File filePath;
    private int sleepTimer = 0;

    public LockTakerRunnableThread(File filePath, int sleepTimer) {
        this.filePath = filePath;
        this.sleepTimer = sleepTimer;
    }

    @Override
    public void run() {

        try {
            // Take out the exclusive lock, sleep, release the lock
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
            FileChannel fileChannel = randomAccessFile.getChannel();

            LOG.debug("Taking on lock for {} ms on {}", sleepTimer, filePath);
            FileLock fileLock = fileChannel.lock(0, Long.MAX_VALUE, true);
            Thread.sleep(sleepTimer);

            LOG.debug("Releasing lock on {}", filePath);
            fileLock.release();
            fileChannel.close();
            randomAccessFile.close();
        } catch(IOException e) {
            if(!filePath.isFile()) {
                LOG.error("File to lock was not found: {}", filePath);
            } else {
                LOG.error("Unable to acquire an exclusive lock on file {}", filePath);
            }
            e.printStackTrace();
        } catch(InterruptedException e) {
            LOG.error("Thread interrupted");
            e.printStackTrace();
        }

        filePath.delete();
    }

}
