package com.yashkakkar.licagentdiary.async.eventbus;

/**
 * Created by Yash Kakkar on 28-06-2017.
 */

public class IsWhatsAppNumberEvent {
    boolean iswhatsAppNumber = false;

    public IsWhatsAppNumberEvent(boolean iswhatsAppNumber) {
        this.iswhatsAppNumber = iswhatsAppNumber;
    }

    public boolean iswhatsAppNumberValid() {
        return iswhatsAppNumber;
    }
}
