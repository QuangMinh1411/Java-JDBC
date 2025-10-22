package com.quangminh.hospitalmanagement.control;

import com.quangminh.hospitalmanagement.HospitalApp;
import com.quangminh.hospitalmanagement.model.Appointment;
import com.quangminh.hospitalmanagement.model.Doctor;
import com.quangminh.hospitalmanagement.model.Patient;
import com.quangminh.hospitalmanagement.util.AppointmentUtil;
import com.quangminh.hospitalmanagement.util.DoctorUtil;
import com.quangminh.hospitalmanagement.util.PatientUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.Callback;

import java.time.LocalDate;

public class HospitalController {
    @FXML private Label lblpName;
    @FXML private TextField txtpName;
    @FXML private Label lblpAge;
    @FXML private TextField txtpAge;
    @FXML private Label lblpGender;
    @FXML private RadioButton rdoMale;
    @FXML private RadioButton rdoFemale;
    @FXML private ToggleGroup GenderGroup;
    @FXML private Button btnAddPatient;
    @FXML private VBox boxPatient;
    @FXML private Label lblpView;


    @FXML private Label lbldName;
    @FXML private TextField txtdName;
    @FXML private Label lblpAge1; // label for Speciality in FXML
    @FXML private TextField txtdSpec;
    @FXML private Button btnAddDoctor;
    @FXML private VBox boxDoctor;
    @FXML private Label lblpViewDoctor;
    @FXML private HBox boxListPatient;
    @FXML private HBox boxListDoctor;
    @FXML private HBox boxDate;
    private ListView<Patient> lvPatient;
    private ListView<Doctor> lvDoctor;
    @FXML private VBox boxAppoint;
    @FXML private DatePicker date;
    private TableView<Patient> tblPatient;
    private TableView<Doctor> tblDoctor;
    private TableView<Appointment> tblAppointment;
    private LocalDate appointment_date;
    @FXML
    public void initialize(){
        date = new DatePicker();
        date.setValue(LocalDate.now());
        lvPatient = new ListView<>();
        lvPatient.getItems().addAll(PatientUtil.getPatientModel());
        lvPatient.setCellFactory(
                new Callback<ListView<Patient>,ListCell<Patient>>() {
                    @Override
                    public ListCell<Patient> call(ListView<Patient> listView) {
                        return new ListCell<Patient>() {
                            @Override
                            public void updateItem(Patient item, boolean empty) {
                                // Must call super
                                super.updateItem(item, empty);

                                int index = this.getIndex();
                                String name = null;

                                // Format name
                                if (item == null || empty) {
                                    // No action to perform
                                } else {
                                    name = item.getId() + ". " +
                                            item.getName();
                                }

                                this.setText(name);
                                setGraphic(null);
                            }
                        };
                    }});

        lvDoctor = new ListView<>();
        lvDoctor.getItems().addAll(DoctorUtil.getDoctorModel());
        lvDoctor.setCellFactory(
                new Callback<ListView<Doctor>,ListCell<Doctor>>() {
                    @Override
                    public ListCell<Doctor> call(ListView<Doctor> listView) {
                        return new ListCell<Doctor>() {
                            @Override
                            public void updateItem(Doctor item, boolean empty) {
                                // Must call super
                                super.updateItem(item, empty);

                                int index = this.getIndex();
                                String name = null;

                                // Format name
                                if (item == null || empty) {
                                    // No action to perform
                                } else {
                                    name = item.getId() + ". " +
                                            item.getName();
                                }

                                this.setText(name);
                                setGraphic(null);
                            }
                        };
                    }});

        boxListPatient.getChildren().add(lvPatient);
        boxListDoctor.getChildren().add(lvDoctor);
        tblPatient = new TableView<>(PatientUtil.getPatients());
        tblPatient.getColumns().addAll(PatientUtil.getIdColumn(),
                PatientUtil.getNameColumn(),PatientUtil.getAgeColumn(),PatientUtil.getGenderColumn());
        boxPatient.getChildren().add(tblPatient);

        tblDoctor = new TableView<>(DoctorUtil.getDoctors());
        tblDoctor.getColumns().addAll(
                DoctorUtil.getIdColumn(),DoctorUtil.getNameColumn(),DoctorUtil.getSpecialtyColumn()
        );
        // Attach doctor table to its container if defined
        if (boxDoctor != null) {
            boxDoctor.getChildren().add(tblDoctor);
        }
        tblAppointment = new TableView<>(AppointmentUtil.getAppointments());
        tblAppointment.getColumns().addAll(
                AppointmentUtil.getPatientName(),AppointmentUtil.getDoctorName(),
                AppointmentUtil.getAppointmentDateColumn()
        );
        boxAppoint.getChildren().addAll(tblAppointment);
    }
    @FXML
    public void addPatient(ActionEvent event) {
        String name = txtpName.getText();
        int age;
        RadioButton selected = (RadioButton) GenderGroup.getSelectedToggle();
        String gender = selected.getText();
        try{
            age = Integer.parseInt(txtpAge.getText());
        }catch (NumberFormatException e){
            showAlert("Invalid Age");
            return;
        }
        if(name.isEmpty()){
            showAlert("Empty Name");
            return;
        }
        PatientUtil.addPatient(name,gender,age);
        refreshTable();
        reset();

    }

    public void reset(){
        txtpName.clear();
        txtpAge.clear();
    }
    private void showAlert(String msg){
        Window owner = btnAddPatient.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.initOwner(owner);
        alert.showAndWait();
        reset();
    }

    public void refreshTable(){
        tblPatient.setItems(PatientUtil.getPatients());
        tblDoctor.setItems(DoctorUtil.getDoctors());
    }

    @FXML
    public void addDoctor(ActionEvent event) {
        String name = txtdName.getText();
        String spec = txtdSpec.getText();
        if (name == null || name.isBlank()) {
            showAlert("Empty Doctor Name");
            return;
        }
        if (spec == null || spec.isBlank()) {
            showAlert("Empty Specialty");
            return;
        }
        DoctorUtil.addDoctor(name, spec);
        refreshTable();
        reset();
    }

    @FXML
    public void selectDate(ActionEvent event) {
        appointment_date = date.getValue();
    }
}
