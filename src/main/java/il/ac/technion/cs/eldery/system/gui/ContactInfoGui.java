package il.ac.technion.cs.eldery.system.gui;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ContactInfoGui extends Application {

    private final TableView<Person> table = new TableView<>();
    final ObservableList<Person> data = FXCollections.observableArrayList();
    final HBox hb = new HBox();

    public static void main(final String[] args) {
        launch(args);
    }

    static boolean validateInput(final String firstName, final String lastName, final String phoneNumber) {
        return firstName != null && lastName != null && phoneNumber != null && !"".equals(firstName) && !"".equals(lastName)
                && !"".equals(phoneNumber) && firstName.chars().allMatch(Character::isLetter) && lastName.chars().allMatch(Character::isLetter)
                && (phoneNumber.length() == 10 || phoneNumber.length() == 9) && phoneNumber.chars().allMatch(Character::isDigit);
    }

    @Override @SuppressWarnings({ "rawtypes", "unchecked" }) public void start(final Stage s) {
        final Scene scene = new Scene(new Group());
        s.setTitle("SOS configuration");
        s.setWidth(450);
        s.setHeight(550);

        final Label label = new Label("Emergency Contacts");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        final TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));

        final TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));

        final TableColumn phoneNumberCol = new TableColumn("Phone Number");
        phoneNumberCol.setMinWidth(200);
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<Person, String>("phoneNumber"));

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, phoneNumberCol);

        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(firstNameCol.getPrefWidth());
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(lastNameCol.getPrefWidth());
        addLastName.setPromptText("Last Name");
        final TextField addPhone = new TextField();
        addPhone.setMaxWidth(phoneNumberCol.getPrefWidth());
        addPhone.setPromptText("Phone Number");

        final Button addButton = new Button("Add");
        addButton.setOnAction(__ -> {
            final String firstName = addFirstName.getText();
            final String lastName = addLastName.getText();
            final String phoneNumber = addPhone.getText();
            if (validateInput(firstName, lastName, phoneNumber)) {
                data.add(new Person(firstName, lastName, phoneNumber));
                addFirstName.clear();
                addLastName.clear();
                addPhone.clear();
            } else {
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Bad Input");
                alert.setContentText("Make sure to enter only valid names and phone numbers");
                alert.showAndWait();
            }
        });

        hb.getChildren().addAll(addFirstName, addLastName, addPhone, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        s.setScene(scene);
        s.show();
    }

    public static class Person {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty phoneNumber;

        Person(final String firstName, final String lastName, final String phoneNumber) {
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.phoneNumber = new SimpleStringProperty(phoneNumber);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(final String firstName) {
            this.firstName.set(firstName);
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(final String lastName) {
            this.lastName.set(lastName);
        }

        public String getPhoneNumber() {
            return phoneNumber.get();
        }

        public void setPhoneNumber(final String phoneNumber) {
            this.phoneNumber.set(phoneNumber);
        }
    }
}