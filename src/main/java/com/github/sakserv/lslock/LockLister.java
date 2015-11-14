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
package com.github.sakserv.lslock;

import com.github.sakserv.lslock.cli.LockListerCliParser;
import com.github.sakserv.lslock.util.CompatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LockLister {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(LockLister.class);

    public static void main(String[] args) {

        // Validate running on Linux
        if(!CompatUtils.isLinux()) {
            LOG.error("Only Linux is supported");
            System.exit(1);
        }

        // Validate that /proc/locks exists
        if(!CompatUtils.procLocksExists()) {
            LOG.error("/proc/locks does not exist, fatal error");
            System.exit(1);
        }

        // Parse the command line args
        LockListerCliParser lockListerCliParser = new LockListerCliParser(args);

        // List the locks
        listLocks(lockListerCliParser.getLockDirectory());
    }

    public static void listLocks(File lockDirectory) {

    }
}
