package de.christophpircher.jfxbullshitbingo;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX and game logic controller
 * @author Christoph Pircher
 * */
public class BullshitBingoController implements Initializable {

    /**
     * Load button. Loads a new bullshit bingo when clicked.
     * */
    @FXML
    private Button btnLoad;

    /**
     * TextField for entering the base URL for the bullshit bingo.
     * */
    @FXML
    private TextField txtBaseURL;

    /**
     * TextField for entering the ID/file name of the bullshit bingo. The ID/name has to be entered without '.json'.
     * */
    @FXML
    private TextField txtBingoID;

    /** Label for displaying the title of the current bullshit bingo */
    @FXML
    private Label lblTitle;

    /** The 5*5 bingo 'sheet'. Contains the bingo fields. */
    @FXML
    private GridPane bingoPane;

    /**
     * 5*5 array which stores the bingo fields for the game logic.
     */
    private final BingoField[][] fields = new BingoField[5][5];

    /**
     * Stores if there is an active bullshit bingo.
     * */
    private boolean started = false;

    /**
     * Checks if there is a bingo in the specified row.
     * @param y Row to be checked.
     * @return true, if there is a bingo in the specified row; false, if not.
     */
    private boolean checkHorizontalBingo(int y){
        boolean result = true;
        for(int x = 0; x<5;++x){
            if(fields[x][y].getState() == BingoField.State.UNSELECTED){
                result = false;
            }
        }
        return result;
    }

    /**
     * Checks if there is a bingo in the specified column.
     * @param x Column to be checked.
     * @return true, if there is a bingo in the specified column; false, if not.
     */
    private boolean checkVerticalBingo(int x){
        boolean result = true;
        for(int y = 0; y<5;++y){
            if(fields[x][y].getState() == BingoField.State.UNSELECTED){
                result = false;
            }
        }
        return result;
    }

    /**
     * Changes the state of all fields in the specified row to 'BINGO'.
     * @param y Row to be changed.
     */
    private void horizontalBingo(int y){
        for(int x = 0; x<5;++x){
            BingoField curr = fields[x][y];
            curr.bingo();

        }

    }

    /**
     * Changes the state of all fields in the specified column to 'BINGO'.
     * @param x Column to be changed.
     */
    private void verticalBingo(int x){
        for(int y = 0; y<5;++y){
            BingoField curr = fields[x][y];
            curr.bingo();
        }
    }

    /**
     * Checks if there is a bingo in the top-left -> bottom-right diagonal.
     * @return true, if there is a bingo; false, if not
     * */
    private boolean checkDiagonalBingoTLBR(){
        boolean result = true;
        for(int x = 0, y = 0; x<5;++x,++y){
            if(fields[x][y].getState() == BingoField.State.UNSELECTED){
                result = false;
            }
        }
        return result;
    }

    /**
     * Checks if there is a bingo in the top-right -> bottom-left diagonal.
     * @return true, if there is a bingo; false, if not
     * */
    private boolean checkDiagonalBingoTRBL(){
        boolean result = true;
        for(int x = 4, y = 0; x>=0;--x,++y){
            if(fields[x][y].getState() == BingoField.State.UNSELECTED){
                result = false;
            }
        }
        return result;
    }

    /**
     * Changes the state of all fields in the top-left -> bottom-right diagonal to 'BINGO'
     * */
    private void diagonalBingoTLBR(){
        for(int x = 0, y = 0; x<5;++x,++y){
            BingoField curr = fields[x][y];
            curr.bingo();
        }
    }

    /**
     * Changes the state of all fields in the top-right -> bottom-left diagonal to 'BINGO'
     * */
    private void diagonalBingoTRBL(){
        for(int x = 4, y = 0; x>=0;--x,++y){
            BingoField curr = fields[x][y];
            curr.bingo();
        }
    }

    /**
     * Checks if there is a bingo of any type which contains the specified field.
     * Stets the state of all fields in a bingo to 'BINGO' for every bingo found.
     * @param field Field which has to be contained in a potential bingo.
     * */
    private void bingo(BingoField field){
        if(checkHorizontalBingo(field.getY())){
            horizontalBingo(field.getY());
        }
        if(checkVerticalBingo(field.getX())){
            verticalBingo(field.getX());
        }
        if(field.isPartOfDiagonalTLBR() && checkDiagonalBingoTLBR()){
            diagonalBingoTLBR();
        }
        if(field.isPartOfDiagonalTRBL() && checkDiagonalBingoTRBL()){
            diagonalBingoTRBL();
        }
    }

