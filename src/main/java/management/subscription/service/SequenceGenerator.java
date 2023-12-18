package management.subscription.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import management.subscription.entity.Autosequence;

@Service
public class SequenceGenerator {

    @Autowired
    private MongoTemplate mongoTemplate;

    public long getSequenceNumber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);

        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
        Autosequence counter = mongoTemplate.findAndModify(query, update, options, Autosequence.class);

        return (counter != null) ? counter.getSeq() : 1;
    }
}

