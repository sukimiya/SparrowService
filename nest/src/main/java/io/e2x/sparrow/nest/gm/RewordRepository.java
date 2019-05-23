package io.e2x.sparrow.nest.gm;

import io.e2x.sparrow.nest.gm.vo.DispatcherReword;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

public interface RewordRepository extends MongoRepository <DispatcherReword, BigInteger> {
    DispatcherReword findBySsn(String s);
    List<DispatcherReword> findAllByTimestampBetween(long start, long end);
    List<DispatcherReword> findAllByTimestampBetweenAndRoleIsLike(long start, long end, String role);
    List<DispatcherReword> findAllByTimestampBetweenAndItemTypeIsLike(long start, long end, String itemType);
    List<DispatcherReword> findAllByItemType(Integer itemType);
    List<DispatcherReword> findAllByItemTypeIsLike(String s);
    List<DispatcherReword> findAllByRoleIsLike(String s);
    List<DispatcherReword> findAllByRole(String s);
    List<DispatcherReword> findAllByItemName(String s);
}
