package com.evaninc.paycheckapplication.server;

import com.evaninc.paycheckapplication.dataobject.Paycheck;
import com.evaninc.paycheckapplication.logic.PaycheckLogic;
import com.evaninc.paycheckapplication.logic.impl.PaycheckLogicImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
public class PaycheckServer {

    private final PaycheckLogic paycheckLogic = new PaycheckLogicImpl();

    final String baseCalculateMapping = "/calculate";

    @GetMapping(baseCalculateMapping + "/hourly")
    public Paycheck calculateHourly(@RequestParam BigDecimal hourlyPay,
                                    @RequestParam BigDecimal hours,
                                    HttpServletRequest request)
    {
        return paycheckLogic.calculateHourlyPaycheck(hourlyPay, hours, request);
    }

    @GetMapping(baseCalculateMapping + "/salary")
    public Paycheck calculateSalary(@RequestParam BigDecimal salaryPay,
                                    HttpServletRequest request)
    {
        return paycheckLogic.calculateSalaryPaycheck(salaryPay, request);
    }

}