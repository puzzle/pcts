package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.example.ExampleDto;
import ch.puzzle.pcts.mapper.ExampleMapper;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.business.ExampleBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ExampleController.class)
class ExampleControllerIT {

    @MockitoBean
    private ExampleBusinessService service;

    @MockitoBean
    private ExampleMapper mapper;

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASEURL = "/api/v1/examples";

    private Example example;
    private ExampleDto requestDto;
    private ExampleDto dto;

    @BeforeEach
    void setUp() {
        example = new Example(1L, "Example 1");
        requestDto = new ExampleDto(null, "Example 1");
        dto = new ExampleDto(1L, "Example 1");
    }

    @DisplayName("Should successfully get all examples")
    @Test
    void shouldGetAllExamples() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(example));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(dto));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(dto.id()))
                .andExpect(jsonPath("$[0].text").value(dto.text()));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get example by id")
    @Test
    void shouldGetExampleById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(example);
        BDDMockito.given(mapper.toDto(any(Example.class))).willReturn(dto);

        mvc.perform(get(BASEURL + "/1").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().isOk());

        verify(service, times(1)).getById(eq(1L));
        verify(mapper, times(1)).toDto(any(Example.class));
    }

    @DisplayName("Should successfully create new example")
    @Test
    void shouldCreateNewExample() throws Exception {
        BDDMockito.given(mapper.fromDto(any(ExampleDto.class))).willReturn(example);
        BDDMockito.given(service.create(any(Example.class))).willReturn(example);
        BDDMockito.given(mapper.toDto(any(Example.class))).willReturn(dto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.text").value("Example 1"));

        verify(mapper, times(1)).fromDto(any(ExampleDto.class));
        verify(service, times(1)).create(any(Example.class));
        verify(mapper, times(1)).toDto(any(Example.class));
    }
}
