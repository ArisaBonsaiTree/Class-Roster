package com.classroster.service;

import com.classroster.dao.ClassRosterDao;
import com.classroster.dao.ClassRosterPersistenceException;
import com.classroster.dto.Student;

import java.util.List;

public class ClassRosterServiceLayerImpl implements ClassRosterServiceLayer{


    ClassRosterDao dao;

    public ClassRosterServiceLayerImpl(ClassRosterDao dao) {
        this.dao = dao;
    }

    @Override
    public void createStudent(Student student) throws ClassRosterDuplicateIdException, ClassRosterDataValidationException, ClassRosterPersistenceException {
        // We should first check to see if the StudentID already exist before we create one!
        if(dao.getStudent(student.getStudentId()) != null){
            throw new ClassRosterDuplicateIdException("ERROR: Could not create student. Student Id " + student.getStudentId() + " already exist");
        }

        // Now that we know the ID is new, validate the data
        validateStudentData(student);

        // Data survived our business rules checks, so now we persist the Student object
        dao.addStudent(student.getStudentId(), student);
    }

    @Override
    public List<Student> getAllStudents() throws ClassRosterPersistenceException {
        return dao.getAllStudents();
    }

    @Override
    public Student getStudent(String studentId) throws ClassRosterPersistenceException {
        return dao.getStudent(studentId);
    }

    @Override
    public Student removeStudent(String studentId) throws ClassRosterPersistenceException {
        return dao.removeStudent(studentId);
    }

    private void validateStudentData(Student student) throws ClassRosterDataValidationException{
        if(student.getFirstName() == null || student.getFirstName().trim().length() == 0 ||
        student.getLastName() == null || student.getLastName().trim().length() == 0){
            throw new ClassRosterDataValidationException("ERROR: All fields [First Name, Last Name] are required");
        }
    }
}
