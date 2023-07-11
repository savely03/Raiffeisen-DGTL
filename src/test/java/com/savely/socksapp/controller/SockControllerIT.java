package com.savely.socksapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.savely.socksapp.dto.SockDto;
import com.savely.socksapp.entity.Sock;
import com.savely.socksapp.exception.IncorrectOperationException;
import com.savely.socksapp.exception.IncorrectPaginationException;
import com.savely.socksapp.exception.IncorrectQuantityException;
import com.savely.socksapp.exception.SockNotFoundException;
import com.savely.socksapp.mapper.SockMapper;
import com.savely.socksapp.repository.SockRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SockControllerIT {

    private static final String ROOT = "/api/v1/socks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SockRepository sockRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SockMapper sockMapper;

    private final Faker faker = new Faker();

    private SockDto socksDto;

    private Sock socks;

    @BeforeEach
    void setUp() {
        socksDto = SockDto.builder().color(faker.color().name())
                .cottonPart(faker.random().nextInt(0, 100)).quantity(faker.random().nextInt(1, 100)).build();
        socks = Sock.builder().color(socksDto.getColor()).quantity(socksDto.getQuantity())
                .cottonPart(socksDto.getCottonPart()).build();
    }

    @Test
    void incomeWhenSocksDoesNotExistTest() throws Exception {
        assertThat(sockRepository.findByColorAndCottonPart(socksDto.getColor(), socksDto.getCottonPart())).isEmpty();

        mockMvc.perform(post(ROOT + "/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socksDto)))
                .andExpect(status().isOk());

        assertThat(sockRepository.findByColorAndCottonPart(socksDto.getColor(), socksDto.getCottonPart()))
                .isPresent().get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(socksDto);

    }

    @Test
    void incomeWhenSocksExistTest() throws Exception {
        socks = sockRepository.save(socks);

        mockMvc.perform(post(ROOT + "/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socksDto)))
                .andExpect(status().isOk());

        Optional<Sock> changedSocks = sockRepository.findByColorAndCottonPart(socksDto.getColor(), socksDto.getCottonPart());

        assertThat(changedSocks).isPresent();
        assertThat(changedSocks.get().getQuantity()).isEqualTo(socks.getQuantity() + socksDto.getQuantity());
        assertThat(changedSocks.get())
                .usingRecursiveComparison()
                .ignoringFields("quantity")
                .isEqualTo(socks);

    }

    @Test
    void outcomeSocksTest() throws Exception {
        socks = sockRepository.save(socks);
        socksDto.setQuantity(socks.getQuantity() - 1);

        mockMvc.perform(post(ROOT + "/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socksDto)))
                .andExpect(status().isOk());

        Optional<Sock> changedSocks = sockRepository.findByColorAndCottonPart(socksDto.getColor(), socksDto.getCottonPart());
        assertThat(changedSocks).isPresent();
        assertThat(changedSocks.get().getQuantity()).isEqualTo(socks.getQuantity() - socksDto.getQuantity());
        assertThat(changedSocks.get())
                .usingRecursiveComparison()
                .ignoringFields("quantity")
                .isEqualTo(socks);
    }

    @Test
    void outcomeWithDeleteSocksTest() throws Exception {
        socks = sockRepository.save(socks);

        mockMvc.perform(post(ROOT + "/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socksDto)))
                .andExpect(status().isOk());

        assertThat(sockRepository.findByColorAndCottonPart(socksDto.getColor(), socksDto.getCottonPart())).isEmpty();
    }


    @Test
    void outcomeWhenSocksDoesNotExistTest() throws Exception {
        mockMvc.perform(post(ROOT + "/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socksDto)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertThat(result.getResolvedException().getClass()).isEqualTo(SockNotFoundException.class));
    }

    @Test
    void outcomeWithIncorrectQuantityTest() throws Exception {
        socks = sockRepository.save(socks);
        socksDto.setQuantity(socks.getQuantity() + 1);

        mockMvc.perform(post(ROOT + "/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(socksDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException().getClass()).isEqualTo(IncorrectQuantityException.class));
    }

    void checkMoreAndLessOperations(Sock socks, String operation, int num) throws Exception {
        mockMvc.perform(get(ROOT + "/filter")
                        .param("color", socks.getColor())
                        .param("cottonPart", String.valueOf(socks.getCottonPart() + num))
                        .param("operation", operation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(socks.getId()))
                .andExpect(jsonPath("$[0].color").value(socks.getColor()))
                .andExpect(jsonPath("$[0].cottonPart").value(socks.getCottonPart()))
                .andExpect(jsonPath("$[0].quantity").value(socks.getQuantity()));
    }

    void checkMoreAndLessOperationsWithNoSocks(Sock socks, String operation) throws Exception {
        mockMvc.perform(get(ROOT + "/filter")
                        .param("color", socks.getColor())
                        .param("cottonPart", String.valueOf(socks.getCottonPart()))
                        .param("operation", operation))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void getSocksWithFiltersTest() throws Exception {
        socks = sockRepository.save(socks);
        checkMoreAndLessOperations(socks, "moreThan", -1);
        checkMoreAndLessOperations(socks, "lessThan", 1);
        checkMoreAndLessOperationsWithNoSocks(socks, "moreThan");
        checkMoreAndLessOperationsWithNoSocks(socks, "lessThan");
    }


    @Test
    void getSocksWithIncorrectOperationTest() throws Exception {
        mockMvc.perform(get(ROOT + "/filter")
                        .param("color", socks.getColor())
                        .param("cottonPart", String.valueOf(socks.getCottonPart()))
                        .param("operation", faker.name().name()))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException().getClass()).isEqualTo(IncorrectOperationException.class));
    }

    @Test
    void getSocksEqualTest() throws Exception {
        socks = sockRepository.save(socks);

        mockMvc.perform(get(ROOT + "/equal")
                        .param("color", socks.getColor())
                        .param("cottonPart", String.valueOf(socks.getCottonPart())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(socks.getId()))
                .andExpect(jsonPath("$.color").value(socks.getColor()))
                .andExpect(jsonPath("$.cottonPart").value(socks.getCottonPart()))
                .andExpect(jsonPath("$.quantity").value(socks.getQuantity()));
    }

    @Test
    void getSocksEqualWhenSocksDoesNotExistTest() throws Exception {
        mockMvc.perform(get(ROOT + "/equal")
                        .param("color", socks.getColor())
                        .param("cottonPart", String.valueOf(socks.getCottonPart())))
                .andExpect(status().isNotFound());
    }

    static Stream<Arguments> getPageAndSize() {
        return Stream.of(
                Arguments.of(1, 2),
                Arguments.of(2, 3),
                Arguments.of(3, 1),
                Arguments.of(4, 2),
                Arguments.of(1, 5)
        );
    }

    static Stream<Arguments> getIncorrectPageAndSize() {
        return Stream.of(
                Arguments.of(0, 5),
                Arguments.of(5, 0),
                Arguments.of(0, 0)
        );
    }


    @ParameterizedTest
    @MethodSource("getPageAndSize")
    void getSocksWithPageAndSize(int page, int size) throws Exception {
        List<SockDto> listSocksDto = sockRepository.saveAll(generateSocks()).stream()
                .map(sockMapper::toDto)
                .toList();

        mockMvc.perform(get(ROOT)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<SockDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            });
                    assertThat(actual).isEqualTo(listSocksDto.subList(page * size - size, page * size));
                });
    }

    @ParameterizedTest
    @MethodSource("getIncorrectPageAndSize")
    void getSocksWithIncorrectPageAndSize(int page, int size) throws Exception {
        mockMvc.perform(get(ROOT)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException().getClass()).isEqualTo(IncorrectPaginationException.class));
    }

    List<Sock> generateSocks() {
        return Stream.iterate(1, i -> i + 1)
                .map(i -> Sock.builder().color(faker.color().name()).quantity(i)
                        .cottonPart(faker.random().nextInt(0, 100)).build())
                .limit(10)
                .collect(Collectors.toList());
    }

    @AfterEach
    void cleanUp() {
        sockRepository.deleteAll();
    }
}