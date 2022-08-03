package com.ptt.control;

import com.ptt.entity.*;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;

@ApplicationScoped
public class InitBean {
    @Inject
    UserRepository userRepository;
    @Inject
    PlanRepository planRepository;
    @Inject
    StepRepository stepRepository;
    @Inject
    OutputArgumentRepository outputArgumentRepository;
    @Inject
    InputArgumentRepository inputArgumentRepository;
    @Inject
    StepParameterRelationRepository relationRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        if(ProfileManager.getActiveProfile().equals("prod")) {
            return;
        }
        User defaultUser = new User();
        defaultUser.username = "default";
        userRepository.persist(defaultUser);
        System.out.println(defaultUser.id);

        Plan plan = new Plan();
        plan.name = "example";
        plan.description = "first demo test plan for testing";
        plan.user = defaultUser;
        planRepository.persist(plan);

        Step start = new Step();
        start.plan = plan;
        start.name = "Sign Up";
        start.description = "Creates an account";
        start.method = "POST";
        start.url = "https://localhost:8081/signup";
        start.body = "{username: 'user', password: 'pw'}";
        start.nextSteps = new ArrayList<>();
        stepRepository.persist(start);

        OutputArgument outArgName = new OutputArgument();
        outArgName.name = "username";
        outArgName.jsonLocation = "username";
        outArgName.step = start;
        outputArgumentRepository.persist(outArgName);

        OutputArgument outArgPw = new OutputArgument();
        outArgPw.name = "password";
        outArgPw.jsonLocation = "password";
        outArgPw.step = start;
        outputArgumentRepository.persist(outArgPw);

        plan.start = start;
        planRepository.persist(plan);

        Step login = new Step();
        login.plan = plan;
        login.name = "Login";
        login.description = "Sign into an account ";
        login.method = "POST";
        login.url = "https://localhost:8081/login";
        login.body = "{username: {{username}}, password: {{password}}}";
        login.nextSteps = new ArrayList<>();
        stepRepository.persist(login);

        InputArgument inArgName = new InputArgument();
        inArgName.name = "username";
        inArgName.step = login;
        inputArgumentRepository.persist(inArgName);

        InputArgument inArgPw = new InputArgument();
        inArgPw.name = "password";
        inArgPw.step = login;
        inputArgumentRepository.persist(inArgPw);

        OutputArgument outArgToken = new OutputArgument();
        outArgToken.name = "token";
        outArgToken.jsonLocation = "token";
        outArgToken.step = login;
        outputArgumentRepository.persist(outArgToken);

        start.nextSteps.add(login);
        stepRepository.persist(start);

        StepParameterRelation nameParamRelation = new StepParameterRelation();
        nameParamRelation.fromArg = outArgName;
        nameParamRelation.toArg = inArgName;
        relationRepository.persist(nameParamRelation);

        StepParameterRelation pwParamRelation = new StepParameterRelation();
        pwParamRelation.fromArg = outArgPw;
        pwParamRelation.toArg = inArgPw;
        relationRepository.persist(pwParamRelation);
    }
}
