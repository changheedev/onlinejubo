package com.github.changhee_choi.jubo.manager.domain.test;

import com.github.changhee_choi.jubo.core.domain.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Changhee Choi
 * @since 27/06/2020
 */
@Entity
@Table(name = "oj_test_user_type")
@DiscriminatorValue("T")
public class TestUserType extends User {

    private String testProp;

    protected TestUserType() {
    }

    public TestUserType(String name, String email, String password, String testProp) {
        super(name, email, password);
        this.testProp = testProp;
    }
}
