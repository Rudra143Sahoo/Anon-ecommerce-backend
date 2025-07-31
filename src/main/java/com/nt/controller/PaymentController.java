package com.nt.controller;


import com.nt.dto.PaymentDTO;
import com.nt.model.PaymentEntity;
import com.nt.repo.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	@Autowired
	 private PaymentRepository paymentRepository ;

    @PostMapping("/create-order")
    public String createOrder(@RequestParam int amount) {
        try {
            RazorpayClient client = new RazorpayClient("rzp_test_qT42bSBD4quTnJ" , "hFh2yxEJ9FxHZMsyUmceXyaw");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
            orderRequest.put("payment_capture", true);

            Order order = client.orders.create(orderRequest);
            return order.toJson().toString(); // ✅ Sends proper JSON response

        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
 // PaymentController.java
    @PostMapping("/save-payment")
    public ResponseEntity<String> savePayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            // Ideally, verify signature first for security (optional but recommended)
            
            // Save to DB
            PaymentEntity payment = new PaymentEntity();
            payment.setOrderId(paymentDTO.getRazorpayOrderId());
            payment.setPaymentId(paymentDTO.getRazorpayPaymentId());
            payment.setStatus(paymentDTO.getStatus());

            paymentRepository.save(payment);

            return ResponseEntity.ok("Payment info saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save payment: " + e.getMessage());
        }
    }

}

