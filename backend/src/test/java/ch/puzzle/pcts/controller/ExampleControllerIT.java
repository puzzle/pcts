package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.example.ExampleDto;
import ch.puzzle.pcts.mapper.ExampleMapper;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.business.ExampleBusinessService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ExampleController.class)
class ExampleControllerIT {

    @MockitoBean
    private ExampleBusinessService service;

    @MockitoBean
    private ExampleMapper mapper;

    @Autowired
    private MockMvc mvc;

    private static final String BASEURL = "/api/v1/examples";

    private Example example;
    private ExampleDto dto;

    @BeforeEach
    void setUp() {
        example = new Example(1L, "Example 1");
        dto = new ExampleDto(1L, "Example 1");
    }

    @DisplayName("Should successfully get all examples")
    @Test
    void shouldGetAllExamples() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(example));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(dto));

        mvc.perform(get(BASEURL).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk());

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get example by id")
    @Test
    void shouldGetExampleById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(example);
        BDDMockito.given(mapper.toDto(any(Example.class))).willReturn(dto);

        mvc.perform(get(BASEURL + "/1").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk());

        verify(service, times(1)).getById(anyLong());
        verify(mapper, times(1)).toDto(any(Example.class));
    }

    @DisplayName("Should successfully create new example")
    @Test
    void shouldCreateNewExample() throws Exception {
        BDDMockito.given(mapper.fromDto(any(ExampleDto.class))).willReturn(example);
        BDDMockito.given(service.create(any(Example.class))).willReturn(example);
        BDDMockito.given(mapper.toDto(any(Example.class))).willReturn(dto);

        mvc
                .perform(post(BASEURL).content("""
                        {"id": null, "name": "Example 1"}
                        """).contentType(MediaType.APPLICATION_JSON).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mapper, times(1)).fromDto(any(ExampleDto.class));
        verify(service, times(1)).create(any(Example.class));
        verify(mapper, times(1)).toDto(any(Example.class));
    }
}
