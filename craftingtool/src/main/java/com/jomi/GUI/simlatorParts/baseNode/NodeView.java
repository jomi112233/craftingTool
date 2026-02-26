package com.jomi.GUI.simlatorParts.baseNode;

import java.util.ArrayList;
import java.util.List;

import com.jomi.App;
import com.jomi.GUI.simlatorParts.ConnectionView;
import com.jomi.Handlers.Init.project.Connection;
import com.jomi.Handlers.Init.project.Node;
import com.jomi.Handlers.Init.project.Project;
import com.jomi.Handlers.Item.LoadedItem;
import com.jomi.Util.TurriHaku;
import com.jomi.Util.NodeShit.searchAction;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class NodeView extends StackPane {

    private final Project project;
    private final Node node;

    private final VBox root = new VBox();
    private final HBox header = new HBox();
    private final VBox body = new VBox();
    private final VBox infoArea = new VBox();
    private final Region spacer1 = new Region();
    private final Region spacer2 = new Region();

    private final Button toggleButton = new Button("+");
    private final Label titleLabel = new Label();

    private Circle inputPort;
    private Circle outputPort;

    private double dragOffsetX;
    private double dragOffsetY;

    private boolean expanded = false;
    private List<NodeSection> dynamicSections;

    public NodeView(Node node, Project project) {
        this.node = node;
        this.project = project;
        dynamicSections = new ArrayList<>();

        getStyleClass().add("node-root");

        header.getStyleClass().add("node-header");

        toggleButton.getStyleClass().add("node-toggle");
        toggleButton.setOnAction(e -> toggle());

        titleLabel.getStyleClass().add("node-title");

        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        header.getChildren().addAll(toggleButton, spacer1, titleLabel, spacer2, infoArea);

        body.getStyleClass().add("node-body");
        body.setVisible(false);
        body.setManaged(false);

        root.getChildren().addAll(header, body);
        getChildren().add(root);

        createPorts();
        enableContextMenu();
        enableDragging();
        enablePortConnections();
    }

    private void toggle() {
        expanded = !expanded;
        toggleButton.setText(expanded ? "-" : "+");

        body.setVisible(expanded);
        body.setManaged(expanded);

        if (expanded) {
            body.getChildren().setAll(renderSections());
        }
    }

    // shows node data
    // determined in the node it self
    private VBox renderSections() {
        VBox box = new VBox(12);
        box.setFillWidth(true);

        for (NodeSection section : getSections()) {

            Label sectionTitle = new Label(section.title());
            sectionTitle.getStyleClass().add("node-section-title");

            VBox itemBox = new VBox(6);

            for (String item : section.items()) {


                if (item.startsWith("search:")) {
                    String key = item.substring(7);
                    itemBox.getChildren().add(renderSearchField(section.title(), key));
                    continue;
                }

                Label label = new Label(item);
                label.setWrapText(true);
                label.getStyleClass().add("node-modifier");
                label.getStyleClass().add("node-item");

                label.setOnMouseClicked(e -> onSectionItemClicked(section.title(), item));

                itemBox.getChildren().add(label);
            }

            box.getChildren().addAll(sectionTitle, itemBox);
        }

        return box;
    }

    private VBox renderSearchField(String section, String key) {

        VBox container = new VBox(4);

        TextField search = new TextField();
        search.setPromptText("Search " + key + "...");

        ListView<String> results = new ListView<>();
        results.setMaxHeight(150);

        // Load all possible entries from the node
        List<? extends searchAction> all = getSearchSource(section, key);

        // Initial fill
        results.getItems().setAll(
            all.stream().map(a -> a.getName()).toList()
        );

        // Live filtering using TurriHaku
        search.textProperty().addListener((obs, oldV, newV) -> {
            List<? extends searchAction> filtered = TurriHaku.search(all, newV);
            results.getItems().setAll(
                filtered.stream().map(a -> a.getName()).toList()
            );
        });

        // Click result
        results.setOnMouseClicked(e -> {
            String selectedName = results.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                onSearchResultSelected(section, key, selectedName);
                refresh();
            }
        });

        container.getChildren().addAll(search, results);
        return container;
    }


    protected List<? extends searchAction> getSearchSource(String section, String key) {
        return List.of(); // default empty
    }



    protected void onSearchResultSelected(String section, String key, String result) {
        // default do nothing
    }


    protected List<NodeSection> getSections() {
        return dynamicSections;
    }

    public Label getTitLabel() {
        return titleLabel;
    }

    private void createPorts() {
        if (node.getInputPort()) {
            inputPort = new Circle(6, Color.DODGERBLUE);
            inputPort.setCursor(Cursor.HAND);
            getChildren().add(inputPort);
        }

        if (node.getOutputPort()) {
            outputPort = new Circle(6, Color.ORANGE);
            outputPort.setCursor(Cursor.HAND);
            getChildren().add(outputPort);
        }
    }

    /**
     * Called when a section item is clicked.
     * Subclasses may override this to handle selection logic.
     */
    protected void onSectionItemClicked(String sectionTitle, String itemText) {
        // default: do nothing
    }

    public void replaceSectionItem(String section, String oldItem, String newItem) {

        List<NodeSection> updated = new ArrayList<>();

        for (NodeSection sec : dynamicSections) {
            if (sec.title().equals(section)) {

                List<String> items = new ArrayList<>(sec.items());
                int index = items.indexOf(oldItem);

                if (index >= 0) {
                    items.set(index, newItem);
                }

                updated.add(new NodeSection(sec.title(), items));
            } else {
                updated.add(sec);
            }
        }

        dynamicSections = updated;
    }




    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        double w = getWidth();
        double headerHeight = header.getHeight();
        double y = headerHeight / 2;

        if (inputPort != null) {
            inputPort.setLayoutX(-15);
            inputPort.setLayoutY(y);
        }

        if (outputPort != null) {
            outputPort.setLayoutX(w + 15);
            outputPort.setLayoutY(y);
        }
    }

    private void enableDragging() {
        setOnMousePressed(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;
            if (e.getTarget() instanceof Circle) return;

            dragOffsetX = e.getSceneX() - getLayoutX();
            dragOffsetY = e.getSceneY() - getLayoutY();
        });

        setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;
            if (e.getTarget() instanceof Circle) return;

            double newX = e.getSceneX() - dragOffsetX;
            double newY = e.getSceneY() - dragOffsetY;

            setLayoutX(newX);
            setLayoutY(newY);

            node.setxPos(newX);
            node.setyPos(newY);
        });
    }

    private void enableContextMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem deleteNodeItem = new MenuItem("Delete Node");
        MenuItem editItem = new MenuItem("Edit Node");
        MenuItem deleteConnectionItem = new MenuItem("Delete Connections");

        editItem.setOnAction(e -> edit());
        deleteNodeItem.setOnAction(e -> deleteNode());
        deleteConnectionItem.setOnAction(e -> deleteConnections());

        menu.getItems().addAll(editItem, deleteNodeItem, deleteConnectionItem);

        setOnContextMenuRequested(e -> menu.show(this, e.getSceneX(), e.getSceneY()));
    }

    protected abstract void edit();

    private void deleteNode() {
        project.removeNodeAndConnection(node);
        App.getInstance().getProjectManager().saveProject(project);
        App.getInstance().getGui().showSimulator();
    }

    private void deleteConnections() {
        project.removeConnectionsOf(node);
        App.getInstance().getGui().showSimulator();
    }

    private void enablePortConnections() {
        if (outputPort == null) return;

        outputPort.setOnMouseReleased(e -> {
            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();

            Pane parent = (Pane) getParent();

            for (var child : parent.getChildren()) {
                if (child instanceof NodeView other
                        && other != this
                        && other.inputPort != null
                        && other.isMouseNearInput(mouseX, mouseY, 50)) {

                    Connection connection = new Connection(
                            node.getId(),
                            true,
                            other.node.getId(),
                            true
                    );

                    project.addConnection(connection);

                    Platform.runLater(() -> {
                        ConnectionView cv = new ConnectionView(outputPort, other.getInputPort(), connection, project);
                        parent.getChildren().add(cv);
                        cv.toBack();
                    });
                    
                }
            }
        });
    }

    public boolean isMouseNearInput(double sceneX, double sceneY, double radius) {
        if (inputPort == null) return false;

        var bounds = inputPort.localToScene(inputPort.getBoundsInLocal());
        double cx = (bounds.getMinX() + bounds.getMaxX()) / 2;
        double cy = (bounds.getMinY() + bounds.getMaxY()) / 2;

        double dx = sceneX - cx;
        double dy = sceneY - cy;

        return Math.sqrt(dx * dx + dy * dy) <= radius;
    }

    public void setInfo(String text) {
        infoArea.getChildren().clear();
        Label label = new Label(text);
        label.getStyleClass().add("node-info");
        infoArea.getChildren().add(label);
    }

    public void applyRarityColor(String rarity) {
        String color = switch (rarity.trim().toLowerCase()) {
            case "normal" -> "#3a3a3a";
            case "magic" -> "#4b6cff";
            case "rare" -> "#b8860b";
            case "basecurrency" -> "#412e00";
            default -> "#ff0000";
        };

        header.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 6 10;"
        );
    }

    public LoadedItem execute(LoadedItem item) {
        return item;
    }

    public Circle getInputPort() { return inputPort; }
    public Circle getOutputPort() { return outputPort; }
    public Node getNode() { return node; }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void refresh() {
        body.getChildren().setAll(renderSections());
    }

    protected Project getProject() {
        return project;
    }
}
