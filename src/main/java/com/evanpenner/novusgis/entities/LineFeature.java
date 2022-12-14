package com.evanpenner.novusgis.entities;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "line_features")
public class LineFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LineString geometry;
    private String name;

    @ManyToOne
    private FeatureType featureType;
}
