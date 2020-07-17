package com.evaninc.dataobject;

import java.math.BigDecimal;

public class Paycheck {
    private final BigDecimal
            hourlyRate,
            hours,
            yearlySalary,
            grossPaycheckAmount,
            netPaycheckAmount,
            netTaxAmount;

    public Paycheck(BigDecimal hourlyRate,
                    BigDecimal hours,
                    BigDecimal yearlySalary,
                    BigDecimal grossPaycheckAmount,
                    BigDecimal netPaycheckAmount,
                    BigDecimal netTaxAmount)
    {
        this.hourlyRate = hourlyRate;
        this.hours = hours;
        this.yearlySalary = yearlySalary;
        this.grossPaycheckAmount = grossPaycheckAmount;
        this.netPaycheckAmount = netPaycheckAmount;
        this.netTaxAmount = netTaxAmount;
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
}
