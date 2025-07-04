package com.example.be.controller;

import com.example.be.config.MomoConfig;

import com.example.be.constants.AppConstants;
import com.example.be.service.PaymentService;
import com.example.be.utils.HmacUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class MomoController {

    private final MomoConfig momoConfig;
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/momo")
    public ResponseEntity<?> createPayment(@RequestParam long amount, @RequestParam String orderId) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toán vé xem phim #" + orderId;

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("partnerCode", momoConfig.getPartnerCode());
        body.put("accessKey", momoConfig.getAccessKey());
        body.put("requestId", requestId);
        body.put("amount", String.valueOf(amount));
        body.put("orderId", orderId);
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", momoConfig.getRedirectUrl());
        body.put("ipnUrl", momoConfig.getIpnUrl());
        body.put("extraData", "");
        body.put("requestType", momoConfig.getRequestType());

        String rawSignature = String.format(
                "accessKey=%s&amount=%s&extraData=&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                momoConfig.getAccessKey(), amount, momoConfig.getIpnUrl(), orderId, orderInfo,
                momoConfig.getPartnerCode(), momoConfig.getRedirectUrl(), requestId, momoConfig.getRequestType());

        String signature = HmacUtil.hmacSHA256(momoConfig.getSecretKey(), rawSignature);
        body.put("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(momoConfig.getEndpoint(), request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String payUrl = (String) response.getBody().get("payUrl");
            return ResponseEntity.ok(Map.of("payUrl", payUrl));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tạo thanh toán thất bại");
    }

    @GetMapping("/momo-return")
    public void momoReturn(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        String resultCode = params.get("resultCode");
        String orderId = params.get("orderId"); // lấy orderId từ params

        String redirectUrl;
        if ("0".equals(resultCode)) {
            redirectUrl = AppConstants.Client_URL+"/user/payment-success?orderId=" + orderId;
        } else {
            redirectUrl = AppConstants.Client_URL+"/user/payment-failed?orderId=" + orderId;
        }

        response.sendRedirect(redirectUrl);
    }


    @PostMapping("/momo-ipn")
    public ResponseEntity<String> momoIpn(@RequestBody Map<String, Object> data) {
        String resultCode = String.valueOf(data.get("resultCode"));
        String orderId = (String) data.get("orderId");
        paymentService.updatePaymentStatus(orderId, resultCode);
        return ResponseEntity.ok("Received");
    }
}
