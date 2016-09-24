package com.practice.shop.web.standalone;

import com.practice.shop.utils.TestUtils;
import com.practice.shop.domain.account.Account;
import com.practice.shop.domain.account.AccountRepository;
import com.practice.shop.domain.account.UserService;
import com.practice.shop.signup.SignupForm;
import com.practice.shop.web.SignupController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    public void testProcessSignupForm_FormEntryIsEmpty() throws Exception {
        mockMvc.perform(post("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name(SignupController.SIGN_UP_VIEW))
                .andExpect(model().attribute("signupForm", equalTo(new SignupForm())))
                .andExpect(model().attributeHasFieldErrors("signupForm", "customer.firstname",
                        "customer.lastname", "customer.email", "password"));

        verifyZeroInteractions(accountRepository);
        verifyZeroInteractions(userService);
    }

    @Test
    public void testProcessSignupForm_EmailEntryContainsPasswordOnly() throws Exception {
        when(accountRepository.save(any(Account.class))).thenReturn(new SignupForm().createAccount());

        mockMvc.perform(post("/signup").param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name(SignupController.SIGN_UP_VIEW))
                .andExpect(model().attributeHasFieldErrors("signupForm", "customer.email",
                        "customer.firstname", "customer.lastname"))
                .andExpect(model().attribute("signupForm", allOf(
                        hasProperty("customer", hasProperty("firstname", nullValue())),
                        hasProperty("customer", hasProperty("lastname", nullValue())),
                        hasProperty("customer", hasProperty("email", nullValue())),
                        hasProperty("password", is("password"))
                )));
        verifyZeroInteractions(accountRepository);
        verifyZeroInteractions(userService);
    }

    @Test
    public void testProcessSignupForm_MalformedEmail() throws Exception {
        final String firstname = "first name", lastname = "last name",
                phoneNumber = "0123456789", email = "email.com", password = "password";
        mockMvc.perform(
                post("/signup")
                        .param("customer.firstname", firstname)
                        .param("customer.lastname", lastname)
                        .param("customer.phoneNumber", phoneNumber)
                        .param("customer.email", email)
                        .param("password", password)
        )
                .andExpect(status().isOk())
                .andExpect(view().name(SignupController.SIGN_UP_VIEW))
                .andExpect(model().attributeHasFieldErrors("signupForm", "customer.email"))
                .andExpect(model().attribute("signupForm", allOf(
                        hasProperty("customer", hasProperty("firstname", is(firstname))),
                        hasProperty("customer", hasProperty("lastname", is(lastname))),
                        hasProperty("customer", hasProperty("phoneNumber", is(phoneNumber))),
                        hasProperty("customer", hasProperty("email", is("email.com"))),
                        hasProperty("password", is(password))
                )));
        verifyZeroInteractions(accountRepository);
        verifyZeroInteractions(userService);
    }

    @Test
    public void testProcessSignupForm_MalformedPhoneNumber() throws Exception {
        String str = TestUtils.generateString(10), phoneNumber = "012345";

        mockMvc.perform(
                post("/signup")
        .param("customer.firstname", str)
        .param("customer.lastname", str)
        .param("customer.email", "email@example.com")
        .param("customer.phoneNumber", phoneNumber)
        .param("customer.password", str)
        .param("customer.country", str)
        .param("customer.city", str)
        .param("customer.street", str))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("signupForm", "customer.phoneNumber"))
                .andExpect(view().name(SignupController.SIGN_UP_VIEW));

        verifyZeroInteractions(accountRepository, userService);
    }

    @Test
    public void testProcessSignupForm_FormEntryIsOK() throws Exception {
        String email = "email@example.com", password = "password",
                firstname = "firstname", lastname = "lastname", country = "Country",
                city = "City", street = "Street, 190";

        Account saved = new Account(email, password, "ROLE_USER");
        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        mockMvc.perform(
                post("/signup")
                        .param("customer.email", email)
                        .param("customer.firstname", firstname)
                        .param("customer.lastname", lastname)
                        .param("address.country", country)
                        .param("address.city", city)
                        .param("address.street", street)
                        .param("password", password)
        )
                .andExpect(status().is3xxRedirection())
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
    }
}
