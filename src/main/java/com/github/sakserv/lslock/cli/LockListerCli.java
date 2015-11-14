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

public class LockListerCli {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(LockListerCli.class);

    private static HashMap<Integer, Integer> procLocksContents = new HashMap<>();

    public static void main(String[] args) {

        LOG.debug("Starting the LockLister");

        // Run compat checks
        if(!CompatUtils.runCompatChecks()) {
            LOG.error("Compat checks failed. Linux is required.");
            System.exit(1);
        }

        // Parse the command line args
        LockListerCliParser lockListerCliParser = new LockListerCliParser(args);
        lockListerCliParser.parse();

        // Get the lock directory
        File lockDirectory = lockListerCliParser.getLockDirectory();

        // List the locks
        try {

            // Load /proc/locks
            parseProcLocks();

            // List the locks
            printLocks(lockListerCliParser.getLockDirectory());

            // Sleep 5 seconds
            Thread.sleep(5000l);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOG.debug("LockLister finished");
    }

    public static void printLocks(File lockDirectory) throws IOException {
        System.out.printf("%-15s %15s %n", "PID", "PATH");

        for(File file: FileUtils.listFiles(lockDirectory, null, true)) {
            //LOG.debug("FILE: {} - Inode: {} - Pid: {}", file, getInode(file), procLocksContents.get(getInode(file)));
            System.out.printf("%-15s %15s %n", procLocksContents.get(getInode(file)), file);
        }
        System.out.println();
    }

    public static int getInode(File lockFile) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(lockFile.toPath(),BasicFileAttributes.class);
        Object fileKey = basicFileAttributes.fileKey();
        String fileAttrString = fileKey.toString();
        return Integer.parseInt(fileAttrString.substring(fileAttrString.indexOf("ino=") + 4,
                fileAttrString.indexOf(")")));
    }

    public static void parseProcLocks() throws IOException {

        procLocksContents.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(new File("/proc/locks")))) {
            for(String line; (line = br.readLine()) != null; ) {
                String[] lineSplit = line.split("\\s+");
                String[] fileIdSplit = lineSplit[5].split(":");
                Integer inode = Integer.parseInt(fileIdSplit[2]);
                Integer pid = Integer.parseInt(lineSplit[4]);
                procLocksContents.put(inode, pid);
            }
        }

    }
}
