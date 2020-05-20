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
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class Doctorlogin implements Initializable {
    @FXML private TextField username;
    @FXML private TextField passwords;
    Stage stage = new Stage();
    static String ysxm = null;
    static String doctorId = null;

    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("doctorlogin.fxml"));
        primaryStage.setTitle("中心医院管理系统");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

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
                ResultSet rs = sta.executeQuery("SELECT * FROM T_KSYS WHERE T_KSYS.YSBH = " + username.getText());
                while(rs.next()) {
                    dlkl = rs.getString("DLKL");
                    ysxm = rs.getString("YSBH");
                }
                if (passwords.getText().equals(dlkl) && username.getText().length() == 6) {
                    doctorId = username.getText();
                    Stage CreateOperation_Stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    CreateOperation_Stage.hide();

                    FXMLLoader root = new FXMLLoader(
                            getClass().getResource("worktable.fxml")
                    );

                    Date dNOW = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentTime = sdf.format(dNOW);
                    try{
                        Statement temp = con.createStatement();
                        temp.executeUpdate("UPDATE T_KSYS SET DLRQ = '"+ currentTime + "' WHERE YSBH = " + username.getText());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        Stage newStage = new Stage(StageStyle.DECORATED);
                        newStage.setTitle("中心医院管理系统");
                        newStage.setScene(new Scene(root.load()));
                        newStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
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
