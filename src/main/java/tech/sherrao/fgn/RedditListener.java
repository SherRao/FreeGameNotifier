package tech.sherrao.fgn;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import static java.text.SimpleDateFormat.*;

@Deprecated
public class RedditListener extends BotComponent implements Runnable {

	private final Logger LOGGER;

	private UserAgent userAgent;
	private Credentials credentials;
	private NetworkAdapter adapter;
	private RedditClient reddit;

	private String lastPostID;
	private SimpleDateFormat timeFormat;

	public RedditListener(@Nonnull Bot bot) {
		super(bot);
		
		this.LOGGER = LoggerFactory.getLogger(RedditListener.class);

		this.userAgent = new UserAgent("bot", "tk.sherrao.fgn", config.getProperty("VERSION"), config.getProperty("REDDIT_USER"));
		this.credentials = Credentials.script(config.getProperty("REDDIT_USER"), config.getProperty("REDDIT_PASS"), config.getProperty("REDDIT_ID"), config.getProperty("REDDIT_SECRET"));
		this.adapter = new OkHttpNetworkAdapter(userAgent);
		this.reddit = OAuthHelper.automatic(adapter, credentials);

		this.timeFormat = new SimpleDateFormat(String.format( "%d:%d:%d %d/%d/%d %d", HOUR_OF_DAY0_FIELD, MINUTE_FIELD, SECOND_FIELD, DAY_OF_WEEK_IN_MONTH_FIELD, MONTH_FIELD, YEAR_FIELD, TIMEZONE_FIELD ));
		
	}

	public void run() {
		Submission latestPost = reddit.subreddit("FreeGameFindings")
				.posts()
				.sorting(SubredditSort.NEW)
				.build()
				.next()
				.get(0);

		if( !latestPost.getId().equals(lastPostID) ) {
			LOGGER.info("Posting free game information for {}", latestPost.getDomain());
			for(Guild server : bot.jda().getGuilds()) {
				server.getDefaultChannel().sendMessage( new EmbedBuilder()
						.setTitle("Click here to go to the game page!", "https://store.steampowered.com/app/730/CounterStrike_Global_Offensive/")
						.setThumbnail("https://steamuserimages-a.akamaihd.net/ugc/110734434613941058/6B7DA71893FFCDAB8B7EA61501A3E562B1F2CFEF/")
						.setAuthor("Steam - Counter Strike: Global Offensive", null, PlatformData.STEAM.icon())
						.setFooter("Developed by Nausher Rao - SherRao#8509\nLast Updated: " + timeFormat.format(Date.from(Instant.now())), "https://cdn.discordapp.com/avatars/190984801929396224/dbcf6dc83c687dd2701c637133ac2191.png?size=256")
						.addField("Discounted Price ", "FREE!", true)
						.addField("Original Price   ", "CDN$ 19.99", true)
						.addField("Time Remaining   ", "22:09:32", true)
						.build() )
					.complete();

			}

			lastPostID = latestPost.getId();

		} else 
			return;

	}

}