    /**
     * Checks all fields with the status 'Bingo' in the specified row if they are still contained in another bingo.
     * Setting the state to 'selected' if not.
     * @param y Row to be checked.
     * */
    private void unBingoHorizontal(int y){
        for(int x = 0; x <5; ++x){
            BingoField curr = fields[x][y];
            if(curr.getState() == BingoField.State.BINGO){
                if(!partOfBingo(curr)){
                    curr.select();
                }
            }
        }
    }

    /**
     * Checks all fields with the status 'Bingo' in the specified column if they are still contained in another bingo.
     * Setting the state to 'selected' if not.
     * @param x Column to be checked.
     * */
    private void unBingoVertical(int x){
        for(int y = 0; y < 5; ++y){
            BingoField curr = fields[x][y];
            if(curr.getState() == BingoField.State.BINGO){
                if(!partOfBingo(curr)){
                    curr.select();
                }
            }
        }
    }

    /**
     * Checks all fields with the status 'Bingo' in the top-left -> bottom-right diagonal if they are still contained in another bingo.
     * Setting the state to 'selected' if not.
     * */
    private void unBingoDiagonalTLBR(){
        for(int pos = 0; pos < 5; ++pos){
            BingoField curr = fields[pos][pos];
            if(curr.getState() == BingoField.State.BINGO){
                if(!partOfBingo(curr)){
                    curr.select();
                }
            }
        }
    }

    /**
     * Checks all fields with the status 'Bingo' in the top-right -> bottom-left diagonal if they are still contained in another bingo.
     * Setting the state to 'selected' if not.
     * */
    private void unBingoDiagonalTRBL(){
        for(int pos = 4; pos >= 0; --pos){
            BingoField curr = fields[pos][4-pos];
            if(curr.getState() == BingoField.State.BINGO){
                if(!partOfBingo(curr)){
                    curr.select();
                }
            }
        }
    }

    /**
     * Sets the status of the specified field to 'UNSELECTED' and checks for all fields in all directions if they are still contained in a bingo and sets the status accordingly.
     * @param field Field to un-bingo
     * */
    private void unBingo(BingoField field){
        field.getStyleClass().removeAll("bingo");
        field.unselect();

        /* If part is part of the top-left-bottom-right diagonal, check if fields in a potential top-left-bottom-right bingo are also part of another bingo. Setting State to "SELECTED" if not*/
        if(field.isPartOfDiagonalTLBR()){
            unBingoDiagonalTLBR();
        }

        /* If part is part of the top-right-bottom-left diagonal, check if fields in a potential top-right-bottom-left bingo are also part of another bingo. Setting State to "SELECTED" if not*/
        if(field.isPartOfDiagonalTRBL()){
            unBingoDiagonalTRBL();
        }

        /* Check all fields in a potential horizontal bingo if they are also a part of another bingo. Setting State to "SELECTED" if not. */
        unBingoHorizontal(field.getY());

        /* Check all fields in a potential vertical bingo if they are also a part of another bingo. Setting State to "SELECTED" if not. */
        unBingoVertical(field.getX());
    }


    /**
     * Checks if the specified field is still part of a bingo.
     * @param field Field to be checked.
     * @return true, if the field is still part of a bingo; false, if not
     */
    private boolean partOfBingo(BingoField field){
        boolean result = checkHorizontalBingo(field.getY());
        result |= checkVerticalBingo(field.getX());
        if(field.isPartOfDiagonalTLBR()){
            result |= checkDiagonalBingoTLBR();
        }
        if(field.isPartOfDiagonalTRBL()){
            result |= checkDiagonalBingoTRBL();
        }

        return result;
    }

