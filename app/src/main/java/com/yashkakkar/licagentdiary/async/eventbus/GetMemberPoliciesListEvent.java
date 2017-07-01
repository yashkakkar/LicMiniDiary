package com.yashkakkar.licagentdiary.async.eventbus;

import com.yashkakkar.licagentdiary.models.Policy;

import java.util.List;

/**
 * Created by Yash Kakkar on 01-07-2017.
 */

public class GetMemberPoliciesListEvent {
    private List<Policy> policies;
    public GetMemberPoliciesListEvent(List<Policy> policies) {
        this.policies = policies;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

}
