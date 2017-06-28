package com.yashkakkar.licagentdiary.async;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yash Kakkar on 26-05-2017.
 */

public class UpgradeMethod {

    private static final String METHODS_PREFIX = "onUpgradeTo";
    private static UpgradeMethod instance;

    private UpgradeMethod(){

    }

    private static UpgradeMethod getInstance() {
        if (instance == null){
            instance = new UpgradeMethod();
        }
        return instance;
    }

    public static void process(int oldVersion, int newVersion){
        List<Method> methodsToRun = getInstance().getMethodsToRun(oldVersion,newVersion);


    }

    private List<Method> getMethodsToRun(int dbOldVersion, int dbNewVersion){
        Method[] declaredMethods = getInstance().getClass().getMethods();
        List<Method> methodToRun = new ArrayList<>();
        for (Method declaredMethod : declaredMethods){
            if (declaredMethod.getName().contains(METHODS_PREFIX)){
                int methodVersionPostFix = Integer.parseInt(declaredMethod.getName().replace(METHODS_PREFIX,""));
                 if (dbOldVersion <= methodVersionPostFix && methodVersionPostFix <= dbNewVersion){
                     methodToRun.add(declaredMethod);
                 }
            }
        }
        return methodToRun;
    }

    // this method is used to upgrade the database
    private void onUpgrade2(){

    }


}
