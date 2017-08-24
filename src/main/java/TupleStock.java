import java.util.Date;

/**
 * Created by m509575 on 23/08/17.
 */
public class TupleStock {

    private Date stockDate;
    private String openData;
    private String highData;
    private String lowData;
    private String closeData;
    private String volumeData;

    public  TupleStock(){}

    public TupleStock(Date stockDate, String openData, String highData, String lowData, String closeData, String volumeData) {
        this.stockDate = stockDate;
        this.openData = openData;
        this.highData = highData;
        this.lowData = lowData;
        this.closeData = closeData;
        this.volumeData = volumeData;
    }

    public Date getStockDate() {
        return stockDate;
    }

    public void setStockDate(Date stockDate) {
        this.stockDate = stockDate;
    }

    public String getOpenData() {
        return openData;
    }

    public void setOpenData(String openData) {
        this.openData = openData;
    }

    public String getHighData() {
        return highData;
    }

    public void setHighData(String highData) {
        this.highData = highData;
    }

    public String getLowData() {
        return lowData;
    }

    public void setLowData(String lowData) {
        this.lowData = lowData;
    }

    public String getCloseData() {
        return closeData;
    }

    public void setCloseData(String closeData) {
        this.closeData = closeData;
    }

    public String getVolumeData() {
        return volumeData;
    }

    public void setVolumeData(String volumeData) {
        this.volumeData = volumeData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TupleStock that = (TupleStock) o;

        if (stockDate != null ? !stockDate.equals(that.stockDate) : that.stockDate != null) return false;
        if (openData != null ? !openData.equals(that.openData) : that.openData != null) return false;
        if (highData != null ? !highData.equals(that.highData) : that.highData != null) return false;
        if (lowData != null ? !lowData.equals(that.lowData) : that.lowData != null) return false;
        if (closeData != null ? !closeData.equals(that.closeData) : that.closeData != null) return false;
        return volumeData != null ? volumeData.equals(that.volumeData) : that.volumeData == null;
    }

    @Override
    public int hashCode() {
        int result = stockDate != null ? stockDate.hashCode() : 0;
        result = 31 * result + (openData != null ? openData.hashCode() : 0);
        result = 31 * result + (highData != null ? highData.hashCode() : 0);
        result = 31 * result + (lowData != null ? lowData.hashCode() : 0);
        result = 31 * result + (closeData != null ? closeData.hashCode() : 0);
        result = 31 * result + (volumeData != null ? volumeData.hashCode() : 0);
        return result;
    }


}
