package com.example.school.view;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.example.school.controller.SchoolController;
import com.example.school.model.Student;

@RunWith(GUITestRunner.class)
public class StudentSwingViewTest extends AssertJSwingJUnitTestCase {

	public static final int TIMEOUT = 5000;
	private StudentSwingView studentSwingView;
	private FrameFixture windows;
	@Mock
	private SchoolController schoolController;
	private AutoCloseable closeable;

	@Override
	public void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			studentSwingView = new StudentSwingView();
			studentSwingView.setSchoolController(schoolController);
			return studentSwingView;
		});
		windows = new FrameFixture(robot(), studentSwingView);
		windows.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		// TODO Auto-generated method stub
		closeable.close();
	}

	@Test
	public void testControlIsInitialStates() {
		windows.label(JLabelMatcher.withText("id"));
		windows.textBox("idTextBox").requireEnabled();
		windows.label(JLabelMatcher.withText("name"));
		windows.textBox("nameTextBox").requireEnabled();
		windows.button(JButtonMatcher.withName("Add Student")).requireDisabled();
		windows.list("Student List");
		windows.button(JButtonMatcher.withName("Rimuovi Selezionati")).requireDisabled();
		windows.label("errorMessageLabel").requireText("");
	}

	@Test
	public void testWhenIdAndNameAreNotEmptyThenAddButtonShouldBeEnabled() {
		windows.textBox("idTextBox").enterText("1");
		windows.textBox("nameTextBox").enterText("text1");
		windows.button(JButtonMatcher.withText("Aggiungi")).requireEnabled();

	}

	@Test
	public void testEitherIdOrNameAreEmptyThenAddButtonShouldBeDisabled() {
		JTextComponentFixture idTextBox = windows.textBox("idTextBox");
		JTextComponentFixture nameTextBox = windows.textBox("nameTextBox");

		idTextBox.enterText("1");
		nameTextBox.enterText(" ");

		windows.button(JButtonMatcher.withText("Aggiungi")).requireDisabled();

		idTextBox.setText("");
		nameTextBox.setText("");

		idTextBox.enterText(" ");
		nameTextBox.enterText("test1");

		windows.button(JButtonMatcher.withText("Aggiungi")).requireDisabled();

	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenStudentsAreInList() {
		GuiActionRunner.execute(() -> studentSwingView.getListStudentsModel().addElement(new Student("1", "test1")));
		windows.list("Student List").selectItem(0);
		JButtonFixture deleteButton = windows.button(JButtonMatcher.withName("Rimuovi Selezionati"));
		deleteButton.requireEnabled();
		windows.list("Student List").clearSelection();
		deleteButton.requireDisabled();

	}

	@Test
	public void testsShowAllStudentsShouldAddStudentDescriptionsToTheList() {
		Student student1 = new Student("1", "test1");
		Student student2 = new Student("2", "test2");
		GuiActionRunner.execute(() -> studentSwingView.showAllStudents(Arrays.asList(student1, student2)));
		String[] listContents = windows.list().contents();
		assertThat(listContents).containsExactly(student1.toString(), student2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Student student = new Student("1", "test");
		// GuiActionRunner.execute(() -> studentSwingView.showError("messaggio di
		// errore", student));
		studentSwingView.showError("messaggio di errore", student);
		windows.label(JLabelMatcher.withName("errorMessageLabel"))
				.requireText("messaggio di errore: " + student.getId());
	}

	@Test
	public void testStudentAddedShouldAddTheStudentToTheListAndResetTheErrorLabel() {
		Student student = new Student("1", "test1");
		// GuiActionRunner.execute(() -> studentSwingView.studentAdded(new Student("1",
		// "test1")));
		studentSwingView.studentAdded(new Student("1", "test1"));
		String[] listContents = windows.list().contents();
		assertThat(listContents).containsExactly(student.toString());
		windows.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testStudentRemovedShouldRemoveTheStudentFromTheListAndResetTheErrorLabel() {
		Student student1 = new Student("1", "test1");
		Student student2 = new Student("2", "test2");
		// we don't use studentAddded method because we can't use other method of the
		// class under test
		GuiActionRunner.execute(() -> {
			DefaultListModel<Student> listStudentsModel = studentSwingView.getListStudentsModel();
			listStudentsModel.addElement(student1);
			listStudentsModel.addElement(student2);
		});
		GuiActionRunner.execute(() -> studentSwingView.studentRemoved(new Student("1", "test1")));
		String[] listContents = windows.list("Student List").contents();
		assertThat(listContents).containsExactly(student2.toString());
		windows.label("errorMessageLabel").requireText(" ");

	}

	@Test
	public void testAddButtonShouldDelegateToSchoolControllerNewStudent() {
		Student student = new Student("1", "test1");
		windows.textBox("idTextBox").enterText("1");
		windows.textBox("nameTextBox").enterText("test1");
		windows.button(JButtonMatcher.withName("Add Student")).click();
		verify(schoolController, timeout(TIMEOUT)).newStudent(student);

	}

	@Test
	public void testDeleteButtonShouldDelegateToSchoolControllerDeleteStudent() {
		Student student1 = new Student("1", "test1");
		Student student2 = new Student("2", "test2");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Student> listStudentsModel = studentSwingView.getListStudentsModel();
			listStudentsModel.addElement(student1);
			listStudentsModel.addElement(student2);
		});
		windows.list("Student List").selectItem(0);
		windows.button(JButtonMatcher.withText("Rimuovi Selezionati")).click();
		verify(schoolController, timeout(TIMEOUT)).deleteStudent(student1);

	}

}
