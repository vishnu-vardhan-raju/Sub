package management.subscription.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import management.subscription.entity.Subscription;

@Service
public class validator {
    
    public void subscriptionValidator(Subscription subscribe) {
        List<String> errorMessages = new ArrayList<>();

        if (subscribe.getCustomerName() == null || subscribe.getCustomerName().isEmpty()) {
            errorMessages.add("Customer name cannot be empty or null");
        }

        if (subscribe.getInvoiceAddress() == null || subscribe.getInvoiceAddress().isEmpty()) {
            errorMessages.add("Invoice address cannot be empty or null");
        }

        if (subscribe.getDeliveryAddress() == null || subscribe.getDeliveryAddress().isEmpty()) {
            errorMessages.add("Delivery address cannot be empty or null");
        }

        if (subscribe.getSubscriptionType() == null || subscribe.getSubscriptionType().isEmpty()) {
            errorMessages.add("SubscriptionType cannot be empty or null");
        }

        if (subscribe.getSubscriptionPlan() == null) {
            errorMessages.add("SubscriptionPlan cannot be empty or null");
        }

        if (subscribe.getExpiration() == null) {
            errorMessages.add("Expiration cannot be null");
        }

        if (subscribe.getRecurrence() == null) {
            errorMessages.add("Recurrence cannot be null");
        }

        if (subscribe.getCurrency() == null || subscribe.getCurrency().isEmpty()) {
            errorMessages.add("Currency cannot be empty or null");
        }

        if (subscribe.getProduct() == null) {
            errorMessages.add("Product cannot be empty or null");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
