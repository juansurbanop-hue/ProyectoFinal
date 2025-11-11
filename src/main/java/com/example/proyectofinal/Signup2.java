package com.example.proyectofinal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Signup2 extends Application {

    private ComboBox<String> religionBox, categoryBox, incomeBox, educationBox, occupationBox;
    private TextField panField, aadharField;
    private ToggleGroup seniorGroup, existingGroup;
    private String formno;

    public Signup2() {
        this.formno = "0000"; // valor por defecto
    }

    public Signup2(String formno) {
        this.formno = formno;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("NEW ACCOUNT APPLICATION FORM - PAGE 2");

        Label title = new Label("Page 2: Additional Details");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label formLabel = new Label("Form No: " + formno);
        formLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // Campos de selección
        religionBox = new ComboBox<>();
        religionBox.getItems().addAll("Hindu", "Muslim", "Sikh", "Christian", "Other");

        categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("General", "OBC", "SC", "ST", "Other");

        incomeBox = new ComboBox<>();
        incomeBox.getItems().addAll("Null", "<1,50,000", "<2,50,000", "<5,00,000", "Upto 10,00,000", "Above 10,00,000");

        educationBox = new ComboBox<>();
        educationBox.getItems().addAll("Non-Graduate", "Graduate", "Post-Graduate", "Doctorate", "Others");

        occupationBox = new ComboBox<>();
        occupationBox.getItems().addAll("Salaried", "Self-Employed", "Business", "Student", "Retired", "Others");

        // Campos de texto
        panField = new TextField();
        aadharField = new TextField();

        // Radio buttons
        RadioButton seniorYes = new RadioButton("Yes");
        RadioButton seniorNo = new RadioButton("No");
        seniorGroup = new ToggleGroup();
        seniorYes.setToggleGroup(seniorGroup);
        seniorNo.setToggleGroup(seniorGroup);
        HBox seniorBox = new HBox(10, seniorYes, seniorNo);

        RadioButton existingYes = new RadioButton("Yes");
        RadioButton existingNo = new RadioButton("No");
        existingGroup = new ToggleGroup();
        existingYes.setToggleGroup(existingGroup);
        existingNo.setToggleGroup(existingGroup);
        HBox existingBox = new HBox(10, existingYes, existingNo);

        // Botón
        Button nextBtn = new Button("Next");
        nextBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        nextBtn.setOnAction(e -> saveToDatabase(stage));

        // Grid de formulario
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(15);
        grid.setPadding(new Insets(30, 50, 30, 50));

        grid.add(formLabel, 0, 0);
        grid.add(title, 0, 1, 2, 1);

        grid.add(new Label("Religion:"), 0, 2);
        grid.add(religionBox, 1, 2);

        grid.add(new Label("Category:"), 0, 3);
        grid.add(categoryBox, 1, 3);

        grid.add(new Label("Income:"), 0, 4);
        grid.add(incomeBox, 1, 4);

        grid.add(new Label("Education Qualification:"), 0, 5);
        grid.add(educationBox, 1, 5);

        grid.add(new Label("Occupation:"), 0, 6);
        grid.add(occupationBox, 1, 6);

        grid.add(new Label("PAN Number:"), 0, 7);
        grid.add(panField, 1, 7);

        grid.add(new Label("Aadhar Number:"), 0, 8);
        grid.add(aadharField, 1, 8);

        grid.add(new Label("Senior Citizen:"), 0, 9);
        grid.add(seniorBox, 1, 9);

        grid.add(new Label("Existing Account:"), 0, 10);
        grid.add(existingBox, 1, 10);

        grid.add(nextBtn, 1, 12);

        grid.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(grid, 650, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void saveToDatabase(Stage stage) {
        String religion = religionBox.getValue();
        String category = categoryBox.getValue();
        String income = incomeBox.getValue();
        String education = educationBox.getValue();
        String occupation = occupationBox.getValue();
        String pan = panField.getText();
        String aadhar = aadharField.getText();

        RadioButton seniorSelected = (RadioButton) seniorGroup.getSelectedToggle();
        RadioButton existingSelected = (RadioButton) existingGroup.getSelectedToggle();

        if (religion == null || category == null || income == null || education == null || occupation == null ||
                pan.isEmpty() || aadhar.isEmpty() || seniorSelected == null || existingSelected == null) {
            showAlert(Alert.AlertType.ERROR, "Please fill all the fields.");
            return;
        }

        String senior = seniorSelected.getText();
        String existing = existingSelected.getText();

        try {
            Conn conn = new Conn();
            Connection c = conn.getConnection();

            String query = "INSERT INTO signup2 (formno, religion, category, income, education, occupation, pan, aadhar, senior, existing) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, formno);
            stmt.setString(2, religion);
            stmt.setString(3, category);
            stmt.setString(4, income);
            stmt.setString(5, education);
            stmt.setString(6, occupation);
            stmt.setString(7, pan);
            stmt.setString(8, aadhar);
            stmt.setString(9, senior);
            stmt.setString(10, existing);

            stmt.executeUpdate();
            conn.close();

            showAlert(Alert.AlertType.INFORMATION, "✅ Data saved successfully!");

            // Aquí podrías abrir Signup3 (cuando lo conviertas a JavaFX)
            // new Signup3(formno).start(new Stage());
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "❌ Database error: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
