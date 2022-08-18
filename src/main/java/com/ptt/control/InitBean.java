package com.ptt.control;

import com.ptt.entity.*;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

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
    @Inject
    NextStepRepository nextStepRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {

        if (ProfileManager.getActiveProfile().equals("prod")) {
            //return;
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

        ScriptStep createUser = new ScriptStep();
        createUser.name = "Create User";
        createUser.description = "Create random user";
        createUser.plan = plan;
        createUser.script = "return {username: \"user\" + Math.floor(Math.random()*10000), password: \"password\", random: \"random\"};";
        scriptStepRepository.persist(createUser);

        OutputArgument outArgRandomSetup = new OutputArgument();
        outArgRandomSetup.name = "random";
        outArgRandomSetup.parameterLocation = "random";
        outArgRandomSetup.outputType = OutputType.PLAIN_TEXT;
        outArgRandomSetup.step = createUser;
        outputArgumentRepository.persist(outArgRandomSetup);

        OutputArgument outArgNameSetup = new OutputArgument();
        outArgNameSetup.name = "uname";
        outArgNameSetup.parameterLocation = "username";
        outArgNameSetup.outputType = OutputType.PLAIN_TEXT;
        outArgNameSetup.step = createUser;
        outputArgumentRepository.persist(outArgNameSetup);

        OutputArgument outArgPwSetup = new OutputArgument();
        outArgPwSetup.name = "password";
        outArgPwSetup.parameterLocation = "password";
        outArgPwSetup.outputType = OutputType.PLAIN_TEXT;
        outArgPwSetup.step = createUser;
        outputArgumentRepository.persist(outArgPwSetup);

        plan.start = createUser;
        planRepository.persist(plan);

        HttpStep startHttp = new HttpStep();
        startHttp.method = "POST";
        startHttp.url = BASE_URL + "/sign-up";
        startHttp.body = "{\"username\": \"{{username}}\", \"password\": \"{{password}}\"}";
        startHttp.plan = plan;
        startHttp.name = "Sign Up";
        startHttp.description = "Creates an account";
        startHttp.responseContentType = RequestContentType.APPLICATION_JSON;
        startHttp.contentType = RequestContentType.APPLICATION_JSON;
        httpStepRepository.persist(startHttp);

        InputArgument inArgNameSignIn = new InputArgument();
        inArgNameSignIn.name = "username";
        inArgNameSignIn.step = startHttp;
        inputArgumentRepository.persist(inArgNameSignIn);

        InputArgument inArgPwSignIn = new InputArgument();
        inArgPwSignIn.name = "password";
        inArgPwSignIn.step = startHttp;
        inputArgumentRepository.persist(inArgPwSignIn);

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

        StepParameterRelation nameParamRelationSetup = new StepParameterRelation();
        nameParamRelationSetup.fromArg = outArgNameSetup;
        nameParamRelationSetup.toArg = inArgNameSignIn;
        relationRepository.persist(nameParamRelationSetup);

        StepParameterRelation pwParamRelationSetup = new StepParameterRelation();
        pwParamRelationSetup.fromArg = outArgPwSetup;
        pwParamRelationSetup.toArg = inArgPwSignIn;
        relationRepository.persist(pwParamRelationSetup);

        NextStep createUserNextStep = new NextStep();
        createUserNextStep.fromStep = createUser;
        createUserNextStep.toStep = startHttp;
        createUserNextStep.repeatAmount = 1;
        nextStepRepository.persist(createUserNextStep);

        createUser.nextSteps.add(createUserNextStep);
        scriptStepRepository.persist(createUser);

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
        
        NextStep startNextStep = new NextStep();
        startNextStep.fromStep = startHttp;
        startNextStep.toStep = convertParameterToBodyStep;
        startNextStep.repeatAmount = 1;
        nextStepRepository.persist(startNextStep);

        startHttp.nextSteps.add(startNextStep);
        httpStepRepository.persist(startHttp);     

        HttpStep loginHttp = new HttpStep();
        loginHttp.method = "POST";
        loginHttp.url = BASE_URL + "/login";
        loginHttp.body = "{{body}}";
        loginHttp.plan = plan;
        loginHttp.name = "Login";
        loginHttp.description = "Sign into an account";
        loginHttp.responseContentType = RequestContentType.APPLICATION_JSON;
        loginHttp.contentType = RequestContentType.APPLICATION_JSON;
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
        
        StepParameterRelation bodyParamRelation = new StepParameterRelation();
        bodyParamRelation.fromArg = outArgBody;
        bodyParamRelation.toArg = inArgBody;
        relationRepository.persist(bodyParamRelation);

        NextStep convertNextStep = new NextStep();
        convertNextStep.fromStep = convertParameterToBodyStep;
        convertNextStep.toStep = loginHttp;
        convertNextStep.repeatAmount = 1;
        nextStepRepository.persist(convertNextStep);

        convertParameterToBodyStep.nextSteps.add(convertNextStep);
        scriptStepRepository.persist(convertParameterToBodyStep);
        
        HttpStep sleepHttp = new HttpStep();
        sleepHttp.method = "GET";
        sleepHttp.url = BASE_URL + "/sleep/{token}/4";
        sleepHttp.body = "";
        sleepHttp.plan = plan;
        sleepHttp.name = "Sleep";
        sleepHttp.description = "Sleep for 4 seconds";
        sleepHttp.responseContentType = RequestContentType.APPLICATION_JSON;
        sleepHttp.contentType = RequestContentType.APPLICATION_JSON;
        httpStepRepository.persist(sleepHttp);

        InputArgument inArgToken = new InputArgument();
        inArgToken.name = "token";
        inArgToken.step = sleepHttp;
        inputArgumentRepository.persist(inArgToken);

        StepParameterRelation tokenParamRelation = new StepParameterRelation();
        tokenParamRelation.fromArg = outArgToken;
        tokenParamRelation.toArg = inArgToken;
        relationRepository.persist(tokenParamRelation);

        NextStep loginNextStep = new NextStep();
        loginNextStep.fromStep = loginHttp;
        loginNextStep.toStep = sleepHttp;
        loginNextStep.repeatAmount = 1;
        nextStepRepository.persist(loginNextStep);

        loginHttp.nextSteps.add(loginNextStep);
        httpStepRepository.persist(loginHttp);

        ScriptStep createInputForMultiPart = new ScriptStep();
        createInputForMultiPart.name = "Create Multipart Form";
        createInputForMultiPart.description = "Creates input arguments for multipart request";
        createInputForMultiPart.plan = plan;
        createInputForMultiPart.script = "return {name: \"filenr-\"+Math.floor(Math.random()*10000), file: \"file content\"};";
        scriptStepRepository.persist(createInputForMultiPart);

        OutputArgument outArgNameInputForMulti = new OutputArgument();
        outArgNameInputForMulti.name = "name";
        outArgNameInputForMulti.parameterLocation = "name";
        outArgNameInputForMulti.outputType = OutputType.PLAIN_TEXT;
        outArgNameInputForMulti.step = createInputForMultiPart;
        outputArgumentRepository.persist(outArgNameInputForMulti);

        OutputArgument outArgFileInputForMulti = new OutputArgument();
        outArgFileInputForMulti.name = "file";
        outArgFileInputForMulti.parameterLocation = "file";
        outArgFileInputForMulti.outputType = OutputType.OCTET_STREAM;
        outArgFileInputForMulti.step = createInputForMultiPart;
        outputArgumentRepository.persist(outArgFileInputForMulti);

        NextStep loginNextStepMulti = new NextStep();
        loginNextStepMulti.fromStep = loginHttp;
        loginNextStepMulti.toStep = createInputForMultiPart;
        loginNextStepMulti.repeatAmount = 3;
        nextStepRepository.persist(loginNextStepMulti);

        loginHttp.nextSteps.add(loginNextStepMulti);
        httpStepRepository.persist(loginHttp);

        HttpStep multiPartHttpStep = new HttpStep();
        multiPartHttpStep.method = "POST";
        multiPartHttpStep.url = BASE_URL + "/multipart";
        multiPartHttpStep.body = "";
        multiPartHttpStep.contentType = RequestContentType.MULTIPART_FORM_DATA;
        multiPartHttpStep.responseContentType = RequestContentType.APPLICATION_JSON;
        multiPartHttpStep.name = "Multipart";
        multiPartHttpStep.description = "Sends a multipart post request to backend";
        multiPartHttpStep.plan = plan;
        httpStepRepository.persist(multiPartHttpStep);
        
        InputArgument inArgMultipartName = new InputArgument();
        inArgMultipartName.name = "name";
        inArgMultipartName.step = multiPartHttpStep;
        inputArgumentRepository.persist(inArgMultipartName);
        
        InputArgument inArgMultipartFile = new InputArgument();
        inArgMultipartFile.name = "file";
        inArgMultipartFile.step = multiPartHttpStep;
        inputArgumentRepository.persist(inArgMultipartFile);

        StepParameterRelation fileNameParamRelation = new StepParameterRelation();
        fileNameParamRelation.fromArg = outArgNameInputForMulti;
        fileNameParamRelation.toArg = inArgMultipartName;
        relationRepository.persist(fileNameParamRelation);
        
        StepParameterRelation fileContentParamRelation = new StepParameterRelation();
        fileContentParamRelation.fromArg = outArgFileInputForMulti;
        fileContentParamRelation.toArg = inArgMultipartFile;
        relationRepository.persist(fileContentParamRelation);

        NextStep createInputNextStep = new NextStep();
        createInputNextStep.fromStep = createInputForMultiPart;
        createInputNextStep.toStep = multiPartHttpStep;
        createInputNextStep.repeatAmount = 1;
        nextStepRepository.persist(createInputNextStep);

        createInputForMultiPart.nextSteps.add(createInputNextStep);
        scriptStepRepository.persist(createInputForMultiPart);
    }
}
