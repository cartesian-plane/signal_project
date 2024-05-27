package com.main;

import com.cardiogenerator.HealthDataSimulator;
import com.data_management.DataStorage;
import java.io.IOException;
import java.util.Arrays;

public class Main {

  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
      // pass the rest of the args to the appropriate class
      String[] otherArgs = Arrays.copyOfRange(args,1, args.length);
      switch (args[0]) {
        case "DataStorage" -> DataStorage.main(otherArgs);
        case "HealthDataSimulator" -> HealthDataSimulator.main(otherArgs);
        default -> System.out.println("Invalid input");
      }
    }
  }
}
