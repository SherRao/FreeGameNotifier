package tk.sherrao.fgn;

import javax.annotation.Nonnull;

public enum PlatformData {

	STEAM("Steam", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Steam_icon_logo.svg/800px-Steam_icon_logo.svg.png"), 
	GOG("GOG", null), 
	EPIC("Epic Games", null), 
	ITCH("Itch.io", null), 
	HUMBLE("Humble Bundle", null);
	
	private String name;
	private String iconUrl;
	
	private PlatformData(@Nonnull String name, String iconUrl ) {
		this.name = name;
		this.iconUrl = iconUrl;
		
	}
	
	@Override
	public String toString() {
		return name;
		
	}
	
	public String icon() {
		return iconUrl;
		
	}
	
}
