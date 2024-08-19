package com.chms.churchmanageapi.controller;

import com.chms.churchmanageapi.dto.PaymentMethodDTO;
import com.chms.churchmanageapi.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
        @ApiResponse(responseCode = "401", description = "Not authorized"),
        @ApiResponse(responseCode = "404", description = "Payment not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
@RestController
@RequestMapping("/payment")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Operation(summary = "Add Payment Methods")
    @PostMapping(value = "/add-payment", produces = "application/json")
    public String addPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO){
        return paymentMethodService.addPaymentMethod(paymentMethodDTO);
    }

    @Operation(summary = "Get All Payment Methods")
    @GetMapping(value = "/get-payment", produces = "application/json")
    public List<PaymentMethodDTO> getAllPaymentMethod(){
        return paymentMethodService.getPaymentMethodList();
    }

}
