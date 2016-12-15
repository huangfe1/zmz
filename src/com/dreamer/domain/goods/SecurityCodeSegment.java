package com.dreamer.domain.goods;

import ps.mx.otter.exception.ApplicationException;

import java.util.ArrayList;
import java.util.List;

public class SecurityCodeSegment {
	
	private String prefix;
	
	private String codeSegment;
	
	private int codeLength;
	
	public SecurityCodeSegment(Integer codeLength,String prefix,String codeSegment){
		this.prefix=prefix;
		this.codeSegment=codeSegment;
		this.codeLength=codeLength;
	}
	
	public List<String> generateCodes(){
		List<String> securityCodes=new ArrayList<String>();
		String[] segment=codeSegment.split("-");
		if(segment.length>2){
			throw new ApplicationException("输入格式不合法 ");
		}
		if(segment.length==1){
			securityCodes.add(segment[0]);
		}
		if(segment.length==2){
			boolean startsWithZero=segment[0].startsWith("0");
			Integer start=removePrefixAndToNumber(segment[0]);
			Integer end=removePrefixAndToNumber(segment[1]);
			for (int index=start;index<=end;index++){
				securityCodes.add(assembleCode(index, startsWithZero));
			}
		}
		return securityCodes;
	}
	
	
	protected Integer removePrefixAndToNumber(String code){
		return Integer.parseInt(code.toLowerCase().replaceAll(prefix.toLowerCase(), "").replaceFirst("^0*", ""));
	}
	
	protected String assembleCode(Integer code, boolean startsWithZero){
		StringBuilder sbd=new StringBuilder(prefix);
		/*int codeLen=String.valueOf(code).length();
		int diff=codeLength-prefix.length()-codeLen;
		if(diff>0){
			for(int index=0;index<diff;index++){
				sbd.append("0");
			}
			sbd.append(code);
		}else{
			sbd.append(code);
		}*/
		if(startsWithZero){
			sbd.append("0");
		}
		sbd.append(code);
		return sbd.toString();
	}
	
	
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getCodeSegment() {
		return codeSegment;
	}

	public void setCodeSegment(String codeSegment) {
		this.codeSegment = codeSegment;
	}

	public int getCodeLength() {
		return codeLength;
	}

	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}

	public static void main(String [] args){
		SecurityCodeSegment app=new SecurityCodeSegment(0,"A","01020-01028");
		List<String> codes=app.generateCodes();
		for(String code:codes){
		}
		
	}

}
