package com.practice.springframework.web.standalone;

import com.shop.springframework.domain.account.AccountRepository;
import com.shop.springframework.domain.account.UserService;
import com.shop.springframework.signup.SignupForm;
import com.shop.springframework.web.SignupController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class SignupControllerTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserService userService;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new SignupController(accountRepository, userService)).build();
    }

    @Test
    public void testShowSignupForm_RequestedWithoutAjax() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name(SignupController.SIGN_UP_VIEW))
                .andExpect(model().attribute("signupForm", equalTo(new SignupForm())));
    }

    @Test
    public void testShowSignupForm_RequestedWithAjax() throws Exception {
        mockMvc.perform(get("/signup").header("X-Requested-With", "XMLHttpRequest"))
                .andExpect(status().isOk())
                .andExpect(view().name(SignupController.SIGN_UP_VIEW.concat(" :: signupForm")))
                .andExpect(model().attribute("signupForm", equalTo(new SignupForm())));
    }

    /*@Test
    public void testProcessSignupForm_FormEntryIsEmpty() throws Exception {
        mockMvc.perform(post("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name(SignupController.SIGN_UP_VIEW))
                .andExpect(model().attribute("signupForm", equalTo(new SignupForm())))
                .andExpect(model().attributeHasFieldErrors("signupForm", "email", "password"));

        verifyZeroInteractions(accountRepository);
        verifyZeroInteractions(userService);
    }*/

    /*@Test
    public void testProcessSignupForm_EmailEntryIsEmpty() throws Exception {
        mockMvc.perform(post("/signup").param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name(SignupController.SIGN_UP_VIEW))
                .andExpect(model().attributeHasFieldErrors("signupForm", "email"))
                .andExpect(model().attribute("signupForm", allOf(
                        hasProperty("email", nullValue()),
                        hasProperty("password", is("password"))
                )));
        verifyZeroInteractions(accountRepository);
        verifyZeroInteractions(userService);
    }*/

    /*@Test
    public void testProcessSignupForm_MalformedEmail() throws Exception {
        mockMvc.perform(
                post("/signup")
                        .param("customer.firstname", "firstName")
                        .param("customer.lastname", "lastName")
                        .param("customer.phoneNumber", "0123456789")
                        .param("customer.email", "email.com")
                        .param("password", "password")
        )
                .andExpect(status().isOk())
                .andExpect(view().name(SignupController.SIGN_UP_VIEW))
                .andExpect(model().attributeHasFieldErrors("signupForm", "email"))
                .andExpect(model().attribute("signupForm", allOf(
                        hasProperty("customer.email", is("email.com")),
                        hasProperty("password", is("password"))
                )));
        verifyZeroInteractions(accountRepository);
        verifyZeroInteractions(userService);
    }*/

    /*@Test
    public void testProcessSignupForm_FormEntryIsOK() throws Exception {
        String email = "email@co.edu", password = "password", role = "ROLE_USER";

        Account saved = new Account(email, password, role);
        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        mockMvc.perform(
                post("/signup")
                        .param("email", email)
                        .param("password", password)
        )
                .andDo(print())
//                .andExpect(status().is3xxRedirection())
                .andExpect(status().isOk())
                .andExpect(model().attributeHasNoErrors("signupForm"))
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        ArgumentCaptor<Account> formObjectArgument = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(formObjectArgument.capture());
        verifyNoMoreInteractions(accountRepository);

        Account formObject = formObjectArgument.getValue();
        assertNull(formObject.getId());
        assertThat(formObject.getPassword(), equalTo(password));
        assertThat(formObject.getEmail(), equalTo(email));

        verify(userService).signin(saved);
        verifyNoMoreInteractions(userService);
    }*/
}
