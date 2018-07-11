package com.gigabit;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {

        String fileName = args[0];
        int iter = Integer.parseInt(args[1]);

        ArrayList logs;
        Core core = new Core();

        logs = core.createDataCollection(fileName, iter);
        int res = core.summaryTimeCounter(logs);
        int[] out = core.outgoingTimeCounter(logs);

        int outSummary = out[0] + out[1] + out[2] + out[3];

        int calls[] = core.callsCounter(logs);
        core.reportCreator(res, outSummary, out, calls);
    }
}
