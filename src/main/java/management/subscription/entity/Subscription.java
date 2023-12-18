package management.subscription.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import management.subscription.Enum.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "new")
public class Subscription extends AuditEntity{

    
   @Transient
    public static final String Sequence = "sequence";

    @Id
    private long id;
    @NotEmpty(message = "Customer Name should not be empty")
    private String customerName;
    @NotNull( message = "Please provide a valid invoice address")
    private String invoiceAddress;
    @NotNull(message = "Please provide a valid Delivery Address")
    private String deliveryAddress;
    @NotNull( message ="Please Select a valid Subscription Plan")
    private Plan subscriptionPlan;
    @NotNull( message = "Expiration can not be null")
    private Date expiration;
    @NotNull(message = "Please select a valid Subscription Type")
    private String subscriptionType;
    @NotNull
    private Recurrence recurrence;
    @NotNull
    private String currency;
    @NotNull( message = "Product can not be Empty")
    private Product product;
   
}
