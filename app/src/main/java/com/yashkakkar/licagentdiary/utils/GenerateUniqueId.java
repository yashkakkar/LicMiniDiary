package com.yashkakkar.licagentdiary.utils;

import java.util.UUID;

/**
 * Created by Yash Kakkar on 13-06-2017.
 */

public class GenerateUniqueId {

    public static GenerateUniqueId newInstance(){
        return new GenerateUniqueId();
    }

    public GenerateUniqueId() {
    }

    public String generateUniqueKeyUsingUUID() {
        // Static factory to retrieve a type 4 (pseudo randomly generated) UUID
        return UUID.randomUUID().toString();
    }

    public  String generateUniqueId(){
        return null;
    }
}
