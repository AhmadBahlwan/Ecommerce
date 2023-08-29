package com.shopping.admin.shipping_rates;

public class ShippingRateAlreadyExistsException extends Exception {

    public ShippingRateAlreadyExistsException(String message) {
        super(message);
    }

}