package com.tecmm.springbootmldynamodb.machineLearning.slr;

import com.tecmm.springbootmldynamodb.machineLearning.dataSets.slr_dataSet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class simpleLinearRegression
{
    // --------- ENTITIES -----------
    ArrayList<BigDecimal> x;
    ArrayList<BigDecimal> y;
    BigDecimal n;

    slr_dataSet mlDataSet = new slr_dataSet();

    BigDecimal n_SLR_BigD = BigDecimal.valueOf(mlDataSet.x.length);


    public String display(){
        ArrayList<BigDecimal> x_SLR_BigD = new ArrayList<>();
        ArrayList<BigDecimal> y_SLR_BigD = new ArrayList<>();

        for (int i = 0; i < mlDataSet.x.length; i++) {
            x_SLR_BigD.add(BigDecimal.valueOf(mlDataSet.x[i]));
            y_SLR_BigD.add(BigDecimal.valueOf(mlDataSet.y[i]));
        }

        this.x = x_SLR_BigD;
        this.y = y_SLR_BigD;
        this.n = n_SLR_BigD;

        return printSLR_Equation();
    }

    private BigDecimal beta1(){
        BigDecimal auxSigmaXY = arraySigma(x).multiply(arraySigma(y));
        BigDecimal auxSigmaXX = arraySigma(x).multiply(arraySigma(x));
        BigDecimal numerator = (n.multiply(arraysMultiplication(x,y))).subtract(auxSigmaXY, MathContext.DECIMAL64);
        BigDecimal denominator = ((n.multiply(arraysMultiplication(x,x))).subtract(auxSigmaXX, MathContext.DECIMAL64));

        return numerator.divide(denominator, MathContext.DECIMAL64);
    }

    private BigDecimal beta0() {
        BigDecimal auxSigmaY = arraySigma(y);
        BigDecimal auxSigmaX = arraySigma(x);

        return auxSigmaY.subtract(beta1().multiply(auxSigmaX)).divide(n, MathContext.DECIMAL64);
    }

    private String printSLR_Equation() {
        return "y = " + beta1() + " x  + " + beta0() + " + epsilon";
    }


    //----- Generic Functions
    private BigDecimal arraysMultiplication(ArrayList<BigDecimal> value1, ArrayList<BigDecimal> value2) {
        ArrayList<BigDecimal> arrayXY = new ArrayList<>();
        for (int i  = 0; i < x.size(); i++) {
            arrayXY.add(value1.get(i).multiply(value2.get(i)));
        }

        BigDecimal result = new BigDecimal(0);
        for (BigDecimal bigDecimal : arrayXY) {
            result = result.add(bigDecimal);
        }

        return result;
    }

    private BigDecimal arraySigma(ArrayList<BigDecimal> list) {
        BigDecimal result = new BigDecimal(0);

        for (BigDecimal bigDecimal : list) {
            result = result.add(bigDecimal);
        }

        return result;
    }

}
