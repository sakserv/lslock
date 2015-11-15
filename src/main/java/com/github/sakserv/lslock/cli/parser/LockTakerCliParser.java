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
package com.github.sakserv.lslock.cli.parser;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LockTakerCliParser extends AbstractCliParser {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(LockTakerCliParser.class);

    // Properties
    private String[] args;
    private Options options = new Options();
    private File lockDirectory;
    private int lockCount;
    private int sleepTimer;
    private static final String NAME = "lslock-test";

    /**
     * Sets the command line args and parses
     * those that were passed in.
     * @param args      Command line arguments
     */
    public LockTakerCliParser(String[] args) {
        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("d", "directory", true, "directory to lock files in");
        options.addOption("l", "lockcount", true, "number of files to lock");
        options.addOption("s", "sleeptimer", true, "how long to sleep after taking the lock");

        parse();
    }

    /**
     * Parses the passed in command line arguments
     */
    @Override
    public void parse() {

        CommandLineParser commandLineParser = new DefaultParser();

        try {
            CommandLine commandLine = commandLineParser.parse(options, args);

            // Handle the help option
            if (commandLine.hasOption("h")) {
                help(options, NAME);
            }

            // Handle the directory
            if (commandLine.hasOption("d")) {
                lockDirectory = new File(commandLine.getOptionValue("d"));
            } else {
                LOG.error("The directory option is required");
                help(options, NAME);
            }

            // Handle the lock file count option
            if (commandLine.hasOption("l")) {
                lockCount = Integer.parseInt(commandLine.getOptionValue("l"));
            } else {
                lockCount = 5;
            }

            // Handle the sleep timer option
            if (commandLine.hasOption("s")) {
                sleepTimer = Integer.parseInt(commandLine.getOptionValue("s"));
            } else {
                sleepTimer = 20000;
            }

        } catch (ParseException e) {
            LOG.error("Failed to parse command line args");
            help(options, NAME);
        }
    }


    public File getLockDirectory() {
        return lockDirectory;
    }

    public int getLockCount() {
        return lockCount;
    }

    public int getSleepTimer() {
        return sleepTimer;
    }
}
