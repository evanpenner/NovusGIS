package com.evanpenner.novusgis.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "feature_types")
public class FeatureType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String icon;
    private String color;

    private String style;

    @ManyToMany
    private List<StatusOption> statusOptions;
}
