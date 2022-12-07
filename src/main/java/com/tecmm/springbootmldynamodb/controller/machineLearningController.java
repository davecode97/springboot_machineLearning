package com.tecmm.springbootmldynamodb.controller;

import com.tecmm.springbootmldynamodb.machineLearning.mlr.Vectorized;
import com.tecmm.springbootmldynamodb.machineLearning.mlr.cramer.Cramer;
import com.tecmm.springbootmldynamodb.machineLearning.slr.simpleLinearRegression;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class machineLearningController {
    @GetMapping("/")
    String getSlr (Model model){
        simpleLinearRegression  linearRegression = new simpleLinearRegression();
        String aValue = linearRegression.display();

        Cramer multiLinearRegressionCramer = new Cramer();
        String bValue = multiLinearRegressionCramer.display();

        Vectorized multiLinearRegressionVectorized = new Vectorized();
        String cValue = multiLinearRegressionVectorized.display();

        model.addAttribute("slr", aValue);
        model.addAttribute("cramer", bValue);
        model.addAttribute("vectorized", cValue);

        return "index";
    }

}
