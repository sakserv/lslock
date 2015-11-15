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
package com.github.sakserv.lslock.util;

import org.slf4j.LoggerFactory;

import java.io.File;

public class CompatUtils {

    // Logger
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CompatUtils.class);

    // Properties
    private static final File procLocksPath = new File("/proc/locks");

    /**
     * Determine if we are running on Linux, as this is a requirement.
     *
     * @return      true if running on Linux, false otherwise
     */
    public static boolean isLinux() {
        if (System.getProperty("os.name").startsWith("Linux")) {
            return true;
        }
        return false;
    }


    /**
     * Check if the /proc/locks file exists, as this is needed
     * to determine which locks are held.
     *
     * @return      true if the /proc/locks file exists
     */
    public static boolean procLocksExists() {
        if(procLocksPath.isFile()) {
            return true;
        }
        return false;
    }

    /**
     * Run the consolidated list of compatibility tests
     * @return      true if compatible, false if not
     */
    public static boolean runCompatChecks() {
        // Validate running on Linux
        if(!CompatUtils.isLinux()) {
            LOG.error("Only Linux is supported");
            return false;
        }

        // Validate that /proc/locks exists
        if(!CompatUtils.procLocksExists()) {
            LOG.error("/proc/locks does not exist, fatal error");
            return false;
        }
        return true;
    }

}
