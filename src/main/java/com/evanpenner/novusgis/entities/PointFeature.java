package com.evanpenner.novusgis.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "point_features")
public class PointFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Point geometry;
}
