package com.springapp.repository;

import com.springapp.entity.DataSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
@Component
public interface DataSetRepository extends JpaRepository<DataSetEntity, UUID> {

    @Query("select dataSet from DataSetEntity dataSet where searchRequestId = :searchRequestId")
    List<DataSetEntity> findBySearchRequestId(@Param("searchRequestId") UUID searchRequestId);
}
