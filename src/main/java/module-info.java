module aleexeyy.com.icq {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires org.java_websocket;
    requires javafx.graphics;
    requires java.naming;
    requires com.fasterxml.jackson.databind;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires com.h2database;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires dotenv.java;
    requires java.desktop;
    requires java.sql;


    exports aleexeyy.com.icq.server to com.fasterxml.jackson.databind;
//    exports aleexeyy.com.icq.server.ui to javafx.fxml, javafx.graphics;
    exports aleexeyy.com.icq.server.message.handlers to org.hibernate.orm.core, com.fasterxml.jackson.databind;
    opens aleexeyy.com.icq.server.db.entities to org.hibernate.orm.core;
    opens aleexeyy.com.icq.server.db.entities.keys to org.hibernate.orm.core;
    opens aleexeyy.com.icq.server.message.handlers to com.fasterxml.jackson.databind;


//    exports aleexeyy.com.icq.client.ui to javafx.graphics;
    exports aleexeyy.com.icq.client.ui.controllers to javafx.fxml;
    opens aleexeyy.com.icq.client.ui.controllers to javafx.fxml;


    opens aleexeyy.com.icq.shared.messages to com.fasterxml.jackson.databind;

    exports aleexeyy.com.icq.client.ui;
    exports aleexeyy.com.icq.server.ui;
    exports aleexeyy.com.icq.shared.messages;

    opens aleexeyy.com.icq.client.ui to javafx.fxml;
    opens aleexeyy.com.icq.server.ui to javafx.fxml;

    // Declare both entry points
    provides javafx.application.Application with
            aleexeyy.com.icq.client.ui.MainApp,
            aleexeyy.com.icq.server.ui.ServerAppUI;

}