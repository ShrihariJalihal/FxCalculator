package com.shj.calculators.fxcalculator.api.impl;

import static com.shj.calculators.fxcalculator.common.FxConstants.CONVERSION_VALUE_NOT_POPULATED;
import static com.shj.calculators.fxcalculator.common.FxConstants.EXCHANGE_RATE_NOT_PROVIDED;
import static com.shj.calculators.fxcalculator.common.FxConstants.INVALID_CURRENCY_DETECTED;
import static com.shj.calculators.fxcalculator.common.FxConstants.NULL_CURRENCY_SUPPLIED;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shj.calculators.fxcalculator.api.FxCalculator;
import com.shj.calculators.fxcalculator.common.FxCurrency;
import com.shj.calculators.fxcalculator.common.FxCurrencyPathFinder;
import com.shj.calculators.fxcalculator.data.FxConversionRates;
import com.shj.calculators.fxcalculator.exception.FxCurrencyConversionException;

public class FxCalculatorImpl implements FxCalculator {
	private final Logger LOG = LogManager.getLogger(FxCalculatorImpl.class.getName());
	private Map<String, Double> conversionMap;

	@Override
	public Double calculateForexValue(FxCurrency fromCurrency, Double amountToConvert, FxCurrency toCurrency)
			throws FxCurrencyConversionException 
	{
		LOG.info("calculateForexValue called");
		// check if from and to currency is valid or not
		// check for same currency format
		if (fromCurrency == toCurrency) {
			return amountToConvert;
		} else {
			// if direct conversion is available
			Double conversionVal = obtainExchangeRateForKey(fromCurrency, toCurrency);

			if (conversionVal != null && conversionVal.doubleValue() > 0.0d) {
				return (conversionVal * amountToConvert);
			} else {
				// instantiate creation of conversion matrix based on input value
				// we need to consider how the conversion inputs are provided and how do we take
				// the nearest conversion value
				return calculateMatrixValue(fromCurrency, amountToConvert, toCurrency);
			}
		}
	}

	private List<FxCurrency> populateConversionMatrixAndReturnPath(FxCurrency fromCurrency, FxCurrency toCurrency)
			throws FxCurrencyConversionException 
	{
		LOG.info("populateConversionMatrixAndReturnPath called");
		List<FxCurrency> pathToCalculateExchangeValue = null;
		FxCurrencyPathFinder pathFinder = new FxCurrencyPathFinder();
		for (String key : getConversionMap().keySet()) {
			if (key.length() == 6) {
				String firstCurr = key.substring(0, 3);
				String secCurr = key.substring(3, 6);

				pathFinder.addEdge(FxCurrency.valueOf(firstCurr), FxCurrency.valueOf(secCurr));
				pathFinder.addEdge(FxCurrency.valueOf(secCurr), FxCurrency.valueOf(firstCurr));
			} else {
				LOG.error(INVALID_CURRENCY_DETECTED + key);
				throw new FxCurrencyConversionException(INVALID_CURRENCY_DETECTED + key);
			}
		}

		pathToCalculateExchangeValue = pathFinder.findPath(fromCurrency, toCurrency);
		LOG.error("populateConversionMatrixAndReturnPath returned: "+pathToCalculateExchangeValue);
		return pathToCalculateExchangeValue;
	}

	private Double calculateMatrixValue(FxCurrency fromCurrency, Double amountToConvert, FxCurrency toCurrency)
			throws FxCurrencyConversionException 
	{
		LOG.info("calculateMatrixValue called");
		List<FxCurrency> pathToCalculateExchangeValue = populateConversionMatrixAndReturnPath(fromCurrency, toCurrency);
		if (pathToCalculateExchangeValue == null || pathToCalculateExchangeValue.isEmpty()) {
			LOG.error(CONVERSION_VALUE_NOT_POPULATED);
			throw new FxCurrencyConversionException(CONVERSION_VALUE_NOT_POPULATED);
		}

		Double conversionValue = Double.valueOf(1.00000d);

		for (int index = 0; index < pathToCalculateExchangeValue.size() - 1; index++) {
			conversionValue = conversionValue * (obtainExchangeRateForKey(pathToCalculateExchangeValue.get(index),
					pathToCalculateExchangeValue.get(index + 1)));

		}

		return amountToConvert * conversionValue;
	}

	private Double obtainExchangeRateForKey(FxCurrency fromCurrency, FxCurrency toCurrency)
			throws FxCurrencyConversionException 
	{
		LOG.info("obtainExchangeRateForKey called");
		Double conversionVal;
		if (fromCurrency != null && toCurrency != null) {

			conversionVal = getConversionMap().get(fromCurrency.toString() + toCurrency.toString());
			if (conversionVal != null && conversionVal.doubleValue() > 0.0d) {
				return conversionVal;
			} else {
				conversionVal = getConversionMap().get(toCurrency.toString() + fromCurrency.toString());
				if (conversionVal != null && conversionVal.doubleValue() > 0.0d) {
					return 1 / conversionVal;
				}
			}
		} else {
			LOG.error(NULL_CURRENCY_SUPPLIED);
			throw new FxCurrencyConversionException(NULL_CURRENCY_SUPPLIED);
		}

		return conversionVal;
	}

	public Map<String, Double> getConversionMap() throws FxCurrencyConversionException {
		if (conversionMap == null || conversionMap.isEmpty()) {
			LOG.info("Populating ConversionMap");
			this.conversionMap = FxConversionRates.inputValuesOfCurrencyConversion();
			if (this.conversionMap == null && this.conversionMap.isEmpty()) {
				LOG.error(EXCHANGE_RATE_NOT_PROVIDED);
				throw new FxCurrencyConversionException(EXCHANGE_RATE_NOT_PROVIDED);
			}
		}
		return this.conversionMap;
	}

}
