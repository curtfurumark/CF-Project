/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.time.LocalDate;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.DatePicker;

/**
 *
 * @author curtr
 */
public class CFDateHandler implements EventHandler {

    @Override
    public void handle(Event event) {
        LocalDate date = ((DatePicker) event.getSource()).getValue();
        System.err.println("Selected date: " + date);
        CFUtil.debug(event);
    }
    
}
