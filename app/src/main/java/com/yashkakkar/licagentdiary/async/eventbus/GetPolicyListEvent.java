package com.yashkakkar.licagentdiary.async.eventbus;

import com.yashkakkar.licagentdiary.models.Policy;

import java.util.List;

/**
 * Created by Yash Kakkar on 06-06-2017.
 */

public class GetPolicyListEvent {
    private final List<Policy> policies;
    public GetPolicyListEvent(List<Policy> policies) {
        this.policies = policies;
    }
    public List<Policy> getPolicies() {
        return policies;
    }
}
