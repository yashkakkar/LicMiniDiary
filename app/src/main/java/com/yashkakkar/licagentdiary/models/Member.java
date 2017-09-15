package com.yashkakkar.licagentdiary.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yash Kakkar on 17-03-2017.
 *
 *
 */


public class Member implements Parcelable{
    private String memberId;
    private String memberName;
    private String memberPhoneNumber;
    private String memberEmailId;
    private String memberGender;
    private String memberImagePath;
    private String memberImageName;
    private byte[] memberImageFile;
    private int memberFav;

    /**
     * A constructor that initializes the Member object
     **/
    public Member(){

    }

    public Member(String memberId, String memberName, String memberPhoneNumber, String memberEmailId, String memberGender, String memberImagePath, String memberImageName, byte[] memberImageFile, int memberFav) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPhoneNumber = memberPhoneNumber;
        this.memberEmailId = memberEmailId;
        this.memberGender = memberGender;
        this.memberImagePath = memberImagePath;
        this.memberImageName = memberImageName;
        this.memberImageFile = memberImageFile;
        this.memberFav = memberFav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Storing the Member data to Parcel object
     **/
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(memberId);
        parcel.writeString(memberName);
        parcel.writeString(memberPhoneNumber);
        parcel.writeString(memberEmailId);
        parcel.writeString(memberGender);
        parcel.writeString(memberImagePath);
        parcel.writeString(memberImageName);
        parcel.writeByteArray(memberImageFile);
        parcel.writeInt(memberFav);
    }


    /**
     * Retrieving Member data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/

    private Member(Parcel in){
        this.memberId = in.readString();
        this.memberName = in.readString();
        this.memberPhoneNumber = in.readString();
        this.memberEmailId = in.readString();
        this.memberGender = in.readString();
        this.memberImagePath = in.readString();
        this.memberImageName = in.readString();
        this.memberImageFile = in.createByteArray();
        this.memberFav = in.readInt();
    }

    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>(){

        @Override
        public Member createFromParcel(Parcel parcel) {
            return new Member(parcel);
        }

        @Override
        public Member[] newArray(int i) {
            return new Member[i];
        }
    };

    public String getMemberId() {

        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhoneNumber() {
        return memberPhoneNumber;
    }

    public void setMemberPhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }

    public String getMemberEmailId() {
        return memberEmailId;
    }

    public void setMemberEmailId(String memberEmailId) {
        this.memberEmailId = memberEmailId;
    }

    public String getMemberGender() {
        return memberGender;
    }

    public void setMemberGender(String memberGender) {
        this.memberGender = memberGender;
    }

    public String getMemberImagePath() {
        return memberImagePath;
    }

    public void setMemberImagePath(String memberImagePath) {
        this.memberImagePath = memberImagePath;
    }

    public String getMemberImageName() {
        return memberImageName;
    }

    public void setMemberImageName(String memberImageName) {
        this.memberImageName = memberImageName;
    }

    public byte[] getMemberImageFile() {
        return memberImageFile;
    }

    public void setMemberImageFile(byte[] memberImageFile) {
        this.memberImageFile = memberImageFile;
    }

    public int getMemberFav() {
        return memberFav;
    }

    public void setMemberFav(int memberFav) {
        this.memberFav = memberFav;
    }
}
