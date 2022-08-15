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
    String BASE_URL = "http://ptt-test-environment-service:8080";
    
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
    @Inject
    ScriptStepRepository scriptStepRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {

        if (ProfileManager.getActiveProfile().equals("prod")) {
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
        startHttp.url = BASE_URL + "/sign-up";
        startHttp.body = "{\"username\": \"user\", \"password\": \"pw\"}";
        startHttp.plan = plan;
        startHttp.name = "Sign Up";
        startHttp.description = "Creates an account";
        startHttp.responseContentType = RequestContentType.APPLICATION_JSON;
        startHttp.contentType = RequestContentType.APPLICATION_JSON;
        startHttp.nextSteps = new ArrayList<>();
        httpStepRepository.persist(startHttp);

        OutputArgument outArgName = new OutputArgument();
        outArgName.name = "username";
        outArgName.parameterLocation = "username";
        outArgName.outputType = OutputType.PLAIN_TEXT;
        outArgName.step = startHttp;
        outputArgumentRepository.persist(outArgName);

        OutputArgument outArgPw = new OutputArgument();
        outArgPw.name = "password";
        outArgPw.parameterLocation = "password";
        outArgPw.outputType = OutputType.PLAIN_TEXT;
        outArgPw.step = startHttp;
        outputArgumentRepository.persist(outArgPw);

        plan.start = startHttp;
        planRepository.persist(plan);

        ScriptStep convertParameterToBodyStep = new ScriptStep();
        convertParameterToBodyStep.name = "Parse Body";
        convertParameterToBodyStep.description = "Takes the input arguments and parses them into a json body";
        convertParameterToBodyStep.plan = plan;
        convertParameterToBodyStep.script = "return {body: `{\"username\": \"${params.get(\"username\")}\", \"password\": \"${params.get(\"password\")}\"}`};";
        scriptStepRepository.persist(convertParameterToBodyStep);

        InputArgument inArgName = new InputArgument();
        inArgName.name = "username";
        inArgName.step = convertParameterToBodyStep;
        inputArgumentRepository.persist(inArgName);

        InputArgument inArgPw = new InputArgument();
        inArgPw.name = "password";
        inArgPw.step = convertParameterToBodyStep;
        inputArgumentRepository.persist(inArgPw);

        OutputArgument outArgBody = new OutputArgument();
        outArgBody.name = "body";
        outArgBody.parameterLocation = "body";
        outArgBody.outputType = OutputType.PLAIN_TEXT;
        outArgBody.step = convertParameterToBodyStep;
        outputArgumentRepository.persist(outArgBody);

        StepParameterRelation nameParamRelation = new StepParameterRelation();
        nameParamRelation.fromArg = outArgName;
        nameParamRelation.toArg = inArgName;
        relationRepository.persist(nameParamRelation);

        StepParameterRelation pwParamRelation = new StepParameterRelation();
        pwParamRelation.fromArg = outArgPw;
        pwParamRelation.toArg = inArgPw;
        relationRepository.persist(pwParamRelation);

        HttpStep loginHttp = new HttpStep();
        loginHttp.method = "POST";
        loginHttp.url = BASE_URL + "/login";
        loginHttp.body = "{{body}}";
        loginHttp.plan = plan;
        loginHttp.name = "Login";
        loginHttp.description = "Sign into an account";
        loginHttp.responseContentType = RequestContentType.APPLICATION_JSON;
        loginHttp.contentType = RequestContentType.APPLICATION_JSON;
        loginHttp.nextSteps = new ArrayList<>();
        httpStepRepository.persist(loginHttp);

        InputArgument inArgBody = new InputArgument();
        inArgBody.name = "body";
        inArgBody.step = loginHttp;
        inputArgumentRepository.persist(inArgBody);

        OutputArgument outArgToken = new OutputArgument();
        outArgToken.name = "token";
        outArgToken.parameterLocation = "token";
        outArgToken.outputType = OutputType.PLAIN_TEXT;
        outArgToken.step = loginHttp;
        outputArgumentRepository.persist(outArgToken);

        startHttp.nextSteps.add(loginHttp);
        httpStepRepository.persist(startHttp);

        StepParameterRelation bodyParamRelation = new StepParameterRelation();
        bodyParamRelation.fromArg = outArgBody;
        bodyParamRelation.toArg = inArgBody;
        relationRepository.persist(bodyParamRelation);

        HttpStep sleepHttp = new HttpStep();
        sleepHttp.method = "GET";
        sleepHttp.url = BASE_URL + "/sleep/{token}/4";
        sleepHttp.body = "";
        sleepHttp.plan = plan;
        sleepHttp.name = "Sleep";
        sleepHttp.description = "Sleep for 4 seconds";
        sleepHttp.responseContentType = RequestContentType.APPLICATION_JSON;
        sleepHttp.contentType = RequestContentType.APPLICATION_JSON;
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