    /**
     * Generates the bingo fields form a BullshitBingo object and adds the onMouseClicked handler to the bingo fields. <br>
     * Shows an error dialogue if there are not enough items in the words list.
     * onMouseClicked: <br>
     * <ul>
     * <li>state 'UNSELECTED': set state to 'SELECTED' and call bingo().</li>
     * <li>state 'SELECTED': set state to 'UNSELECTED'.</li>
     * <li>state 'BINGO': call unBingo().</li>
     * </ul>
     * @param bullshitBingo BullshitBingo object to be used to generate the bingo fields.
     * */
    private void startBullshitBingo(BullshitBingo bullshitBingo){
        try{
            lblTitle.setText(bullshitBingo.getTitle());
            for(int y = 0; y<5;++y){
                for(int x = 0; x< 5 ; ++x){
                    BingoField field = new BingoField(x,y, bullshitBingo.getRandomWord() ,x == y, x == (4-y));
                    bingoPane.add(field,x,y);
                    field.setWrapText(true);
                    field.setTextAlignment(TextAlignment.CENTER);
                    field.setAlignment(Pos.CENTER);
                    field.setMaxWidth(Double.MAX_VALUE);
                    field.setMaxHeight(Double.MAX_VALUE);
                    field.getStyleClass().add("bingoField");
                    field.setOnMouseClicked(e->{
                        switch (field.getState()){
                            case UNSELECTED:
                                field.select();
                                bingo(field);
                                break;
                            case SELECTED:
                                field.unselect();
                                break;
                            case BINGO:
                                unBingo(field);
                        }
                    });
                    fields[x][y] = field;
                }
            }
        } catch (IllegalStateException e){ //Not enough items in the words list
            /* No need for Platform.runLater(), method is only called in a Platform.runLater() block */
            bingoPane.getChildren().clear();
            lblTitle.setText("");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Could not load bullshit bingo");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            started = false;
            return;
        }
        bingoPane.getStyleClass().add("bingoField");
        started = true;
    }

    /**
     * Loads a new bullshit bingo with retrofit and Gson. If a bullshit bingo is currently active, the user will be asked to confirm that they really want to reload.
     * Shows an error dialogue on error.
     * */
    private void loadBullshitBingo(){
        if(started){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Restart bullshit bingo?");
            alert.setHeaderText(null);
            alert.setContentText("You will lose your current bullshit bingo! Do you really want to reload?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK){
                started = false;
                bingoPane.getChildren().clear(); //remove all fields from GridPane, otherwise they will be on top of each-other
            }
        }
        if(!started){
            lblTitle.setText("");
            bingoPane.getStyleClass().removeAll("bingoField");

            String baseurl = txtBaseURL.getText();
            if(!baseurl.endsWith("/")){ //Add a '/' to the base URL if not already present.
                baseurl += "/";
            }
            if(!baseurl.startsWith("https://") && !baseurl.startsWith("http://")){ //add 'https://' if no protocol is specified
                baseurl = "https://" + baseurl;
            }
            String bbID = txtBingoID.getText();

            if(!baseurl.equals("https:///") && !bbID.equals("")){ //Only load if user has filled in both text fields

               /* Load bullshit bingo with retrofit and convert json to BullshitBingo object with Gson */
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl).addConverterFactory(GsonConverterFactory.create(new Gson())).build();
                BullshitBingoAPI api = retrofit.create(BullshitBingoAPI.class);

                api.getBullshitBingo(bbID).enqueue(new Callback<BullshitBingo>() { //enqueue prevent UI-thread from blocking
                    @Override
                    public void onResponse(Call<BullshitBingo> call, Response<BullshitBingo> response) {
                        if(response.isSuccessful()){
                            BullshitBingo bullshitBingo = response.body();
                            if(bullshitBingo != null){
                                Platform.runLater(() -> startBullshitBingo(bullshitBingo));
                            }
                        }else{
                            Platform.runLater(()->{
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Could not load bullshit bingo");
                                alert.setHeaderText(null);
                                alert.setContentText(response.code() + ": " +response.message());
                                alert.showAndWait();
                            });
                        }
                    }
                    @Override
                    public void onFailure(Call<BullshitBingo> call, Throwable t) {
                        Platform.runLater(()->{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Could not load bullshit bingo");
                            alert.setHeaderText(null);
                            alert.setContentText(t.getMessage());
                            alert.showAndWait();
                        });
                    }
                });
            }
        }
    }


    /** Sets the OnAction handler of the load button to call loadBullshitBingo() and sets the title label to an empty string */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb){
        lblTitle.setText("");
        btnLoad.setOnAction(e->{
            loadBullshitBingo();
        });
    }
}