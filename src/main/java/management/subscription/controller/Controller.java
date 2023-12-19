package management.subscription.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;
import management.subscription.Generic.CommonRequest;
import management.subscription.Generic.CommonResponse;
import management.subscription.entity.Subscription;
import management.subscription.service.SubscriptionService;
import java.util.List;
import java.util.Map;




@RestController
@Slf4j
@RequestMapping("api/subscriptions")
public class Controller {

    @Autowired
    private SubscriptionService subscriptionService;
   


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CommonRequest<Subscription> users){
      long startTime = System.currentTimeMillis();
       try {
         CommonResponse<List<Subscription>> response =subscriptionService.createSubscription( users.getData());
         long endTime = System.currentTimeMillis();
        log.info("API response in {} ms", endTime - startTime);
        return ResponseEntity.ok(response);

       } catch (Exception e) {
         log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    
    @GetMapping
    public ResponseEntity<?> getSubscription(@RequestParam Map<String, Object> filterFields){
      long startTime = System.currentTimeMillis();
        try {
          ResponseEntity<?> response = subscriptionService.getSubscription(filterFields);
            long endTime = System.currentTimeMillis();
        log.info("API response in {} ms", endTime - startTime);
         return ResponseEntity.ok(response);
        } catch (Exception e) {
          log.warn(e.getMessage());
         return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateSubscription(@Validated @RequestBody CommonRequest<Subscription> Updatables){
      long startTime = System.currentTimeMillis();
       try {
        CommonResponse<?> response = subscriptionService.updateSubscrition( Updatables.getData());
          long endTime = System.currentTimeMillis();
        log.info("API response in {} ms", endTime - startTime);
        return ResponseEntity.ok(response);
       } catch (Exception e) {
          log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
       }

    }

    @DeleteMapping
    public ResponseEntity<?> deleteSubscription(@Validated @RequestBody CommonRequest<Long> Updatables){
      long startTime = System.currentTimeMillis();
       try {
        CommonResponse<?> response = subscriptionService.deleteSubscriptions( Updatables.getData());
          long endTime = System.currentTimeMillis();
        log.info("API response in {} ms", endTime - startTime);
        return ResponseEntity.ok(response);
       } catch (Exception e) {
          log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
       }

    }

}
