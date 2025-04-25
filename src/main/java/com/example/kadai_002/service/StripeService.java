package com.example.kadai_002.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.kadai_002.form.ReservationInputForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
    private String stripeApiKey;
   
    public String createStripeSession(String houseName, ReservationInputForm form, HttpServletRequest httpServletRequest) {
    	 Stripe.apiKey = stripeApiKey;
    	 
        String requestUrl = new String(httpServletRequest.getRequestURL());
        
        // userId を null チェック
        Integer userId = form.getUserId();
        String userIdStr = (userId != null) ? userId.toString() : "defaultUserId";
           
        
        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()   
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(houseName)
                                        .build())
                                .setUnitAmount(form.getAmount().longValue())
                                .setCurrency("jpy")                                
                                .build())
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved")
                .setCancelUrl(requestUrl.replace("/reservations/confirm", ""))
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                    .putMetadata("houseId", form.getHouseId().toString())
                    .putMetadata("userId", userIdStr)
                    .putMetadata("reservationDate", form.getReservationDate())
                    .putMetadata("reservationTime", form.getReservationTime())
                    .putMetadata("numberOfPeople", form.getNumberOfPeople().toString())
                    .putMetadata("amount", form.getAmount().toString())
                    .build())
                .build();
        try {
            Session session = Session.create(params);
            return session.getId();
        } catch (StripeException e) {
            e.printStackTrace();
            return "";
        }
    } 

}
    
