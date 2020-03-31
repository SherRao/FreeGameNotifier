package tk.sherrao.fgn;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import net.dv8tion.jda.api.JDA;

public class Console implements Runnable {

	private final Bot bot;
	
	private Scanner scanner;
	private Map<String, Runnable> commands;
	private boolean running;
	
	public Console(Bot bot) {
		this.bot = bot;
		
		this.scanner = new Scanner(System.in);
		this.commands = new HashMap<>();
		this.running = true;
	
	}

	public void addCommands() {
		commands.put("quit", () -> {bot.shutdown();});
		
	}
	
	public void run() {
		while(running) {
			System.out.print("> ");
			String input = scanner.nextLine();
			Runnable func = commands.get(input.toLowerCase());
			if(func != null)
				func.run();
				
			else 
				System.out.println("That is not a valid command!");
				
		}
	}

}
