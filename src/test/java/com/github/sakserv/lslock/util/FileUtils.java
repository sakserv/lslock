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
import java.io.IOException;

public class FileUtils {

    // Logger
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Recursively delete the directory
     * @param directory
     */
    public static void deleteDirectory(File directory) {
        String directoryAbsPath = directory.getAbsolutePath();

        LOG.info("FILEUTILS: Deleting contents of directory: {}", directoryAbsPath);

        File[] files = directory.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    f.setWritable(true);
                    deleteDirectory(f);
                } else {
                    LOG.info("FILEUTILS: Deleting file: {}", f.getAbsolutePath());
                    f.setWritable(true);
                    f.delete();
                }
            }
        }
        LOG.info("FILEUTILS: Deleting file: {}", directory.getAbsolutePath());
        directory.setWritable(true);
        directory.delete();
    }

    /**
     * Create an empty file at the specified path
     * @param filePath      File object representing the path to the file to be created
     * @throws IOException      If the file cannot be created, throw IOException
     */
    public static void createEmptyFile(File filePath) throws IOException {
        filePath.getParentFile().mkdirs();
        filePath.createNewFile();
    }

}
