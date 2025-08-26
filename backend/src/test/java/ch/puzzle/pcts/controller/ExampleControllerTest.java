package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.mapper.ExampleMapper;
import ch.puzzle.pcts.service.business.ExampleBusinessService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ExampleControllerTest {

    @Mock
    private ExampleMapper mapper;

    @Mock
    private ExampleBusinessService service;

    @InjectMocks
    private ExampleController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

}