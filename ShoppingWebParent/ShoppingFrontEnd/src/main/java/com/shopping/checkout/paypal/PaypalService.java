package com.shopping.checkout.paypal;

import com.shopping.setting.PaymentSettingBag;
import com.shopping.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Component
public class PaypalService {
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private final SettingService settingService;

    public PaypalService(SettingService settingService) {
        this.settingService = settingService;
    }
//    @Autowired
//    private RestTemplate restTemplate;

    public boolean validateOrder(String orderId) throws PaypalApiException {
        PaypalOrderResponse orderResponse = getOrderDetails(orderId);
        return orderResponse != null && orderResponse.validate(orderId);
    }

    private PaypalOrderResponse getOrderDetails(String orderId) throws PaypalApiException {
        ResponseEntity<PaypalOrderResponse> response = makeRequest(orderId);
        HttpStatusCode statusCode = response.getStatusCode();

        if (!statusCode.equals(OK)) {
            throwExceptionForNonOkResponse(statusCode);
        }

        return response.getBody();
    }

    private ResponseEntity<PaypalOrderResponse> makeRequest(String orderId) {
        PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
        String baseURL = paymentSettings.getURL();
        String requestURL = baseURL + GET_ORDER_API + orderId;
        String clientId = paymentSettings.getClientID();
        String clientSecret = paymentSettings.getClientSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.add("Authorization", "Bearer access_tokenA21AALJqsFJkm9pmjHQok_xdc7i3n_3CkkHcnkWiEPL0x2CvTp9JEk1v32_q4P2vPGKue90pc_aBDZ8x2QnU1jDUpHRiZOPMw");
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestURL, HttpMethod.GET, request, PaypalOrderResponse.class);
    }

    private void throwExceptionForNonOkResponse(HttpStatusCode statusCode) throws PaypalApiException {
        String message = null;
        if (statusCode.equals(NOT_FOUND)) {
            message = "Order ID  not fount";
        } else if (statusCode.equals(BAD_REQUEST)) {
            message = "Bad Request to paypal Checkout API";
        } else if (statusCode.equals(INTERNAL_SERVER_ERROR)) {
            message = "Paypal Server Error";
        } else {
            message = "Paypal returned non-Ok status code";
        }
        throw new PaypalApiException(message);
    }
}
