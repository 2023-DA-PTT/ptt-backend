package com.ptt.control;

import com.ptt.entity.User;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class InitBean {
    @Inject
    UserRepository userRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        User defaultUser = new User();
        defaultUser.username = "default";
        userRepository.persist(defaultUser);
        System.out.println(defaultUser.id);
    }
}
