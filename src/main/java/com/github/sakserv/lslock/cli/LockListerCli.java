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

import com.github.sakserv.lslock.cli.parser.LockListerCliParser;
import com.github.sakserv.lslock.util.CompatUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

/**
 * CLI and main driver for the LockLister
 * Reads /proc/locks and the files in the supplied
 * directory and joins them on inode.
 *
 * The final output contains the pid of the process
 * that is holding on to the file based lock
 */
public class LockListerCli {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(LockListerCli.class);

    // Properties
    // The hashmap provides the contents of /proc/locks
    // as <inode, pid>
    private static HashMap<Integer, Integer> procLocksContents = new HashMap<>();


    /**
     * Main driver
     * @param args      Command line args
     */
    public static void main(String[] args) {

        LOG.debug("Starting the LockLister");

        // Run compat checks
        if(!CompatUtils.runCompatChecks()) {
            LOG.error("Compat checks failed. Linux is required.");
            System.exit(1);
        }

        // Parse the command line args
        LockListerCliParser lockListerCliParser = new LockListerCliParser(args);

        // List the locks
        try {

            // Load /proc/locks
            // The hashmap provides the contents of /proc/locks
            // as <inode, pid>
            HashMap<Integer, Integer> procLocksContents = parseProcLocks();

            // List the locks
            printLocks(lockListerCliParser.getLockDirectory(), procLocksContents);

        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.debug("LockLister finished");
    }

    /**
     * Prints the final output for the locks
     *
     * @param lockDirectory     Directory to recurse for lock files
     * @throws IOException      If the lockdirectory is missing
     */
    public static void printLocks(File lockDirectory, HashMap<Integer, Integer> procLocksContents) throws IOException {
        System.out.printf("%-15s %15s %n", "PID", "PATH");

        for(File file: FileUtils.listFiles(lockDirectory, null, true)) {
            System.out.printf("%-15s %15s %n", procLocksContents.get(getInode(file)), file);
        }
        System.out.println();
    }

    /**
     * Returns the inode of the supplied File
     *
     * @param lockFile      Retrieve inode for this File
     * @return      The inode of the supplied lockFile
     * @throws IOException      If the File can not be found
     */
    public static int getInode(File lockFile) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(lockFile.toPath(),BasicFileAttributes.class);
        Object fileKey = basicFileAttributes.fileKey();
        String fileAttrString = fileKey.toString();
        return Integer.parseInt(fileAttrString.substring(fileAttrString.indexOf("ino=") + 4,
                fileAttrString.indexOf(")")));
    }

    /**
     * Loads the procLocksConents HashMap with <inode,pid>
     *
     * @throws IOException      if /proc/locks doesn't exist
     */
    public static HashMap<Integer, Integer> parseProcLocks() throws IOException {

        // The hashmap provides the contents of /proc/locks
        // as <inode, pid>
        HashMap<Integer, Integer> procLocksContents = new HashMap<>();

        // Clear the HashMap
        procLocksContents.clear();

        // Read /proc/locks, load the HashMap
        try(BufferedReader br = new BufferedReader(new FileReader(new File("/proc/locks")))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] lineSplit = line.split("\\s+");
                String[] fileIdSplit = lineSplit[5].split(":");
                Integer inode = Integer.parseInt(fileIdSplit[2]);
                Integer pid = Integer.parseInt(lineSplit[4]);
                procLocksContents.put(inode, pid);
            }
        }

        return procLocksContents;

    }
}
