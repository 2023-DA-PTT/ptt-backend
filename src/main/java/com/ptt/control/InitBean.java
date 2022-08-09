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
    HttpStepRepository httpStepRepository;
    @Inject
    StepParameterRelationRepository relationRepository;
    @Inject
    PlanRunRepository planRunRepository;
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

        HttpStep startHttp = new HttpStep();
        startHttp.id = new StepId(start, StepType.HTTP);
        startHttp.method = "POST";
        startHttp.url = "http://ptt-test-environment-service:8080/sign-up";
        startHttp.body = "{\"username\": \"user\", \"password\": \"pw\"}";
        startHttp.parameterMap.put(outArgName, "username");
        startHttp.parameterMap.put(outArgPw, "password");
        httpStepRepository.persist(startHttp);

        plan.start = start;
        planRepository.persist(plan);

        Step login = new Step();
        login.plan = plan;
        login.name = "Login";
        login.description = "Sign into an account ";
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

        HttpStep loginHttp = new HttpStep();
        loginHttp.id = new StepId(login, StepType.HTTP);
        loginHttp.method = "POST";
        loginHttp.url = "http://ptt-test-environment-service:8080/login";
        loginHttp.body = "{\"username\": \"{{username}}\", \"password\": \"{{password}}\"}";
        loginHttp.parameterMap.put(outArgToken, "token");
        httpStepRepository.persist(loginHttp);

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

        Step sleep = new Step();
        sleep.plan = plan;
        sleep.name = "Sleep";
        sleep.description = "Sleep for 4 seconds";
        sleep.nextSteps = new ArrayList<>();
        stepRepository.persist(sleep);

        InputArgument inArgToken = new InputArgument();
        inArgToken.name = "token";
        inArgToken.step = sleep;
        inputArgumentRepository.persist(inArgToken);
        
        HttpStep sleepHttp = new HttpStep();
        sleepHttp.id = new StepId(sleep, StepType.HTTP);
        sleepHttp.method = "GET";
        sleepHttp.url = "http://ptt-test-environment-service:8080/sleep/{token}/4";
        sleepHttp.body = "";
        httpStepRepository.persist(sleepHttp);

        StepParameterRelation tokenParamRelation = new StepParameterRelation();
        tokenParamRelation.fromArg = outArgToken;
        tokenParamRelation.toArg = inArgToken;
        relationRepository.persist(tokenParamRelation);

        login.nextSteps.add(sleep);
        stepRepository.persist(login);

        PlanRun planRun = new PlanRun();
        planRun.plan = plan;
        planRun.startTime = System.currentTimeMillis();
        planRun.duration = 5 * 60 * 1000; // 5 min
        planRunRepository.persist(planRun);
    }
}
