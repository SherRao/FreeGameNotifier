package tk.sherrao.fgn;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.sherrao.fgn.config.Configuration;

public abstract class BotComponent {

	protected final Bot bot;
	protected final Logger logger;
	protected final Configuration config;
	
	public BotComponent(@Nonnull final Bot bot) {
		this.bot = bot;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.config = bot.getConfig();
		
	}
	
	public final Bot bot() {
		return bot;
		
	}
	
}