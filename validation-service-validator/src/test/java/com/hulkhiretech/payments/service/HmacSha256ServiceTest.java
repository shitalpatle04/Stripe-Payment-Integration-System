package com.hulkhiretech.payments.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.service.data.TestDataBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HmacSha256ServiceTest {

    @Test
    public void testCalculateHmacSha256ProducesDeterministic32ByteHmac() {
    	HmacSha256Service service = new HmacSha256Service(null);
        
    	 try {
    		 // Set the private secretKey field using reflection since we're not
    		 // running inside Spring

    		 Field secretKeyField =
    		     HmacSha256Service.class.getDeclaredField("secretKey");
    		 secretKeyField.setAccessible(true);

    		 // Use a deterministic test key
    		 secretKeyField.set(service, "THIS_IS_MY_SECRET");
    		 
    		 log.info("Successfully set secretKey field for testing");
		} catch (Exception e) {
			e.printStackTrace();
		}
        

        PaymentRequest request = TestDataBuilder.buildPaymentRequest();

        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try {
            json = mapper.writeValueAsString(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔥 correct method name
        String signature = service.calculateHmacSha256(json);

        assertNotNull(signature);

        byte[] decoded = Base64.getDecoder().decode(signature);

        assertEquals(32, decoded.length);

        // deterministic check
        String signature2 = service.calculateHmacSha256(json);
        assertEquals(signature, signature2);
    }
}