package com.shopping.checkout.paypal;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class PaypalApiTests {

    private static final String BASE_URL="https://api-m.sandbox.paypal.com";  // https://api.sandbox.paypal.com
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "ASHJdZHPfTg9zWmtknN7Qiiu4H0_aoiW_4zNWUy6rbCwY6a58pl7gURmpsNcOqpbm0F7fQsrMD2Pz-JF";
    private static final String CLIENT_SECRET = "ELk8q7ZBZQVop28jD4O5JUDyue2wBZBsXmJGLfjw66BL0_hRJcLmCmEi-7mWjw0dZH_H71Ovhfba1DDo";

    @Test
    public void testGetOrderDetails() {
        String orderId = "6YT13658P96239219";   //transaction id which is printed when complete the checkout
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.add("Authorization", "Bearer access_tokenA21AALJqsFJkm9pmjHQok_xdc7i3n_3CkkHcnkWiEPL0x2CvTp9JEk1v32_q4P2vPGKue90pc_aBDZ8x2QnU1jDUpHRiZOPMw");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PaypalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PaypalOrderResponse.class);
        PaypalOrderResponse orderResponse = response.getBody();

        System.out.println("Order ID: " + orderResponse.getId());
        System.out.println("Validate: " + orderResponse.validate(orderId));
    }

    @Test
    public void test() throws IOException {
        URL url = new URL("https://api-m.sandbox.paypal.com/v2/checkout/orders/5O190127TN364715T");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        httpConn.setRequestProperty("Authorization", "Bearer A21AALJqsFJkm9pmjHQok_xdc7i3n_3CkkHcnkWiEPL0x2CvTp9JEk1v32_q4P2vPGKue90pc_aBDZ8x2QnU1jDUpHRiZOPMw");

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
    }
}
