package com.pepkor.console.page;

public interface iConsolePage {
	public String getText();
	public iConsolePage processInput(String input);
	public void refresh() throws Exception;
}
