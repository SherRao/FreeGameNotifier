package tech.sherrao.fgn;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.security.auth.login.LoginException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.lukaspradel.steamapi.data.json.appnews.GetNewsForApp;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient.SteamWebApiClientBuilder;
import com.lukaspradel.steamapi.webapi.request.GetNewsForAppRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import tech.sherrao.fgn.config.Configuration;
import tech.sherrao.fgn.config.ServerSettings;

public class Bot extends AnnotatedEventManager {

	private final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
	
	private JDA jda;
	private Configuration config;
	private ScheduledExecutorService exec;
	
	private Console console;
	private RedditListener reddit;
	private Map<String, ServerSettings> serverSettings;
	
	private Bot() {
		try {
			config = new Configuration(this);
			
			jda = JDABuilder.createDefault( config.getProperty("TOKEN") )
					.setStatus(OnlineStatus.DO_NOT_DISTURB)
					.setEventManager(this)
					.setActivity(Activity.watching("Free Games!"))
					.build();
			jda.awaitReady();
			jda.addEventListener(this);
			
			exec = Executors.newScheduledThreadPool(3);
			//reddit = new RedditListener(this);
			//exec.scheduleWithFixedDelay(reddit, 2, 10, TimeUnit.SECONDS);
			
			
		} catch (LoginException e) {
			LOGGER.error("Could not successfully login to the Discord servers!", e);

		} catch (InterruptedException e) {
			LOGGER.error("Error while trying to await a connection to be established!", e);			

		} catch (JsonParseException e) {
			LOGGER.error("Error while trying to parse JSON!", e);
			
		} catch (IOException e) {
			LOGGER.error("Error while trying to write to the disk!", e);
			
		} 
		
		Runtime.getRuntime().addShutdownHook( new Thread(() -> { shutdown(); } ) );
	}
	
	@SubscribeEvent
	public void onGuildMessageReceivedEvent(GuildMessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		if( message.startsWith(config.getProperty("PREFIX")) ) {
			String[] tokens = message.toLowerCase().split(" ");
			tokens[0] = tokens[0].replace(config.getProperty("PREFIX"), "");
			switch(tokens[0]) {
				case "github":
				case "git":
					event.getChannel().sendMessage("https://github.com/sherrao").queue();
					break;
					
			}
			
		} else 
			return;
		
	}
	
	public void shutdown() {
		try {
			jda.shutdownNow();
			config.shutdown();
	
		} catch (JSONException e) {
			LOGGER.error("Error while parsing JSON data while shutting down!", e);

		} catch(IOException e) {
			LOGGER.error("Error while trying to I/O to/from the disk while shutting down!", e);
			
		}
	}
	
	public JDA jda() {
		return jda;
		
	}
	
	public Configuration getConfig() {
		return config;
		
	}
	
	public ScheduledExecutorService executor() {
		return exec;
		
	}
	
	public static void main(String... vargs) {
		new Bot();
		
	}
	
	public void steam() {
		SteamWebApiClient api = new SteamWebApiClientBuilder("").build();
		
		
	}
	
}