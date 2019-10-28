package de.as.tasmota.rule.helper.logic.formatter;

import java.util.ArrayList;
import java.util.List;

public class RulePacker {

    public List<String> pack(final String rule) {
	List<String> result = new ArrayList<>();

	String packed = rule;
	// remove comments ((?m) means 'use multiline')
	packed = packed.replaceAll("(?m)//.*$", "");

	// TODO: Trennen in 3 Teile (--)

	// remove double spaces
	packed = packed.replaceAll("\\s+", " ");

	packed = packed.trim();

	System.out.println(packed);

	result.add(packed);
	return result;
    }
}
