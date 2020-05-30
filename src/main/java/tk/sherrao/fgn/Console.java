package tk.sherrao.fgn;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Console extends BotComponent implements Runnable {
	
	private Scanner scanner;
	private Map<String, Runnable> commands;
	private boolean running;
	
	public Console(Bot bot) {
		super(bot);
		
		this.scanner = new Scanner(System.in);
		this.commands = new HashMap<>();
		this.running = true;
	
	}

	public void addCommands() {
		commands.put("quit", () -> {
			
		} );
		
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
