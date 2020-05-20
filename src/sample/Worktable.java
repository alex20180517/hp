package sample;

import com.sun.javafx.collections.MappingChange;
import com.sun.prism.impl.Disposer;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.sql.*;

public class Worktable{
    @FXML public Text huanyin;
    @FXML public Label timeLabel;

    private ObservableList<PatientInfo> personData = FXCollections.observableArrayList();
    @FXML private TableView<PatientInfo> patientInfoTable;
    @FXML private TableColumn<PatientInfo, String> ghbhColumn;
    @FXML private TableColumn<PatientInfo, String> brxmColumn;
    @FXML private TableColumn<PatientInfo, String> rqsjColumn;
    @FXML private TableColumn<PatientInfo, String> hzlbColumn;

    private ObservableList<Income> incomeData = FXCollections.observableArrayList();
    @FXML private TableView<Income> incomeInfoTable;
    @FXML private TableColumn<Income, String> KSMCColumn;
    @FXML private TableColumn<Income, String> YSBHColumn;
    @FXML private TableColumn<Income, String> YSMCColumn;
    @FXML private TableColumn<Income, String> HZLBColumn;
    @FXML private TableColumn<Income, String> GHRCColumn;
    @FXML private TableColumn<Income, String> SRHJColumn;

    private String GHBH;
    private String BRMC;
    private Date RQSJ;
    private String GHZL;

    Stage stage = new Stage();
    @FXML
    public void initialize(){
        //primaryStage.setTitle("中心医院管理系统");
        //primaryStage.setScene(new Scene(root, 600, 400));
        ghbhColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().GHBHProperty()));
        brxmColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().BRMCProperty()));
        rqsjColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().registDateProperty()));
        hzlbColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isProProperty()));
        showpatientlist();
        KSMCColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().KSMCProperty()));
        YSBHColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().YSBHProperty()));
        YSMCColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().YSMCProperty()));
        HZLBColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().HZLBProperty()));
        GHRCColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().GHRCProperty()));
        SRHJColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().SRHJProperty()));
        showIncomeInfo();
        //primaryStage.show();
    }

    public void showpatientlist(){
        try{
            String url = "jdbc:mysql://localhost:3306/hp?useSSL=false";
            System.out.println("正在连接数据库……");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = null;
            con = DriverManager.getConnection(url,"root","wzq20109810");
            if(con != null){
                System.out.println("成功连接到数据库");
            }
            PreparedStatement statement = null;
            ResultSet rs = null;

            try{
                statement = (PreparedStatement) con.prepareStatement("SELECT * FROM T_GHXX, T_BRXX, T_KSYS "
                        + "WHERE T_GHXX.BRBH = T_BRXX.BRBH "
                        + "AND T_GHXX.YSBH = T_KSYS.YSBH "
                        + "AND T_GHXX.YSBH = ?");
                statement.setString(1,Doctorlogin.doctorId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try{
                rs = statement.executeQuery();
                System.out.println(rs);
                String ghbh = null;
                String brmc = null;
                String rqsj = null;
                String hzlb = null;
                while (rs.next()){
                    ghbh = rs.getString("GHBH").trim();
                    brmc = rs.getString("BRMC").trim();
                    rqsj = rs.getString("RQSJ").trim();
                    rqsj = rqsj.substring(0,rqsj.length()-2);
                    if( rs.getInt("SFZJ") == 1)
                    {
                        hzlb = "专家号";
                    }else
                    {
                        hzlb = "普通号";
                    }
                    System.out.println(ghbh + brmc + rqsj + hzlb);
                    personData.add(new PatientInfo(ghbh, brmc, rqsj, hzlb));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            patientInfoTable.setItems(personData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showIncomeInfo() {
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
                statement=(com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT "
                        + "KSMC, T_KSYS.YSBH, YSMC, T_KSYS.SFZJ, COUNT(*), T_GHXX.GHFY , MIN(T_GHXX.RQSJ), MAX(T_GHXX.RQSJ)"
                        + "FROM T_GHXX, T_KSXX, T_KSYS, T_HZXX "
                        + "WHERE T_KSYS.KSBH = T_KSXX.KSBH "
                        + "AND T_GHXX.YSBH = T_KSYS.YSBH "
                        + "AND T_GHXX.HZBH = T_HZXX.HZBH "
                        + "GROUP BY T_KSYS.YSBH"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                rs = statement.executeQuery();
                String KSMC = null;
                String YSBH = null;
                String YSMC = null;
                String HZLB = null;
                String GHRC = null;
                String SRHJ = null;
                String earliestTime = null;
                String latestTime = null;
                String timeLabelStr = null;
                while(rs.next())
                {
                    System.out.println(rs.getString("T_KSYS.YSBH"));
                    System.out.println(Doctorlogin.doctorId);
                    if(rs.getString("T_KSYS.YSBH").equals(Doctorlogin.doctorId)) {
                        earliestTime = rs.getString("MIN(T_GHXX.RQSJ)");
                        latestTime = rs.getString("MAX(T_GHXX.RQSJ)");
                        //System.out.println(earliestTime);
                        //System.out.println(latestTime);
                        timeLabelStr = "开始时间" + earliestTime.substring(0, earliestTime.length() - 2) +
                                "   " + "结束时间" + latestTime.substring(0, latestTime.length() - 2);
                        timeLabel.setText(timeLabelStr);
                    }
                    KSMC = rs.getString("KSMC").trim();
                    YSBH = rs.getString("T_KSYS.YSBH").trim();
                    YSMC = rs.getString("YSMC").trim();
                    if(rs.getInt("T_KSYS.SFZJ") == 1)
                    {
                        HZLB = "专家号";
                    }else {
                        HZLB = "普通号";
                    }

                    int patientCount = rs.getInt("COUNT(*)");
                    BigDecimal regiFeeDec = rs.getBigDecimal("T_GHXX.GHFY");
                    double regiFee = regiFeeDec.doubleValue();
                    double totalFee = patientCount * regiFee;

                    GHRC = String.valueOf(patientCount);
                    SRHJ = String.valueOf(totalFee);
                    System.out.println(KSMC + YSBH + YSMC + HZLB + GHRC + SRHJ);
                    incomeData.add(new Income(KSMC, YSBH, YSMC, HZLB, GHRC, SRHJ));
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        incomeInfoTable.setItems(incomeData);
    }
}
