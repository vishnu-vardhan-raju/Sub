package management.subscription.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuditEntity {
    
    
    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;


    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

}
