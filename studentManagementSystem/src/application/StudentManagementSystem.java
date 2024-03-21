package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StudentManagementSystem extends Application {

    // Sample data for students and courses
    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private ObservableList<String> courseOptions = FXCollections.observableArrayList();
    private ListView<Student> studentListView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Management System");

        // Sample data initialization
        initializeSampleData();

        // Create UI components
        Button addStudentButton = new Button("Add Student");
        Button updateStudentButton = new Button("Update Student");
        Button viewStudentButton = new Button("View Student Details");
        Button addCourseButton = new Button("Add a new course");
        Button enrollStudentButton = new Button("Enroll Student");
        Button assignGradeButton = new Button("Assign Grade");
        ComboBox<String> courseComboBox = new ComboBox<>(courseOptions);
        studentListView = new ListView<>();
        studentListView.setItems(FXCollections.observableArrayList(students));
        studentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        // Add event handlers
        addStudentButton.setOnAction(event -> showAddStudentDialog(primaryStage));
        updateStudentButton.setOnAction(event -> showUpdateStudentDialog(primaryStage, studentListView.getSelectionModel().getSelectedItem()));
        viewStudentButton.setOnAction(event -> showViewStudentDetailsDialog(primaryStage, studentListView.getSelectionModel().getSelectedItem()));
        addCourseButton.setOnAction(e -> addNewCourse(primaryStage));
        enrollStudentButton.setOnAction(event -> showEnrollStudentDialog(primaryStage, courseComboBox.getValue(), studentListView.getSelectionModel().getSelectedItem()));
        assignGradeButton.setOnAction(event -> showAssignGradeDialog(primaryStage, studentListView.getSelectionModel().getSelectedItem()));

        // Create layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        GridPane.setConstraints(addStudentButton, 0, 0);
        GridPane.setConstraints(updateStudentButton, 0, 1);
        GridPane.setConstraints(viewStudentButton, 0, 2);
        GridPane.setConstraints(addCourseButton, 2, 3);
        GridPane.setConstraints(enrollStudentButton, 0, 3);
        GridPane.setConstraints(assignGradeButton, 0, 4);
        GridPane.setConstraints(courseComboBox, 1, 3);
        GridPane.setConstraints(studentListView, 1, 0, 1, 3);
        grid.getChildren().addAll(addStudentButton, updateStudentButton, viewStudentButton, addCourseButton, enrollStudentButton, assignGradeButton, courseComboBox, studentListView);

        // Add mouse event listener
        studentListView.setOnMouseClicked(this::studentListView_onMouseClicked);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddStudentDialog(Stage primaryStage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Enter Student Details");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Student student = new Student(name, students.size() + 100);
            students.add(student);
            studentListView.getItems().add(student); // Update the ListView
        });
    }

    private void showUpdateStudentDialog(Stage primaryStage, Student student) {
        if (student != null) {
            TextInputDialog dialog = new TextInputDialog(student.getName());
            dialog.setTitle("Update Student");
            dialog.setHeaderText("Update Student Details");
            dialog.setContentText("Name:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                student.setName(name);
                // Update the ListView
                int index = studentListView.getItems().indexOf(student);
                if (index != -1) {
                    studentListView.getItems().set(index, student); // Update the ListView
                }
            });
        } else {
            showAlert("Update Student", "Please select a student.");
        }
    }

    private void showViewStudentDetailsDialog(Stage primaryStage, Student student) {
        if (student != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Details");
            alert.setHeaderText(null);
            alert.setContentText("Name: " + student.getName() + "\nID: " + student.getID());
            alert.showAndWait();
        } else {
            showAlert("View Student Details", "Please select a student.");
        }
    }
    
    private void addNewCourse(Stage primaryStage) {
        // Create a new window for adding a course
        Stage addCourseStage = new Stage();
        addCourseStage.setTitle("Add a new course");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Course code label and text field
        Label codeLabel = new Label("Course Code:");
        GridPane.setConstraints(codeLabel, 0, 0);
        TextField codeField = new TextField();
        GridPane.setConstraints(codeField, 1, 0);

        // Course name label and text field
        Label nameLabel = new Label("Course Name:");
        GridPane.setConstraints(nameLabel, 0, 1);
        TextField nameField = new TextField();
        GridPane.setConstraints(nameField, 1, 1);

        // Course weight label and text field
        Label weightLabel = new Label("Course Weight:");
        GridPane.setConstraints(weightLabel, 0, 2);
        TextField weightField = new TextField();
        GridPane.setConstraints(weightField, 1, 2);

        // Max capacity label and text field
        Label capacityLabel = new Label("Max Capacity:");
        GridPane.setConstraints(capacityLabel, 0, 3);
        TextField capacityField = new TextField();
        GridPane.setConstraints(capacityField, 1, 3);

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Parse input and create new course
            String code = codeField.getText();
            String name = nameField.getText();
            double weight = Double.parseDouble(weightField.getText());
            int capacity = Integer.parseInt(capacityField.getText());
            Course newCourse = new Course(code, name, weight, capacity);
            courses.add(newCourse);
            courseOptions.add(newCourse.getName());
            addCourseStage.close();
        });
        GridPane.setConstraints(submitButton, 1, 4);

        grid.getChildren().addAll(codeLabel, codeField, nameLabel, nameField,
                weightLabel, weightField, capacityLabel, capacityField, submitButton);

        Scene scene = new Scene(grid, 300, 200);
        addCourseStage.setScene(scene);
        addCourseStage.show();
    }

    private void showEnrollStudentDialog(Stage primaryStage, String course, Student student) {
        if (student != null && course != null) {
            student.enrollCourse(course);
            showAlert("Enroll Student", "Student enrolled successfully.");
        } else {
            showAlert("Enroll Student", "Please select a course and a student.");
        }
    }

    private void showAssignGradeDialog(Stage primaryStage, Student student) {
    	// Implement logic to show dialog for assigning a grade to a student
        if (student != null && !student.getEnrolledCourses().isEmpty()) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>(student.getEnrolledCourses().get(0), student.getEnrolledCourses());
            dialog.setTitle("Assign Grade");
            dialog.setHeaderText("Assign Grade for " + student.getName());
            dialog.setContentText("Select the course:");

            Optional<String> courseResult = dialog.showAndWait();
            courseResult.ifPresent(courseName -> {
                TextInputDialog gradeDialog = new TextInputDialog();
                gradeDialog.setTitle("Assign Grade");
                gradeDialog.setHeaderText("Assign Grade for " + student.getName() + " in " + courseName);
                gradeDialog.setContentText("Enter the grade:");

                Optional<String> result = gradeDialog.showAndWait();
                result.ifPresent(grade -> {
                    try {
                        double parsedGrade = Double.parseDouble(grade);
                        if (parsedGrade >= 0 && parsedGrade <= 100) {
                            student.setGrade(courseName, parsedGrade);
                            showAlert("Success", "Grade assigned successfully.");
                        } else {
                            showAlert("Error", "Grade must be between 0 and 100.", Alert.AlertType.ERROR);
                        }
                    } catch (NumberFormatException e) {
                        showAlert("Error", "Invalid grade format. Please enter a valid number.", Alert.AlertType.ERROR);
                    }
                });
            });
        } else {
            showAlert("Error", "No enrolled courses found for the selected student.", Alert.AlertType.ERROR);
        }
    }
    
    private void showStudentCoursesAndGrades(Student student) {
        if (student != null) {
            Stage courseGradeStage = new Stage();
            courseGradeStage.setTitle("Courses and Grades for " + student.getName());

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10, 10, 10, 10));
            grid.setVgap(5);
            grid.setHgap(10);

            Label courseLabel = new Label("Course");
            GridPane.setConstraints(courseLabel, 0, 0);
            Label gradeLabel = new Label("Grade");
            GridPane.setConstraints(gradeLabel, 1, 0);

            // Add header row
            grid.getChildren().addAll(courseLabel, gradeLabel);

            int rowIndex = 1;
            for (String courseName : student.getEnrolledCourses()) {
                Label courseNameLabel = new Label(courseName);
                GridPane.setConstraints(courseNameLabel, 0, rowIndex);
                Label gradeLabelValue = null;

                double grade = student.getGrades().getOrDefault(courseName, -1.0); // Use getOrDefault to handle non-existent grades
                if (grade != -1.0) {
                    gradeLabelValue = new Label(String.valueOf(grade));
                } else {
                    gradeLabelValue = new Label("Not Assigned");
                }
                GridPane.setConstraints(gradeLabelValue, 1, rowIndex);

                grid.getChildren().addAll(courseNameLabel, gradeLabelValue);
                rowIndex++;
            }

            Scene scene = new Scene(grid, 300, 100);
            courseGradeStage.setScene(scene);
            courseGradeStage.showAndWait();
        } else {
            showAlert("Error", "Please select a student.");
        }
    }
    
    private void studentListView_onMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {  // Handle double click on student
            Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
            showStudentCoursesAndGrades(selectedStudent);
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showAlert(String title, String content, AlertType error) {
    	Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
  	}

    private void initializeSampleData() {
        // Adding sample courses
        Course course1 = new Course("CSE101", "Introduction to Computer Science", 3.0, 50);
        Course course2 = new Course("ENG101", "English Composition", 2.0, 40);
        Course course3 = new Course("CS209", "Data Analytics", 3.0, 30);

        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courseOptions.addAll(course1.getName(), course2.getName(), course3.getName());

        // Adding sample students
        Student student1 = new Student("Charles Brown", 1001);
        Student student2 = new Student("Fred Blay", 1002);
        Student student3 = new Student("Samuel Henning", 1003);
        students.add(student1);
        students.add(student2);
        students.add(student3);

        // Enrolling students in sample courses
        course1.enrollStudent(student1);
        course1.enrollStudent(student2);
        course2.enrollStudent(student1);
    }
}

