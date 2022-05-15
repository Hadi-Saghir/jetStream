package application.components.flight;

import application.Controller;
import application.ErrorHandler;
import application.api.Db;
import javafx.event.ActionEvent;

import java.time.LocalDate;

/**
 * #comment (comment this class and create javadoc to every method)
 */
public class Search {
    private Controller controller;
    private Db db;
    private ErrorHandler errorHandler;

    public Search(Controller controller, Db db, ErrorHandler errorHandler){
        this.controller = controller;
        this.db = db;
        this.errorHandler = errorHandler;
    }

    public void checkboxEvent(ActionEvent e){
        if (controller.round_trip_checkbox.isSelected()){
            controller.return_date_pick_hbox.setDisable(false);
            controller.date_next_day_return_button.setDisable(false);
            controller.date_previous_day_return_button.setDisable(false);
        }else {
            controller.return_date_pick_hbox.setDisable(true);
            controller.date_next_day_return_button.setDisable(true);
            controller.date_previous_day_return_button.setDisable(true);
        }

    }

    public void searchFlight() {
        System.out.println(controller.round_trip_checkbox.isSelected() + " checkbox");

        String from = controller.from_input_flight_textfield.getText();
        String to = controller.display_input_flights.getText();
        LocalDate d = controller.date_input_flight.getValue();
        LocalDate Rd = controller.dateR_input_flight.getValue();

        if (!controller.round_trip_checkbox.isSelected()){
            if (!(from.isEmpty()) && !(to.isEmpty())){
                if (d == null) {
                    boolean ok = errorHandler.confirmThisAction("Confirm action", "Search without date?", "To continue search without specifying the date confirm.");
                    if (ok) {
                        controller.available_flights_list = db.searchFlight(from, to, null, null);
                    }
                } else {
                    controller.available_flights_list = db.searchFlight(from, to, String.valueOf(d), null);
                }
            }
        }else {
            if (!from.isEmpty() && !to.isEmpty()){
                if (!(d == null) && (!(Rd ==null))){
                    controller.available_flights_list = db.searchFlight(from, to, String.valueOf(d), String.valueOf(Rd));
                }else
                    controller.available_flights_list = db.searchFlight(from, to, null, null);
            }
        }

        // fill out the flights on  screen
        if (controller.available_flights_list.isEmpty()){
            controller.fillFlights(null);
        }else {
            System.out.println(controller.available_flights_list.size() );
            controller.fillFlights(controller.available_flights_list);
        }
    }



    public void searchHit() {
        if (!controller.search_f_name.getText().isEmpty()){
            controller.available_flights_list.clear();
            System.out.println("search from controll " + controller.search_f_name.getText());
            controller.available_flights_list = db.seachFlightFromSearchField(controller.search_f_name.getText());
            if (!controller.available_flights_list.isEmpty()){
                controller.fillFlights(controller.available_flights_list);
            }
        }
    }
}