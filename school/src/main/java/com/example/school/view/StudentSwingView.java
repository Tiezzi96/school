package com.example.school.view;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.example.school.controller.SchoolController;
import com.example.school.model.Student;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StudentSwingView extends JFrame implements StudentView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtid;
	private JLabel lblName;
	private JTextField txtName;
	private JButton AddButton;
	private JList<Student> listStudents;
	private JButton delButton;
	private JLabel errorLabel;
	private DefaultListModel<Student> listStudentsModel;
	private JScrollPane scrollPane;
	private SchoolController schoolController;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentSwingView frame = new StudentSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	DefaultListModel<Student> getListStudentsModel() {
		return listStudentsModel;
	}

	/**
	 * Create the frame.
	 */
	public StudentSwingView() {
		setTitle("Student View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 537, 392);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(51, 51, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblId = new JLabel("id");
		lblId.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.fill = GridBagConstraints.VERTICAL;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		contentPane.add(lblId, gbc_lblId);

		txtid = new JTextField();
		KeyListener addButtonListerner = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				AddButton.setEnabled(!txtid.getText().trim().isEmpty() && !txtName.getText().trim().isEmpty());
			}
		};
		txtid.setName("idTextBox");
		txtid.addKeyListener(addButtonListerner);
		GridBagConstraints gbc_txtid = new GridBagConstraints();
		gbc_txtid.insets = new Insets(0, 0, 5, 0);
		gbc_txtid.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtid.gridx = 1;
		gbc_txtid.gridy = 0;
		contentPane.add(txtid, gbc_txtid);
		txtid.setColumns(10);

		lblName = new JLabel("name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		contentPane.add(lblName, gbc_lblName);

		txtName = new JTextField();
		txtName.setName("nameTextBox");
		txtName.addKeyListener(addButtonListerner);
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 1;
		contentPane.add(txtName, gbc_txtName);
		txtName.setColumns(10);

		AddButton = new JButton("Aggiungi");
		AddButton.addActionListener(e -> new Thread(() -> {
			try {
				Thread.sleep(1000);

			} catch (Exception exception) {
				// TODO: handle exception
			}
			schoolController.newStudent(new Student(txtid.getText(), txtName.getText()));

		}).start());
		AddButton.setEnabled(false);
		AddButton.setName("Add Student");
		GridBagConstraints gbc_addButton = new GridBagConstraints();
		gbc_addButton.insets = new Insets(0, 0, 5, 0);
		gbc_addButton.gridx = 1;
		gbc_addButton.gridy = 2;
		contentPane.add(AddButton, gbc_addButton);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);

		listStudentsModel = new DefaultListModel<>();
		listStudents = new JList<>(listStudentsModel);

		listStudents.setName("Student List");

		scrollPane.setViewportView(listStudents);

		delButton = new JButton("Rimuovi Selezionati");
		delButton.addActionListener(e -> new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (Exception e1) {
				// TODO: handle exception
			}
			schoolController.deleteStudent(listStudents.getSelectedValue());
		}).start());
		delButton.setEnabled(false);
		delButton.setName("Rimuovi Selezionati");
		GridBagConstraints gbc_delButton = new GridBagConstraints();
		gbc_delButton.insets = new Insets(0, 0, 5, 0);
		gbc_delButton.gridx = 1;
		gbc_delButton.gridy = 5;
		contentPane.add(delButton, gbc_delButton);
		listStudents.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("listStudents.getSelectedIndex(): " + listStudents.getSelectedIndex());
				delButton.setEnabled(listStudents.getSelectedIndex() != -1);
			}
		});

		errorLabel = new JLabel("");
		errorLabel.setName("errorMessageLabel");
		GridBagConstraints gbc_errorLabel = new GridBagConstraints();
		gbc_errorLabel.insets = new Insets(0, 0, 5, 0);
		gbc_errorLabel.gridx = 1;
		gbc_errorLabel.gridy = 6;
		contentPane.add(errorLabel, gbc_errorLabel);
	}

	@Override
	public void showAllStudents(List<Student> students) {
		// TODO Auto-generated method stub
		students.stream().forEach(listStudentsModel::addElement);

	}

	@Override
	public void showError(String message, Student student) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> errorLabel.setText(message + ": " + student.getId()));

	}

	@Override
	public void studentAdded(Student student) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
			listStudentsModel.addElement(student);

			resetErrorLabel();
		});
	}

	@Override
	public void studentRemoved(Student student) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(() -> {
			listStudentsModel.removeElement(student);
			resetErrorLabel();
					});

	}

	private void resetErrorLabel() {
		errorLabel.setText(" ");
	}

	public void setSchoolController(SchoolController schoolController) {
		// TODO Auto-generated method stub
		this.schoolController = schoolController;

	}
}
