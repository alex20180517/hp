package sample;

/*
* JDBC-API都定义在java.sql库中
* Driver接口用于创建连结对象
* Connection接口用于与数据库q取得连接
* Statement接口用于执行SQl语句
* ResultSet包含执行SQl语句后返回的数据集合
* */

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Controller(){

    }

    public void initialize(URL arg0, ResourceBundle arg1){

    }

    public void gotodoctor(javafx.event.ActionEvent actionEvent)  throws Exception{
        Stage CreateOperation_Stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        CreateOperation_Stage.hide();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("Doctorlogin.fxml")
        );
        AnchorPane root = new AnchorPane();
        Scene myScene = new Scene(root);
        try {
            myScene.setRoot((Parent) loader.load());
            Stage newStage = new Stage();
            newStage.setTitle("中心医院管理系统");
            newStage.setScene(myScene);
            newStage.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void gotopatient(ActionEvent actionEvent) throws Exception{
        Stage CreateOperation_Stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        CreateOperation_Stage.hide();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("Patientlogin.fxml")
        );
        AnchorPane root = new AnchorPane();
        Scene myScene = new Scene(root);
        try {
            myScene.setRoot((Parent) loader.load());
            Stage newStage = new Stage();
            newStage.setTitle("中心医院管理系统");
            newStage.setScene(myScene);
            newStage.show();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
