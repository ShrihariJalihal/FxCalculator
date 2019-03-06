package com.shj.calculators.fxcalculator.main;

import java.util.Scanner;

import com.shj.calculators.fxcalculator.api.FxCalculator;
import com.shj.calculators.fxcalculator.api.impl.FxCalculatorImpl;
import com.shj.calculators.fxcalculator.common.CurrencyUtil;
import com.shj.calculators.fxcalculator.common.FxCurrency;

public class FxCalculatorCommandLine {
	FxCalculator calculator = new FxCalculatorImpl();

	public static void main(String[] args) {

		FxCalculatorCommandLine calculatorCommandLine = new FxCalculatorCommandLine();
		if (args == null || args.length <= 0) {
			System.out.println("Please provide inut. <ccy1> <amount1> in <ccy2>");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			args = new String[4];
			args[0] = scanner.next();
			args[1] = scanner.next();
			args[2] = scanner.next();
			args[3] = scanner.next();
		}
		
		if (args == null || args.length != 4) {
			System.out.println("Invalid Input");
		}
		
		System.out.println(calculatorCommandLine.formattedForexValue(FxCurrency.valueOf(args[0].trim().toUpperCase()), Double.valueOf(args[1]), FxCurrency.valueOf(args[3].trim().toUpperCase())));
	}

	private String formattedForexValue(FxCurrency fromCurrency, Double amountToBeConverted, FxCurrency toCurrency) {
		StringBuilder messageToDisplayUser = new StringBuilder();
		try {
			Double convertedAmount = calculator.calculateForexValue(fromCurrency, amountToBeConverted, toCurrency);
			if (convertedAmount != null && convertedAmount > 0) {
				messageToDisplayUser
						.append(fromCurrency.toString())
						.append(" ")
						.append(CurrencyUtil.formatCurrency(amountToBeConverted, fromCurrency.getDecimalPlace()))
						.append(" = ")
						.append(toCurrency.toString()).append(" ")
						.append(CurrencyUtil.formatCurrency(convertedAmount, toCurrency.getDecimalPlace()));
			}

		} catch (Exception sd) {
			messageToDisplayUser = new StringBuilder();
			messageToDisplayUser.append("Unable to find rate for ").append(fromCurrency.toString()).append("/")
					.append(toCurrency.toString());
		}
		return messageToDisplayUser.toString();
	}

}
