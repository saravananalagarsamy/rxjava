import java.sql.Time;
import java.util.Date;

/**
 * Created by m509575 on 23/08/17.
 */
public class Stocks<name,data> {
    private String stockName;
    private TupleStock stockData;

    public Stocks(String name, String data){
        this.stockName = name;
        setStockData(data);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stocks stocks = (Stocks) o;

        if (stockName != null ? !stockName.equals(stocks.stockName) : stocks.stockName != null) return false;
        return stockData != null ? stockData.equals(stocks.stockData) : stocks.stockData == null;
    }

    @Override
    public int hashCode() {
        int result = stockName != null ? stockName.hashCode() : 0;
        result = 31 * result + (stockData != null ? stockData.hashCode() : 0);
        return result;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public TupleStock getStockData() {
        return stockData;
    }

    public void setStockData(TupleStock stockData) {
        this.stockData = stockData;
    }

    public void setStockData(String stockData) {
        TupleStock stock = new TupleStock();
        stock.setStockDate(new Date(stockData.split(",")[0]));
        stock.setOpenData(stockData.split(",")[1]);
        stock.setHighData(stockData.split(",")[2]);
        stock.setLowData(stockData.split(",")[3]);
        stock.setCloseData(stockData.split(",")[4]);
        stock.setVolumeData(stockData.split(",")[5]);
        this.stockData = stock;
    }
}
