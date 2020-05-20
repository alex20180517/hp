package sample;

import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Guahao{

    @FXML private TextField keshiName;
    @FXML public ChoiceBox<String> haozhongType;
    @FXML private TextField doctorName;
    @FXML private TextField haozhongName;
    @FXML private TextField jiaokuanValue;
    @FXML private TextField yingjiaoValue;
    @FXML private TextField zhaolingValue;
    @FXML private TextField guahaoNumber;
    @FXML private Button confirm;
    @FXML private Button clear;
    boolean isBalanceSufficient;

    private Set<String> autoCompletions;
    SuggestionProvider<String> provider;


    Stage stage = new Stage();

    @FXML
    public void initialize(){
        isBalanceSufficient = true;
        autoCompletions = new HashSet<>(Arrays.asList("A","B","C"));
        provider = SuggestionProvider.create(autoCompletions);
        TextFields.bindAutoCompletion(doctorName, provider);

        try {
            String url = "jdbc:mysql://localhost:3306/hp?useSSL=false";
            System.out.println("正在连接数据库……");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = null;
            con = DriverManager.getConnection(url, "root", "wzq20109810");
            if (con != null) {
                System.out.println("成功连接到数据库");
            }
            PreparedStatement statement = null;
            ResultSet rs = null;

            LinkedList<String> searchResult = new LinkedList<>();
            try {
                statement=(PreparedStatement) con.prepareStatement("SELECT * from T_KSXX");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {
                rs = statement.executeQuery();
                while(rs.next())
                {
                    String str1 = rs.getString("KSBH").trim();
                    String str2 = rs.getString("KSMC").trim();
                    String str3 = rs.getString("PYZS").trim();
                    String togetherStr = str1 + " " + str2 + " " + str3;
                    searchResult.add(togetherStr);
                }
                //
                TextFields.bindAutoCompletion(keshiName, searchResult);

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            haozhongType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println(newValue);
                    sfzjChanged(haozhongType.getItems().get((int) newValue));
                }
            });

            jiaokuanValue.textProperty().addListener((obs, oldText, newText) -> {
                if(!jiaokuanValue.getText().equals("") && !yingjiaoValue.getText().equals("") && !isBalanceSufficient &&!(jiaokuanValue.getText() == null) && !(yingjiaoValue.getText()==null))
                {
                    Double zhaoling = Double.parseDouble(jiaokuanValue.getText()) -  Double.parseDouble(yingjiaoValue.getText().substring(1));
                    if(zhaoling >= 0.0)
                    {
                        zhaolingValue.setText(Double.toString(zhaoling));
                    }else
                    {
                        zhaolingValue.setText("");
                    }
                }else
                {
                    zhaolingValue.setText("");
                }
            });
            //?
            keshiName.textProperty().addListener((obs, oldText, newText) -> {
                on_enter_keshiName();
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML public void on_enter_keshiName() {
        if(keshiName.getText().length() > 10 )
        {
            refreshPayandName();
        }
    }

    @FXML public void refreshPayandName() {
        try {
            String url = "jdbc:mysql://localhost:3306/hp?useSSL=false";
            System.out.println("正在连接数据库……");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = null;
            con = DriverManager.getConnection(url, "root", "wzq20109810");
            if (con != null) {
                System.out.println("成功连接到数据库");
            }
            PreparedStatement statement = null;
            ResultSet rs = null;
            LinkedList<String> searchResult = new LinkedList<>();

            int isPro;
            System.out.println(haozhongType.getValue());
            if(!(haozhongType.getValue() == null) && haozhongType.getValue().equals("专家号"))
            {
                isPro = 1;
            }else {
                isPro = 0;
            }

            try {
                String myPreState = "SELECT * from T_HZXX WHERE KSBH = '%1$s' AND SFZJ = %2$d";
                String myfinalState = String.format(myPreState, keshiName.getText().substring(0, 6),isPro);
                System.out.println(myfinalState);
                statement=(com.mysql.jdbc.PreparedStatement) con.prepareStatement(myfinalState);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            double need2Pay = 0;
            try {
                rs = statement.executeQuery();
                String togetherStr = null;
                String need2PayStr = null;//实际需要支付的费用
                while(rs.next())
                {
                    String str1 = rs.getString("HZBH").trim();
                    String str2 = rs.getString("HZMC").trim();
                    String str3 = rs.getString("PYZS").trim();
                    togetherStr = str1 + " " + str2 + " " + str3;
                    searchResult.add(togetherStr);

                    need2PayStr = rs.getString("GHFY").trim();
                    need2Pay = Double.parseDouble(need2PayStr);
                }
                haozhongName.setText(togetherStr);
                yingjiaoValue.setText(need2PayStr);

                String patientID = Patientlogin.patientID;

                try {
                    String myPreState = "SELECT * from T_BRXX WHERE BRBH = '%1$s'";
                    String myfinalState = String.format(myPreState, patientID);
                    System.out.println(myfinalState);
                    statement=(PreparedStatement) con.prepareStatement(myfinalState);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    rs = statement.executeQuery();
                    double ycValue = 0;
                    while(rs.next())
                    {
                        BigDecimal dec = rs.getBigDecimal("YCJE");
                        ycValue = dec.doubleValue();
                    }
                    if(ycValue >= need2Pay)
                    {
                        isBalanceSufficient = true;
                        Double zero = 0.0;
                        String aString = Double.toString(ycValue - zero);
                        jiaokuanValue.setEditable(true);
                        jiaokuanValue.setText(aString);
                        jiaokuanValue.setEditable(false);
                        zhaolingValue.setText(Double.toString(ycValue - need2Pay));
                        zhaolingValue.setEditable(false);
                    }else
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("余额不足！");
                        alert.showAndWait();
                        isBalanceSufficient = false;
                        jiaokuanValue.setEditable(true);
                        jiaokuanValue.clear();
                        zhaolingValue.setEditable(true);
                        zhaolingValue.clear();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void prepareDoctorNameAutocomplete()
    {

    }

    @FXML public void on_enter_doctorName() {
        int isPro = 0;
        if (haozhongType.getValue().equals("专家号")) {
            isPro = 1;
        } else {
            isPro = 0;
        }

        try {
            String url = "jdbc:mysql://localhost:3306/hp?useSSL=false";
            System.out.println("正在连接数据库……");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = null;
            con = DriverManager.getConnection(url, "root", "wzq20109810");
            if (con != null) {
                System.out.println("成功连接到数据库");
            }
            PreparedStatement statement = null;
            ResultSet rs = null;
            LinkedList<String> searchResult = new LinkedList<>();
            System.out.println("preparing doctor info\n");

            try {
                String fetchString = null;
                if(keshiName.getText().length() > 10)
                {
                    String temp = "SELECT * FROM T_KSYS WHERE KSBH = '%1$s' AND SFZJ = %2$d";
                    fetchString = String.format(temp, keshiName.getText().substring(0, 6),isPro);
                }else
                {
                    fetchString =  "SELECT * FROM T_KSYS";
                }
                statement=(PreparedStatement) con.prepareStatement(fetchString);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {
                rs = statement.executeQuery();
                while(rs.next())
                {
                    String str1 = rs.getString("YSBH").trim();
                    String str2 = rs.getString("YSMC").trim();
                    String str3 = rs.getString("PYZS").trim();
                    String togetherStr = str1 + " " + str2 + " " + str3;
                    System.out.println(togetherStr);
                    searchResult.add(togetherStr);
                }
                Set<String> filteredAutoCompletions = new HashSet<>(searchResult);
                provider.clearSuggestions();
                provider.addPossibleSuggestions(filteredAutoCompletions);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML public void sfzjChanged(String s) {
        int isPro;
        System.out.println("payment output!");
        System.out.println(haozhongType.getValue());
        if(s.equals("专家号"))
        {
            isPro = 1;
        }else {
            isPro = 0;
        }

        try{
            String url = "jdbc:mysql://localhost:3306/hp?useSSL=false";
            System.out.println("正在连接数据库……");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = null;
            con = DriverManager.getConnection(url, "root", "wzq20109810");
            if (con != null) {
                System.out.println("成功连接到数据库");
            }
            PreparedStatement statement = null;
            ResultSet rs = null;
            LinkedList<String> searchResult = new LinkedList<>();

            try {
                String myPreState = "SELECT * from T_HZXX WHERE KSBH = '%1$s' AND SFZJ = %2$d";

                String myfinalState = String.format(myPreState, keshiName.getText().substring(0, 6),isPro);
                System.out.println(myfinalState);
                statement=(com.mysql.jdbc.PreparedStatement) con.prepareStatement(myfinalState);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            double need2Pay = 0;
            try {
                rs = statement.executeQuery();
                String togetherStr = null;
                String need2PayStr = null;
                while(rs.next())
                {
                    String str1 = rs.getString("HZBH").trim();
                    String str2 = rs.getString("HZMC").trim();
                    String str3 = rs.getString("PYZS").trim();
                    togetherStr = str1 + " " + str2 + " " + str3;
                    searchResult.add(togetherStr);

                    need2PayStr = rs.getString("GHFY").trim();
                    need2Pay = Double.parseDouble(need2PayStr);
                }
                haozhongName.setText(togetherStr);
                yingjiaoValue.setText(need2PayStr);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            String patientID = Patientlogin.patientID;

            try {
                String myPreState = "SELECT * from T_BRXX WHERE BRBH = '%1$s'";
                String myfinalState = String.format(myPreState, patientID);
                System.out.println(myfinalState);
                statement=(com.mysql.jdbc.PreparedStatement) con.prepareStatement(myfinalState);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {
                rs = statement.executeQuery();
                double ycValue = 0;
                while(rs.next())
                {
                    BigDecimal dec = rs.getBigDecimal("YCJE");
                    ycValue = dec.doubleValue();
                }
                if(ycValue >= need2Pay)
                {
                    isBalanceSufficient = true;
                    double zero = 0.0;
                    String aString = Double.toString(ycValue - zero) + "元";
                    jiaokuanValue.setText(aString);
                    jiaokuanValue.setEditable(false);
                    zhaolingValue.setText(Double.toString(ycValue - need2Pay) + "元");
                    zhaolingValue.setEditable(false);
                }else
                {
                    isBalanceSufficient = false;
                    jiaokuanValue.setEditable(true);
                    jiaokuanValue.clear();
                    zhaolingValue.setEditable(true);
                    zhaolingValue.clear();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML public void Clear() {
        keshiName.clear();
        doctorName.clear();
        haozhongName.clear();
        jiaokuanValue.clear();
        yingjiaoValue.clear();
        zhaolingValue.clear();
        guahaoNumber.clear();
        jiaokuanValue.setEditable(true);
        zhaolingValue.setEditable(true);
    }

    @FXML public void Quit(ActionEvent actionEvent) {
        Stage CreateOperation_Stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        CreateOperation_Stage.close();
    }

    public void on_guahaoNumber_clicked() {
        try {
            String url = "jdbc:mysql://localhost:3306/hp?useSSL=false";
            System.out.println("正在连接数据库……");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = null;
            con = DriverManager.getConnection(url, "root", "wzq20109810");
            if (con != null) {
                System.out.println("成功连接到数据库");
            }
            PreparedStatement statement = null;
            ResultSet rs = null;

            System.out.println("preparing guahao Number info\n");

            try {
                statement=(PreparedStatement) con.prepareStatement("SELECT COUNT(*) from T_GHXX");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {
                rs = statement.executeQuery();
                String numStr = null;
                while(rs.next())
                {
                    numStr = rs.getString("COUNT(*)").trim();
                    System.out.println("The total number of guahao is " + numStr);
                }
                int num = (int)Double.parseDouble(numStr) + 1;
                String outputStr = String.format("%06d", num);
                guahaoNumber.setText("本次挂号号码：" + outputStr);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Sure(ActionEvent actionEvent) {
        if(keshiName.getText().equals("")||doctorName.getText().equals("")||haozhongName.getText().equals("")||zhaolingValue.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("信息填写不完整！");
            alert.showAndWait();
        }else{
            String GHBH = null;
            String HZBH = haozhongName.getText().substring(0, 6);
            String YSBH = doctorName.getText().substring(0, 6);
            String BRBH = Patientlogin.patientID;
            int GHRC = 0;
            boolean THBZ = false;
            BigDecimal GHFY = new BigDecimal(yingjiaoValue.getText());

            java.util.Date dNow = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String RQSJ = sdf.format(dNow);

            try {
                String url = "jdbc:mysql://localhost:3306/hp?useSSL=false";
                System.out.println("正在连接数据库……");
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = null;
                con = DriverManager.getConnection(url, "root", "wzq20109810");
                if (con != null) {
                    System.out.println("成功连接到数据库");
                }
                PreparedStatement statement = null;
                ResultSet rs = null;

                try {
                    String preStr = "SELECT MAX(T_GHXX.GHRC), T_HZXX.GHRS from T_GHXX, T_HZXX"
                            + " WHERE T_HZXX.HZBH = T_GHXX.HZBH AND T_HZXX.HZBH = '%1$s'";
                    preStr = String.format(preStr, HZBH);
                    statement=(PreparedStatement) con.prepareStatement(preStr);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    rs = statement.executeQuery();
                    int maxGHRC;
                    while(rs.next())
                    {
                        GHRC = rs.getInt("MAX(T_GHXX.GHRC)");
                        maxGHRC = rs.getInt("T_HZXX.GHRS");
                        System.out.println(GHRC + "  " + maxGHRC);
                        if (maxGHRC <= GHRC)
                        {
                            JOptionPane.showMessageDialog(null, "挂号人数已满", "抱歉", 1);
                            return;
                        }
                        GHRC++;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    statement=(PreparedStatement) con.prepareStatement("SELECT COUNT(*) from T_GHXX");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    rs = statement.executeQuery();
                    String numStr = null;
                    while(rs.next())
                    {
                        numStr = rs.getString("COUNT(*)").trim();
                    }
                    int num = (int)Double.parseDouble(numStr) + 1;
                    GHBH = String.format("%06d", num);
                    System.out.println("GHBH is " + GHBH);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    //成功挂号后将信息记录到挂号信息表中
                    String insertGHXX = "INSERT INTO T_GHXX VALUES(?,?,?,?,?,?,?,?)";
                    statement=(PreparedStatement) con.prepareStatement(insertGHXX);
                    statement.setString(1, GHBH);
                    statement.setString(2, HZBH);
                    statement.setString(3, YSBH);
                    statement.setString(4, BRBH);
                    statement.setInt(5, GHRC);
                    statement.setBoolean(6, THBZ);
                    statement.setBigDecimal(7, GHFY);
                    statement.setString(8, RQSJ);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    System.out.println(statement);
                    int isSuc = statement.executeUpdate();
                    System.out.println(isSuc);
                    if(isSuc > 0)
                    {
                        try {
                            Statement temp = con.createStatement();
                            temp.executeUpdate("UPDATE T_BRXX SET YCJE = YCJE - "+ yingjiaoValue.getText() + " WHERE BRBH = " + Patientlogin.patientID);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("挂号成功！");
                        alert.showAndWait();
                        Clear();
                    }else
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("挂号人数已满！");
                        alert.showAndWait();
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
