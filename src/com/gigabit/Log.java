package com.gigabit;

public class Log {
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

    @Override
    public String toString(){
        return this.clid + ";" + this.src + ";" +  this.dst+ ";" + this.dcontext+ ";" + this.channel + ";" + this.srcchannel + ";" + this.dstchannel
                + ";" + this.action+ ";" + this.where + ";" + this.start+ ";" + this.answer+ ";" + this.end + ";" + this.duration
                + ";" + this.billsec + ";" + this.status + ";" + this.accountcode + ";" + this.uniqueid + ";" + this.userfield;

    }

    public String outgoingProvider(){
        if (this.where.contains("Dongle") && this.where.contains("VDF") ||
                this.where.contains("Dongle") && this.where.contains("Vodafone") ||
                this.where.contains("Dongle") && this.where.contains("MTS"))
            return "MTS";

        else if (this.where.contains("Dongle") && this.where.contains("KS") ||
                this.where.contains("Dongle") && this.where.contains("KYIVSTAR"))
            return "KS";

        else if (this.where.contains("Dongle") && this.where.contains("LIFE"))
            return "Life";

        else if (this.where.contains("SIP/intertelecom"))
            return "Intertelecom";

        else
            return "dafaq";
    }
}
