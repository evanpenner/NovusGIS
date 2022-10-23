package com.evanpenner.novusgis.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentProject")
    private Project parentProject;

    @OneToMany(mappedBy = "parentProject")
    @JsonBackReference
    private List<Project> subProjects;

    private String name;
    @ManyToOne
    private Customer customer;

    @OneToMany
    private List<PointFeature> pointFeatures;

    @OneToMany
    private List<LineFeature> lineFeatures;

    public List<PointFeature> getPointFeatures() {
        if (subProjects != null && subProjects.size() != 0) {
            List<PointFeature> pointFeatures = new ArrayList<>();
            for (Project subProj : subProjects) {
                pointFeatures.addAll(subProj.getPointFeatures());
            }
            pointFeatures.addAll(this.pointFeatures);
            return pointFeatures;
        }
        return this.pointFeatures;
    }

    public List<LineFeature> getLineFeatures() {
        if (subProjects != null && subProjects.size() != 0) {
            List<LineFeature> lineFeatures = new ArrayList<>();
            for (Project subProj : subProjects) {
                lineFeatures.addAll(subProj.getLineFeatures());
            }
            lineFeatures.addAll(this.lineFeatures);
            return lineFeatures;
        }
        return this.lineFeatures;

    }
}
