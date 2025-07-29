package ru.pro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.dto.WalletRequest;
import ru.pro.service.WalletService;

import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
@Import(WalletControllerTest.TestConfig.class)
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public WalletService clientService() {
            return mock(WalletService.class);
        }
    }

    @Nested
    @DisplayName("Валидные данные")
    class ValidInputTests {
        private static Stream<WalletRequest> provideValidWalletRequest() {
            return Stream.of(new WalletRequest("d8fcdc72-2b69-496f-9d5b-7a3cbd74d944", "100.00", "DEPOSIT"));
        }

        private static Stream<String> provideValidId() {
            return Stream.of("d8fcdc72-2b69-496f-9d5b-7a3cbd74d944");
        }

        @ParameterizedTest(name = "Валидный Request #{index}")
        @MethodSource("provideValidWalletRequest")
        void whenValidWalletRequestThenReturnsOk(WalletRequest request) throws Exception {
            String jsonContent = objectMapper.writeValueAsString(request);
            WalletDto dto = new WalletDto(UUID.fromString(request.getId()), request.getAmount());

            when(service.updateWalletBalance(any())).thenReturn(dto);

            mockMvc.perform(post("/api/v1/wallet")
                            .contentType(APPLICATION_JSON)
                            .content(jsonContent)
                            .accept(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON));

            verify(service).updateWalletBalance(any());
        }

        @ParameterizedTest(name = "Валидный Request #{index}")
        @MethodSource("provideValidId")
        void whenValidIdThenReturnsOk(String id) throws Exception {
            WalletDto dto = new WalletDto(UUID.fromString(id), "100.00");

            when(service.getWalletBalance(id)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/wallets/{walletId}", id)
                            .accept(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.amount").value("100.00"));

            verify(service).getWalletBalance(id);
        }
    }

    @Nested
    @DisplayName("Невалидные данные")
    class InvalidInputTests {
        private static Stream<WalletRequest> provideInvalidWalletRequest() {
            return Stream.of(
                    new WalletRequest("123", "100.00", "DEPOSIT"),
                    new WalletRequest(null, "100.00", "DEPOSIT"),
                    new WalletRequest("d8fcdc72-2b69-496f-9d5b-7a3cbd74d944", "Y0.00", "DEPOSIT"),
                    new WalletRequest("d8fcdc72-2b69-496f-9d5b-7a3cbd74d944", null, "DEPOSIT"),
                    new WalletRequest("d8fcdc72-2b69-496f-9d5b-7a3cbd74d944", "100.00", "1q7"),
                    new WalletRequest("d8fcdc72-2b69-496f-9d5b-7a3cbd74d944", "100.00", null),
                    new WalletRequest("d8fcdc72-2b69-496f-9d5b-7a3cbd74d944", null, null),
                    new WalletRequest(null, null, "DEPOSIT"),
                    new WalletRequest(null, null, null)
            );
        }

        private static Stream<String> provideInvalidId() {
            return Stream.of("123", null);
        }

        @ParameterizedTest(name = "Невалидный Request: {0}")
        @MethodSource("provideInvalidWalletRequest")
        void whenInvalidWalletRequestThenReturnsBadRequest(WalletRequest request) throws Exception {
            String jsonContent = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/wallet")
                            .contentType(APPLICATION_JSON)
                            .content(jsonContent)
                            .accept(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "Невалидный Request: {0}")
        @MethodSource("provideInvalidId")
        void whenInvalidIdThenReturnsBadRequest(String id) throws Exception {

            mockMvc.perform(get("/api/v1/wallets/{walletId}", id)
                            .accept(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is5xxServerError());
        }
    }
}