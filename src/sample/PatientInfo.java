package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatientInfo {
    private final StringProperty GHBH;
    private final StringProperty BRMC;
    private final StringProperty registDate;
    private final StringProperty isPro;

    public PatientInfo(String GHBH,String BRMC,String registDate, String isPro)
    {
        this.GHBH = new SimpleStringProperty(GHBH);
        this.BRMC = new SimpleStringProperty(BRMC);
        this.registDate = new SimpleStringProperty(registDate);
        this.isPro = new SimpleStringProperty(isPro);
    }

    public String GHBHProperty() {
        return GHBH.get();
    }
    public String BRMCProperty() {
        return BRMC.get();
    }
    public String registDateProperty()
    {
        return registDate.get();
    }
    public String isProProperty()
    {
        return isPro.get();
    }
}
