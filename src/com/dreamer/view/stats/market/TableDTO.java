package com.dreamer.view.stats.market;

import java.util.ArrayList;
import java.util.List;

public class TableDTO {
	
	private List<ColumnDTO> columns=new ArrayList<ColumnDTO>();
	
	private Integer maxRow;
	
	private String owner;

	public List<ColumnDTO> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnDTO> columns) {
		this.columns = columns;
	}

	public Integer getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(Integer maxRow) {
		this.maxRow = maxRow;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
