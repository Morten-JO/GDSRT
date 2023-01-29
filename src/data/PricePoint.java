package data;

import util.ValueUtil;

public class PricePoint {

	private final static float TRADE_INCREMENT_PERCENTAGER = 0.005f;

	private float minimumPrice = 0f;
	private float maximumPrice = 0f;
	private float medianPrice = 0f;

	public void incrementAll(int quantity, PricePoint point) {
		incrementMaximumPrice(quantity * point.getMaximumPrice());
		incrementMedianPrice(quantity * point.getMedianPrice());
		incrementMinimumPrice(quantity * point.getMinimumPrice());
	}

	public PricePoint(float min, float med, float max) {
		this.minimumPrice = min;
		this.medianPrice = med;
		this.maximumPrice = max;
	}

	/**
	 * Returns yes if another PricePoint at any(Minimum,Median or Maximum) is in any of the points.
	 * @param point
	 * @return
	 */
	public boolean isInBounds(PricePoint point) {
		if(minimumPrice <= point.getMaximumPrice() && point.getMinimumPrice() <= maximumPrice) {
			return true;
		}
		return false;
	}

	public boolean calculatePricesBasedOnValue(float value) {
		//Adjust median
		float multiplier = 20f;
		//Handle outliers
		float diff = Math.abs(value - medianPrice);
		if(diff > medianPrice) {
			multiplier = 100f * (diff / medianPrice);
		}

		float tradeIncrementPercentage = 1f / multiplier;

		if(minimumPrice > value) {
			minimumPrice = value;
		} else if(maximumPrice < value) {
			maximumPrice = value;
		} else {
			float minDiff = (value - minimumPrice) * (tradeIncrementPercentage/2.0f);
			float maxDiff = (maximumPrice - value) * (tradeIncrementPercentage/2.0f);
			minimumPrice += minDiff;
			maximumPrice -= maxDiff;
			if(minimumPrice > medianPrice) {
				minimumPrice = medianPrice;
			}
			if(maximumPrice < medianPrice) {
				maximumPrice = medianPrice;
			}
		}
		if(ValueUtil.isAroundEqual(value, medianPrice, 0.3f)) {
			return true;
		}


		if(medianPrice > value) {
			medianPrice -= Math.max((medianPrice - value) / multiplier, 0.2f);
		} else if(medianPrice < value) {
			medianPrice += Math.max((value - medianPrice) / multiplier, 0.2f);
		}
		return true;
	}

	public float getMinimumPrice() {
		return minimumPrice;
	}
	public void setMinimumPrice(float minimumPrice) {
		this.minimumPrice = minimumPrice;
	}
	public float getMaximumPrice() {
		return maximumPrice;
	}
	public void setMaximumPrice(float maximumPrice) {
		this.maximumPrice = maximumPrice;
	}
	public float getMedianPrice() {
		return medianPrice;
	}
	public void setMedianPrice(float medianPrice) {
		this.medianPrice = medianPrice;
	}
	public void incrementMinimumPrice(float minimumPrice) {
		this.minimumPrice += minimumPrice;
	}
	public void incrementMaximumPrice(float maximumPrice) {
		this.maximumPrice += maximumPrice;
	}
	public void incrementMedianPrice(float medianPrice) {
		this.medianPrice += medianPrice;
	}
}
