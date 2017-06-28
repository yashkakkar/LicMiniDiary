package com.yashkakkar.licagentdiary.async.eventbus;

import com.yashkakkar.licagentdiary.models.Member;

import java.util.List;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class GetMemberListEvent {
    private final List<Member> members;
    public GetMemberListEvent(List<Member> members) {
        this.members = members;
    }
    public List<Member> getMembers(){
        return members;
    }
}
