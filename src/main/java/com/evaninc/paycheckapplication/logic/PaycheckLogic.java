package com.evaninc.paycheckapplication.logic;

import com.evaninc.paycheckapplication.dataobject.Paycheck;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface PaycheckLogic {

    /* Calculates the paycheck of an hourly based job
     *
     * @param hourlyPay the amount of money being paid per hour
     * @param hours the amount of hours worked in that pay period
     * @returns the paycheck object which contains the net paycheck amount
     */
    Paycheck calculateHourlyPaycheck(BigDecimal hourlyPay, BigDecimal hours, HttpServletRequest request);

    /*
     * Calculate the paycheck of a salary based job
     *
     * @param the amount of money being paid per year
     * @returns the paycheck object which contains the net paycheck amount
     */
    Paycheck calculateSalaryPaycheck(BigDecimal salaryPay, HttpServletRequest request);
}

