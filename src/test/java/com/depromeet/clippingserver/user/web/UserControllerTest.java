package com.depromeet.clippingserver.user.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;

import com.depromeet.clippingserver.user.domain.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService userService;

	@Test
	public void getUserId() throws Exception {
		final String DEVICE_KEY = "#$9981";

		given(userService.join(DEVICE_KEY)).willReturn(1L);

		mvc.perform(post("/join").header("DeviceKey", DEVICE_KEY))
								 .andExpect(status().isOk())
								 .andExpect(jsonPath("$.userId").value(1))
								 .andDo(print());
	}

}
