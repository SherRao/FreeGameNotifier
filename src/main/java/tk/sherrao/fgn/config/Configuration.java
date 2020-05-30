package tk.sherrao.fgn.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Nonnull;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

import com.fasterxml.jackson.core.JsonParseException;

import io.github.cdimascio.dotenv.Dotenv;
import tk.sherrao.fgn.Bot;
import tk.sherrao.fgn.BotComponent;

public class Configuration extends BotComponent {
	
	private Dotenv dotenv;
	private File jsonFile;
	private JSONObject jsonInput;
	
	public Configuration(@Nonnull Bot bot) 
			throws JsonParseException, IOException {
		super(bot);
		
		this.dotenv = Dotenv.load();
		this.jsonFile = new File("data.json");
		if(!jsonFile.exists()) {
			logger.info("Created 'data.json' for first time use");
			jsonFile.createNewFile();
			new PrintWriter(jsonFile)
				.append("{}")
				.close();
			
		}
		
		this.jsonInput = new JSONObject( new JSONTokener(new FileReader(jsonFile)) );
		
	}
	
	public void shutdown() 
			throws JSONException, IOException {
		jsonFile.delete();
		jsonFile.createNewFile();
		jsonInput.write( new FileWriter(jsonFile) );
		
	}
	
	public String getProperty(@Nonnull String key) {
		return dotenv.get(key);
		
	}
	
	public int getInt(@Nonnull String key) {
		return jsonInput.getInt(key);
		
	}
	
	public double getFloat(@Nonnull String key) {
		return jsonInput.getFloat(key);
		
	}
	
	public boolean getBoolean(@Nonnull String key) {
		return jsonInput.getBoolean(key);
			
	}
	
	public String getData(@Nonnull String key) {
		return jsonInput.getString(key);
		
	}
	
	public Object setData(@Nonnull String key, Object value) {
		jsonInput.append(key, value);
		return jsonInput.get(key);
		
	}
	
}