package com.springapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author davidgiametta
 * @since 4/6/18
 */
@Entity
@Data
@Builder
@Table(name = "dataset")
@AllArgsConstructor
@NoArgsConstructor
public class DataSetEntity {
    @Id
    private UUID id;

    private UUID searchRequestId;

    private String data;
}
