package com.skaiciuokle.bustoskaiciuokle;

import com.skaiciuokle.data.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.text.DecimalFormat;

import java.io.IOException;


public class InputController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label chooseLabel, detailsLoan;
    @FXML
    private TextField sumText;
    @FXML
    private TextField percentageText, termYearsText, termMonthsText, startFilter, endFilter,startPost, postPercentage, durationMonths;
    @FXML
    private Button submitButton;
    @FXML
    private Button exportButton;
    @FXML
    private CheckBox filter;
    @FXML
    private CheckBox postponement;
    @FXML
    private RadioButton annuityButton, linearButton;
    @FXML
    private TableView table;
    @FXML
    private LineChart<?,?> chart;
    final ObservableList<Payment> data = FXCollections.observableArrayList();

    int months;
    double sum;
    double percentage;
    int termYears;
    int termMonths;
    String loanType;
    int error = 0;
    DecimalFormat df = new DecimalFormat("#.00");
    XYChart.Series series = new XYChart.Series();
    XYChart.Series series2 = new XYChart.Series();

    public void submit(ActionEvent event) {
        table.getItems().clear();
        try {
            sum = Double.parseDouble(sumText.getText());
        } catch (NumberFormatException e) {
            detailsLoan.setText("Loan sum input is wrong");
        }
        try {
            percentage = Double.parseDouble(percentageText.getText());
        } catch (NumberFormatException e) {
            detailsLoan.setText("Loan percentage is wrong");
        }
        try {
            termYears = Integer.parseInt(termYearsText.getText());
        } catch (NumberFormatException e) {
            detailsLoan.setText("Loan term in years is wrong");
        }
        try {
            termMonths = Integer.parseInt(termMonthsText.getText());
        } catch (NumberFormatException e) {
            detailsLoan.setText("Loan term in months input is wrong");
        }
        if (error == 0) {
            detailsLoan.setText("Enter details of your loan");
        }
        double currentAmount = sum;
        months = termYears * 12 + termMonths;
        double[] amountArray = new double[months];
        double[] percentArray = new double[months];
        double[] unpaidArray = new double[months];
        double monthlyPayment = sum / months;

        double all = 0;
        double left[]=new double[months];
        String str;
        double postSum;
        if (Objects.equals(loanType, "Linear")) {
            chart.getData().clear();
            series.setName("Linear");
            for (int i = 0; i < months; i++) {
                unpaidArray[i] = currentAmount;
                percentArray[i] = percentage;
                if (currentAmount < 0) {
                    amountArray[i] = monthlyPayment;
                } else {
                    amountArray[i] = monthlyPayment + currentAmount * percentage / 100 / 12;
                }
                currentAmount -= amountArray[i];
                all += amountArray[i];
            }
            for(int i = 0;i<months;i++){
                left[i]=all;
                left[i]-=amountArray[i];
                for(int j = 0;j<i;j++){
                    left[i]-=amountArray[j];
                }
            }
            if(filter.isSelected()){
                for (int i = Integer.parseInt(startFilter.getText()); i <= Integer.parseInt(endFilter.getText()); i++) {
                    if(postponement.isSelected()&&i==Integer.parseInt(startPost.getText())){
                        postSum=sum*Double.parseDouble(postPercentage.getText())/100/12;
                        for(int j = 1;j<=Integer.parseInt(durationMonths.getText());j++){
                            data.add(new Payment(Integer.toString(j),df.format(postSum),df.format(Double.parseDouble(postPercentage.getText())),df.format(0)));
                        }
                    }
                    str = Integer.toString(i);
                    data.add(new Payment(str, df.format(amountArray[i-1]), df.format(percentArray[i-1]), df.format(left[i-1])));
                    series.getData().add(new XYChart.Data(str, (int) amountArray[i-1]));
                    all -= amountArray[i];
                }
                chart.getData().add(series);
            }
            else {
                for (int i = 0; i < months; i++) {
                    if(postponement.isSelected()&&i==Integer.parseInt(startPost.getText())){
                        postSum=sum*Double.parseDouble(postPercentage.getText())/100/12;
                        for(int j = 1;j<=Integer.parseInt(durationMonths.getText());j++){
                            data.add(new Payment(Integer.toString(j),df.format(postSum),df.format(Double.parseDouble(postPercentage.getText())),df.format(0)));
                        }
                    }
                    str = Integer.toString(i + 1);
                    data.add(new Payment(str, df.format(amountArray[i]), df.format(percentArray[i]), df.format(left[i])));
                    series.getData().add(new XYChart.Data(str, (int) amountArray[i]));
                    all -= amountArray[i];
                }
                chart.getData().add(series);
            }
        } else if (Objects.equals(loanType, "Annuity")) {
            currentAmount *= percentage / 100 / 12;
            currentAmount /= 1 - Math.pow(1 + percentage / 100 / 12, months * (-1));
            all = currentAmount * months;
            chart.getData().clear();
            series2.setName("Annuity");
            for(int i = 0;i<months;i++){
                left[i]=all;
                left[i]-=currentAmount;
                for(int j = 0;j<i;j++){
                    left[i]-=currentAmount;
                }
            }
            if (filter.isSelected()) {
                for (int i = Integer.parseInt(startFilter.getText()); i <= Integer.parseInt(endFilter.getText()); i++) {
                    if(postponement.isSelected()&&i==Integer.parseInt(startPost.getText())){
                        postSum=sum*Double.parseDouble(postPercentage.getText())/100/12;
                        for(int j = 1;j<=Integer.parseInt(durationMonths.getText());j++){
                            data.add(new Payment(Integer.toString(j),df.format(postSum),df.format(Double.parseDouble(postPercentage.getText())),df.format(0)));
                        }
                    }
                    str = Integer.toString(i);
                    data.add(new Payment(str, df.format(currentAmount), df.format(percentage), df.format(left[i-1])));
                    series2.getData().add(new XYChart.Data(str, (int) currentAmount));
                    all -= currentAmount;
                }
                chart.getData().add(series2);
            } else{
                for (int i = 0; i < months; i++) {
                    if(postponement.isSelected()&&i==Integer.parseInt(startPost.getText())){
                        postSum=sum*Double.parseDouble(postPercentage.getText())/100/12;
                        for(int j = 1;j<=Integer.parseInt(durationMonths.getText());j++){
                            data.add(new Payment(Integer.toString(j),df.format(postSum),df.format(Double.parseDouble(postPercentage.getText())),df.format(0)));
                        }
                    }
                    str = Integer.toString(i + 1);
                    data.add(new Payment(str, df.format(currentAmount), df.format(percentage), df.format(left[i])));
                    series2.getData().add(new XYChart.Data(str, (int) currentAmount));
                    all -= currentAmount;
                }
                chart.getData().add(series2);
            }
        }
    }


    public void getType(ActionEvent event){
        if(annuityButton.isSelected()){
            loanType=annuityButton.getText();
        }
        else if(linearButton.isSelected()){
            loanType=linearButton.getText();
        }
    }

    public void export(ActionEvent event) throws IOException {
        Writer writer = null;
        try {
            File file = new File("Person.csv.");
            writer = new BufferedWriter(new FileWriter(file));
            for (Payment payment : data) {
                String text = payment.getMonthCol()+ ";"+payment.getAmountCol() + ";" + payment.getPercentCol() + ";" + payment.getUnpaidCol() + "\n";
                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            writer.flush();
            writer.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn monthCol = new TableColumn("Month");
        TableColumn amountCol = new TableColumn("Amount");
        TableColumn percentCol = new TableColumn("Percent");
        TableColumn unpaidCol = new TableColumn("Unpaid");

        table.getColumns().addAll(monthCol,amountCol,percentCol,unpaidCol);

        monthCol.setCellValueFactory(new PropertyValueFactory<Payment,Integer>("monthCol"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Payment,Double>("amountCol"));
        percentCol.setCellValueFactory(new PropertyValueFactory<Payment,Double>("percentCol"));
        unpaidCol.setCellValueFactory(new PropertyValueFactory<Payment,Double>("unpaidCol"));

        table.setItems(data);
    }
}
