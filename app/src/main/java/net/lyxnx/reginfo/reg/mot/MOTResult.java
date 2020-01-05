//package net.lyxnx.reginfo.reg.mot;
//
//import java.io.Serializable;
//import java.util.List;
//
//public class MOTResult implements Serializable {
//    private final String mileage;
//    private final String date;
//    private final List<String> refusalNotices;
//    private final List<String> advisoryNotices;
//
//    public MOTResult(String mileage, String date, List<String> refusalNotices, List<String> advisoryNotices) {
//        this.mileage = mileage;
//        this.date = date;
//        this.refusalNotices = refusalNotices;
//        this.advisoryNotices = advisoryNotices;
//    }
//
//    public String getMileage() {
//        return mileage;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public List<String> getRefusalNotices() {
//        return refusalNotices;
//    }
//
//    public List<String> getAdvisoryNotices() {
//        return advisoryNotices;
//    }
//
//    public boolean isPass() {
//        return getRefusalNotices().isEmpty(); // passed if no refusal notices
//    }
//}