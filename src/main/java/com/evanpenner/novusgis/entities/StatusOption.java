package com.evanpenner.novusgis.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class StatusOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the status option.
     */
    private String name;

    /**
     * The permission that will be required.
     */
    private String scope;

    /**
     * The possible values to be used.
     */
    @OneToMany
    private List<StatusValue> values;
}
