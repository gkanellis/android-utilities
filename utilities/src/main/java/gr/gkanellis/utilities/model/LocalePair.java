package gr.gkanellis.utilities.model;

import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class LocalePair {

	private String id;
	private Locale locale;

}
