package com.evanpenner.novusgis.views;

import com.evanpenner.novusgis.entities.Project;
import com.evanpenner.novusgis.repositories.ProjectRepository;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route("projects")
@RouteAlias("")
@PageTitle("Projects")
public class ProjectsView extends VerticalLayout {
    private final ProjectRepository projectRepository;

    public ProjectsView(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        buildBody();
    }


    private List<Project> managers;
    private Map<Project, List<Project>> staffGroupedByMangers;

    private Project draggedItem;

    private void buildBody() {

        List<Project> people = projectRepository.findAll();
        managers = people.stream().filter(project -> project.getParentProject() == null)
                .collect(Collectors.toList());
        staffGroupedByMangers = people.stream()
                .filter(person -> person.getParentProject() != null)
                .collect(Collectors.groupingBy(Project::getParentProject,
                        Collectors.toList()));

        TreeGrid<Project> treeGrid = setupTreeGrid();

        TreeData<Project> treeData = new TreeData<>();
        treeData.addItems(managers, this::getStaff);
        TreeDataProvider<Project> treeDataProvider = new TreeDataProvider<>(
                treeData);
        treeGrid.setDataProvider(treeDataProvider);

        treeGrid.setRowsDraggable(true);
        treeGrid.setDropMode(GridDropMode.ON_TOP);
        // Only allow dragging staff
        //treeGrid.setDragFilter(person -> !person.isManager());
        // Only allow dropping on managers
        //treeGrid.setDropFilter(person -> person.isManager());

        treeGrid.addDragStartListener(
                e -> draggedItem = e.getDraggedItems().get(0));

        treeGrid.addDropListener(e -> {
            Project newManager = e.getDropTargetItem().orElse(null);
            boolean isSameManager = newManager != null
                    && newManager.getId().equals(draggedItem.getParentProject().getId());

            if (newManager == null || isSameManager)
                return;

            draggedItem.setParentProject(newManager);

            treeData.removeItem(draggedItem);
            treeData.addItem(newManager, draggedItem);
            projectRepository.saveAll(List.of(draggedItem, newManager));
            treeDataProvider.refreshAll();
        });

        treeGrid.addDragEndListener(e -> draggedItem = null);

        add(treeGrid);
    }

    private static TreeGrid<Project> setupTreeGrid() {
        TreeGrid<Project> treeGrid = new TreeGrid<>();
        treeGrid.addHierarchyColumn(Project::getName)
                .setHeader("Sub-Project Name");
        treeGrid.addColumn(Project::getName).setHeader("Project Name");
        treeGrid.addColumn(project -> project.getCustomer().getName()).setHeader("Customer Name");
        //treeGrid.addColumn(Person::getEmail).setHeader("Email");

        return treeGrid;
    }

    private List<Project> getStaff(Project manager) {
        return staffGroupedByMangers.getOrDefault(manager,
                Collections.emptyList());
    }
}
