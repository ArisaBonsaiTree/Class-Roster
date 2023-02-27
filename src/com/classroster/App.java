package com.classroster;

import com.classroster.controller.ClassRosterController;
import com.classroster.dao.ClassRosterAuditDao;
import com.classroster.dao.ClassRosterAuditDaoFileImpl;
import com.classroster.dao.ClassRosterDao;
import com.classroster.dao.ClassRosterDaoFileImpl;
import com.classroster.service.ClassRosterServiceLayer;
import com.classroster.service.ClassRosterServiceLayerImpl;
import com.classroster.ui.ClassRosterView;
import com.classroster.ui.UserIO;
import com.classroster.ui.UserIOConsoleImpl;

public class App {

    public static void main(String[] args) {
        // Instantiate the UserIO implementation
        UserIO myIo = new UserIOConsoleImpl();

        // Instantiate the View and wire the UserIO implementation into it
        ClassRosterView myView = new ClassRosterView(myIo);

        // Instantiate the DAO
        ClassRosterDao myDao = new ClassRosterDaoFileImpl();

        // Instantiate the Audit DAO
        ClassRosterAuditDao myAuditDao = new ClassRosterAuditDaoFileImpl();

        // Instantiate the Service Layer and wire the DAO and Audit DAO into it
        ClassRosterServiceLayer myService = new ClassRosterServiceLayerImpl(myDao, myAuditDao);

        // Instantiate the Controller and wire the Service Layer into it
        ClassRosterController controller = new ClassRosterController(myView, myService);

        // Kick off the Controller
        controller.run();
    }
}


