package io.github.wyvern2742.bmod.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import io.github.wyvern2742.bmod.BMod;
import io.github.wyvern2742.bmod.configuration.Permissions;
import io.github.wyvern2742.bmod.configuration.Strings;

/**
 * Displays a list of users currently playing on the server
 */
public class ListCommand extends AbstractCommand {

	public ListCommand(BMod plugin) {
		super(plugin, new String[] { "list", "ls" }, Strings.COMMAND_LIST_SUMMERY, Strings.COMMAND_LIST_DESCRIPTION,
				Permissions.COMMAND_LIST);
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player[] players = Sponge.getServer().getOnlinePlayers()
				.toArray(new Player[Sponge.getServer().getOnlinePlayers().size()]);

		Builder textBuilder = Text.builder();

		if (players.length == 1) {
			// English single
			textBuilder.append(Text.of(Strings.PREFIX, TextColors.GRAY, "There is ", TextColors.GOLD, players.length,
					TextColors.GRAY, " player online"));
		} else {
			// English Plural
			textBuilder.append(Text.of(Strings.PREFIX, TextColors.GRAY, "There are ", TextColors.GOLD, players.length,
					TextColors.GRAY, " players online"));
		}

		if (players.length == 0) {
			// No players are online, exit early for speed
			src.sendMessage(textBuilder.build());

			// * Incorrect documentation for CommandResult.successCount()
			return CommandResult.successCount(0);
		}

		Builder playerNames = Text.builder();

		// Construct list of player names
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];

			if (player.getDisplayNameData().displayName().exists()) {
				playerNames.append(player.getDisplayNameData().displayName().get());
			} else {
				playerNames.append(Text.of(player.getName()));
			}
			if (i + 1 != players.length) {
				playerNames.append(Text.of(", "));
			}
		}

		if (src instanceof Player) {
			// Caller is a player, add hovertext to show all players
			Builder hoverText = Text.builder();
			hoverText.append(
					Text.of(TextColors.GOLD, "Current Players ", TextColors.GRAY, players.length, Text.NEW_LINE));
			hoverText.append(Text.of(TextColors.GRAY, playerNames.build()));

			textBuilder.onHover(TextActions.showText(hoverText.build()));
		} else {
			// Caller is console, add all player's names to main text body
			textBuilder.append(Text.of(Text.NEW_LINE, Strings.PREFIX, TextColors.GRAY, playerNames.build()));
		}

		src.sendMessage(textBuilder.build());
		return CommandResult.empty();
	}
}
