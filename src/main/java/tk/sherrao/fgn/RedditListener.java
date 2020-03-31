package tk.sherrao.fgn;

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

public class RedditListener implements Runnable {

	private final Logger LOGGER;
	private final Bot bot;

	private UserAgent userAgent;
	private Credentials credentials;
	private NetworkAdapter adapter;
	private RedditClient reddit;

	private String lastPostID;

	public RedditListener(@Nonnull Bot bot) {
		this.LOGGER = LoggerFactory.getLogger(RedditListener.class);
		this.bot = bot;

		this.userAgent = new UserAgent("bot", "tk.sherrao.fgn", Configuration.get("VERSION"),
				Configuration.get("REDDIT_USER"));
		this.credentials = Credentials.script(Configuration.get("REDDIT_USER"), Configuration.get("REDDIT_PASS"),
				Configuration.get("REDDIT_ID"), Configuration.get("REDDIT_SECRET"));

		this.adapter = new OkHttpNetworkAdapter(userAgent);
		this.reddit = OAuthHelper.automatic(adapter, credentials);

	}

	public void run() {
		Submission latestPost = reddit.subreddit("FreeGameFindings").posts().sorting(SubredditSort.NEW).build().next()
				.get(0);

		if( !latestPost.getId().equals(lastPostID) ) {
			LOGGER.info("Posting free game information for {}", latestPost.getUrl());
			for(Guild server : bot.jda().getGuilds()) {
				server.getDefaultChannel()
						.sendMessage(new EmbedBuilder().setTitle(latestPost.getTitle())
								.setDescription(latestPost.getBody()).setAuthor(latestPost.getTitle()).build())
						.complete();

			}

			lastPostID = latestPost.getId();

		} else 
			return;

	}

}