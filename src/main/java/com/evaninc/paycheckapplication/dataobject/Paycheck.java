package com.evaninc.paycheckapplication.dataobject;

import java.math.BigDecimal;

public class Paycheck {
    private final BigDecimal
            hourlyRate,
            hours,
            yearlySalary,
            grossPaycheckAmount,
            netPaycheckAmount,
            netTaxAmount;
    private final String state;

    public Paycheck(BigDecimal hourlyRate,
                    BigDecimal hours,
                    BigDecimal yearlySalary,
                    BigDecimal grossPaycheckAmount,
                    BigDecimal netPaycheckAmount,
                    BigDecimal netTaxAmount,
                    String state)
    {
        this.hourlyRate = hourlyRate;
        this.hours = hours;
        this.yearlySalary = yearlySalary;
        this.grossPaycheckAmount = grossPaycheckAmount;
        this.netPaycheckAmount = netPaycheckAmount;
        this.netTaxAmount = netTaxAmount;
        this.state = state;
    }

    public BigDecimal getGrossPaycheckAmount() {
        return grossPaycheckAmount;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public BigDecimal getNetPaycheckAmount() {
        return netPaycheckAmount;
    }

    public BigDecimal getNetTaxAmount() {
        return netTaxAmount;
    }

    public BigDecimal getYearlySalary() {
        return yearlySalary;
    }

    public String getState() {
        return state;
    }
}
