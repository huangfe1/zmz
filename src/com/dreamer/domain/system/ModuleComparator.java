package com.dreamer.domain.system;

import java.util.Comparator;

import org.springframework.stereotype.Component;
@Component
public class ModuleComparator implements Comparator<Module> {
	@Override
	public int compare(Module o1, Module o2) {
		// TODO Auto-generated method stub
		return o1.getSequence().compareTo(o2.getSequence());
	}

}
