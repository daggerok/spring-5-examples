package daggerok.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query(
      value = " select o.orderNumber from #{#entityName} o ",
      countQuery = " select count(o.id) from #{#entityName} o "
  )
  Page<OrderNumber> findAllOrderNumbers(final Pageable pageable);

  @Query(
      value = " select o.price from #{#entityName} o ",
      countQuery = " select count(o.id) from #{#entityName} o "
  )
  List<Price> findAllPrices();
}
