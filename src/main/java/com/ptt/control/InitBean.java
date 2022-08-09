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
    OutputArgumentRepository outputArgumentRepository;
    @Inject
    InputArgumentRepository inputArgumentRepository;
    @Inject
    StepRepository httpStepRepository;
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

        HttpStep startHttp = new HttpStep();
        startHttp.method = "POST";
        startHttp.url = "http://ptt-test-environment-service:8080/sign-up";
        startHttp.body = "{\"username\": \"user\", \"password\": \"pw\"}";
        startHttp.plan = plan;
        startHttp.name = "Sign Up";
        startHttp.description = "Creates an account";
        startHttp.nextSteps = new ArrayList<>();
        httpStepRepository.persist(startHttp);

        OutputArgument outArgName = new OutputArgument();
        outArgName.name = "username";
        outArgName.jsonLocation = "username";
        outArgName.step = startHttp;
        outputArgumentRepository.persist(outArgName);

        OutputArgument outArgPw = new OutputArgument();
        outArgPw.name = "password";
        outArgPw.jsonLocation = "password";
        outArgPw.step = startHttp;
        outputArgumentRepository.persist(outArgPw);

        startHttp.parameterMap.put(outArgName, "username");
        startHttp.parameterMap.put(outArgPw, "password");
        httpStepRepository.persist(startHttp);

        plan.start = startHttp;
        planRepository.persist(plan);

        HttpStep loginHttp = new HttpStep();
        loginHttp.method = "POST";
        loginHttp.url = "http://ptt-test-environment-service:8080/login";
        loginHttp.body = "{\"username\": \"{{username}}\", \"password\": \"{{password}}\"}";
        loginHttp.plan = plan;
        loginHttp.name = "Login";
        loginHttp.description = "Sign into an account ";
        loginHttp.nextSteps = new ArrayList<>();
        httpStepRepository.persist(loginHttp);
        
        InputArgument inArgName = new InputArgument();
        inArgName.name = "username";
        inArgName.step = loginHttp;
        inputArgumentRepository.persist(inArgName);
        
        InputArgument inArgPw = new InputArgument();
        inArgPw.name = "password";
        inArgPw.step = loginHttp;
        inputArgumentRepository.persist(inArgPw);
        
        OutputArgument outArgToken = new OutputArgument();
        outArgToken.name = "token";
        outArgToken.jsonLocation = "token";
        outArgToken.step = loginHttp;
        outputArgumentRepository.persist(outArgToken);

        loginHttp.parameterMap.put(outArgToken, "token");
        httpStepRepository.persist(loginHttp);

        startHttp.nextSteps.add(loginHttp);
        httpStepRepository.persist(startHttp);
        
        StepParameterRelation nameParamRelation = new StepParameterRelation();
        nameParamRelation.fromArg = outArgName;
        nameParamRelation.toArg = inArgName;
        relationRepository.persist(nameParamRelation);

        StepParameterRelation pwParamRelation = new StepParameterRelation();
        pwParamRelation.fromArg = outArgPw;
        pwParamRelation.toArg = inArgPw;
        relationRepository.persist(pwParamRelation);

        HttpStep sleepHttp = new HttpStep();
        sleepHttp.method = "GET";
        sleepHttp.url = "http://ptt-test-environment-service:8080/sleep/{token}/4";
        sleepHttp.body = "";
        sleepHttp.plan = plan;
        sleepHttp.name = "Sleep";
        sleepHttp.description = "Sleep for 4 seconds";
        sleepHttp.nextSteps = new ArrayList<>();
        httpStepRepository.persist(sleepHttp);

        InputArgument inArgToken = new InputArgument();
        inArgToken.name = "token";
        inArgToken.step = sleepHttp;
        inputArgumentRepository.persist(inArgToken);

        StepParameterRelation tokenParamRelation = new StepParameterRelation();
        tokenParamRelation.fromArg = outArgToken;
        tokenParamRelation.toArg = inArgToken;
        relationRepository.persist(tokenParamRelation);

        loginHttp.nextSteps.add(sleepHttp);
        httpStepRepository.persist(loginHttp);

        PlanRun planRun = new PlanRun();
        planRun.plan = plan;
        planRun.startTime = System.currentTimeMillis();
        planRun.duration = 5 * 60 * 1000; // 5 min
        planRunRepository.persist(planRun);
    }
}
