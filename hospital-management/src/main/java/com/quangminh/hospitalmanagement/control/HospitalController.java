package com.quangminh.hospitalmanagement.control;

import com.quangminh.hospitalmanagement.HospitalApp;
import com.quangminh.hospitalmanagement.model.Doctor;
import com.quangminh.hospitalmanagement.model.Patient;
import com.quangminh.hospitalmanagement.util.DoctorUtil;
import com.quangminh.hospitalmanagement.util.PatientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

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

    private TableView<Patient> tblPatient;
    private TableView<Doctor> tblDoctor;
    @FXML
    public void initialize(){
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
        if (tblDoctor != null) {
            tblDoctor.setItems(DoctorUtil.getDoctors());
        }
        if (txtdName != null) txtdName.clear();
        if (txtdSpec != null) txtdSpec.clear();
    }
}