class Student {
    private String name;
    private int ID = 10653;
    private Map<String, Double> grades;
    private List<String> enrolledCourses;

    public Student(String name, int ID) {
        this.name = name;
        this.ID = ID;
        this.grades = new HashMap<>();
        this.enrolledCourses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }
    
    public Map<String, Double> getGrades(){
    	return grades;
    }

    public void enrollCourse(String course) {
        enrolledCourses.add(course);
    }

    public void setGrade(String course, double grade) {
        grades.put(course, grade);
    }
    
    public String getCourse(String course) {
    	return enrolledCourses.get(enrolledCourses.indexOf(course));
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public double calculateOverallGrade() {
        double totalWeightedGrade = 0.0;
        double totalWeight = 0.0;

        for (Map.Entry<String, Double> entry : grades.entrySet()) {
            double grade = entry.getValue();
            // Assuming courses have a weight
            double courseWeight = getCourseWeight(entry.getKey());
            // Calculate weighted grade for each course
            totalWeightedGrade += grade * courseWeight;
            totalWeight += courseWeight;
        }

        // Calculate overall grade (average)
        double overallGrade = totalWeightedGrade / totalWeight;
        return overallGrade;
    }

    private double getCourseWeight(String courseName) {
        // Dummy method to get course weight
        return 3.0; // Placeholder value, replace with actual course weight lookup
    }

    @Override
    public String toString() {
        return name;
    }
}

class Course {
    private String courseCode;
    private String name;
    private int maxCapacity;
    private double courseWeight;
    private List<Student> enrolledStudents;
    private static int totalEnrolledStudents;

    public Course(String courseCode, String name, double courseWeight, int maxCapacity) {
        this.courseCode = courseCode;
        this.name = name;
        this.courseWeight = courseWeight;
        this.maxCapacity = maxCapacity;
        enrolledStudents = new ArrayList<>();
    }

    public Course(String name) {
		this.name = name;
	}

	public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public double getCourseWeight() {
        return courseWeight;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void enrollStudent(Student student) {
        if (enrolledStudents.size() < maxCapacity) {
            enrolledStudents.add(student);
            student.enrollCourse(courseCode);
            totalEnrolledStudents++;
        } else {
            System.out.println("Course is already full.");
        }
    }

    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }
}

