package com.app.dto.figure;

import java.util.List;

public class FigurePageResponse {

	private List<FigureResponse> figures;
	private long total;
	private int page;
	private int limit;
	private int totalPages;

	public FigurePageResponse(List<FigureResponse> figures, long total, int page, int limit) {
		this.figures = figures;
		this.total = total;
		this.page = page;
		this.limit = limit;
		this.totalPages = (int) Math.ceil((double) total / limit);
	}

	public List<FigureResponse> getFigures() {
		return figures;
	}

	public long getTotal() {
		return total;
	}

	public int getPage() {
		return page;
	}

	public int getLimit() {
		return limit;
	}

	public int getTotalPages() {
		return totalPages;
	}
}
