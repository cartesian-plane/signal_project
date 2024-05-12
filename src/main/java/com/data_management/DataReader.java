package com.data_management;

import java.io.File;
import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @param source source file to read from
     * @throws IOException if there is an error reading the data
     */
    void readData(File source, DataStorage dataStorage) throws IOException;
}
