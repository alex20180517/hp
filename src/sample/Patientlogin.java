package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Patientlogin implements Initializable {
    static String patientID;
    @FXML private TextField username;
    @FXML private TextField passwords;
    Stage stage = new Stage();
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("patientlogin.fxml"));
        primaryStage.setTitle("中心医院管理系统");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    //public static void main(String[] args) {
    //    launch(args);
    //}

    //public void  showWindow() throws Exception {
    //start(stage);
    //}

    public void check(javafx.event.ActionEvent actionEvent) throws Exception {
        if(username.getText().equals("")||passwords.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("用户名和密码不能为空！");
            alert.showAndWait();
        }

        //验证用户名和密码是否正确
        else {
            try {
                String dlkl = null;
                String url = "jdbc:mysql://localhost:3306/hp?useSSL=false";
                System.out.println("正在连接数据库……");
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = null;
                con = DriverManager.getConnection(url,"root","wzq20109810");
                if(con != null){
                    System.out.println("成功连接到数据库");
                }
                Statement sta = con.createStatement();
                ResultSet rs = sta.executeQuery("SELECT * FROM T_BRXX WHERE T_BRXX.BRBH = " + username.getText());
                while(rs.next()) {
                    dlkl = rs.getString("DLKL");
                }
                if (passwords.getText().equals(dlkl) && username.getText().length() == 6) {
                    patientID = username.getText();
                    Stage CreateOperation_Stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    CreateOperation_Stage.hide();

                    Date dNOW = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentTime = sdf.format(dNOW);
                    try{
                        Statement temp = con.createStatement();
                        temp.executeUpdate("UPDATE T_BRXX SET DLRQ = '"+ currentTime + "' WHERE BRBH = " + username.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("guahao.fxml")
                    );
                    AnchorPane root = new AnchorPane();
                    Scene myScene = new Scene(root);
                    try {
                        myScene.setRoot(loader.load());
                        Stage newStage = new Stage();
                        newStage.setTitle("中心医院管理系统");
                        newStage.setScene(myScene);
                        newStage.show();
                    }catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("用户名或密码错误！");
                    alert.showAndWait();
                    username.setText("");
                    passwords.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void enter(ActionEvent actionEvent) {
        username.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    try {
                        check(actionEvent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        passwords.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    try {
                        check(actionEvent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
