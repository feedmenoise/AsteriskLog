package com.gigabit;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Core {

    public ArrayList createDataCollection(String fileName, int iter) throws IOException {
        ArrayList <Log> arrayList = new ArrayList<Log>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        for (int i = 0; i < iter; i++) {
            String readLine = reader.readLine();
            readLine = readLine.replace(",t,,,","-");
            String[] s = readLine.split(",");
            try {
                Log list = new Log(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], s[9], s[10], s[11], s[12], s[13], s[14], s[15], s[16], s[17]);
                arrayList.add(list);
            } catch (Exception e) {
                String error = "Error from collection creation: " + e + " in line: ";
                log(error);
                log(readLine);
            }
        }

        reader.close();
        return arrayList;
    }

    public int summaryTimeCounter(ArrayList<Log> arrayList) throws IOException {

        int sum = 0;
        for (Log i:arrayList) {
            try {
                String value = i.getDuration().replaceAll("\"","");
                int s = Integer.parseInt(value);
                if (s > 7200) {
                    String error = "something wrong with: " + i.getUniqueid();
                    log(error);
                } else
                    sum += s;
            } catch (Exception e) {
                try {String value = i.getStatus().replace("\"","");
                    int s = Integer.parseInt(value);
                    if (s > 7200) {
                        String error = "something wrong with: " + i.getUniqueid();
                        log(error);
                    } else
                        sum += s;
                } catch (Exception er) {
                    String error = "Error from summaryTimeCounter: " + er + " in line:";
                    log(error);
                    String problemString = i.getClid() + ";" + i.getSrc() + ";" +  i.getDst()+ ";" + i.getDcontext()+ ";" + i.getChannel()+ ";" + i.getSrcchannel()+ ";" + i.getDstchannel()
                            + ";" + i.getAction()+ ";" + i.getWhere()+ ";" + i.getStart()+ ";" + i.getAnswer()+ ";" + i.getEnd()+ ";" + i.getDuration()
                            + ";" + i.getBillsec()+ ";" + i.getStatus()+ ";" + i.getAccountcode()+ ";" + i.getUniqueid()+ ";" + i.getUserfield();
                    log(problemString);
                }
            }
        }
        return sum;
    }

    public int[] outgoingTimeCounter(ArrayList<Log> arrayList) throws IOException{

        int[] arr = new int[4];
        arr[0] = 0; //MTS
        arr[1] = 0; //KYIVSTAR
        arr[2] = 0; //LIFE
        arr[3] = 0; //INTERTELECOM
        for (Log i:arrayList) {

            if (i.getWhere().contains("Dongle") && i.getWhere().contains("VDF") ||
                    i.getWhere().contains("Dongle") && i.getWhere().contains("Vodafone") ||
                    i.getWhere().contains("Dongle") && i.getWhere().contains("MTS")){
                arr[0] += counter(i);
            }
            else if (i.getWhere().contains("Dongle") && i.getWhere().contains("KS") ||
                    i.getWhere().contains("Dongle") && i.getWhere().contains("KYIVSTAR")) {
                arr[1] += counter(i);
            }

            else if (i.getWhere().contains("Dongle") && i.getWhere().contains("LIFE")) {
                arr[2] += counter(i);
            }

            else if (i.getWhere().contains("SIP/intertelecom")) {
                arr[3] += counter(i);
                //System.out.println("Intertelecom minutes: " + i.getDuration() + " from " + i.getUniqueid());
            }

            else {
                //System.out.println("STRANNO VRODE VSE NORM DOLZHNO BITCH");
                //System.out.println(i.getWhere());
            }
        }

        return arr;
    }

    public int[] callsCounter(ArrayList<Log> logs) {
        Set<String> unique = new LinkedHashSet<>();
        int[] res = new int [3];

        for (Log i : logs) {
            if (i.getUniqueid().contains("DOCUM"))
                unique.add(i.getUserfield());
            else
                unique.add(i.getUniqueid());
        }

        int answeredSumm = 0;
        for (String j : unique) {
            for (Log k : logs) {
                if (k.getUniqueid().contains("DOCUM")) {
                    if (k.getUserfield().equals(j) && k.getAccountcode().contains("ANSWERED")){
                        answeredSumm++;

                    }
                }
                else {
                    if (k.getUniqueid().equals(j) && k.getStatus().contains("ANSWERED")) {
                        answeredSumm++;
                    }
                }
            }
        }

        int notAnswered = unique.size() - answeredSumm;

        res[0] = unique.size();
        res[1] = answeredSumm;
        res[2] = notAnswered;

        return res;

    }

    public int counter(Log i) throws IOException{
        try {
            String value = i.getDuration().replaceAll("\"","");
            int s = Integer.parseInt(value);

            if (s > 7200) {
                String error = "something wrong with: " + i.getUniqueid();
                log(error);
                return 0;
            }
            else
                return s;
        } catch (Exception e) {

            String value = i.getStatus().replace("\"","");

            int s = Integer.parseInt(value);
            if (s > 7200) {
                String error = "something wrong with: " + i.getUniqueid();
                log(error);
                return 0;
            }
            else
                return s;
        }
    }

    public void log(String s) throws IOException {

        FileWriter fileWriter = new FileWriter("AsteriskLogLogs", true);
        Date date = new Date();
        fileWriter.write(date.toString() + ": " + s);
        fileWriter.write(System.lineSeparator());
        fileWriter.close();

    }

    public void reportCreator(int res, int outSummary, int[] out, int[] calls) throws IOException {

        FileWriter fileWriter = new FileWriter("AsteriskLogReport", true);
        Date date = new Date();

        fileWriter.write("-----------" + date.toString() + "-----------");
        fileWriter.write(System.lineSeparator());
        fileWriter.write("Incoming: " + ((float) res - (float) outSummary) / 60);
        fileWriter.write(System.lineSeparator());
        fileWriter.write("Outgoing: " + (float) outSummary / 60);
        fileWriter.write(System.lineSeparator());
        fileWriter.write("Outgoing MTS: " + (float) out[0] / 60);
        fileWriter.write(System.lineSeparator());
        fileWriter.write("Outgoing KS: " + (float) out[1] / 60);
        fileWriter.write(System.lineSeparator());
        fileWriter.write("Outgoing Life: " + (float) out[2] / 60);
        fileWriter.write(System.lineSeparator());
        fileWriter.write("Outgoing Intertelecom: " + (float) out[3] / 60);
        fileWriter.write(System.lineSeparator());
        fileWriter.write("Summary calls: " + calls[0]);
        fileWriter.write(System.lineSeparator());
        fileWriter.write("Answered calls: " + calls[1]);
        fileWriter.write(System.lineSeparator());
        fileWriter.write("No answered: " + calls[2]);
        fileWriter.write(System.lineSeparator());
        fileWriter.close();

        System.out.println("Done");
    }
}
