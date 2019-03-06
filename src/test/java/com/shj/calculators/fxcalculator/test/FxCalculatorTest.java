package com.shj.calculators.fxcalculator.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.shj.calculators.fxcalculator.api.FxCalculator;
import com.shj.calculators.fxcalculator.api.impl.FxCalculatorImpl;
import com.shj.calculators.fxcalculator.common.CurrencyUtil;
import com.shj.calculators.fxcalculator.common.FxCurrency;
import com.shj.calculators.fxcalculator.exception.FxCurrencyConversionException;

public class FxCalculatorTest {

	private FxCalculator fxCalculator = new FxCalculatorImpl();;

	@Test(expected = FxCurrencyConversionException.class)
	public void oneOfTheCurrencyIsNullTest() {
		assertThat(CurrencyUtil.formatCurrency(fxCalculator.calculateForexValue(null, 100.00d, FxCurrency.DKK),
				FxCurrency.DKK.getDecimalPlace()), is(equalTo("505.76")));
	}

	@Test
	public void directConversionTest() {
		assertThat(
				CurrencyUtil.formatCurrency(fxCalculator.calculateForexValue(FxCurrency.AUD, 100.00d, FxCurrency.USD),
						FxCurrency.USD.getDecimalPlace()),
				is(equalTo("83.71")));
	}

	@Test
	public void sameCurrenciesTest() {
		assertThat(
				CurrencyUtil.formatCurrency(fxCalculator.calculateForexValue(FxCurrency.AUD, 100.00d, FxCurrency.AUD),
						FxCurrency.AUD.getDecimalPlace()),
				is(equalTo("100.00")));
	}

	@Test
	public void invertedConversionTest() {
		assertThat(
				CurrencyUtil.formatCurrency(fxCalculator.calculateForexValue(FxCurrency.USD, 100.00d, FxCurrency.JPY),
						FxCurrency.JPY.getDecimalPlace()),
				is(equalTo("11995")));
	}

	@Test
	public void indirectConversionOneLevelTest() {
		assertThat(
				CurrencyUtil.formatCurrency(fxCalculator.calculateForexValue(FxCurrency.AUD, 100.00d, FxCurrency.JPY),
						FxCurrency.JPY.getDecimalPlace()),
				is(equalTo("10041")));
	}

	@Test
	public void indirectConversionTwoLevelTest() {
		assertThat(
				CurrencyUtil.formatCurrency(fxCalculator.calculateForexValue(FxCurrency.AUD, 100.00d, FxCurrency.DKK),
						FxCurrency.DKK.getDecimalPlace()),
				is(equalTo("505.76")));
	}

	@Test(expected = FxCurrencyConversionException.class)
	public void validCurreciesButDoesntHaveExchangeRateSpecifedDirectlyOrIndirectlyTest() {
		assertThat(
				CurrencyUtil.formatCurrency(fxCalculator.calculateForexValue(FxCurrency.KRW, 100.00d, FxCurrency.FJD),
						FxCurrency.FJD.getDecimalPlace()),
				is(equalTo("505.76")));
	}
}
