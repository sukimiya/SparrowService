package io.e2x.sparrow.nest.gm;

import io.e2x.sparrow.nest.gm.vo.ItemTipVO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemTipRepository extends MongoRepository<ItemTipVO, Integer> {
}
