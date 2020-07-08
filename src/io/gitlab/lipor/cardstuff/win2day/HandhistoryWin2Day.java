package io.gitlab.lipor.cardstuff.win2day;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import io.gitlab.lipor.HelperFunctions;
import io.gitlab.lipor.cardstuff.Action;
import io.gitlab.lipor.cardstuff.Card;
import io.gitlab.lipor.cardstuff.Hand;
import io.gitlab.lipor.cardstuff.Handhistory;
import io.gitlab.lipor.cardstuff.Player;
import io.gitlab.lipor.cardstuff.Table;
import io.gitlab.lipor.cardstuff.win2day.Win2DayXml.Game;
import io.gitlab.lipor.cardstuff.win2day.Win2DayXml.Game.Round;
import io.gitlab.lipor.cardstuffExceptions.CardException;
import io.gitlab.lipor.cardstuffExceptions.HandIllegalCardCountException;
import io.gitlab.lipor.cardstuffExceptions.HandhistoryException;
import io.gitlab.lipor.cardstuffExceptions.Messages;

public class HandhistoryWin2Day extends Handhistory {

	@Override
	public ArrayList<Hand> importHistory(String path) throws HandhistoryException {
		try {
			JAXBContext context = JAXBContext.newInstance(Win2DayXml.class);
			Unmarshaller un = context.createUnmarshaller();
			Win2DayXml xml = (Win2DayXml) un.unmarshal(new File(path));

			ArrayList<Hand> result = new ArrayList();
			for (int z = 0; z < xml.games.size(); z++) {

				Game game = xml.games.get(z);
				Hand hand = new Hand();

				hand.setPokerroom(WIN2DAY);
				hand.setGame(game.gamecode);
				hand.setGametype(Table.NO_LIMIT);
				hand.setTabletype(Table.TOURNAMENT);
				Calendar date = Calendar.getInstance();
				date.setTime(game.general.startdate);
				hand.setDate(date);

				List<Player> players = new ArrayList();
				int buttonPos = 0;
				for (int i = 0; i < game.general.players.size(); i++) {
					io.gitlab.lipor.cardstuff.win2day.Win2DayXml.Game.General.Player player = game.general.players
							.get(i);
					if (player.isDealer) {
						buttonPos = i + 1;
					}
					players.add(new Player(player.name, player.chips, Player.AKTIV));
				}
				hand.setCountSeats(players.size());
				hand.setPlayers(players.toArray(new Player[0]));
				hand.setButtonPos(buttonPos > 0 ? buttonPos : 1);
				hand.setNikname(players.get(0).getName());

				for (int i = 0; i < game.rounds.size(); i++) {
					Round round = game.rounds.get(i);
					if (i == 1) {
						hand.addAction(new Action(Action.DEAL, null, null));
						hand.addAction(new Action(Action.DEAL, null, null));
					}

					for (int o = 0; o < HelperFunctions.nonNullList(round.cards).size(); o++) {
						io.gitlab.lipor.cardstuff.win2day.Win2DayXml.Game.Round.Card card = round.cards.get(o);
						if (card.typeIdentifier.equals("Pocket") && !HelperFunctions.isEmpty(card.cards)) {
							Player player = hand.getPlayerByName(card.playerName);
							player.addPocketCard(card.cards.get(0));
							player.addPocketCard(card.cards.get(1));
						} else {
							int actionType = 0;
							if (card.typeIdentifier.equals("Flop")) {
								actionType = Action.FLOP;
							} else if (card.typeIdentifier.equals("Turn")) {
								actionType = Action.TURN;
							} else if (card.typeIdentifier.equals("River")) {
								actionType = Action.RIVER;
							}
							if (actionType > 0) {
								Action action = new Action(actionType, null, card.cards);
								HelperFunctions.stream(card.cards).forEach(x -> {
									try {
										hand.addBoardCard(x);
									} catch (Exception e) {
									}
								});
								hand.addAction(action);
							}
						}
					}

					for (int o = 0; o < HelperFunctions.nonNullList(round.actions).size(); o++) {
						io.gitlab.lipor.cardstuff.win2day.Win2DayXml.Game.Round.Action action = round.actions.get(o);
						if (action.typeId == Action.ANTE) {
							hand.setAnte(action.sum);
						}
						if (action.typeId == Action.SMALLBLIND) {
							hand.setSmallblind(action.sum);
						}
						if (action.typeId == Action.BIGBLIND) {
							hand.setBigblind(action.sum);
						}
						Action newAction = new Action(action.typeId, hand.getPlayerByName(action.playerName),
								action.sum);
						hand.addAction(newAction);
					}
				}
				result.add(hand);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HandhistoryException(Messages.HandhistoryException_0);
		}
	}
}
