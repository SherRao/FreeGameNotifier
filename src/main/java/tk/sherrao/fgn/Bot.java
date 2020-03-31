package tk.sherrao.fgn;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

public class Bot extends AnnotatedEventManager {

	private JDA jda;
	private ScheduledExecutorService exec;
	
	private Console console;
	private RedditListener reddit;
	
	private Bot() {
		try {
			jda = JDABuilder.createDefault( Configuration.get("TOKEN") )
					.setStatus(OnlineStatus.DO_NOT_DISTURB)
					.setEventManager(this)
					.setActivity(Activity.watching("Free Games!"))
					.build();
			
			jda.awaitReady();

			exec = Executors.newScheduledThreadPool(3);
			reddit = new RedditListener(this);
			
			exec.scheduleWithFixedDelay(reddit, 2, 10, TimeUnit.SECONDS);
			
		} catch (LoginException e) {
			System.err.println("Could not successfully login to the Discord servers!");
			e.printStackTrace();

		} catch (InterruptedException e) {
			System.err.println("Error while trying to await a connection to be established!");			
			e.printStackTrace();

		}
		
		Runtime.getRuntime().addShutdownHook( new Thread(() -> { shutdown(); }) );
		
	}
	
	public void shutdown() {
		jda.shutdownNow();
		
	}
	
	public JDA jda() {
		return jda;
		
	}
	
	public ScheduledExecutorService executor() {
		return exec;
		
	}
	
	public static void main(String... vargs) {
		new Bot();
		
	}
	
}