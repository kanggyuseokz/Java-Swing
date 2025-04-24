package coinmockproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coin {
	private String name;
	private String symbol;
	private double priceUsd;
	
}
