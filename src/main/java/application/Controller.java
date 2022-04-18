package application;

import application.model.*;
import application.auth.Purchase;
import application.database.Connection;
import application.moveScreen.MoveScreen;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import worldMap.World;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class Controller {
    //<editor-fold desc="Global variables" >

    // Default variables
    private CreateWorld createWorld;
    private World world;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private static User user;
    private File dir;
    private File[] files;
    private static boolean editingProfile;

    // FXML variables
    @FXML private ButtonBar logout;
    @FXML private TextField login_pass;
    @FXML private ScrollPane scrollPane;
    @FXML private ScrollPane scrollFlights;
    @FXML private TextField login_email;
    @FXML private Label error;
    @FXML private Label registration_error;
    @FXML private Label success_msg;
    @FXML private Label u_name, u_id;
    @FXML private VBox display_flight;
    @FXML private ImageView profilePicture;
    @FXML private ImageView profilePicturePreview;
    @FXML private TextField profileFirstName;
    @FXML private TextField profileLastName;
    @FXML private TextField profileEmail;
    @FXML private TextField profileAdress;
    @FXML private TextField profileNumber;
    @FXML private GridPane profileSelector;
    @FXML private PasswordField profilePassword;
    @FXML private Button btnEditProfile;

    // From game
    @FXML private StackPane game1;
    @FXML private StackPane game2;
    @FXML private Button quizButton;

    // Register a new user
    @FXML private TextField firstName, lastName, adress, email, number, password;

    // Seat
    private GridPane grid_left = new GridPane(); //Layout
    private GridPane grid_right = new GridPane(); //Layout
    private GridPane grid_business = new GridPane(); //Layout
    private AnchorPane pane = new AnchorPane();
    private HBox seatHbox;
    private Label newSeat = new Label();
    private Label label = new Label();      // Label
    private Label showSeat = new Label();

    private String returnSeat;
    private boolean typeSeat = false; // false = economy, true = business
    private int height = 600;
    private int width = 600;
    private int SeatAmount;

    // toggle options
    @FXML private Button iconProfile, iconFlight, iconHistorik, iconGame, iconSupport, iconCloseSeat;
    @FXML private AnchorPane pnlProfile, pnlHistorik, pnlFlight, pnlGame, pnlSupport;

    //Admin panels
    @FXML private AnchorPane pnlFlights, pnlTickets, pnlMember;
    @FXML private Button flightsBtn, membersBtn, ticketsBtn, logoutButton;
    //</editor-fold>

    //<editor-fold desc="Flights and dashboard variables">
    private ArrayList<Flight> avalibleFlights = new ArrayList<>();
    @FXML private TextField from_input_flight,disc_input_flight;
    @FXML private DatePicker date_input_flight;
    @FXML private Label no_flight_aval_msg;

    @FXML private AnchorPane pnlPayment;
    @FXML private TextField card_nbr,card_fname, card_lname, card_month, card_year, card_cvc;
    @FXML private Button card_prev_btn, card_purchase_btn, seat_next_btn;
    @FXML private ImageView rotate_logo_success_purchase;

    @FXML private AnchorPane pnl_success_purchase;
    @FXML private Button redirect_to_dash_btn, print_ticket_purchase_btn;

    // From seat
    @FXML private AnchorPane pnlSeat, pnlPassager;
    @FXML private TextField first_name_seat_pnl, last_name_seat_pnl, four_digit_seat_pnl, email_seat_pnl;
    @FXML private AnchorPane flight_seats_eco, flights_seats_business;
    @FXML private Label seat_nbr_seat_pnl, msg_seat_pnl, flight_nbr_seat_pnl, price_seat_pnl;
    //</editor-fold>

    //<editor-fold desc="Search variables">
    @FXML private TextField search_f_name;
    @FXML private ListView<String> searchListAppear, searchListAppear2, searchListAppear3;

    //</editor-fold

    //----------------- HOME -----------------//

    /**
     * the method will switch the user to the Home page
     * @param e
     * @throws IOException
     */
    public void switchToHome(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //stage.initStyle(StageStyle.TRANSPARENT);
        //MoveScreen.moveScreen(root,stage);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param e
     * @throws IOException
     */
    public void switchToGames (ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/games/Games.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("JetStream | Games");
        stage.setScene(scene);
        stage.show();

        quizButton = (Button) root.lookup("#quizButton");
        game1 = (StackPane) root.lookup("#game1");
        game2 = (StackPane) root.lookup("#game2");
        ImageView imageView = new ImageView(new Image("application/gamePosters/MusicQuiz.png"));
        ImageView imageView2 = new ImageView(new Image("application/gamePosters/PONG.png"));
        game1.getChildren().add(imageView);
        game2.getChildren().add(imageView2);
    }

    /**
     *
     */
    public void playPong(){
        Pong pong = new Pong();
        try {
            Stage primary = new Stage();
            pong.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void playQuiz(){
        MPlayer mPlayer = new MPlayer();
        try {
           Stage primary = new Stage();
            mPlayer.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void playPiano(){
        Piano piano = new Piano();
        try {
            Stage primary = new Stage();
            piano.start(primary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * navigate to dashboard pages
     * @param e
     * @throws IOException
     */
    public void switchToDashboard(ActionEvent e) throws IOException {
        if (!login_pass.getText().isEmpty() && !login_email.getText().isEmpty()) {
            User user = Connection.authenticationUser(login_email.getText(), login_pass.getText());
            if (user != null) {
                renderDashboard(e, user);
            } else {
                error.setText("Wrong email or pass!");
            }
        } else {
            renderDashboard(e, user);
        }
    } // the method will switch the user to the dashboard page

    /**
     * @param e
     * @param user
     * @throws IOException
     */
    public void renderDashboard(ActionEvent e, User user) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Dashboard.fxml")));
        this.user = user;

        initializeFXML(); // look up for elements in javaFX
        createWorld = new CreateWorld();
        world = createWorld.init(this);

        // Seat window
        HBox hboxLR_seat = new HBox();
        hboxLR_seat.getChildren().addAll(grid_left);
        grid_left.setHgap(5);
        grid_left.setVgap(5);
        grid_business.setHgap(5);
        grid_business.setVgap(5);
        HBox hboxTLR_seat = new HBox();
        hboxTLR_seat.getChildren().add(grid_business);
        hboxTLR_seat.setAlignment(Pos.TOP_CENTER);
        flight_seats_eco.getChildren().add(hboxLR_seat);
        flights_seats_business.getChildren().add(hboxTLR_seat);
        //seatBox.getChildren().addAll(hboxLR_seat);

        // world map
        scrollPane.setContent(new StackPane(world));
        scrollPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        u_name.setText(user.getName());
        u_id.setText(user.getId());
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        //MoveScreen.moveScreen(root,stage);
        stage.setTitle("JetStream | Dashboard");
        stage.setScene(scene);
        stage.show();
    } // the method will render dashboard page for user

    /**
     *
     */
    public void initializeFXML(){
        ////// look up for ticket variables

        // Purchase info
        card_nbr = (TextField) root.lookup("#card_nbr");
        card_fname = (TextField) root.lookup("#card_fname");
        card_lname = (TextField) root.lookup("#card_lname");
        card_month = (TextField) root.lookup("#card_month");
        card_year = (TextField) root.lookup("#card_year");
        card_cvc = (TextField) root.lookup("#card_cvc");
        rotate_logo_success_purchase = (ImageView) root.lookup("#rotate_logo_success_purchase");

        //Profile info
        if (user != null) {
            profilePicture = (ImageView) root.lookup("#profilePicture");
            profilePicturePreview = (ImageView) root.lookup("#profilePicturePreview");
            profileFirstName = (TextField) root.lookup("#profileFirstName");
            profileLastName = (TextField) root.lookup("#profileLastName");
            profileEmail = (TextField) root.lookup("#profileEmail");
            profileAdress = (TextField) root.lookup("#profileAdress");
            profileNumber = (TextField) root.lookup("#profileNumber");
            profilePassword = (PasswordField) root.lookup("#profilePassword");
            profilePicturePreview = (ImageView) root.lookup("#profilePicturePreview");
            profileSelector = (GridPane) root.lookup("#profileSelector");
            btnEditProfile = (Button) root.lookup("#btnEditProfile");

            profileFirstName.setText(user.getName());
            profileLastName.setText(user.getLname());
            profileEmail.setText(user.getEmail());
            profileAdress.setText(user.getAdress());
            profileNumber.setText(user.getNumber());
            profilePassword.setText(user.getPassword());

            try {
                profilePicturePreview.setImage(Connection.getProfilePicture(user));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            profileFirstName.setDisable(true);
            profileLastName.setDisable(true);
            profileEmail.setDisable(true);
            profileAdress.setDisable(true);
            profileNumber.setDisable(true);
            profilePassword.setDisable(true);
        }

        // Passenger info
        first_name_seat_pnl = (TextField) root.lookup("#name_seat_pnl");
        last_name_seat_pnl = (TextField) root.lookup("#lname_seat_pnl");
        four_digit_seat_pnl = (TextField) root.lookup("#fourdigit_seat_pnl");
        email_seat_pnl = (TextField) root.lookup("#email_seat_pnl");
        seat_nbr_seat_pnl = (Label) root.lookup("#seat_nbr_seat_pnl");
        msg_seat_pnl = (Label) root.lookup("#msg_seat_pnl");
        flight_nbr_seat_pnl = (Label) root.lookup("#flight_nbr_seat_pnl");
        price_seat_pnl = (Label) root.lookup("#price_seat_pnl");

        // look up for global variables
        u_name = (Label) root.lookup("#u_name");
        u_id = (Label) root.lookup("#u_id");
        searchListAppear = (ListView<String>) root.lookup("#searchListAprear");
        searchListAppear2 = (ListView<String>) root.lookup("#searchListAprear2");
        searchListAppear3 = (ListView<String>) root.lookup("#searchListAprear3");
        seat_nbr_seat_pnl = (Label) root.lookup("#seat_nbr_seat_pnl");
        flight_seats_eco = (AnchorPane) root.lookup("#flight_seats_eco");
        flights_seats_business = (AnchorPane) root.lookup("#flights_seats_business");
        pnlSeat = (AnchorPane) root.lookup("#pnlSeat");
        pnlPassager = (AnchorPane) root.lookup("#pnlPassanger");
        scrollFlights = (ScrollPane) root.lookup("#scrollFlights");
        scrollFlights.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        display_flight = (VBox) scrollFlights.getContent();
        scrollPane = (ScrollPane) root.lookup("#scrollPane");
        search_f_name = (TextField) root.lookup("#search_f_name");

        if (user != null) {
            try {
                Image image = Connection.getProfilePicture(user);
                profilePicture.setImage(image);
                profilePicturePreview.setImage(image);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param e
     * @throws IOException
     */
    public void noLoginRequired(ActionEvent e) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Dashboard.fxml")));
        initializeFXML();
        scrollFlights = (ScrollPane) root.lookup("#scrollFlights");
        scrollFlights.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pnlSeat = (AnchorPane) root.lookup("#pnlSeat");

        display_flight = (VBox) scrollFlights.getContent();
        scrollPane = (ScrollPane) root.lookup("#scrollPane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        createWorld = new CreateWorld();
        world = createWorld.init(this);

        scrollPane.setContent(new StackPane(world));
        scrollPane.setBackground(new Background(new BackgroundFill(world.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        u_name = (Label) root.lookup("#u_name");
        u_id = (Label) root.lookup("#u_id");
        u_name.setText(null);
        u_id.setText(null);
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        MoveScreen.moveScreen(root,stage);
        stage.setTitle("Test dashboard window");
        stage.setScene(scene);
        stage.show();
    }// shortcut login to user dashboard

    /**
     * @param e
     * @throws IOException
     */
    public void switchToChecking(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Checking.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Checking window");
        stage.setScene(scene);
        stage.show();
    }// the method will switch the user to the checking page

    /**
     * @param e
     * @throws IOException
     */
    public void switchToRegistration(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Registration.fxml")));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Registration window");
        stage.setScene(scene);
        stage.show();
    }// the method will switch the user to the registration page

    /**
     * @param e
     * @throws SQLException
     * @throws IOException
     */
    public void registeruser(ActionEvent e) throws SQLException, IOException {
        user = new User(null, firstName.getText(), lastName.getText(), adress.getText(), email.getText(), number.getText(), password.getText(), false);
        System.out.println(user.getName() + "fsdfsdfsdf");
        boolean ok = Connection.saveUser(user);
        if (ok) {
            renderLoginPage(e, "successfully registered the user!");
        } else {
            registration_error.setText("Couldn't register the information");
        }
    }// the method will register the user and return to the login page

    /**
     * @param e
     * @throws IOException
     */
    public void switchToLogin(ActionEvent e) throws IOException {
        renderLoginPage(e, null);
    }// the method will switch the user to the login page

    /**
     * @param e
     * @param msg
     * @throws IOException
     */
    public void renderLoginPage(ActionEvent e, String msg) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/Login.fxml")));
        success_msg = (Label) root.lookup("#success_msg");
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Login window");
        stage.setScene(scene);
        success_msg.setText(msg);
        stage.show();
    }// render login page

    /**
     * flight lists dashboard
     * @param flights
     */
    public void fillFlights (ArrayList <Flight> flights) {
        display_flight.getChildren().clear();
        grid_left.getChildren().clear();
        Stage infoStage = new Stage();
        AtomicBoolean openedStage = new AtomicBoolean(false);
        ArrayList<Flight> compare = new ArrayList<>();

        if (flights != null) {
        for (int i = 0; i < flights.size();i++){

            StackPane stackholer = new StackPane();
            HBox hbox = new HBox(1);
            HBox hboxChildCenter = new HBox(1);
            HBox hboxChildRight = new HBox(1);

            Image img = new Image("/application/image/jetStream.png");
            ImageView image = new ImageView(img);
            image.setFitWidth(30);
            image.setFitHeight(40);

            // flight icons
            Image onboard = new Image("/application/image/onboard.png");
            ImageView onboardIcon = new ImageView(onboard);
            onboardIcon.setFitWidth(30);
            onboardIcon.setOpacity(0.5);
            onboardIcon.setFitHeight(30);

            Image path = new Image("/application/image/path.png");
            ImageView pathIcon = new ImageView(path);
            pathIcon.setFitWidth(70);
            pathIcon.setFitHeight(30);
            pathIcon.setStyle("-fx-margin: 0 40 0 40; -fx-opacity: 0.5");

            Image landing = new Image("/application/image/landing.png");
            ImageView landingIcon = new ImageView(landing);
            landingIcon.setOpacity(0.5);
            landingIcon.setFitWidth(25);
            landingIcon.setFitHeight(25);

            Label titleF = new Label();
            titleF.setMaxSize(50,40);
            titleF.setStyle("-fx-text-fill: #999;");
            titleF.setText(flights.get(i).getDeparture_name());

            Text depTime = new Text();
            String tmp = flights.get(i).getDeparture_time();
            String[] sorted = tmp.split(":");
            String s = sorted[0] + ": " + sorted[1];
            depTime.setText(s);
            depTime.setFill(Color.valueOf("#eee"));
            depTime.setStyle("-fx-font-weight: bold");
            Text desTime = new Text();
            desTime.setText(s); // calculate arriving time
            desTime.setStyle("-fx-font-weight: bold");
            desTime.setFill(Color.valueOf("#eee"));


            Label titleD = new Label();
            titleD.setMaxSize(50,40);
            titleD.setText(flights.get(i).getDestination_name());
            titleD.setStyle("-fx-text-fill: #999;");


            Label date = new Label();
            date.setText(flights.get(i).getDestination_time());
            // box holderx
            VBox boardingBox = new VBox();
            boardingBox.setAlignment(Pos.CENTER_LEFT);
            boardingBox.getChildren().addAll(onboardIcon, depTime, titleF);
            // box holder
            VBox landingBox = new VBox();
            landingBox.setAlignment(Pos.CENTER_LEFT);
            landingBox.getChildren().addAll(landingIcon,desTime, titleD);

            Button btn = new Button("Book");
            btn.setStyle("-fx-background-color:  #ff8000; -fx-text-fill: #333; -fx-padding: 10; ");

            btn.setOnAction(e -> {
                // the for loop is going to restore the seat opacity
                System.out.println("Book clicked");
            });

            hboxChildCenter.getChildren().addAll(boardingBox, pathIcon, landingBox);
            hboxChildCenter.setSpacing(15);
            hboxChildCenter.setAlignment(Pos.CENTER_LEFT);

            hboxChildRight.getChildren().add(btn);
            hboxChildRight.setAlignment(Pos.CENTER_RIGHT);

            /***************  main box to hold the list  *********************/
            hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#151D3B"), CornerRadii.EMPTY, Insets.EMPTY)));
            hbox.getChildren().addAll(hboxChildCenter, hboxChildRight);
            hbox.setHgrow(hboxChildCenter, Priority.ALWAYS);
            hbox.setPadding(new Insets(5));
            hbox.setEffect(new DropShadow(2.0, Color.BLACK));
            hbox.setAlignment(Pos.TOP_LEFT);
            hbox.setSpacing(30);

            stackholer.getChildren().add(hbox);
            stackholer.setAlignment(Pos.TOP_LEFT);
            display_flight.getChildren().addAll(stackholer); // the box
            display_flight.setAlignment(Pos.TOP_LEFT);

            int finalI1 = i;
            // to click
            hbox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                for (int g = 0; g < display_flight.getChildren().size(); g++){
                    display_flight.getChildren().get(g).setOpacity(1);
                }

                // chosen flight from flight list will get an opacity of 0.8
                display_flight.getChildren().get(finalI1).setOpacity(0.95);
                for (int m = 0; m < display_flight.getChildren().size(); m++){
                    if (display_flight.getChildren().get(m) != display_flight.getChildren().get(finalI1)) {
                        display_flight.getChildren().get(m).setOpacity(1);
                    }
                }
                // the chosen flight will be added inside an arraylist
                compare.add(flights.get(finalI1));
                // create the seats for chosen flight
                chooseSeat(60, 9);
                price_seat_pnl.setText(flights.get(finalI1).getPrice());
                flight_nbr_seat_pnl.setText(flights.get(finalI1).getId());
                // flights seat panel will be shown
                pnlSeat.toFront();

            });
            // to hover
            hbox.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#112"), CornerRadii.EMPTY, Insets.EMPTY)));
            });
            hbox.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                hbox.setBackground(new Background(new BackgroundFill(Color.valueOf("#151D3B"), CornerRadii.EMPTY, Insets.EMPTY)));
            });
        }

        }else {
            System.out.println("flights list is null");
        }
    } // the method will show the flights list on the right side of the dashboard when a user choose a country

    public void editProfile() throws SQLException {
        if (editingProfile == false) {
            profileFirstName.setDisable(false);
            profileLastName.setDisable(false);
            profileEmail.setDisable(false);
            profileAdress.setDisable(false);
            profileNumber.setDisable(false);
            profilePassword.setDisable(false);
            btnEditProfile.setText("Confirm");
            editingProfile = true;
        } else {

            User editedUser = user;
            Boolean edited = false;

            if (!profileFirstName.getText().isEmpty()) {
                editedUser.setName(profileFirstName.getText());
                edited = true;
            }
            if(!profileLastName.getText().isEmpty()) {
                editedUser.setLname(profileLastName.getText());
                edited = true;
            }
            if (!profileEmail.getText().isEmpty()) {
                editedUser.setEmail(profileEmail.getText());
                edited = true;
            }
            if (!profileAdress.getText().isEmpty()){
                editedUser.setAdress(profileAdress.getText());
                edited = true;
            }
            if (!profileNumber.getText().isEmpty()){
                editedUser.setNumber(profileNumber.getText());
                edited = true;
            }
            if (!profilePassword.getText().isEmpty()){
                editedUser.setPassword(profilePassword.getText());
                edited = true;
            }

            if (edited) {
                System.out.println("Updating user..");
                user = editedUser;
                Connection.updateUser(user);
            } else {
                System.out.println("ypoo");
            }

            profileFirstName.setDisable(true);
            profileLastName.setDisable(true);
            profileEmail.setDisable(true);
            profileAdress.setDisable(true);
            profileNumber.setDisable(true);
            profilePassword.setDisable(true);
            editingProfile = false;
            btnEditProfile.setText("Edit");
        }
    }

    public void changeImage() {

        profileSelector.setVisible(true);
        dir = new File("src/main/resources/application/profiles/64x64");
        files = dir.listFiles();
        int b = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                profileSelector.add(new ImageView(new Image(files[b].getAbsolutePath())), i, j);
                b++;
            }
        }
    }

    public void clickGrid(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != profileSelector) {
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            dir = new File("src/main/resources/application/profiles/256x256");
            files = dir.listFiles();
            Image image = new Image(files[(colIndex*5)+rowIndex].getAbsolutePath());
            profilePicturePreview.setImage(image);
            profilePicture.setImage(image);
            String profilePic = (files[(colIndex*5)+rowIndex].getAbsolutePath());
            System.out.println(profilePic);
            profilePic = profilePic.substring(profilePic.indexOf("application") , profilePic.length());
            profilePic = profilePic.replace("\\","/");
            //profilePic = "file:" + profilePic;
            System.out.println(profilePic);

            try {
                Connection.setProfilePicture(profilePic, user);
            } catch (SQLException e) {
                e.printStackTrace();
            }



            profileSelector.setVisible(false);
            System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
        }
    }

    /**
     * create seats
     * @param econonySeats
     * @param businessSeats
     */
    public void chooseSeat(int econonySeats, int businessSeats) {
        grid_left.getChildren().removeAll();
        grid_business.getChildren().removeAll();
        this.SeatAmount = econonySeats;
        // 72/6 = 12
        // 12 row
        // 6 column
        boolean business = false;
        //
        for(int i = 0;i < econonySeats/10; i++){ // cal
            for(int j = 0;j <econonySeats/6; j++){ // row
                business = false;
                build_eco_seats(i,j, business);
            }
        }
        for(int i = 0;i < businessSeats/3; i++){ // cal
            for(int j = 0;j <businessSeats/3; j++){ // row
                business = true;
                build_eco_seats(i,j, business);
            }
        }

    }// the method will show the chosen seat on the screen

    /**
     * @param columnIndex
     * @param rowIndex
     * @param business
     */
    public void build_eco_seats(int columnIndex, int rowIndex, boolean business) {
        Label label = new Label();
        label.setMinWidth(30);
        label.setMinHeight(30);
        label.setText(label.getId());
        label.setBackground(new Background(new BackgroundFill(Color.rgb(223, 223, 222), new CornerRadii(5), Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(Color.rgb(247, 245, 242), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        label.setId(rowIndex+ " " + columnIndex);
        //grid_left.setColumnIndex(label, columnIndex);
        if (business){
            System.out.println("business: " + business);
            grid_business.add(label, columnIndex,rowIndex);
        }

        else if(!business) {
            System.out.println("business: " + business);
            if (grid_left.getColumnCount() == 3 && grid_left.getRowCount() == 0){
                System.out.println("column 3");
                grid_left.add(label, columnIndex, rowIndex);
                grid_left.setMargin(label, new Insets(0, 0, 0, 20));
            } else if (grid_left.getColumnCount() == 4 && grid_left.getRowCount() > 0) {
                grid_left.add(label, columnIndex, rowIndex);
            }
            else {
                grid_left.setMargin(label, new Insets(0, 0, 0, 0));
                grid_left.add(label, columnIndex, rowIndex);
            }
        }

        //grid_left.getColumnCount();
        label.setOnMouseClicked(e ->{
            seat_nbr_seat_pnl.setText(label.getId());
            // seat color change
            for (int i = 0; i < grid_left.getChildren().size(); i++){
                grid_left.getChildren().get(i).setOpacity(1);
                if (!Objects.equals(grid_left.getChildren().get(i).getId(), label.getId())){
                    grid_left.getChildren().get(i).setOpacity(0.5);
                }
            }
        });
    }

    /**
     * Purchase  ticket.
     * @param e
     */
    public void sendMailTest(ActionEvent e){
        Purchase.sendEmail(null,null, null,null, null);
    }

    /**
     * @param e
     */
    public void purchaseHandle(ActionEvent e){
        if (e.getSource() == card_prev_btn){
            pnlPassager.toFront();
            pnlPayment.toBack();
        }else if(e.getSource() == card_purchase_btn){
            System.out.println("purchase clicked");
            //<editor-fold desc="file">
                String nbr = card_nbr.getText();
                String name = card_fname.getText();
                String lname = card_lname.getText();
                String month = card_month.getText();
                String year = card_year.getText();
                String cvc = card_cvc.getText();
            //</editor-fold>

            if (!card_nbr.getText().isEmpty()){
                boolean validCard = Purchase.purchaseTicket(nbr, name, lname, month, year, cvc);
                if (validCard){
                    System.out.println("valid card");
                    boolean saveTicket = Connection.savePurchasedTicket(u_id.getText(), flight_nbr_seat_pnl.getText(), seat_nbr_seat_pnl.getText(), false);
                    if (saveTicket){
                        if (!email_seat_pnl.getText().isEmpty()){
                            boolean sentMail = Purchase.sendEmail(email_seat_pnl.getText(), first_name_seat_pnl.getText(), flight_nbr_seat_pnl.getText(), seat_nbr_seat_pnl.getText(), price_seat_pnl.getText());
                            if (sentMail){
                                System.out.println("Email successfully sent!");
                                pnl_success_purchase.toFront();
                            }else {
                                JOptionPane.showMessageDialog(null, "The email address is not correct!");
                            }
                        }
                        System.out.println("saved information in database");
                    }else {
                        JOptionPane.showMessageDialog(null, "Did not saved the purchase in database");
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Card is not valid");
                }
            }else {
                JOptionPane.showMessageDialog(null, "Purchase successfully done!");
            }
        }else if(e.getSource() == seat_next_btn){
            //<editor-fold desc="file">
                String name_s = first_name_seat_pnl.getText();
                String lname_s = last_name_seat_pnl.getText();
                String fourdigit = four_digit_seat_pnl.getText();
                String email = email_seat_pnl.getText();
                String seatnbr = seat_nbr_seat_pnl.getText();
            //</editor-fold>
           if (!name_s.isEmpty() && !lname_s.isEmpty() && !fourdigit.isEmpty() && !email.isEmpty() && !seatnbr.isEmpty()){
                pnlPayment.toFront();
            }else {
               msg_seat_pnl.setText("Empty field!");
               PauseTransition pause = new PauseTransition(Duration.seconds(2));
               pause.setOnFinished(a -> msg_seat_pnl.setText(null));
               pause.play();
            }
        }else if(e.getSource() == redirect_to_dash_btn){
            pnlFlight.toFront();
            restore_psgr_info();
            pnl_success_purchase.toBack();
            pnlPayment.toBack();
        }
    }
    public void restore_psgr_info(){
        first_name_seat_pnl.clear();
        last_name_seat_pnl.clear();
        four_digit_seat_pnl.clear();
        email_seat_pnl.clear();
        seat_nbr_seat_pnl.setText(null);
        flight_nbr_seat_pnl.setText(null);
        price_seat_pnl.setText(null);
        card_nbr.clear();
        card_fname.clear();
        card_lname.clear();
        card_month.clear();
        card_year.clear();
        card_cvc.clear();
    }

    /**
     * Navigate to admin pages.
     * @param e
     */
    public void switchToAdminView(ActionEvent e) {

        if (!login_pass.getText().isEmpty() && !login_email.getText().isEmpty()) {
            try {
                User user = Connection.authenticationAdmin(login_email.getText(), login_pass.getText());
                if (user != null) {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("admin/AdminView.fxml")));
                    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setTitle("Admin window");
                    stage.setScene(scene);
                    stage.show();
                } else {
                    error.setText("Wrong email or pass!");
                }
            }catch (IOException io){
                io.printStackTrace();
            }
        } else {
            error.setText("Fill the field!");
        }
    }

    /**
     * Dev test.
     * @param e
     */
    public void testDev(ActionEvent e){
        if (e.getSource() == iconProfile) {
            pnlProfile.toFront();
        }
        else if(e.getSource() == iconCloseSeat){
            pnlSeat.toBack();
            restore_psgr_info();
        }
        else if (e.getSource() == iconFlight) {
            pnlFlight.toFront();
        }
        else if (e.getSource() == iconHistorik) {
            pnlHistorik.toFront();
        }
        else if (e.getSource() == iconGame) {
            pnlGame.toFront();
        }
        else if(e.getSource() == iconSupport){
            pnlSupport.toFront();
        }

    }

    /**
     * Administrator dev.
     * @param e
     * @throws IOException
     */
    public void adminDev(ActionEvent e) throws IOException {
        if(e.getSource() == logoutButton)
        {
            switchToLogin(e);
        }

        else if(e.getSource() == flightsBtn)
        {
            pnlFlights.toFront();
        }

        else if(e.getSource() == ticketsBtn)
        {
            pnlTickets.toFront();
        }

        else if(e.getSource() == membersBtn)
        {
            pnlMember.toFront();
        }

    }

    //----------------- SEARCH FLIGHTS -----------------//

    /**
     * @param e
     */
    public void seachFlights(ActionEvent e) {
        LocalDate d = date_input_flight.getValue();
        System.out.println("Date: " +d);
        if (!(from_input_flight.getText().isEmpty()) && !(disc_input_flight.getText().isEmpty())){
            if (d != null) {
                avalibleFlights = Connection.searchFlight(from_input_flight.getText(), disc_input_flight.getText(), String.valueOf(d));
            } else {
                avalibleFlights = Connection.searchFlight(from_input_flight.getText(), disc_input_flight.getText());
            }
            if (avalibleFlights.isEmpty()){
                System.out.println("no flights available");
                fillFlights(null);
                //no_flight_aval_msg.setText("No flights available!");
            }else {
                //no_flight_aval_msg.setText("sf");
                fillFlights(avalibleFlights);
            }
        }
    }

    //----------------- SEARCH FIELD -----------------//

    /**
     *
     */
    public void searchHit(){
        if (!search_f_name.getText().isEmpty()){
            avalibleFlights.clear();
            avalibleFlights = Connection.seachFlightFromSearchField(search_f_name.getText());
            if (!avalibleFlights.isEmpty()){
                fillFlights(avalibleFlights);
            }else {
                JOptionPane.showMessageDialog(null, "No flight with: " + search_f_name.getText());
            }
        }else
            JOptionPane.showMessageDialog(null, "empty search field!");
    }

    /**
     *
     */
    public void searchAppear(){ // on key pressed search and show name
        if (search_f_name != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(search_f_name.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (searchListAppear != null){
                    searchListAppear.getItems().removeAll();
                }
                searchListAppear.setVisible(true);
                searchListAppear.setItems(searchAprear);
                searchListAppear.getSelectionModel().selectedItemProperty().addListener(e ->{
                    search_f_name.setText(searchListAppear.getSelectionModel().getSelectedItem());
                        searchListAppear.setVisible(false);
            });
            }
        }
    }

    /**
     *
     */
    public void departureNameAppear(){// on key pressed search and show name
        if (from_input_flight != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(from_input_flight.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (searchListAppear2 != null){
                    searchListAppear2.getItems().removeAll();
                }
                searchListAppear2.setVisible(true);
                searchListAppear2.setItems(searchAprear);
                searchListAppear2.getSelectionModel().selectedItemProperty().addListener(e ->{
                    from_input_flight.setText(searchListAppear2.getSelectionModel().getSelectedItem());
                        searchListAppear2.setVisible(false);
            });
            }
        }
    }

    /**
     *
     */
    public void destinationNameAppear(){// on key pressed search and show name
        if (disc_input_flight != null){
            ObservableList<String> searchAprear = FXCollections.observableList(propareSearchTerm(disc_input_flight.getText().toLowerCase()));
            if (!searchAprear.isEmpty()){
                if (searchListAppear3 != null){
                    searchListAppear3.getItems().removeAll();
                }
                searchListAppear3.setVisible(true);
                searchListAppear3.setItems(searchAprear);
                searchListAppear3.getSelectionModel().selectedItemProperty().addListener(e ->{
                    disc_input_flight.setText(searchListAppear3.getSelectionModel().getSelectedItem());
                        searchListAppear3.setVisible(false);
            });
            }
        }
    }

    /**
     * @param srch
     * @return
     */
    private ArrayList<String> propareSearchTerm(String srch){
        ArrayList<String> obs;
        if (srch.length() > 1){
            String searchTarget = srch.substring(0, 1).toUpperCase() + srch.substring(1); // convert first character to Uppercase
            obs = compareSearchKey(searchTarget);
        }else {
            String searchTarget = srch.toUpperCase();
            obs = compareSearchKey(searchTarget);
        }
        return obs;
    }

    /**
     * @param searchTargetKey
     * @return
     */
    private ArrayList<String> compareSearchKey(String searchTargetKey) {
        ArrayList<String> obs  = new ArrayList<>();
        for (Enum item : CountryList.values()) {
            if (item.toString().contains(searchTargetKey)){
                obs.add(item.toString());
                System.out.println(item + " /" + searchTargetKey);
            }
        }
        return obs;
    }


    //----------------- MENU (Not used) -----------------//

    public void openMenu() {
        logout.setLayoutX(0); // this will move in menu from outside the window
        System.out.println("Menu opened");
    } // the method will open the menu once the user clicked on his profile

    public void closeMenu() {
        logout.setLayoutX(-84); // this will move out the menu outside the window
        System.out.println("Menu closed");
    } // the method will close the menu

    public void exit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You are about to Exit!");
        alert.setContentText("Do you really want to Exit?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scrollPane.getScene().getWindow();
            System.out.println("You have successfully exited!");
            stage.close();
        }
    } // the method will close a scene

    public void exitProgram(){
        System.exit(0);
    } // to determinate the program


    //----------------- GETTERS AND SETTERS -----------------//
    public Scene getScene() {
        return scene;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
