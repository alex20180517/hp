package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Income {
    private final StringProperty KSMC;
    private final StringProperty YSBH;
    private final StringProperty YSMC;
    private final StringProperty HZLB;
    private final StringProperty GHRC;
    private final StringProperty SRHJ;

    public Income(String KSMC,String YSBH,String YSMC, String HZLB, String GHRC, String SRHJ)
    {
        this.KSMC = new SimpleStringProperty(KSMC);
        this.YSBH = new SimpleStringProperty(YSBH);
        this.YSMC = new SimpleStringProperty(YSMC);
        this.HZLB = new SimpleStringProperty(HZLB);
        this.GHRC = new SimpleStringProperty(GHRC);
        this.SRHJ = new SimpleStringProperty(SRHJ);
    }

    public String KSMCProperty() {
        return KSMC.get();
    }

    public String YSBHProperty() {
        return YSBH.get();
    }

    public String YSMCProperty() {
        return YSMC.get();
    }

    public String HZLBProperty() {
        return HZLB.get();
    }

    public String GHRCProperty() {
        return GHRC.get();
    }

    public String SRHJProperty() {
        return SRHJ.get();
    }
}
