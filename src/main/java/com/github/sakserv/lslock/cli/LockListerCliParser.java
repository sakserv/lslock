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

import com.github.sakserv.lslock.LockLister;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LockListerCliParser {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(LockListerCliParser.class);

    private String[] args;
    private Options options = new Options();
    private File lockDirectory;

    public LockListerCliParser(String[] args) {
        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("d", "directory", true, "directory to check for locked files");
    }

    public void parse() {
        CommandLineParser commandLineParser = new DefaultParser();

        try {
            CommandLine commandLine = commandLineParser.parse(options, args);

            if(commandLine.hasOption("h")) {
                help();
            }

            if(commandLine.hasOption("d")) {
                lockDirectory = new File(commandLine.getOptionValue("v"));
                if(!lockDirectory.isDirectory()) {
                    LOG.error("The specified directory {} does not exist!", lockDirectory.getAbsolutePath());
                    help();
                }
            } else {
                LOG.error("The directory option is required");
                help();
            }

        } catch (ParseException e) {
            LOG.error("Failed to parse command line args");
            help();
        }
    }

    public void help() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
        System.exit(1);
    }

    public File getLockDirectory() {
        return lockDirectory;
    }
}
