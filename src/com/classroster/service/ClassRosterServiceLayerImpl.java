package com.classroster.service;

import com.classroster.dao.ClassRosterAuditDao;
import com.classroster.dao.ClassRosterDao;
import com.classroster.dao.ClassRosterPersistenceException;
import com.classroster.dto.Student;

import java.util.List;

public class ClassRosterServiceLayerImpl implements ClassRosterServiceLayer{


    private ClassRosterDao dao;
    private ClassRosterAuditDao auditDao;


    public ClassRosterServiceLayerImpl(ClassRosterDao dao, ClassRosterAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
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

        // The student was successfulyl created, now write the audit log
        auditDao.writeAuditEntry("Student " + student.getStudentId() + " CREATED.");;
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
        Student removedStudent = dao.removeStudent(studentId);

        // Write to audit log
        auditDao.writeAuditEntry("Student " + studentId + " REMOVED.");

        return removedStudent;
    }

    private void validateStudentData(Student student) throws ClassRosterDataValidationException{
        if(student.getFirstName() == null || student.getFirstName().trim().length() == 0 ||
        student.getLastName() == null || student.getLastName().trim().length() == 0){
            throw new ClassRosterDataValidationException("ERROR: All fields [First Name, Last Name] are required");
        }
    }
}
