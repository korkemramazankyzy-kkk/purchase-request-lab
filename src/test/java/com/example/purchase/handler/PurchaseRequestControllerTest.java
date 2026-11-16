
package com.example.purchase.handler;

import com.example.purchase.domain.DuplicatePurchaseRequest;
import com.example.purchase.domain.PurchaseRequest;
import com.example.purchase.domain.PurchaseRequestNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.purchase.config.Application;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseRequestController.class)
@ContextConfiguration(classes = Application.class)
@Import(ApiExceptionHandler.class)class PurchaseRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseRequestRegistrationService registrationService;

    private final UUID id = UUID.randomUUID();

    @Test
    void createReturns201AndLocation() throws Exception {
        PurchaseRequest request = new PurchaseRequest(
                id, "PR-TEST-101", "DRAFT", "Test purchase request"
        );

        when(registrationService.register("PR-TEST-101", "Test purchase request"))
                .thenReturn(request);

        mockMvc.perform(post("/api/v1/purchase-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "businessKey": "PR-TEST-101",
                                  "title": "Test purchase request"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/api/v1/purchase-requests/" + id
                ))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.businessKey").value("PR-TEST-101"));
    }

    @Test
    void createWithBlankTitleReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/purchase-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "businessKey": "PR-TEST-102",
                                  "title": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.correlationId").exists());

        verify(registrationService, never())
                .register("PR-TEST-102", "");
    }

    @Test
    void getExistingRequestReturns200() throws Exception {
        PurchaseRequest request = new PurchaseRequest(
                id, "PR-TEST-103", "DRAFT", "Test purchase request"
        );

        when(registrationService.getById(id)).thenReturn(request);

        mockMvc.perform(get("/api/v1/purchase-requests/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.businessKey").value("PR-TEST-103"));
    }

    @Test
    void getMissingRequestReturns404() throws Exception {
        when(registrationService.getById(id))
                .thenThrow(new PurchaseRequestNotFoundException("Request not found"));

        mockMvc.perform(get("/api/v1/purchase-requests/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.correlationId").exists());
    }

    @Test
    void createDuplicateReturns409() throws Exception {
        when(registrationService.register("PR-TEST-104", "Test purchase request"))
                .thenThrow(new DuplicatePurchaseRequest("PR-TEST-104"));

        mockMvc.perform(post("/api/v1/purchase-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "businessKey": "PR-TEST-104",
                                  "title": "Test purchase request"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.correlationId").exists());
    }

    @Test
    void invalidUuidReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/purchase-requests/not-a-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void correlationIdIsReturnedInResponse() throws Exception {
        String correlationId = "test-correlation-123";

        when(registrationService.getById(id)).thenReturn(
                new PurchaseRequest(id, "PR-TEST-105", "DRAFT", "Test request")
        );

        mockMvc.perform(get("/api/v1/purchase-requests/" + id)
                        .header("X-Correlation-Id", correlationId))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Correlation-Id", correlationId));
    }
}
