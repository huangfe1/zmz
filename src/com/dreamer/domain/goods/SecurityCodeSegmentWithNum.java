package com.dreamer.domain.goods;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityCodeSegmentWithNum extends SecurityCodeSegment {

	public SecurityCodeSegmentWithNum(Integer codeLength,String prefix,String codeSegment){
		super(codeLength,prefix,codeSegment);
	}

	@Override
	public List<String> generateCodes() {
		// TODO Auto-generated method stub
		HashSet<String> securityCodes=new HashSet<String>();
		securityCodes.add(getCodeSegment());
		Integer start=removePrefixAndToNumber(getCodeSegment());
		for(int index=1;index<getCodeLength();index++){
			securityCodes.add(assembleCode(start+index,false));
		}
		return securityCodes.stream().collect(Collectors.toList());
	}
	
	
}
