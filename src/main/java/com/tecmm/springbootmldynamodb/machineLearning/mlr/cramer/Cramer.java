package com.tecmm.springbootmldynamodb.machineLearning.mlr.cramer;

import com.tecmm.springbootmldynamodb.machineLearning.dataSets.mlrCramer_dataSet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public class Cramer
{
    mlrCramer_dataSet mlrDataSet = new mlrCramer_dataSet();
    double sum = 0;

    double sigmaX0I           = sigma(mlrDataSet .x0);                                    // 1. SIGMA X0, i
    double sigmaX1I           = sigma(mlrDataSet .x1);                                    // 2. SIGMA X1, i
    double sigmaX2I           = sigma(mlrDataSet .x2);                                    // 3. SIGMA X2, i
    double sigmaYI             = sigma(mlrDataSet .y);                                      // 4. SIGMA Y, i
    double sigmaX1SquareI = sigmaSquare(mlrDataSet .x1);                         // 5. SIGMA X1^2, i
    double sigmaX2SquareI = sigmaSquare(mlrDataSet .x2);                         // 6. SIGMA X2^2, i
    double sigmaX1iYI        = sigma(mlrDataSet .x1, mlrDataSet .y);             // 7. SIGMA X1,i y
    double sigmaX2iYI        = sigma(mlrDataSet .x2, mlrDataSet .y);             // 8. SIGMA X2,i y
    double sigmaX1iX2I      = sigma(mlrDataSet .x1, mlrDataSet .x2);           // 9. SIGMA X1,i X2, i

    double[][] pivotMatrix = pivotMatrix();

    BigDecimal DS = new BigDecimal(0);
    BigDecimal D0 = new BigDecimal(0);
    BigDecimal D1 = new BigDecimal(0);
    BigDecimal D2 = new BigDecimal(0);


    public String display()
    {
        DS = determinant(Objects.requireNonNull(setCramerModel("SYSTEM")));
        D0 = determinant(Objects.requireNonNull(setCramerModel("BETA0")));
        D1 = determinant(Objects.requireNonNull(setCramerModel("BETA1")));
        D2 = determinant(Objects.requireNonNull(setCramerModel("BETA2")));

        return "y = "
                + D0.divide(DS, MathContext.DECIMAL64) + "  +  "
                + D1.divide(DS, MathContext.DECIMAL64) + " x1   +   "
                + D2.divide(DS, MathContext.DECIMAL64) + " x2"
                + "    + epsilon";

    }

    private cramerModel setCramerModel(String typo) {
        cramerModel cramerModel = new cramerModel();

        if (typo.matches("SYSTEM"))
        {
            cramerModel.set_pos00(sigmaX0I);
            cramerModel.set_pos01(sigmaX1I);
            cramerModel.set_pos02(sigmaX2I);
            cramerModel.set_pos03(sigmaX0I);
            cramerModel.set_pos04(sigmaX1I);

            cramerModel.set_pos10(sigmaX1I);
            cramerModel.set_pos11(sigmaX1SquareI);
            cramerModel.set_pos12(sigmaX1iX2I);
            cramerModel.set_pos13(sigmaX1I);
            cramerModel.set_pos14(sigmaX1SquareI);

            cramerModel.set_pos20(sigmaX2I);
            cramerModel.set_pos21(sigmaX1iX2I);
            cramerModel.set_pos22(sigmaX2SquareI);
            cramerModel.set_pos23(sigmaX2I);
            cramerModel.set_pos24(sigmaX1iX2I);

            return cramerModel;
        }
        if (typo.matches("BETA0"))
        {
            cramerModel.set_pos00(pivotMatrix[0][0]);
            cramerModel.set_pos01(sigmaX1I);
            cramerModel.set_pos02(sigmaX2I);
            cramerModel.set_pos03(sigmaX0I);
            cramerModel.set_pos04(sigmaX1I);

            cramerModel.set_pos10(pivotMatrix[0][1]);
            cramerModel.set_pos11(sigmaX1SquareI);
            cramerModel.set_pos12(sigmaX1iX2I);
            cramerModel.set_pos13(sigmaX1I);
            cramerModel.set_pos14(sigmaX1SquareI);

            cramerModel.set_pos20(pivotMatrix[0][2]);
            cramerModel.set_pos21(sigmaX1iX2I);
            cramerModel.set_pos22(sigmaX2SquareI);
            cramerModel.set_pos23(sigmaX2I);
            cramerModel.set_pos24(sigmaX1iX2I);

            return cramerModel;
        }
        if (typo.matches("BETA1"))
        {
            cramerModel.set_pos00(sigmaX0I);
            cramerModel.set_pos01(pivotMatrix[0][0]);
            cramerModel.set_pos02(sigmaX2I);
            cramerModel.set_pos03(sigmaX0I);
            cramerModel.set_pos04(sigmaX1I);

            cramerModel.set_pos10(sigmaX1I);
            cramerModel.set_pos11(pivotMatrix[0][1]);
            cramerModel.set_pos12(sigmaX1iX2I);
            cramerModel.set_pos13(sigmaX1I);
            cramerModel.set_pos14(sigmaX1SquareI);

            cramerModel.set_pos20(sigmaX2I);
            cramerModel.set_pos21(pivotMatrix[0][2]);
            cramerModel.set_pos22(sigmaX2SquareI);
            cramerModel.set_pos23(sigmaX2I);
            cramerModel.set_pos24(sigmaX1iX2I);

            return cramerModel;
        }
        if (typo.matches("BETA2"))
        {
            cramerModel.set_pos00(sigmaX0I);
            cramerModel.set_pos01(sigmaX1I);
            cramerModel.set_pos02(pivotMatrix[0][0]);
            cramerModel.set_pos03(sigmaX0I);
            cramerModel.set_pos04(sigmaX1I);

            cramerModel.set_pos10(sigmaX1I);
            cramerModel.set_pos11(sigmaX1SquareI);
            cramerModel.set_pos12(pivotMatrix[0][1]);
            cramerModel.set_pos13(sigmaX1I);
            cramerModel.set_pos14(sigmaX1SquareI);

            cramerModel.set_pos20(sigmaX2I);
            cramerModel.set_pos21(sigmaX1iX2I);
            cramerModel.set_pos22(pivotMatrix[0][2]);
            cramerModel.set_pos23(sigmaX2I);
            cramerModel.set_pos24(sigmaX1iX2I);

            return cramerModel;
        }

        return null;
    }

    private BigDecimal determinant(cramerModel cramerModel)
    {
        double[][] determinantSystem = new double[3][5];
        determinantSystem[0][0] = cramerModel.get_pos00();
        determinantSystem[0][1] = cramerModel.get_pos01();
        determinantSystem[0][2] = cramerModel.get_pos02();
        determinantSystem[0][3] = cramerModel.get_pos03();
        determinantSystem[0][4] = cramerModel.get_pos04();

        determinantSystem[1][0] = cramerModel.get_pos10();
        determinantSystem[1][1] = cramerModel.get_pos11();
        determinantSystem[1][2] = cramerModel.get_pos12();
        determinantSystem[1][3] = cramerModel.get_pos13();
        determinantSystem[1][4] = cramerModel.get_pos14();

        determinantSystem[2][0] = cramerModel.get_pos20();
        determinantSystem[2][1] = cramerModel.get_pos21();
        determinantSystem[2][2] = cramerModel.get_pos22();
        determinantSystem[2][3] = cramerModel.get_pos23();
        determinantSystem[2][4] = cramerModel.get_pos24();

        BigDecimal aux1BD = new BigDecimal(0);
        BigDecimal aux2BD = new BigDecimal(0);

        for (int i = 0; i <determinantSystem.length-2; i++)
            for (int j = 0; j < determinantSystem[0].length-2; j++)
                aux1BD = aux1BD.add(BigDecimal.valueOf((determinantSystem[i][j] * determinantSystem[i + 1][j + 1] * determinantSystem[i + 2][j + 2])));


        for (int k = 0; k < determinantSystem.length-1; k++)
        {
            for (int m = determinantSystem[0].length-1; m > 0; m--)
            {
                if (m != 1 && k  != 1) {
                    aux2BD = aux2BD.add(BigDecimal.valueOf((determinantSystem[k][m] * determinantSystem[k+1][m-1] * determinantSystem[k+2][m-2])));
                } else {
                    break;
                }
            }
            if (k == 1)
                break;

        }

        return aux1BD.subtract(aux2BD, MathContext.DECIMAL64);
    }

    private double[][] pivotMatrix()
    {
        double beta1 = sigmaYI;
        double beta2 = sigmaX1iYI;
        double beta3 = sigmaX2iYI;

        pivotMatrix = new double [1][3];
        pivotMatrix[0][0] = beta1;
        pivotMatrix[0][1] = beta2;
        pivotMatrix[0][2] = beta3;

        return pivotMatrix;
    }

    private  double sigma(double[][] array)
    {
        // sum
        sum = 0.0;
        for (double[] i : array)
            for (double num : i)
                sum+=num;

        return sum;
    }

    private double sigma(double[][] array1, double[][] array2)
    {
        sum = 0.0;
        double[][] auxArray= new double[array1.length][array1[0].length];

        for (int row = 0; row < array1.length; row++)
        {
            for (int col = 0; col < array1[row].length; col++)
            {
                auxArray[row][col] = array1[row][col] * array2[row][col];
                sum  += auxArray[row][col];
            }
        }

        return sum;
    }

    private  double sigmaSquare(double[][] array)
    {
        sum = 0.0;
        for (double[] i : array)
            for (double num : i)
                sum+=Math.pow(num, 2);
        return sum;
    }
}
