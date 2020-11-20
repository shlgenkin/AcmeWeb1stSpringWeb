/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.statusmgr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServerStatusControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

        this.mockMvc.perform(get("/server/status")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up"));
    }

    @Test
    public void paramGreetingShouldReturnTailoredMessage() throws Exception {

        this.mockMvc.perform(get("/server/status").param("name", "RebYid"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by RebYid"));
    }

    @Test
    public void withParamShouldReturnTailored_Name_Basic_Mem_Op_Ext() throws Exception {
        this.mockMvc.perform(get("/server/status/detailed?details=operations"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is operating normally"));

        this.mockMvc.perform(get("/server/status/detailed?details=memory,operations,extensions&name=yaakov"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by yaakov"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and its memory is running low, " +
                        "and is operating normally, and is using these extensions - [Hypervisor, Kubernetes, RAID-6]"));

        this.mockMvc.perform(get("/server/status/detailed?details=operations,memory&name=yaakov"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by yaakov"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up, and is operating normally, " +
                        "and its memory is running low"));

        this.mockMvc.perform(get("/server/status/detailed?name=yaakov&details=memory,operations"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by yaakov"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up, " +
                        "and its memory is running low, and is operating normally"));

        this.mockMvc.perform(get("/server/status/detailed?name=yaakov&details=memory,operations,memory"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contentHeader").value("Server Status requested by yaakov"))
                .andExpect(jsonPath("$.statusDesc").value("Server is up, " +
                        "and its memory is running low, and is operating normally, and its memory is running low"));
    }

    @Test
    public void noDetailsParamShouldReturnCustomException() throws Exception {
        this.mockMvc.perform(get("/server/status/detailed"))
                .andDo(print()).andExpect(status().isBadRequest()).andExpect(status().reason(is("Required List parameter 'details' is not present in request")));

/*
   A URL request that ends with 'details' or 'details=' is a tricky case.
   If 'details' parameter requirement and error implemented by using a null check and
   generating a custom message, the following test would fail. In some implementations no error would be
   generated and in others the default message would be returned instead of the custom one.
   Checking for the details list via .isEmpty() as this implementation does solves this issue.
 */
        this.mockMvc.perform(get("/server/status/detailed?details="))
                .andDo(print()).andExpect(status().isBadRequest()).andExpect(status().reason(is("Required List parameter 'details' is not present in request")));
    }

    @Test
    public void withParamShouldReturnCustomExceptionInvalidDetails() throws Exception {
        this.mockMvc.perform(get("/server/status/detailed?details=memory,operations,RanDom"))
                .andDo(print()).andExpect(status().isBadRequest()).andExpect(status().reason(is("Invalid details option: RanDom")));

        this.mockMvc.perform(get("/server/status/detailed?details=memory,operations,RanDom&name=yaakov"))
                .andDo(print()).andExpect(status().isBadRequest()).andExpect(status().reason(is("Invalid details option: RanDom")));

    }

}
