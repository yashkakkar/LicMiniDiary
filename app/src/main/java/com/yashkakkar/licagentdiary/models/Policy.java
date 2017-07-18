package com.yashkakkar.licagentdiary.models;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Yash Kakkar on 22-05-2017.
 */

public class Policy implements Comparable<Policy>{

    private String memberId; // policy associated to policyholder --> member
    private String policyId; // p1,p2,p3....
    private String policyName;
    private String policyNumber;
    private String docDate;
    private String dlpDate;
    private String domDate;
    private String dobDate;
    private String fup;
    private String termTable;
    private String mode;
    private String saAmount;
    private String premiumAmount;
    private String nomineeName;
    private int marked;
    private int bookMarked;
    private int policyStatus; // LAPSED(0), LAPSING(1) , ACTIVE(2)(IN FORCE), MATURED(4), UNKNOWN(5)

    private SimpleDateFormat simpleDateFormat;
    public Policy(){

    }

    public Policy(String memberId, String policyId, String policyName, String policyNumber, String docDate, String dlpDate, String domDate, String dobDate,String fup, String termTable, String mode, String saAmount, String premiumAmount, String nomineeName) {
        this.memberId = memberId;
        this.policyId = policyId;
        this.policyName = policyName;
        this.policyNumber = policyNumber;
        this.docDate = docDate;
        this.dlpDate = dlpDate;
        this.domDate = domDate;
        this.dobDate = dobDate;
        this.fup = fup;
        this.termTable = termTable;
        this.mode = mode;
        this.saAmount = saAmount;
        this.premiumAmount = premiumAmount;
        this.nomineeName = nomineeName;
    }

    public Policy(String memberId, String policyId, String policyName, String policyNumber, String docDate, String dlpDate, String domDate, String dobDate,String fup, String termTable, String mode, String saAmount, String premiumAmount, String nomineeName, int marked, int bookMarked, int policyStatus) {
        this.memberId = memberId;
        this.policyId = policyId;
        this.policyName = policyName;
        this.policyNumber = policyNumber;
        this.docDate = docDate;
        this.dlpDate = dlpDate;
        this.domDate = domDate;
        this.dobDate = dobDate;
        this.fup = fup;
        this.termTable = termTable;
        this.mode = mode;
        this.saAmount = saAmount;
        this.premiumAmount = premiumAmount;
        this.nomineeName = nomineeName;
        this.marked = marked;
        this.bookMarked = bookMarked;
        this.policyStatus = policyStatus;
    }

    public int calculateAgeFromDob(String dobDate){
        int years = 0;
        int months = 0;
        // int days = 0;

        Calendar now = Calendar.getInstance();
        // extract date, month , year for dobdate
        String d = dobDate;
        String date[] = d.split("/");
        int dobDay = Integer.parseInt(date[0]);
        int dobMonth = Integer.parseInt(date[1]);
        int dobYear = Integer.parseInt(date[2]);
        // GET current date, month, year
        int currentDate = now.get(Calendar.DATE);
        int currentMonth = now.get(Calendar.MONTH)+1;
        int currentYear = now.get(Calendar.YEAR);

        // get difference between month
        months = currentMonth - dobMonth;

        // get difference between year
        years = currentYear - dobYear;

        //if month difference is in negative then reduce years by one and calculate the number of months.
        if( months < 0){
            years--;
            months = 12 - dobMonth + currentMonth;
            if(currentDate < dobDay)
                months--;
        }else if( months == 0 && currentDate < dobDay){
            years--;
            months = 11;
        }

        // calculate the days
        return years;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getDlpDate() {
        return dlpDate;
    }

    public void setDlpDate(String dlpDate) {
        this.dlpDate = dlpDate;
    }

    public String getDomDate() {
        return domDate;
    }

    public void setDomDate(String domDate) {
        this.domDate = domDate;
    }

    public String getDobDate() {
        return dobDate;
    }

    public void setDobDate(String dobDate) {
        this.dobDate = dobDate;
    }

    public String getFup() {
        return fup;
    }

    public void setFup(String fup) {
        this.fup = fup;
    }

    public String getTermTable() {
        return termTable;
    }

    public void setTermTable(String termTable) {
        this.termTable = termTable;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSaAmount() {
        return saAmount;
    }

    public void setSaAmount(String saAmount) {
        this.saAmount = saAmount;
    }

    public String getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(String premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public int getMarked() {
        return marked;
    }

    public void setMarked(int marked) {
        this.marked = marked;
    }

    public int getBookMarked() {
        return bookMarked;
    }

    public void setBookMarked(int bookMarked) {
        this.bookMarked = bookMarked;
    }

    public int getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(int policyStatus) {
        this.policyStatus = policyStatus;
    }

    public Date getDocInDateFormat() throws ParseException {
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return simpleDateFormat.parse(docDate);
    }

    @Override
    public int compareTo(@NonNull Policy p) {
        try {
            return getDocInDateFormat().compareTo(p.getDocInDateFormat());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
