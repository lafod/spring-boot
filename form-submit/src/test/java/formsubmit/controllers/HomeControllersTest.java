package formsubmit.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import formsubmit.Application;
import formsubmit.domain.ProfileForm;
import formsubmit.domain.UserProfileSession;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class HomeControllersTest {

	@Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void should_redirect_to_profile() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/profile"));
    }
    
    @Test
    public void should_redirect_to_tastes() throws Exception {
        MockHttpSession session = new MockHttpSession();
        UserProfileSession sessionBean = new UserProfileSession();
        ProfileForm form = new ProfileForm();
        form.setBirthDate(LocalDate.of(1987, 02, 3));
        form.setEmail("lanre@afod.com");
        form.setTastes(Arrays.asList("spring", "groovy"));
        form.setTwitterHandle("lafod");
        sessionBean.saveForm(form);
        session.setAttribute("scopedTarget.userProfileSession", sessionBean);

        this.mockMvc.perform(get("/").session(session))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/search/mixed;keywords=spring,groovy"));
    }
	

}
