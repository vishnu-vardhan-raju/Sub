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

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import management.subscription.Generic.CommonRequest;
import management.subscription.Generic.CommonResponse;
import management.subscription.entity.Subscription;
import management.subscription.service.DemoService;
import management.subscription.service.ValidationException;

import java.util.List;
import java.util.Map;




@RestController
@Slf4j
@RequestMapping("/controller")
public class Controller {

    @Autowired
    private DemoService demoService;
   


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CommonRequest<Subscription> users){
       try {
         CommonResponse<List<Subscription>> response =demoService.createSubscription( users.getData());
        return ResponseEntity.ok(response);

       } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    
    @GetMapping
    public ResponseEntity<?> getSubscription(@RequestParam Map<String, Object> filterFields){
        try {
          ResponseEntity<?> response = demoService.getSubscription(filterFields);
         return ResponseEntity.ok(response);
        } catch (Exception e) {
          // TODO: handle exception
                  return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PatchMapping
    public ResponseEntity<?> updateSubscription(@Validated @RequestBody CommonRequest<Subscription> Updatables){
       try {
        CommonResponse<?> response = demoService.updateSubscrition( Updatables.getData());
        return ResponseEntity.ok(response);
       } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
       }

    }

    @DeleteMapping
    public ResponseEntity<?> deleteSubscription(@Validated @RequestBody CommonRequest<Long> Updatables){
       try {
        CommonResponse<?> response = demoService.deleteSubscriptions( Updatables.getData());
        return ResponseEntity.ok(response);
       } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
       }

    }

}
