package com.gigabit;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {

        //String fileName = "feb.asterisk";
        String fileName = args[0];
        String fileName2 = fileName + ".complete";

        int iter = Integer.parseInt(args[1]);

        ArrayList logs;

        Core core = new Core();
        core.convert(fileName, fileName2, iter);
        logs = core.createDataCollection(fileName2, iter);
        int res = core.summaryTimeCounter(logs);
        int[] out = core.outgoingTimeCounter(logs);

        int outSummary = out[0] + out[1] + out[2] + out[3];

        int calls[] = core.callsCounter(logs);
        core.reportCreator(res, outSummary, out, calls);
    }
}
class Core {
    public void convert(String fileName, String fileName2, int iter) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        FileWriter fileWriter = new FileWriter(fileName2, false);

        int i = 0;
        for (int j = 0; j < iter; j++)  {
            i++;
            String s = reader.readLine();
            if (s.contains(",t,,,")) {
                s = s.replace(",t,,,","-");
                fileWriter.write(s);
                fileWriter.write(System.lineSeparator());
            }
            else {
                fileWriter.write(s);
                fileWriter.write(System.lineSeparator());
            }
        }
        fileWriter.close();
        reader.close();
    }

    public ArrayList createDataCollection(String fileName, int iter) throws IOException {
        ArrayList <Log> arrayList = new ArrayList<Log>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        for (int i = 0; i < iter; i++) {
            String readLine = reader.readLine();

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

class Log {
    private String clid;                //""                                ""
    private String src;                 //"+380997717356"                   "8016"
    private String dst;                 //"+380660640093"                   "+380636805521"
    private String dcontext;            //"from-gsm"                        "internal"
    private String channel;             //""""" <+380997717356>"            """"" <8016>"
    private String srcchannel;          //"Dongle/MTS-SUP-0100000151"       "SIP/8016-00000ed9"
    private String dstchannel;          //"SIP/8006-00000fa5"               "Dongle/LIFE-010000003c"
    private String action;              //"Queue"                           "Dial"
    private String where;               //"support-420"                     "Dongle/LIFE/+380636805521"
    private String start;               //"2018-06-30 17:59:35"             "2018-06-30 14:45:02"
    private String answer;              //"2018-06-30 17:59:35"             "2018-06-30 14:45:14"
    private String end;                 //"2018-06-30 17:59:50"             "2018-06-30 14:46:38"
    private String duration;            //15                                95
    private String billsec;             //15                                83
    private String status;              //"NO ANSWER"                       "ANSWERED"
    private String accountcode;         //"DOCUMENTATION"                   "DOCUMENTATION"
    private String uniqueid;            //"1530369902.8942"                 "1530369902.8942"
    private String userfield;           //"+380660640093"                   "+380636805521"
    // In
    // "","+380997717356","+380660640093","from-gsm",""""" <+380997717356>","Dongle/MTS-SUP-0100000151",
    // "SIP/8006-00000fa5","Queue","support-420","2018-06-30 17:59:35","2018-06-30 17:59:35",
    // "2018-06-30 17:59:50",15,15,"NO ANSWER","DOCUMENTATION",,"+380660640093"

    // OUT
    //    "","8016","+380636805521","internal",""""" <8016>","SIP/8016-00000ed9",
    // "Dongle/LIFE-010000003c","Dial","Dongle/LIFE/+380636805521","2018-06-30 14:45:02","2018-06-30 14:45:14",
    // "2018-06-30 14:46:38",95,83,"ANSWERED","DOCUMENTATION","1530369902.8942","+380636805521"


    public String getClid() {
        return clid;
    }

    public void setClid(String clid) {
        this.clid = clid;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getDcontext() {
        return dcontext;
    }

    public void setDcontext(String dcontext) {
        this.dcontext = dcontext;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSrcchannel() {
        return srcchannel;
    }

    public void setSrcchannel(String srcchannel) {
        this.srcchannel = srcchannel;
    }

    public String getDstchannel() {
        return dstchannel;
    }

    public void setDstchannel(String dstchannel) {
        this.dstchannel = dstchannel;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBillsec() {
        return billsec;
    }

    public void setBillsec(String billsec) {
        this.billsec = billsec;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountcode() {
        return accountcode;
    }

    public void setAccountcode(String accountcode) {
        this.accountcode = accountcode;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getUserfield() {
        return userfield;
    }

    public void setUserfield(String userfield) {
        this.userfield = userfield;
    }

    public Log(String clid, String src, String dst, String dcontext, String channel, String srcchannel, String dstchannel, String action, String where, String start, String answer, String end, String duration, String billsec, String status, String accountcode, String uniqueid, String userfield) {
        this.clid = clid;
        this.src = src;
        this.dst = dst;

        this.dcontext = dcontext;
        this.channel = channel;
        this.srcchannel = srcchannel;
        this.dstchannel = dstchannel;
        this.action = action;
        this.where = where;
        this.start = start;
        this.answer = answer;
        this.end = end;
        this.duration = duration;
        this.billsec = billsec;
        this.status = status;
        this.accountcode = accountcode;
        this.uniqueid = uniqueid;
        this.userfield = userfield;
    }
}