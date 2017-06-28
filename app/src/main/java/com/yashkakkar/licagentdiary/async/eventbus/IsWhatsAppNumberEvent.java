package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 28-06-2017.
 */

public class IsWhatsAppNumberEvent {
    boolean iswhatsAppNumber = false;
    String whatsAppId;

    public IsWhatsAppNumberEvent(boolean iswhatsAppNumber, String _id) {
        this.iswhatsAppNumber = iswhatsAppNumber;
        this.whatsAppId = _id;
    }

    public boolean iswhatsAppNumberValid() {
        return iswhatsAppNumber;
    }

    public String getWhatsAppId() {
        return whatsAppId;
    }

}
