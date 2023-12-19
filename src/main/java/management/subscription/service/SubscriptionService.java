package management.subscription.service;



// import java.lang.reflect.Field;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import management.subscription.Generic.CommonResponse;
import management.subscription.entity.Subscription;
import management.subscription.MongoAuditConfig.AuditingConfig;



@Service
@Slf4j
public class SubscriptionService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SequenceGenerator sequence;

    @Autowired
    private validator validator;

    @Autowired
    private AuditingConfig auditor;

    public CommonResponse<List<Subscription>> createSubscription(List<Subscription> Users){
        long startTime = System.currentTimeMillis();
        for(Subscription user : Users){
            validator.subscriptionValidator(user);
            user.setId(sequence.getSequenceNumber(Subscription.Sequence));
            user.setCreateDate(LocalDateTime.now());
            user.setCreatedBy(auditor.myAuditorProvider().getCurrentAuditor().get());
        }
        mongoTemplate.insertAll(Users);
        long endTime = System.currentTimeMillis();
        log.info("DB query executed in {} ms", endTime - startTime);

        return new CommonResponse<List<Subscription>>(Users,"Subscriptions Created Successfully");
    }


    public ResponseEntity<?> getSubscription(@RequestParam Map<String, Object> filterFields){
        long startTime = System.currentTimeMillis();

            Object pageObject = filterFields.remove("page");
            Object sizeObject = filterFields.remove("size");
            Object sortFieldObject = filterFields.remove("sortField");
            Object sortInObject = filterFields.remove("sortIn");
            Object pagenullObject = filterFields.remove("pagenull");
            int page = (pageObject != null) ? Integer.parseInt(pageObject.toString()) : 0;
            int size = (sizeObject != null) ? Integer.parseInt(sizeObject.toString()) : 5;
            String sortField = (sortFieldObject != null) ? sortFieldObject.toString() : "id";
            String sortIn = (sortInObject != null) ? (String) sortInObject : "asc";
            boolean pagenull = (pagenullObject != null) ? Boolean.parseBoolean(pagenullObject.toString()) : false;
            Sort.Direction direction = sortIn.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortField);
            Pageable pageable = PageRequest.of(page, size, sort);

            Query query = new Query();

            // Adding criteria for each field in the filterFields map
            for (Map.Entry<String, Object> entry : filterFields.entrySet()) {
                String fieldName = entry.getKey();
                Object fieldValue = entry.getValue();
        
                // Special handling for the 'id' field
                if ("id".equals(fieldName)) {
                    try {
                        // Assuming 'id' is a long value
                        query.addCriteria(Criteria.where(fieldName).is(Long.parseLong(fieldValue.toString())));
                    } catch (NumberFormatException e) {
                        // Handle parsing error if necessary
                        e.printStackTrace();
                        return ResponseEntity.badRequest().body(new CommonResponse<>(null, "Invalid 'id' value."));
                    }
                } else {
                    // General handling for other fields
                    query.addCriteria(Criteria.where(fieldName).is(fieldValue));
                }
            }

            // Applying pagination settings if pagenull is false
            if (!pagenull) {
                query.with(pageable);
            }

            long count = mongoTemplate.count(query, Subscription.class);
            List<Subscription> subscriptions = mongoTemplate.find(query, Subscription.class);

            if (!subscriptions.isEmpty()) {
                if (!pagenull) {
                    long endTime = System.currentTimeMillis();
                    log.info("DB query executed in {} ms", endTime - startTime);
                    // with pagination settings
                    return ResponseEntity.ok(new CommonResponse<>(new PageImpl<>(subscriptions, pageable, count), "Subscriptions retrieved successfully."));
                } else {
                    // If pagenull is true, return all results without pagination
                    long endTime = System.currentTimeMillis();
                    log.info("DB query executed in {} ms", endTime - startTime);
                    return ResponseEntity.ok(new CommonResponse<>(subscriptions, "Subscriptions retrieved successfully."));
                }
            } else {
                // If subscriptions list is empty
                return ResponseEntity.ok(new CommonResponse<>(null, "No subscriptions found."));
            }
        
    }

    public CommonResponse<?> updateSubscrition(List<Subscription> Updatables){
        long startTime = System.currentTimeMillis();

        List<Long> notFoundIds = new ArrayList<>();
        List<Subscription> updatedSubscriptions = new ArrayList<>();

        for (Subscription updatedSubscription : Updatables) {
           long subscriptionId = updatedSubscription.getId();
           Subscription existingSubscription = mongoTemplate.findById(subscriptionId, Subscription.class);
           if (existingSubscription != null) {
            updateSubscriptionFields(existingSubscription, updatedSubscription);
            mongoTemplate.save(existingSubscription);
            updatedSubscriptions.add(existingSubscription);
           }
           else {
            notFoundIds.add(subscriptionId);
            }
           
        }
        if (!notFoundIds.isEmpty()) {
            String errorMessage = "Subscriptions with IDs not found in the database: " +
                    notFoundIds.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", "));
            return new CommonResponse<>(errorMessage);
        }
        long endTime = System.currentTimeMillis();
        log.info("DB query executed in {} ms", endTime - startTime);

        return new CommonResponse<>(updatedSubscriptions, "Subscriptions updated successfully.");
    }

    public Subscription updateSubscriptionFields(Subscription existing, Subscription updated){
            if (!updated.getCustomerName().equals(existing.getCustomerName())) {
                throw new  ValidationException(List.of("Customer name should match with the existing user"));
            }
            existing.setUpdatedBy(auditor.myAuditorProvider().getCurrentAuditor().get());
            if (updated.getInvoiceAddress()!=null){
                existing.setInvoiceAddress(updated.getInvoiceAddress());
            }
            if (updated.getDeliveryAddress()!=null) {
                existing.setDeliveryAddress(updated.getDeliveryAddress());
            }
            if (updated.getSubscriptionPlan()!=null){ 
                existing.setSubscriptionPlan(updated.getSubscriptionPlan());
            }
            if (updated.getExpiration()!=null){ 
                existing.setExpiration(updated.getExpiration());
            }
            if (updated.getSubscriptionType()!=null) {
                existing.setSubscriptionType(updated.getSubscriptionType());
            }
            if (updated.getRecurrence()!=null) {
                existing.setRecurrence(updated.getRecurrence());
            }
            if (updated.getCurrency()!=null) {
                existing.setCurrency(updated.getCurrency());
            }
            if (updated.getProduct()!=null) {
                existing.setProduct(updated.getProduct());
            }

            return existing;
    }

    
    
    public CommonResponse<?> deleteSubscriptions(List<Long> subscriptionIds){
        long startTime = System.currentTimeMillis();
        List<Long> FoundIds = new ArrayList<>();
        List<Long> notFoundIds = new ArrayList<>();

        for (Long subscriptionId : subscriptionIds){
            Query query = new Query(Criteria.where("_id").is(subscriptionId));
            Subscription mongoSubscription = mongoTemplate.findOne(query, Subscription.class);
            if (mongoSubscription != null){
              FoundIds.add(subscriptionId);  
            }
            else{
                notFoundIds.add(subscriptionId);
            }
        }
        List<Subscription> deletedSubscriptions = new ArrayList<>();
       
        if(notFoundIds.size() > 0 && notFoundIds != null){
            String errorMessage = "Subscriptions with IDs not found in the database: " +
                        notFoundIds.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(", "));
             long endTime = System.currentTimeMillis();
            log.info("DB query executed in {} ms", endTime - startTime);
            return  new CommonResponse<>(errorMessage);
        }
         else if (FoundIds.size() > 0 && FoundIds != null){
            for (long id : FoundIds){
               Query query = new Query(Criteria.where("_id").is(id));
               Subscription deletedSubscription = mongoTemplate.findAndRemove(query, Subscription.class);
                deletedSubscriptions.add(deletedSubscription);
                
            }
        }  else{
             return  new CommonResponse<>("Input cannot be empty");
        }
        long endTime = System.currentTimeMillis();
        log.info("DB query executed in {} ms", endTime - startTime);
        return new CommonResponse<>(deletedSubscriptions,"Subscriptions Deleted successfully");
    }

}   
